/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
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
package fr.paris.lutece.plugins.shoppingcart.business.portlet;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * this class provides Data Access methods for ShoppingCartPortlet objects
 */
public final class ShoppingCartPortletDAO implements IShoppingCartPortletDAO
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_portlet FROM shoppingcart_portlet WHERE id_portlet = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO shoppingcart_portlet ( id_portlet ) VALUES ( ? )";
    private static final String SQL_QUERY_DELETE = "DELETE FROM shoppingcart_portlet WHERE id_portlet = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE shoppingcart_portlet SET id_portlet = ? WHERE id_portlet = ? ";

    ///////////////////////////////////////////////////////////////////////////////////////
    // Access methods to data

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( Portlet portlet )
    {
        if ( portlet instanceof ShoppingCartPortlet )
        {
            ShoppingCartPortlet p = (ShoppingCartPortlet) portlet;
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
            daoUtil.setInt( 1, p.getId( ) );
            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Portlet portlet )
    {
        if ( portlet instanceof ShoppingCartPortlet )
        {
            ShoppingCartPortlet p = (ShoppingCartPortlet) portlet;
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
            daoUtil.setInt( 1, p.getId( ) );
            daoUtil.setInt( 2, p.getId( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Portlet load( int nIdPortlet )
    {
        ShoppingCartPortlet portlet = new ShoppingCartPortlet( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nIdPortlet );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            portlet.setId( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );

        return portlet;
    }

}
