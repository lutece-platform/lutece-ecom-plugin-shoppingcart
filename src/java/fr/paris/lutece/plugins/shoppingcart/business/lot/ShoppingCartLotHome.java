package fr.paris.lutece.plugins.shoppingcart.business.lot;

import fr.paris.lutece.plugins.shoppingcart.service.ShoppingCartPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;


/**
 * Home for shopping cart lot
 */
public class ShoppingCartLotHome
{
    private static IShoppingCartLotDAO _dao = SpringContextService.getBean( "shoppingcart.shoppingCartLoDAO" );
    private static Plugin _plugin = PluginService.getPlugin( ShoppingCartPlugin.PLUGIN_NAME );

    /**
     * Get the last id lot used by a user
     * @param strUserName The name of the user to get the lot id of
     * @param plugin The plugin to use the pool of
     * @return The last id lot used by a user, or 0 if the user is not
     *         associated with any lot
     */
    public static int getLastIdlotOfUser( String strUserName )
    {
        return _dao.getLastIdlotOfUser( strUserName, _plugin );
    }

    /**
     * Get a new id lot for a user. The returned id lot will be reserved for the
     * given user and can not be used by any other user
     * @param strUserName The name of the user to get the lot id for
     * @param plugin The plugin to use the pool of
     * @return The id lot
     */
    public static int getNewIdLotForUser( String strUserName )
    {
        return _dao.getNewIdLotForUser( strUserName, _plugin );
    }

    /**
     * Get the name of the user associated with a given lot
     * @param nIdLot The id of the lot to get the name of the user associated
     *            with
     * @param plugin The plugin to use the pool of
     * @return The name of the user, or null if the lot was not found
     */
    public static String getUserNameFromIdLot( int nIdLot )
    {
        return _dao.getUserNameFromIdLot( nIdLot, _plugin );
    }
}
