package fr.paris.lutece.plugins.shoppingcart.service;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItemFilter;
import fr.paris.lutece.plugins.shoppingcart.service.persistence.IShoppingCartPersistenceService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * Service to manage shopping cart of users
 */
public final class ShoppingCartService
{
    /**
     * Datastore key for the boolean value that indicates whether shopping cart
     * items of registered users must be saved in the database
     */
    public static final String DATASTORE_KEY_ENABLE_DATABASE_PERSISTENCE = "shoppingcart.enableDatabasePersistence";

    private static final String BEAN_NAME_DATABASE_PERSISTENCE_SERVICE = "shoppingcart.databasePersistenceService";
    private static final String BEAN_NAME_SESSION_PERSISTENCE_SERVICE = "shoppingcart.sessionPersistenceService";

    private static final String CACHE_KEY_ANONYMOUS_PERSISTENCE_SERVICE = "shoppingcart.cacheKeyAnonymousPersistenceService";
    private static final String CACHE_KEY_LOGGED_IN_USERS_PERSISTENCE_SERVICE = "shoppingcart.cacheKeyLoggedInUsersPersistenceService";

    /**
     * The instance of the service
     */
    private static ShoppingCartService _instance = new ShoppingCartService( );

    /**
     * Private constructor
     */
    private ShoppingCartService( )
    {
    }

    /**
     * Get the service instance
     * @return The instance of the service
     */
    public static ShoppingCartService getInstance( )
    {
        return _instance;
    }

    /**
     * Add an item to the shopping cart of a user
     * @param item The item to add
     * @param user The user
     */
    public void addItemToShoppingCart( ShoppingCartItem item, LuteceUser user )
    {
        item.setIdUser( user.getName( ) );
        addItemToShoppingCart( item );
    }

    /**
     * Add an item to the shopping cart of a user. The user id attribute of the
     * item must have been set.
     * @param item The item to add to the shopping cart
     */
    public void addItemToShoppingCart( ShoppingCartItem item )
    {
        if ( item.getIdLot( ) == 0 )
        {
            item.setIdLot( ShoppingCartLotService.getInstance( ).getNewIdLotForUser( item.getIdUser( ) ) );
        }
        getPersistenceService( StringUtils.isEmpty( item.getIdUser( ) ) ).saveItem( item );
    }

    /**
     * Get the list of shopping cart items of a user
     * @param user The user
     * @return The list of shopping cart items of the user
     */
    public List<ShoppingCartItem> getShoppingCartOfUser( LuteceUser user )
    {
        return getShoppingCartOfUser( user == null ? null : user.getName( ) );
    }

    /**
     * Get the list of shopping cart items of a user
     * @param strUserName The name of the lutece user
     * @return The list of shopping cart items of the user
     */
    public List<ShoppingCartItem> getShoppingCartOfUser( String strUserName )
    {
        IShoppingCartPersistenceService persistenceService = getPersistenceService( isUserAnonymous( strUserName ) );
        return persistenceService.getItemsOfUser( strUserName );
    }

    /**
     * Get the list of shopping cart items for a given user
     * @param user The user to get the shopping cart of
     * @param strIdProvider The provider of items to get
     * @return The list of shopping cart items
     */
    public List<ShoppingCartItem> getShoppingCartItems( LuteceUser user, String strIdProvider )
    {
        ShoppingCartItemFilter filter = new ShoppingCartItemFilter( );
        filter.setIdUser( user.getName( ) );
        filter.setIdProvider( strIdProvider );
        return getShoppingCartItemsByFilter( filter );
    }

    /**
     * Get a shopping cart item from its id
     * @param nIdShoppingCartItem The id of the shopping cart item
     * @param strUserName The name of the user associated with the shopping cart
     *            item
     * @return The item, or null if the item was not found
     */
    public ShoppingCartItem getShoppingCartItem( int nIdShoppingCartItem, String strUserName )
    {
        IShoppingCartPersistenceService persistenceService = getPersistenceService( isUserAnonymous( strUserName ) );
        return persistenceService.findItemById( nIdShoppingCartItem );
    }

    /**
     * Get the list of shopping cart items that match a given filter. This
     * method will only consider items saved in the database. Items saved in
     * session will be ignored.
     * @param filter The filter
     * @return The list of shopping cart items
     */
    public List<ShoppingCartItem> getShoppingCartItemsByFilter( ShoppingCartItemFilter filter )
    {
        return getPersistenceService( false ).findItemsByFilter( filter );
    }

