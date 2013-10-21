package fr.paris.lutece.plugins.shoppingcart.service.provider;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import org.apache.commons.lang.StringUtils;


/**
 * Management class for shopping cart item providers
 */
public final class ShoppingCartItemProviderManagementService
{
    /**
     * Default constructor
     */
    private ShoppingCartItemProviderManagementService( )
    {
        // Do nothing
    }

    /**
     * Notify the provider of the item that the item will be removed from the
     * shopping cart of the user
     * @param item The item that will be removed
     */
    public static void notifyItemRemoval( ShoppingCartItem item )
    {
        for ( IShoppingCartItemProviderService providerService : SpringContextService
                .getBeansOfType( IShoppingCartItemProviderService.class ) )
        {
            if ( StringUtils.equals( item.getIdProvider( ), providerService.getProviderId( ) ) )
            {
                providerService.notifyItemRemoval( item.getResourceType( ), item.getIdResource( ) );
                return;
            }
        }
    }
}
