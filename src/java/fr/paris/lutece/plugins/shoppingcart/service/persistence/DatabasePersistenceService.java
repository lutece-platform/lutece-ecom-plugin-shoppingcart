package fr.paris.lutece.plugins.shoppingcart.service.persistence;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItemFilter;
import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItemHome;
import fr.paris.lutece.plugins.shoppingcart.service.provider.ShoppingCartItemProviderManagementService;

import java.util.List;


/**
 * Service to save shopping cart items into the database
 */
public class DatabasePersistenceService implements IShoppingCartPersistenceService
{
    private static final String SERVICE_NAME = "shoppingcart.seccionPersistenceService";

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveItem( ShoppingCartItem item )
    {
        ShoppingCartItemHome.create( item );
    }

    /**
     * {@inheritDoc}
     * @return false
     */
    @Override
    public boolean supportAnonymousUsers( )
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ShoppingCartItem> findItemsByFilter( ShoppingCartItemFilter filter )
    {
        return ShoppingCartItemHome.findByFilter( filter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ShoppingCartItem> getItemsOfUser( String strUserName )
    {
        ShoppingCartItemFilter filter = new ShoppingCartItemFilter( );
        filter.setIdUser( strUserName );
        return findItemsByFilter( filter );
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
        if ( bNotifyProviderService )
        {
            ShoppingCartItem item = ShoppingCartItemHome.findByPrimaryKey( nIdShoppingCartItem );
            ShoppingCartItemProviderManagementService.notifyItemRemoval( item );
        }
        ShoppingCartItemHome.remove( nIdShoppingCartItem );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void emptyShoppingCartOfUser( String strUserName, boolean bNotifyProviderService )
    {
        ShoppingCartItemFilter filter = new ShoppingCartItemFilter( );
        filter.setIdUser( strUserName );
        List<ShoppingCartItem> listItems = findItemsByFilter( filter );
        if ( listItems != null )
        {
            for ( ShoppingCartItem item : listItems )
            {
                if ( bNotifyProviderService )
                {
                    ShoppingCartItemProviderManagementService.notifyItemRemoval( item );
                }
                ShoppingCartItemHome.remove( item.getIdItem( ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShoppingCartItem findItemById( int nIdShoppingCartItem )
    {
        return ShoppingCartItemHome.findByPrimaryKey( nIdShoppingCartItem );
    }

}
