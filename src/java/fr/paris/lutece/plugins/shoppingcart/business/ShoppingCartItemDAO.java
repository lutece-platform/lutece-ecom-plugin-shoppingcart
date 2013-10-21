/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *	 and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *	 and the following disclaimer in the documentation and/or other materials
 *	 provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *	 contributors may be used to endorse or promote products derived from
 *	 this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.shoppingcart.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This class provides Data Access methods for ShoppingItem objects
 */

public final class ShoppingCartItemDAO implements IShoppingCartItemDAO
{

    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_item ) FROM shoppingcart_shopping_item";
    private static final String SQL_QUERY_SELECT = "SELECT id_item, id_provider, id_lot, id_user, resource_type, id_resource, item_price, date_creation FROM shoppingcart_shopping_item ";
    private static final String SQL_QUERY_SELECT_BY_PRIMARY_KEY = SQL_QUERY_SELECT + " WHERE id_item = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO shoppingcart_shopping_item ( id_item, id_provider, id_lot, id_user, resource_type, id_resource, item_price, date_creation ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM shoppingcart_shopping_item WHERE id_item = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE shoppingcart_shopping_item SET id_item = ?, id_provider = ?, id_lot = ?, id_user = ?, resource_type = ?, id_resource = ?, item_price = ?, date_creation = ? WHERE id_item = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_item, id_provider, id_lot, id_user, resource_type, id_resource, item_price, date_creation FROM shoppingcart_shopping_item";

    private static final String SQL_FILTER_ID_PROVIDER = " id_provider = ? ";
    private static final String SQL_FILTER_ID_USER = " id_user = ? ";
    private static final String SQL_FILTER_ID_RESOURCE = " id_resource = ? ";
    private static final String SQL_FILTER_RESOURCE_TYPE = " resource_type = ? ";
    private static final String SQL_FILTER_ITEM_PRICE = " item_price = ? ";
    private static final String SQL_FILTER_ID_LOT = " id_lot = ? ";
    private static final String SQL_FILTER_DATE_CREATION_MIN = " date_creation < ? ";
    private static final String SQL_FILTER_DATE_CREATION_MAX = " date_creation > ? ";

    private static final String CONSTANT_WHERE = " WHERE ";
    private static final String CONSTANT_AND = " AND ";

    /**
     * Generates a new primary key
     * @param plugin The plugin
     * @return The new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery( );

        int nKey = 1;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free( );

        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( ShoppingCartItem item, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        item.setIdItem( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, item.getIdItem( ) );
        daoUtil.setString( 2, item.getIdProvider( ) );
        daoUtil.setInt( 3, item.getIdLot( ) );
        daoUtil.setString( 4, item.getIdUser( ) );
        daoUtil.setString( 5, item.getResourceType( ) );
        daoUtil.setString( 6, item.getIdResource( ) );
        daoUtil.setInt( 7, (int) ( item.getItemPrice( ) * 100 ) );
        daoUtil.setDate( 8, new Date( item.getDateCreation( ).getTime( ) ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ShoppingCartItem load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );

        ShoppingCartItem shoppingItem = null;

        if ( daoUtil.next( ) )
        {
            shoppingItem = new ShoppingCartItem( );
            shoppingItem.setIdItem( daoUtil.getInt( 1 ) );
            shoppingItem.setIdProvider( daoUtil.getString( 2 ) );
            shoppingItem.setIdLot( daoUtil.getInt( 3 ) );
            shoppingItem.setIdUser( daoUtil.getString( 4 ) );
            shoppingItem.setResourceType( daoUtil.getString( 5 ) );
            shoppingItem.setIdResource( daoUtil.getString( 6 ) );
            shoppingItem.setItemPrice( daoUtil.getInt( 7 ) / 100d );
            shoppingItem.setDateCreation( daoUtil.getDate( 8 ) );
        }

        daoUtil.free( );
        return shoppingItem;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nShoppingItemId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nShoppingItemId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( ShoppingCartItem item, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, item.getIdItem( ) );
        daoUtil.setString( 2, item.getIdProvider( ) );
        daoUtil.setInt( 3, item.getIdLot( ) );
        daoUtil.setString( 4, item.getIdUser( ) );
        daoUtil.setString( 5, item.getResourceType( ) );
        daoUtil.setString( 6, item.getIdResource( ) );
        daoUtil.setInt( 7, (int) ( item.getItemPrice( ) * 100d ) );
        daoUtil.setDate( 8, new Date( item.getDateCreation( ).getTime( ) ) );
        daoUtil.setInt( 9, item.getIdItem( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<ShoppingCartItem> selectShoppingItemsList( Plugin plugin )
    {
        Collection<ShoppingCartItem> shoppingItemList = new ArrayList<ShoppingCartItem>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            ShoppingCartItem shoppingItem = new ShoppingCartItem( );

            shoppingItem.setIdItem( daoUtil.getInt( 1 ) );
            shoppingItem.setIdProvider( daoUtil.getString( 2 ) );
            shoppingItem.setIdLot( daoUtil.getInt( 3 ) );
            shoppingItem.setIdUser( daoUtil.getString( 4 ) );
            shoppingItem.setResourceType( daoUtil.getString( 5 ) );
            shoppingItem.setIdResource( daoUtil.getString( 6 ) );
            shoppingItem.setItemPrice( daoUtil.getInt( 7 ) / 100d );
            shoppingItem.setDateCreation( daoUtil.getDate( 8 ) );

            shoppingItemList.add( shoppingItem );
        }

        daoUtil.free( );
        return shoppingItemList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<ShoppingCartItem> findByFilter( ShoppingCartItemFilter filter, Plugin plugin )
    {

        StringBuilder sbSql = new StringBuilder( SQL_QUERY_SELECT );

        boolean bIsFirst = true;
        if ( filter.getIdProvider( ) != null )
        {
            sbSql.append( CONSTANT_WHERE );
            sbSql.append( SQL_FILTER_ID_PROVIDER );
            bIsFirst = false;
        }
        if ( filter.getIdResource( ) != null )
        {
            sbSql.append( bIsFirst ? CONSTANT_WHERE : CONSTANT_AND );
            sbSql.append( SQL_FILTER_ID_RESOURCE );
            bIsFirst = false;
        }
        if ( filter.getIdUser( ) != null )
        {
            sbSql.append( bIsFirst ? CONSTANT_WHERE : CONSTANT_AND );
            sbSql.append( SQL_FILTER_ID_USER );
            bIsFirst = false;
        }
        if ( filter.getIdLot( ) > 0 )
        {
            sbSql.append( bIsFirst ? CONSTANT_WHERE : CONSTANT_AND );
            sbSql.append( SQL_FILTER_ID_LOT );
            bIsFirst = false;
        }
        if ( filter.getResourceType( ) != null )
        {
            sbSql.append( bIsFirst ? CONSTANT_WHERE : CONSTANT_AND );
            sbSql.append( SQL_FILTER_RESOURCE_TYPE );
            bIsFirst = false;
        }
        if ( filter.getItemPrice( ) > 0 )
        {
            sbSql.append( bIsFirst ? CONSTANT_WHERE : CONSTANT_AND );
            sbSql.append( SQL_FILTER_ITEM_PRICE );
            bIsFirst = false;
        }
        if ( filter.getDateCreationMin( ) != null )
        {
            sbSql.append( bIsFirst ? CONSTANT_WHERE : CONSTANT_AND );
            sbSql.append( SQL_FILTER_DATE_CREATION_MIN );
            bIsFirst = false;
        }
        if ( filter.getDateCreationMax( ) != null )
        {
            sbSql.append( bIsFirst ? CONSTANT_WHERE : CONSTANT_AND );
            sbSql.append( SQL_FILTER_DATE_CREATION_MAX );
            bIsFirst = false;
        }

        DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin );

        int nIndex = 1;
        if ( filter.getIdProvider( ) != null )
        {
            daoUtil.setString( nIndex++, filter.getIdProvider( ) );
        }
        if ( filter.getIdResource( ) != null )
        {
            daoUtil.setString( nIndex++, filter.getIdResource( ) );
        }
        if ( filter.getIdUser( ) != null )
        {
            daoUtil.setString( nIndex++, filter.getIdUser( ) );
        }
        if ( filter.getIdLot( ) > 0 )
        {
            daoUtil.setInt( nIndex++, filter.getIdLot( ) );
        }
        if ( filter.getResourceType( ) != null )
        {
            daoUtil.setString( nIndex++, filter.getResourceType( ) );
        }
        if ( filter.getItemPrice( ) > 0 )
        {
            daoUtil.setInt( nIndex++, (int) ( filter.getItemPrice( ) * 100d ) );
        }
        if ( filter.getDateCreationMin( ) != null )
        {
            daoUtil.setDate( nIndex++, new Date( filter.getDateCreationMin( ).getTime( ) ) );
        }
        if ( filter.getDateCreationMax( ) != null )
        {
            // We dont increment the last index because we wont use it
            daoUtil.setDate( nIndex, new Date( filter.getDateCreationMax( ).getTime( ) ) );
        }

        daoUtil.executeQuery( );

        List<ShoppingCartItem> listItems = new ArrayList<ShoppingCartItem>( );
        while ( daoUtil.next( ) )
        {
            ShoppingCartItem shoppingItem = new ShoppingCartItem( );
            shoppingItem.setIdItem( daoUtil.getInt( 1 ) );
            shoppingItem.setIdProvider( daoUtil.getString( 2 ) );
            shoppingItem.setIdLot( daoUtil.getInt( 3 ) );
            shoppingItem.setIdUser( daoUtil.getString( 4 ) );
            shoppingItem.setResourceType( daoUtil.getString( 5 ) );
            shoppingItem.setIdResource( daoUtil.getString( 6 ) );
            shoppingItem.setItemPrice( daoUtil.getDouble( 7 ) );
            shoppingItem.setDateCreation( daoUtil.getDate( 8 ) );
        }

        daoUtil.free( );
        return listItems;
    }

}
