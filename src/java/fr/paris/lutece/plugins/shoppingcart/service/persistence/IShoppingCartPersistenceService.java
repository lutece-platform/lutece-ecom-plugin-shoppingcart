package fr.paris.lutece.plugins.shoppingcart.service.persistence;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItemFilter;

import java.util.List;


/**
 * Interface for shopping cart persistence services
 */
public interface IShoppingCartPersistenceService
{
    /**
     * Save a shopping cart item
     * @param item The item to save
     */
    void saveItem( ShoppingCartItem item );

    /**
     * Check whether this service support anonymous users
     * @return True if the service support anonymous users, false otherwise
     */
    boolean supportAnonymousUsers( );

    /**
     * Find items by filter
     * @param filter The filter that items must match
     * @return The list of items that match the current filter, or an empty list
     *         if no items was found. This method can also return null if the
     *         context is not correct.
     */
    List<ShoppingCartItem> findItemsByFilter( ShoppingCartItemFilter filter );

    /**
     * Get the items of a given user
     * @param strUserName The name of the user to get the items of
     * @return The list of items, or an empty list if no items was found. This
     *         method can also return null if the context is not correct.
     */
    List<ShoppingCartItem> getItemsOfUser( String strUserName );

    /**
     * Get the name of the persistence service
     * @return The name of the persistence service
     */
    String getServiceName( );

    /**
     * Remove an item from the shopping cart of a user
     * @param strUserName The name of the user to remove the item of
     * @param nIdShoppingCartItem The id of the item to remove
     * @param bNotifyProviderService True to notify the provider service, false
     *            otherwise
     */
    void removeItemFromUserShoppingCart( String strUserName, int nIdShoppingCartItem, boolean bNotifyProviderService );

    /**
     * Remove every shopping cart items of a given user
     * @param strUserName The name of the user to remove items of
     * @param bNotifyProviderService True to notify the provider service, false
     *            otherwise
     */
    void emptyShoppingCartOfUser( String strUserName, boolean bNotifyProviderService );

    /**
     * Get a shopping cart item from its id
     * @param nIdShoppingCartItem The id of the shopping cart item
     * @return The shopping cart item, or null if the item was not found
     */
    ShoppingCartItem findItemById( int nIdShoppingCartItem );
}
