package fr.paris.lutece.plugins.shoppingcart.service.persistence;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.plugins.shoppingcart.service.provider.ShoppingCartItemProviderManagementService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Class that manage shopping card items saved in an http session
 */
public class SessionedShoppingCartItem implements Serializable
{
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -4310290073260410209L;

    private boolean _bNotifyProvider;
    private List<ShoppingCartItem> _listItems;

    /**
     * Creates a new session removal listener for a list of items
     * @param listItems The list of items of this listener, or null to set no
     *            items
     * @param bNotifyProvider True to notify the provider service when the
     *            session expired, false otherwise
     */
    public SessionedShoppingCartItem( List<ShoppingCartItem> listItems, boolean bNotifyProvider )
    {
        this._bNotifyProvider = bNotifyProvider;
        this._listItems = listItems != null ? listItems : new ArrayList<ShoppingCartItem>( );
    }

    /**
     * Get the list of items associated with this listener. The returned object
     * is not a copy of the list of this class, and can therefore be modified.
     * @return The list of items of this listener. The returned list is never
     *         null
     */
    public List<ShoppingCartItem> getItemList( )
    {
        return _listItems;
    }

    /**
     * Set the notify provider flag
     * @param bNotifyProvider The notify provider flag
     */
    public void setNotifyProvider( boolean bNotifyProvider )
    {
        this._bNotifyProvider = bNotifyProvider;
    }

    /**
     * {@inheritDoc}
     */
    protected void finalize( ) throws Throwable
    {
        try
        {
            if ( _bNotifyProvider )
            {
                for ( ShoppingCartItem item : _listItems )
                {
                    ShoppingCartItemProviderManagementService.notifyItemRemoval( item );
                }
                _bNotifyProvider = false;
            }
        }
        catch ( Exception e )
        {
            // Since we are in a finalize method, we try/catch every exception to avoid error leak
            AppLogService.error( e.getMessage( ), e );
        }
        super.finalize( );
    }
}
