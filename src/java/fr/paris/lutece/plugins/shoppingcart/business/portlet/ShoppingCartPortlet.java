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

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItemDTO;
import fr.paris.lutece.plugins.shoppingcart.service.ShoppingCartService;
import fr.paris.lutece.plugins.shoppingcart.service.provider.ShoppingCartItemProviderManagementService;
import fr.paris.lutece.portal.business.portlet.PortletHtmlContent;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business objects ShoppingCartPortlet
 */
public class ShoppingCartPortlet extends PortletHtmlContent
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String TEMPLATE_PORTELT_SHOPPING_CART = "/skin/plugins/shoppingcart/portlet/portlet_shoppingcart.html";

    private static final String MARK_LIST_ITEMS = "list_items";
    private static final String MARK_HAS_PRICE = "has_price";

    /**
     * Sets the identifier of the portlet type to value specified
     */
    public ShoppingCartPortlet( )
    {
        setPortletTypeId( ShoppingCartPortletHome.getInstance( ).getPortletTypeId( ) );
    }

    /**
     * Returns the HTML code of the ShoppingCartPortlet portlet with XML heading
     * 
     * @param request The HTTP servlet request
     * @return the HTML code of the ShoppingCartPortlet portlet
     */
    @Override
    public String getHtmlContent( HttpServletRequest request )
    {
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        List<ShoppingCartItem> listItems = ShoppingCartService.getInstance( ).getShoppingCartOfUser( user );

        List<ShoppingCartItemDTO> listDto = new ArrayList<ShoppingCartItemDTO>( listItems.size( ) );
        boolean bHasPrice = false;
        int nIdLot = 0;
        for ( ShoppingCartItem item : listItems )
        {
            ShoppingCartItemDTO itemDTO = new ShoppingCartItemDTO( item );
            if ( itemDTO.getIdLot( ) == ShoppingCartItem.NEW_ID_LOT_FOR_ANONYMOUS_USER )
            {
                itemDTO.setIdLot( ++nIdLot );
            }
            else if ( itemDTO.getIdLot( ) == ShoppingCartItem.LAST_ID_LOT_FOR_ANONYMOUS_USER )
            {
                itemDTO.setIdLot( nIdLot );
            }
            itemDTO.setDescription( ShoppingCartItemProviderManagementService.getItemDescription(
                    item.getIdProvider( ), item.getResourceType( ), item.getIdResource( ) ) );
            itemDTO.setModificationUrl( ShoppingCartItemProviderManagementService.getItemModificationUrl(
                    item.getIdProvider( ), item.getResourceType( ), item.getIdResource( ) ) );
            listDto.add( itemDTO );
            if ( itemDTO.getItemPrice( ) > 0d )
            {
                bHasPrice = true;
            }
        }

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_LIST_ITEMS, listDto );
        model.put( MARK_HAS_PRICE, bHasPrice );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PORTELT_SHOPPING_CART, request.getLocale( ),
                model );
        return template.getHtml( );
    }

    /**
     * Updates the current instance of the ShoppingCartPortlet object
     */
    public void update( )
    {
        ShoppingCartPortletHome.getInstance( ).update( this );
    }

    /**
     * Removes the current instance of the ShoppingCartPortlet object
     */
    @Override
    public void remove( )
    {
        ShoppingCartPortletHome.getInstance( ).remove( this );
    }

    /**
     * {@inheritDoc}
     * @return false
     */
    @Override
    public boolean canBeCachedForAnonymousUsers( )
    {
        return false;
    }

    /**
     * {@inheritDoc}
     * @return false
     */
    @Override
    public boolean canBeCachedForConnectedUsers( )
    {
        return false;
    }
}
