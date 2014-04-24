/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import java.util.List;


/**
 * Interface for shopping cart persistence services
 */
public interface IShoppingCartPersistenceService
{
    /**
     * Save a shopping cart item
     * @param item The item to save
     */
    void saveItem( ShoppingCartItem item );

    /**
     * Check whether this service support anonymous users
     * @return True if the service support anonymous users, false otherwise
     */
    boolean supportAnonymousUsers( );

    /**
     * Find items by filter
     * @param filter The filter that items must match
     * @return The list of items that match the current filter, or an empty list
     *         if no items was found. This method can also return null if the
     *         context is not correct.
     */
    List<ShoppingCartItem> findItemsByFilter( ShoppingCartItemFilter filter );

    /**
     * Get the items of a given user
     * @param strUserName The name of the user to get the items of
     * @return The list of items, or an empty list if no items was found. This
     *         method can also return null if the context is not correct.
     */
    List<ShoppingCartItem> getItemsOfUser( String strUserName );

    /**
     * Get the name of the persistence service
     * @return The name of the persistence service
     */
    String getServiceName( );

    /**
     * Remove an item from the shopping cart of a user
     * @param strUserName The name of the user to remove the item of
     * @param nIdShoppingCartItem The id of the item to remove
     * @param bNotifyProviderService True to notify the provider service, false
     *            otherwise
     */
    void removeItemFromUserShoppingCart( String strUserName, int nIdShoppingCartItem, boolean bNotifyProviderService );

    /**
     * Remove every shopping cart items of a given user
     * @param strUserName The name of the user to remove items of
     * @param bNotifyProviderService True to notify the provider service, false
     *            otherwise
     */
    void emptyShoppingCartOfUser( String strUserName, boolean bNotifyProviderService );

    /**
     * Get a shopping cart item from its id
     * @param nIdShoppingCartItem The id of the shopping cart item
     * @return The shopping cart item, or null if the item was not found
     */
    ShoppingCartItem findItemById( int nIdShoppingCartItem );
}
