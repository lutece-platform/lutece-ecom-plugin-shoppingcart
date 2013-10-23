package fr.paris.lutece.plugins.shoppingcart.service.persistence;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItemFilter;
import fr.paris.lutece.plugins.shoppingcart.service.provider.ShoppingCartItemProviderManagementService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.LocalVariables;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.mutable.MutableInt;


/**
 * Shopping cart persistence service that saves items into session
 */
public class SessionPersistenceService implements IShoppingCartPersistenceService
{
    /**
     * Session key of the Boolean attribute that indicates whether the current
     * user has items saved in session
     */
    public static final String SESSION_ATTRIBUTE_HAS_SESSION_ITEMS = "shoppingcart.hasSessionItems";

    private static final String SESSION_ATTRIBUTE_SHOPPING_CART = "shoppingcart.sessionShoppingCartItems";
    private static final String SESSION_ATTRIBUTE_ID_SHOPPING_CART_ITEM = "shoppingcart.sessionIdShoppingCart";

    private static final String SERVICE_NAME = "shoppingcart.seccionPersistenceService";

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveItem( ShoppingCartItem item )
    {
        if ( item.getDateCreation( ) == null )
        {
            item.setDateCreation( new Date( ) );
        }

        SessionedShoppingCartItem sessionedItem = getSessionedShoppingCartItem( );
        List<ShoppingCartItem> listItems;
        if ( sessionedItem == null )
        {
            listItems = new ArrayList<ShoppingCartItem>( );
            sessionedItem = new SessionedShoppingCartItem( listItems, true );
            saveSessionedShoppingCartItem( sessionedItem );
        }
        else
        {
            listItems = sessionedItem.getItemList( );
        }
        item.setIdItem( getNewShoppingCartItemId( ) );
        listItems.add( item );
        HttpSession session = getSession( );
        if ( session != null )
        {
            session.setAttribute( SESSION_ATTRIBUTE_HAS_SESSION_ITEMS, true );
        }
    }

