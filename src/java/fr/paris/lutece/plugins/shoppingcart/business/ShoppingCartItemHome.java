/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.plugins.shoppingcart.service.ShoppingCartPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for
 * ShoppingItem objects
 */

public final class ShoppingCartItemHome
{

    // Static variable pointed at the DAO instance

    private static IShoppingCartItemDAO _dao = SpringContextService.getBean( "shoppingcart.shoppingCartItemDAO" );
    private static Plugin _plugin = PluginService.getPlugin( ShoppingCartPlugin.PLUGIN_NAME );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ShoppingCartItemHome( )
    {
    }

    /**
     * Create an instance of the shoppingItem class
     * @param item The item to create
     */
    public static void create( ShoppingCartItem item )
    {
        if ( item.getDateCreation( ) == null )
        {
            item.setDateCreation( new Date( ) );
        }
        _dao.insert( item, _plugin );
    }

    /**
     * Update of the shoppingItem which is specified in parameter
     * @param item The instance of the ShoppingItem which contains the
     *            data to store
     */
    public static void update( ShoppingCartItem item )
    {
        _dao.store( item, _plugin );
    }

    /**
     * Remove the shoppingItem whose identifier is specified in parameter
     * @param nShoppingItemId The shoppingItem Id
     */
    public static void remove( int nShoppingItemId )
    {
        _dao.delete( nShoppingItemId, _plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a shoppingItem whose identifier is specified in
     * parameter
     * @param nKey The shoppingItem primary key
     * @return an instance of ShoppingItem
     */
    public static ShoppingCartItem findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Get the list of items that match the given filter
     * @param filter The filter
     * @return The list of item, or an empty list if no item was found
     */
    public static List<ShoppingCartItem> findByFilter( ShoppingCartItemFilter filter )
    {
        return _dao.findByFilter( filter, _plugin );
    }

    /**
     * Load the data of all the shoppingItem objects and returns them in form of
     * a collection
     * @return the collection which contains the data of all the shoppingItem
     *         objects
     */
    public static Collection<ShoppingCartItem> getShoppingItemsList( )
    {
        return _dao.selectShoppingItemsList( _plugin );
    }
}
