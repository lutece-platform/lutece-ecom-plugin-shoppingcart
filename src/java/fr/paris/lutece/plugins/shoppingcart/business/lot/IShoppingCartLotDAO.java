package fr.paris.lutece.plugins.shoppingcart.business.lot;

import fr.paris.lutece.portal.service.plugin.Plugin;


/**
 * Interface for shopping cart lot
 */
public interface IShoppingCartLotDAO
{
    /**
     * Get the last id lot used by a user
     * @param strUserName The name of the user to get the lot id of
     * @param plugin The plugin to use the pool of
     * @return The last id lot used by a user, or 0 if the user is not
     *         associated with any lot
     */
    int getLastIdlotOfUser( String strUserName, Plugin plugin );

    /**
     * Get a new id lot for a user. The returned id lot will be reserved for the
     * given user and can not be used by any other user
     * @param strUserName The name of the user to get the lot id for
     * @param plugin The plugin to use the pool of
     * @return The id lot
     */
    int getNewIdLotForUser( String strUserName, Plugin plugin );

    /**
     * Get the name of the user associated with a given lot
     * @param nIdLot The id of the lot to get the name of the user associated
     *            with
     * @param plugin The plugin to use the pool of
     * @return The name of the user, or null if the lot was not found
     */
    String getUserNameFromIdLot( int nIdLot, Plugin plugin );
}