    /**
     * {@inheritDoc}
     * @return True
     */
    @Override
    public boolean supportAnonymousUsers( )
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ShoppingCartItem> findItemsByFilter( ShoppingCartItemFilter filter )
    {
        List<ShoppingCartItem> listResult = null;
        listResult = new ArrayList<ShoppingCartItem>( );
        List<ShoppingCartItem> listItems;
        SessionedShoppingCartItem sessionedItem = getSessionedShoppingCartItem( );
        if ( sessionedItem != null )
        {
            listItems = sessionedItem.getItemList( );
            for ( ShoppingCartItem item : listItems )
            {
                if ( doesItemMatchFilter( item, filter ) )
                {
                    listResult.add( item );
                }
            }
        }

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ShoppingCartItem> getItemsOfUser( String strUserName )
    {
        return getItemsOfUser( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServiceName( )
    {
        return SERVICE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeItemFromUserShoppingCart( String strUserName, int nIdShoppingCartItem,
            boolean bNotifyProviderService )
    {
        SessionedShoppingCartItem sessionedItem = getSessionedShoppingCartItem( );
        if ( sessionedItem != null && nIdShoppingCartItem > 0 )
        {
            List<ShoppingCartItem> listItems = sessionedItem.getItemList( );
            ShoppingCartItem itemFound = null;
            for ( ShoppingCartItem item : listItems )
            {
                if ( item.getIdItem( ) == nIdShoppingCartItem )
                {
                    itemFound = item;
                    break;
                }
            }
            if ( itemFound != null )
            {
                if ( bNotifyProviderService )
                {
                    ShoppingCartItemProviderManagementService.notifyItemRemoval( itemFound );
                }
                listItems.remove( itemFound );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void emptyShoppingCartOfUser( String strUserName, boolean bNotifyProviderService )
    {
        SessionedShoppingCartItem sessionedItem = getSessionedShoppingCartItem( );
        if ( sessionedItem != null )
        {
            if ( bNotifyProviderService )
            {
                List<ShoppingCartItem> listItems = sessionedItem.getItemList( );
                for ( ShoppingCartItem item : listItems )
                {
                    ShoppingCartItemProviderManagementService.notifyItemRemoval( item );
                }
            }
            sessionedItem.setNotifyProvider( false );
            saveSessionedShoppingCartItem( null );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShoppingCartItem findItemById( int nIdShoppingCartItem )
    {
        List<ShoppingCartItem> listItems = getItemsOfUser( );
        for ( ShoppingCartItem item : listItems )
        {
            if ( item.getIdItem( ) == nIdShoppingCartItem )
            {
                return item;
            }
        }
        return null;
    }

    /**
     * Check if an item match values of a filter
     * @param item The item to check
     * @param filter The values that the item must have
     * @return True if the item match the filter, false otherwise
     */
    private boolean doesItemMatchFilter( ShoppingCartItem item, ShoppingCartItemFilter filter )
    {
        if ( ( filter.getIdProvider( ) != null && !StringUtils.equals( filter.getIdProvider( ), item.getIdProvider( ) ) )
                || ( filter.getIdLot( ) > 0 && filter.getIdLot( ) != item.getIdLot( ) )
                || ( filter.getIdResource( ) != null && !StringUtils.equals( filter.getIdResource( ),
                        item.getIdResource( ) ) )
                || ( filter.getIdUser( ) != null && !StringUtils.equals( filter.getIdUser( ), item.getIdUser( ) ) )
                || ( filter.getItemPrice( ) > 0d && filter.getItemPrice( ) == item.getItemPrice( ) )
                || ( filter.getResourceType( ) != null && !StringUtils.equals( filter.getResourceType( ),
                        item.getResourceType( ) ) )
                || ( filter.getDateCreationMin( ) != null && filter.getDateCreationMin( ).getTime( ) > item
                        .getDateCreation( ).getTime( ) )
                || ( filter.getDateCreationMax( ) != null && filter.getDateCreationMax( ).getTime( ) < item
                        .getDateCreation( ).getTime( ) ) )
        {
            return false;
        }
        return true;
    }

    /**
     * Get the SessionedShoppingCartItem of the current user, if any.
     * @return The SessionedShoppingCartItem of the current user or null if none
     *         was found.
     */
    private SessionedShoppingCartItem getSessionedShoppingCartItem( )
    {
        HttpSession session = getSession( );
        if ( session != null )
        {
            return (SessionedShoppingCartItem) session.getAttribute( SESSION_ATTRIBUTE_SHOPPING_CART );
        }
        AppLogService.error(
                "No request or session attached to this context : could not return the sessioned shopping card item",
                new AppException( ) );
        return null;
    }

    /**
     * Save the SessionedShoppingCartItem of the current user into the session
     * @param sessionedItem The item to save
     */
    private void saveSessionedShoppingCartItem( SessionedShoppingCartItem sessionedItem )
    {
        HttpSession session = getSession( );
        if ( session != null )
        {
            session.setAttribute( SESSION_ATTRIBUTE_SHOPPING_CART, sessionedItem );
            session.setAttribute( SESSION_ATTRIBUTE_HAS_SESSION_ITEMS, sessionedItem != null
                    && sessionedItem.getItemList( ).size( ) > 0 );
        }
    }

    /**
     * Get the session of the current context.
     * @return The session of the current context, or null if it is not a
     *         request context
     */
    private HttpSession getSession( )
    {
        HttpServletRequest request = LocalVariables.getRequest( );
        if ( request != null )
        {
            return request.getSession( true );
        }
        AppLogService.error( "No request attached to this context : could not save the sessioned shopping card item",
                new AppException( ) );
        return null;
    }

    /**
     * Get the items of the current user that are stored in session
     * @return The items of the current user
     */
    private List<ShoppingCartItem> getItemsOfUser( )
    {
        SessionedShoppingCartItem sessionedItem = getSessionedShoppingCartItem( );
        if ( sessionedItem == null )
        {
            sessionedItem = new SessionedShoppingCartItem( null, true );
            saveSessionedShoppingCartItem( sessionedItem );
        }
        return new ArrayList<ShoppingCartItem>( sessionedItem.getItemList( ) );
    }

    /**
     * Get a new id for shopping cart items of a user
     * @return The new id, or 0 if this is not a request session
     */
    private int getNewShoppingCartItemId( )
    {
        HttpSession session = getSession( );
        if ( session != null )
        {
            MutableInt nCurrentId = (MutableInt) session.getAttribute( SESSION_ATTRIBUTE_ID_SHOPPING_CART_ITEM );
            if ( nCurrentId == null )
            {
                nCurrentId = new MutableInt( 1 );
                session.setAttribute( SESSION_ATTRIBUTE_ID_SHOPPING_CART_ITEM, nCurrentId );
            }
            else
            {
                nCurrentId.increment( );
            }
            return nCurrentId.intValue( );
        }
        return 0;
    }
}
