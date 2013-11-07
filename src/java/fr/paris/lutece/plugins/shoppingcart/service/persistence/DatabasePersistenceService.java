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
package fr.paris.lutece.plugins.shoppingcart.service.persistence;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItemFilter;
import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItemHome;
import fr.paris.lutece.plugins.shoppingcart.service.provider.ShoppingCartItemProviderManagementService;

import java.util.List;


/**
 * Service to save shopping cart items into the database
 */
public class DatabasePersistenceService implements IShoppingCartPersistenceService
{
    private static final String SERVICE_NAME = "shoppingcart.seccionPersistenceService";

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveItem( ShoppingCartItem item )
    {
        ShoppingCartItemHome.create( item );
    }

    /**
     * {@inheritDoc}
     * @return false
     */
    @Override
    public boolean supportAnonymousUsers( )
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ShoppingCartItem> findItemsByFilter( ShoppingCartItemFilter filter )
    {
        return ShoppingCartItemHome.findByFilter( filter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ShoppingCartItem> getItemsOfUser( String strUserName )
    {
        ShoppingCartItemFilter filter = new ShoppingCartItemFilter( );
        filter.setIdUser( strUserName );
        return findItemsByFilter( filter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServiceName( )
    {
        return SERVICE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeItemFromUserShoppingCart( String strUserName, int nIdShoppingCartItem,
            boolean bNotifyProviderService )
    {
        if ( bNotifyProviderService )
        {
            ShoppingCartItem item = ShoppingCartItemHome.findByPrimaryKey( nIdShoppingCartItem );
            ShoppingCartItemProviderManagementService.notifyItemRemoval( item );
        }
        ShoppingCartItemHome.remove( nIdShoppingCartItem );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void emptyShoppingCartOfUser( String strUserName, boolean bNotifyProviderService )
    {
        ShoppingCartItemFilter filter = new ShoppingCartItemFilter( );
        filter.setIdUser( strUserName );
        List<ShoppingCartItem> listItems = findItemsByFilter( filter );
        if ( listItems != null )
        {
            for ( ShoppingCartItem item : listItems )
            {
                if ( bNotifyProviderService )
                {
                    ShoppingCartItemProviderManagementService.notifyItemRemoval( item );
                }
                ShoppingCartItemHome.remove( item.getIdItem( ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShoppingCartItem findItemById( int nIdShoppingCartItem )
    {
        return ShoppingCartItemHome.findByPrimaryKey( nIdShoppingCartItem );
    }

}
