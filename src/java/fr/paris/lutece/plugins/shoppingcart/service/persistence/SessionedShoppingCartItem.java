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
import fr.paris.lutece.plugins.shoppingcart.service.provider.ShoppingCartItemProviderManagementService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Class that manage shopping card items saved in an http session
 */
public class SessionedShoppingCartItem implements Serializable
{
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -4310290073260410209L;

    private boolean _bNotifyProvider;
    private List<ShoppingCartItem> _listItems;

    /**
     * Creates a new session removal listener for a list of items
     * @param listItems The list of items of this listener, or null to set no
     *            items
     * @param bNotifyProvider True to notify the provider service when the
     *            session expired, false otherwise
     */
    public SessionedShoppingCartItem( List<ShoppingCartItem> listItems, boolean bNotifyProvider )
    {
        this._bNotifyProvider = bNotifyProvider;
        this._listItems = listItems != null ? listItems : new ArrayList<ShoppingCartItem>( );
    }

    /**
     * Get the list of items associated with this listener. The returned object
     * is not a copy of the list of this class, and can therefore be modified.
     * @return The list of items of this listener. The returned list is never
     *         null
     */
    public List<ShoppingCartItem> getItemList( )
    {
        return _listItems;
    }

    /**
     * Set the notify provider flag
     * @param bNotifyProvider The notify provider flag
     */
    public void setNotifyProvider( boolean bNotifyProvider )
    {
        this._bNotifyProvider = bNotifyProvider;
    }

    /**
     * {@inheritDoc}
     */
    protected void finalize( ) throws Throwable
    {
        try
        {
            if ( _bNotifyProvider )
            {
                for ( ShoppingCartItem item : _listItems )
                {
                    ShoppingCartItemProviderManagementService.notifyItemRemoval( item );
                }
                _bNotifyProvider = false;
            }
        }
        catch ( Exception e )
        {
            // Since we are in a finalize method, we try/catch every exception to avoid error leak
            AppLogService.error( e.getMessage( ), e );
        }
        super.finalize( );
    }
}
