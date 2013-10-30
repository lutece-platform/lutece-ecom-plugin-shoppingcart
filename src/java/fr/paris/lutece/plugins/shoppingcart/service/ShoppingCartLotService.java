package fr.paris.lutece.plugins.shoppingcart.service;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.plugins.shoppingcart.business.lot.ShoppingCartLotHome;
import fr.paris.lutece.portal.service.security.LuteceUser;


/**
 * Service for shopping cart lot
 */
public final class ShoppingCartLotService
{
    private static ShoppingCartLotService _instance = new ShoppingCartLotService( );

    /**
     * Default constructor
     */
    private ShoppingCartLotService( )
    {
        // Private constructor
    }

    /**
     * Get the instance of the service
     * @return The instance of the service
     */
    public static ShoppingCartLotService getInstance( )
    {
        return _instance;
    }

    /**
     * Get the last id lot used by a user
     * @param user The user to get the lot id of
     * @return The last id lot used by a user, or 0 if the user is not
     *         associated with any lot
     */
    public int getLastIdlotOfUser( LuteceUser user )
    {
        return getLastIdlotOfUser( user != null ? user.getName( ) : null );
    }

    /**
     * Get the last id lot used by a user
     * @param strUserName The name of the user to get the lot id of
     * @return The last id lot used by a user, or 0 if the user is not
     *         associated with any lot
     */
    public int getLastIdlotOfUser( String strUserName )
    {
        if ( strUserName == null )
        {
            return ShoppingCartItem.LAST_ID_LOT_FOR_ANONYMOUS_USER;
        }
        return ShoppingCartLotHome.getLastIdlotOfUser( strUserName );
    }

    /**
     * Get a new id lot for a user. The returned id lot will be reserved for the
     * given user and can not be used by any other user
     * @param user The user to get the lot id for
     * @return The id lot
     */
    public int getNewIdLotForUser( LuteceUser user )
    {
        return getNewIdLotForUser( user != null ? user.getName( ) : null );
    }

    /**
     * Get a new id lot for a user. The returned id lot will be reserved for the
     * given user and can not be used by any other user
     * @param strUserName The name of the user to get the lot id for
     * @return The id lot
     */
    public int getNewIdLotForUser( String strUserName )
    {
        if ( strUserName == null )
        {
            return ShoppingCartItem.NEW_ID_LOT_FOR_ANONYMOUS_USER;
        }
        return ShoppingCartLotHome.getNewIdLotForUser( strUserName );
    }

    /**
     * Get the name of the user associated with a given lot
     * @param nIdLot The id of the lot to get the name of the user associated
     *            with
     * @return The name of the user, or null if the lot was not found
     */
    public String getUserNameFromIdLot( int nIdLot )
    {
        return ShoppingCartLotHome.getUserNameFromIdLot( nIdLot );
    }
}
