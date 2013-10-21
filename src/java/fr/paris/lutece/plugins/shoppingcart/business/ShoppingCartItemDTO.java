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
package fr.paris.lutece.plugins.shoppingcart.business;

import java.util.Date;


/**
 * DTO for shopping cart items
 */
public class ShoppingCartItemDTO
{
    private int _nIdItem;
    private int _nIdLot;
    private String _strResourceType;
    private String _strIdResource;
    private double _dItemPrice;
    private Date _dateCreation;
    private String _strDescription;
    private String _strModificationUrl;

    /**
     * Returns the IdItem
     * @return The IdItem
     */
    public int getIdItem( )
    {
        return _nIdItem;
    }

    /**
     * Sets the IdItem
     * @param nIdItem The IdItem
     */
    public void setIdItem( int nIdItem )
    {
        _nIdItem = nIdItem;
    }

    /**
     * Returns the id of the lot
     * @return The id of the lot
     */
    public int getIdLot( )
    {
        return _nIdLot;
    }

    /**
     * Sets the id of the lot
     * @param nIdLot The id of the lot
     */
    public void setIdLot( int nIdLot )
    {
        _nIdLot = nIdLot;
    }

    /**
     * Returns the type of the resource added to the shopping cart
     * @return The type of the resource added to the shopping cart
     */
    public String getResourceType( )
    {
        return _strResourceType;
    }

    /**
     * Sets the type of the resource added to the shopping cart
     * @param strResourceType The type of the resource added to the shopping
     *            cart
     */
    public void setResourceType( String strResourceType )
    {
        _strResourceType = strResourceType;
    }

    /**
     * Returns the id of the resource added to the shopping cart
     * @return The id of the resource added to the shopping cart
     */
    public String getIdResource( )
    {
        return _strIdResource;
    }

    /**
     * Sets the id of the resource added to the shopping cart
     * @param strIdResource The id of the resource added to the shopping cart
     */
    public void setIdResource( String strIdResource )
    {
        _strIdResource = strIdResource;
    }

    /**
     * Returns the price of the item
     * @return The price of the item
     */
    public double getItemPrice( )
    {
        return _dItemPrice;
    }

    /**
     * Sets the price of the item
     * @param dItemPrice The price of the item
     */
    public void setItemPrice( double dItemPrice )
    {
        _dItemPrice = dItemPrice;
    }

    /**
     * Get the creation date of this shopping item
     * @return The creation date of this shopping item
     */
    public Date getDateCreation( )
    {
        return _dateCreation;
    }

    /**
     * Set the creation date of this shopping item
     * @param dateCreation The creation date of this shopping item
     */
    public void setDateCreation( Date dateCreation )
    {
        this._dateCreation = dateCreation;
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
}