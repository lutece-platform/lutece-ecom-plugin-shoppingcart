package fr.paris.lutece.plugins.shoppingcart.service;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItemFilter;
import fr.paris.lutece.plugins.shoppingcart.service.persistence.IShoppingCartPersistenceService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * Service to manage shopping cart of users
 */
public final class ShoppingCartService
{
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
        getPersistenceService( StringUtils.isEmpty( item.getIdUser( ) ) ).saveItem( item );
    }

    /**
     * Get the list of shopping cart items of a user
     * @param user The user
     * @return The list of shopping cart items of the user
     */
    public List<ShoppingCartItem> getUserShoppingCart( LuteceUser user )
    {
        return getUserShoppingCart( user == null ? null : user.getName( ) );
    }

    /**
     * Get the list of shopping cart items of a user
     * @param strUserName The name of the lutece user
     * @return The list of shopping cart items of the user
     */
    public List<ShoppingCartItem> getUserShoppingCart( String strUserName )
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
                for ( ShoppingCartItem item : listItems )
                {
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
        return StringUtils.isEmpty( strUserName ) && StringUtils.equals( strUserName, LuteceUser.ANONYMOUS_USERNAME );
    }

    /**
     * Get the persistence service for the current user
     * @param bForAnonymousUser True to get the service for anonymous users,
     *            false
     *            otherwise
     * @return The persistence service
     */
    private IShoppingCartPersistenceService getPersistenceService( boolean bForAnonymousUser )
    {
        String strKey = bForAnonymousUser ? CACHE_KEY_ANONYMOUS_PERSISTENCE_SERVICE
                : CACHE_KEY_LOGGED_IN_USERS_PERSISTENCE_SERVICE;

        IShoppingCartPersistenceService persistenceService = (IShoppingCartPersistenceService) ShoppingCartCacheService
                .getInstance( ).getFromCache( strKey );
        if ( persistenceService == null )
        {
            persistenceService = SpringContextService
                    .getBean( bForAnonymousUser ? BEAN_NAME_SESSION_PERSISTENCE_SERVICE
                            : BEAN_NAME_DATABASE_PERSISTENCE_SERVICE );
            ShoppingCartCacheService.getInstance( ).putInCache( strKey, persistenceService );
        }

        return persistenceService;
    }
}