    /**
     * Remove a shopping cart item and notify
     * @param strUserName The name of the user the item belongs to
     * @param nIdItem The id of the item to remove
     * @param bNotifyProviderService True to notify the provider service that
     *            the item was removed
     */
    public void removeShoppingCartItem( String strUserName, int nIdItem, boolean bNotifyProviderService )
    {
        IShoppingCartPersistenceService persistenceService = getPersistenceService( isUserAnonymous( strUserName ) );
        persistenceService.removeItemFromUserShoppingCart( strUserName, nIdItem, bNotifyProviderService );
    }

    /**
     * Remove every items of the shopping of a user
     * @param strUserName The name of the user to empty the shopping cart of, or
     *            null if the user has not logged in.
     * @param bNotifyProviderService True to notify the provider service of
     *            every item removed, false otherwise
     */
    public void emptyShoppingCartOfUser( String strUserName, boolean bNotifyProviderService )
    {
        IShoppingCartPersistenceService persistenceService = getPersistenceService( isUserAnonymous( strUserName ) );
        persistenceService.emptyShoppingCartOfUser( strUserName, bNotifyProviderService );
    }

    /**
     * Check if the current user has items in his anonymous shopping cart
     * @param strUserName The name of the user to check the shopping cart of
     */
    protected void checkAnonymousShoppingCart( String strUserName )
    {
        // If the user is not anonymous
        if ( !isUserAnonymous( strUserName ) )
        {
            IShoppingCartPersistenceService anonymousUserPersistenceService = getPersistenceService( true );
            List<ShoppingCartItem> listItems = anonymousUserPersistenceService.getItemsOfUser( strUserName );
            // If the user has items in his anonymous shopping cart, we remove them
            if ( listItems != null && listItems.size( ) > 0 )
            {
                IShoppingCartPersistenceService loggedInUserPersistenceService = getPersistenceService( false );
                // we add items of the anonymous service to the logged in service
                ShoppingCartLotService instance = ShoppingCartLotService.getInstance( );
                for ( ShoppingCartItem item : listItems )
                {
                    item.setIdUser( strUserName );
                    if ( item.getIdLot( ) == ShoppingCartItem.NEW_ID_LOT_FOR_ANONYMOUS_USER )
                    {
                        item.setIdLot( instance.getNewIdLotForUser( strUserName ) );
                    }
                    else if ( item.getIdLot( ) == ShoppingCartItem.LAST_ID_LOT_FOR_ANONYMOUS_USER )
                    {
                        item.setIdLot( instance.getLastIdlotOfUser( strUserName ) );
                    }
                    loggedInUserPersistenceService.saveItem( item );
                }
                // Now that items are transfered, we empty the anonymous shopping cart
                anonymousUserPersistenceService.emptyShoppingCartOfUser( strUserName, false );
            }
        }
    }

    /**
     * Check if a user name is an anonymous user name
     * @param strUserName The user name to check
     * @return True if the user name is an anonymous user name
     */
    private boolean isUserAnonymous( String strUserName )
    {
        return StringUtils.isEmpty( strUserName ) || StringUtils.equals( strUserName, LuteceUser.ANONYMOUS_USERNAME );
    }

    /**
     * Get the persistence service for the current user
     * @param bForAnonymousUser True to get the service for anonymous users,
     *            false otherwise
     * @return The persistence service
     */
    private IShoppingCartPersistenceService getPersistenceService( boolean bForAnonymousUser )
    {
        boolean bDatabasePersistenceEnable = Boolean.parseBoolean( DatastoreService.getInstanceDataValue(
                DATASTORE_KEY_ENABLE_DATABASE_PERSISTENCE, null ) );
        String strKey = bForAnonymousUser || !bDatabasePersistenceEnable ? CACHE_KEY_ANONYMOUS_PERSISTENCE_SERVICE
                : CACHE_KEY_LOGGED_IN_USERS_PERSISTENCE_SERVICE;

        IShoppingCartPersistenceService persistenceService = (IShoppingCartPersistenceService) ShoppingCartCacheService
                .getInstance( ).getFromCache( strKey );
        if ( persistenceService == null )
        {
            persistenceService = SpringContextService
                    .getBean( bForAnonymousUser || !bDatabasePersistenceEnable ? BEAN_NAME_SESSION_PERSISTENCE_SERVICE
                            : BEAN_NAME_DATABASE_PERSISTENCE_SERVICE );
            ShoppingCartCacheService.getInstance( ).putInCache( strKey, persistenceService );
        }

        return persistenceService;
    }
}
