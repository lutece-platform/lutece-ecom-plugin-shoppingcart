package fr.paris.lutece.plugins.shoppingcart.service.provider;

/**
 * Interface for shopping cart item provider services
 */
public interface IShoppingCartItemProviderService
{
    /**
     * Notify the provider that the resource will be removed from the
     * shopping cart of the user
     * @param strResourceType The type of the removed resource
     * @param strResourceId The id of the removed resource
     */
    void notifyItemRemoval( String strResourceType, String strResourceId );

    /**
     * Get the unique identifier of this provider service
     * @return The unique identifier of this provider service
     */
    String getProviderId( );

    /**
     * Get the description of a resource. The description can be HTML code.
     * @param strResourceType The type of
     * @param strResourceId The id of the resource to get the description of
     * @return The description of the resource
     */
    String getItemDescription( String strResourceType, String strResourceId );

    /**
     * Get the URL to modify a resource if it can be modified
     * @param strResourceType The type of the resource to modify
     * @param strResourceId The id of the resource to modify
     * @return The URL to modify the resource, or null if the resource can not
     *         be modified
     */
    String getItemModificationUrl( String strResourceType, String strResourceId );
}
