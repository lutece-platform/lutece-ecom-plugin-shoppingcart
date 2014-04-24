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
package fr.paris.lutece.plugins.shoppingcart.business;

/**
 * DTO for shopping cart items
 */
public class ShoppingCartItemDTO extends ShoppingCartItem
{
    private static final long serialVersionUID = -151748226768049873L;

    private String _strDescription;
    private String _strModificationUrl;

    /**
     * Creates a new DTO from values of an item
     * @param item The item to get values of
     */
    public ShoppingCartItemDTO( ShoppingCartItem item )
    {
        setIdItem( item.getIdItem( ) );
        setIdLot( item.getIdLot( ) );
        setResourceType( item.getResourceType( ) );
        setIdResource( item.getIdResource( ) );
        setItemPrice( item.getItemPrice( ) );
        setDateCreation( item.getDateCreation( ) );
    }

    /**
     * Get the description of the item
     * @return The description of the item
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Set the description of the item
     * @param strDescription The description of the item
     */
    public void setDescription( String strDescription )
    {
        this._strDescription = strDescription;
    }

    /**
     * Get the URL to modify the item, or null if the item can not be modified
     * @return The URL to modify the item, or null if the item can not be
     *         modified
     */
    public String getModificationUrl( )
    {
        return _strModificationUrl;
    }

    /**
     * Set the URL to modify the item
     * @param strModificationUrl The URL to modify the item, or null if the item
     *            can not be modified
     */
    public void setModificationUrl( String strModificationUrl )
    {
        this._strModificationUrl = strModificationUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object o )
    {
        return super.equals( o );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode( )
    {
        return super.hashCode( );
    }
}