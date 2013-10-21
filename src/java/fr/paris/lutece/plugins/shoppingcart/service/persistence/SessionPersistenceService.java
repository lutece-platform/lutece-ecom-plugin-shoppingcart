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


/**
 * Shopping cart persistence service that saves items into session
 */
public class SessionPersistenceService implements IShoppingCartPersistenceService
{
    private static final String SESSION_ATTRIBUTE_SHOPPING_CART = "shoppingcart.sessionShoppingCartItems";

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
        item.setIdItem( listItems.size( ) + 1 );
        listItems.add( item );
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
        SessionedShoppingCartItem sessionedItem = getSessionedShoppingCartItem( );
        if ( sessionedItem != null )
        {
            return sessionedItem.getItemList( );
        }
        return null;
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
        HttpServletRequest request = LocalVariables.getRequest( );
        if ( request != null )
        {
            HttpSession session = request.getSession( false );
            if ( session != null )
            {
                return (SessionedShoppingCartItem) session.getAttribute( SESSION_ATTRIBUTE_SHOPPING_CART );
            }
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
        HttpServletRequest request = LocalVariables.getRequest( );
        if ( request != null )
        {
            HttpSession session = request.getSession( true );
            if ( session != null )
            {
                session.setAttribute( SESSION_ATTRIBUTE_SHOPPING_CART, sessionedItem );
            }
        }
        else
        {
            AppLogService.error(
                    "No request attached to this context : could not save the sessioned shopping card item",
                    new AppException( ) );
        }
    }

}
