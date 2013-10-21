package fr.paris.lutece.plugins.shoppingcart.service;

import fr.paris.lutece.portal.service.cache.AbstractCacheableService;


/**
 * Cache for subscription service
 */
public final class ShoppingCartCacheService extends AbstractCacheableService
{
    private static final String CACHE_SERVICE_NAME = "ShoppingCartCacheService";

    private static ShoppingCartCacheService _instance = new ShoppingCartCacheService( );

    /**
     * Private constructor
     */
    private ShoppingCartCacheService( )
    {
        this.initCache( );
    }

    /**
     * Get the instance of the cache service
     * @return The instance of the cache service
     */
    public static ShoppingCartCacheService getInstance( )
    {
        return _instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName( )
    {
        return CACHE_SERVICE_NAME;
    }

}
