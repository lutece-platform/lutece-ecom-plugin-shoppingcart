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
 * Filter for {@link ShoppingCartItem}
 */
public class ShoppingCartItemFilter
{
    private String _strIdProvider;
    private int _nIdLot;
    private String _strIdUser;
    private String _strResourceType;
    private String _strIdResource;
    private double _dItemPrice;
    private Date _dateCreationMin;
    private Date _dateCreationMax;

    /**
     * Returns the id of the service that provides the item
     * @return The id of the service that provides the item
     */
    public String getIdProvider( )
    {
        return _strIdProvider;
    }

    /**
     * Sets the id of the service that provides the item
     * @param strIdProvider The id of the service that provides the item
     */
    public void setIdProvider( String strIdProvider )
    {
        _strIdProvider = strIdProvider;
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
     * Returns the id of the user
     * @return The id of the user
     */
    public String getIdUser( )
    {
        return _strIdUser;
    }

    /**
     * Sets the id of the user
     * @param strIdUser The id of the user
     */
    public void setIdUser( String strIdUser )
    {
        _strIdUser = strIdUser;
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
     * Sets the ItemPrice
     * @param dItemPrice The ItemPrice
     */
    public void setItemPrice( double dItemPrice )
    {
        _dItemPrice = dItemPrice;
    }

    /**
     * Get the minimum creation date of this filter
     * @return The minimum creation date of this filter
     */
    public Date getDateCreationMin( )
    {
        return _dateCreationMin;
    }

    /**
     * Set the minimum creation date of this filter
     * @param dateCreationMin The minimum creation date of this filter
     */
    public void setDateCreationMin( Date dateCreationMin )
    {
        this._dateCreationMin = dateCreationMin;
    }

    /**
     * Get the maximum creation date of this filter
     * @return The maximum creation date of this filter
     */
    public Date getDateCreationMax( )
    {
        return _dateCreationMax;
    }

    /**
     * Set the maximum creation date of this filter
     * @param dateCreationMax the maximum creation date of this filter
     */
    public void setDateCreationMax( Date dateCreationMax )
    {
        this._dateCreationMax = dateCreationMax;
    }
}