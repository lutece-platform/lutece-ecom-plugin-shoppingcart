package fr.paris.lutece.plugins.shoppingcart.business.lot;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * DAO for shopping cart lot
 */
public class ShoppingCartLotDAO implements IShoppingCartLotDAO
{

    private static final String SQL_QUERY_GET_LAST_LOT_OF_USER = " SELECT max(id_lot) FROM shoppingcart_user_lot WHERE id_user = ? ";
    private static final String SQL_QUERY_GET_NEW_LOT_FOR_USER = " SELECT max(id_lot) FROM shoppingcart_user_lot ";
    private static final String SQL_QUERY_INSERT_NEW_LOT_FOR_USER = " INSERT INTO shoppingcart_user_lot(id_user, id_lot) VALUES (?,?) ";
    private static final String SQL_QUERY_GET_USER_NAME_FROM_ID_LOT = " SELECT id_user FROM shoppingcart_user_lot WHERE id_lot = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLastIdlotOfUser( String strUserName, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_LAST_LOT_OF_USER, plugin );
        daoUtil.setString( 1, strUserName );
        daoUtil.executeQuery( );
        int nIdLot = 0;
        if ( daoUtil.next( ) )
        {
            nIdLot = daoUtil.getInt( 1 );
        }
        daoUtil.free( );
        return nIdLot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized int getNewIdLotForUser( String strUserName, Plugin plugin )
    {
        // We get the id lot
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_NEW_LOT_FOR_USER, plugin );
        daoUtil.executeQuery( );

        int nIdLot;
        if ( daoUtil.next( ) )
        {
            nIdLot = daoUtil.getInt( 1 ) + 1;
        }
        else
        {
            nIdLot = 1;
        }

        daoUtil.free( );
        // We save the id lot
        daoUtil = new DAOUtil( SQL_QUERY_INSERT_NEW_LOT_FOR_USER, plugin );
        daoUtil.setString( 1, strUserName );
        daoUtil.setInt( 2, nIdLot );
        daoUtil.executeUpdate( );
        daoUtil.free( );
        return nIdLot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserNameFromIdLot( int nIdLot, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_USER_NAME_FROM_ID_LOT, plugin );
        daoUtil.setInt( 1, nIdLot );
        daoUtil.executeQuery( );
        String strUserName = null;
        if ( daoUtil.next( ) )
        {
            strUserName = daoUtil.getString( 1 );
        }
        daoUtil.free( );
        return strUserName;
    }
}
