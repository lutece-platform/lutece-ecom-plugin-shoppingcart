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

import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;


/**
 * This class provides instances management methods for ShoppingCartPortlet
 * objects
 */
public class ShoppingCartPortletHome extends PortletHome
{
    // Static variable pointed at the DAO instance
    private static IShoppingCartPortletDAO _dao = (IShoppingCartPortletDAO) SpringContextService
            .getBean( "shoppingcart.ShoppingCartPortletDAO" );

    /* This class implements the Singleton design pattern. */
    private static volatile ShoppingCartPortletHome _singleton;

    /**
     * Constructor
     */
    private ShoppingCartPortletHome( )
    {
    }

    /**
     * Returns the identifier of the portlet type
     * 
     * @return the portlet type identifier
     */
    public String getPortletTypeId( )
    {
        String strCurrentClassName = this.getClass( ).getName( );
        String strPortletTypeId = PortletTypeHome.getPortletTypeId( strCurrentClassName );

        return strPortletTypeId;
    }

    /**
     * Returns the instance of ShoppingCartPortlet Portlet
     * 
     * @return the ShoppingCartPortlet Portlet instance
     */
    public static PortletHome getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = new ShoppingCartPortletHome( );
        }

        return _singleton;
    }

    /**
     * Returns the instance of the portlet DAO singleton
     * 
     * @return the instance of the DAO singleton
     */
    public IPortletInterfaceDAO getDAO( )
    {
        return _dao;
    }

}
