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

package fr.paris.lutece.plugins.shoppingcart.web;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItemDTO;
import fr.paris.lutece.plugins.shoppingcart.service.ShoppingCartService;
import fr.paris.lutece.plugins.shoppingcart.service.provider.ShoppingCartItemProviderManagementService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * This class provides a simple implementation of an XPage
 */

@Controller( xpageName = "shoppingcart", pageTitleProperty = "shoppingcart.myshoppingCart.pageTitle", pagePathProperty = "shoppingcart.myshoppingCart.pagePathLabel" )
public class ShoppingCartApp extends MVCApplication
{
    private static final String TEMPLATE_MY_SHOPPING_CART = "/skin/plugins/shoppingcart/my_shoppingcart.html";
    private static final String VIEW_MY_SHOPPING_CART = "myShoppingCart";

    private static final String ACTION_CONFIRM_REMOVE_ITEM = "confirmRemoveItem";
    private static final String ACTION_REMOVE_ITEM = "removeItem";

    private static final String MESSAGE_CONFIRM_REMOVE_ITEM = "shoppingcart.myshoppingCart.confirmRemoveItem";

    private static final String PARAMETER_ID_ITEM = "idItem";

    private static final String MARK_LIST_ITEMS = "list_items";
    private static final String MARK_HAS_PRICE = "has_price";

    private static final String PATH_PORTAL = "jsp/site/";

    /**
     * Returns the content of the shopping cart page.
     * @param request The HTTP request
     * @return The view
     */
    @View( value = VIEW_MY_SHOPPING_CART, defaultView = true )
    public XPage viewHome( HttpServletRequest request )
    {
        //        ShoppingCartItem newItem = new ShoppingCartItem( );
        //        newItem.setIdLot( 1 );
        //        newItem.setIdResource( "3" );
        //        newItem.setIdProvider( "dummyProviderService" );
        //        newItem.setResourceType( "resource type" );
        //        newItem.setItemPrice( 0d );
        //        ShoppingCartService.getInstance( ).addItemToShoppingCart( newItem );

        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        List<ShoppingCartItem> listItems = ShoppingCartService.getInstance( ).getShoppingCartOfUser( user );

        List<ShoppingCartItemDTO> listDto = new ArrayList<ShoppingCartItemDTO>( listItems.size( ) );
        boolean bHasPrice = false;
        for ( ShoppingCartItem item : listItems )
        {
            ShoppingCartItemDTO itemDTO = new ShoppingCartItemDTO( item );
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
        return getXPage( TEMPLATE_MY_SHOPPING_CART, request.getLocale( ), model );
    }

    /**
     * Get the confirmation message before removing an item from the shopping
     * cart of the user
     * @param request The request
     * @return An XPage
     * @throws SiteMessageException The message to display
     */
    @Action( ACTION_CONFIRM_REMOVE_ITEM )
    public XPage getConfirmRemoveItem( HttpServletRequest request ) throws SiteMessageException
    {
        String strIdItem = request.getParameter( PARAMETER_ID_ITEM );
        if ( StringUtils.isEmpty( strIdItem ) || !StringUtils.isNumeric( strIdItem ) )
        {
            return redirectView( request, VIEW_MY_SHOPPING_CART );
        }

        UrlItem url = new UrlItem( PATH_PORTAL + getActionUrl( ACTION_REMOVE_ITEM ) );
        url.addParameter( PARAMETER_ID_ITEM, strIdItem );
        SiteMessageService.setMessage( request, MESSAGE_CONFIRM_REMOVE_ITEM, SiteMessage.TYPE_CONFIRMATION,
                url.getUrl( ) );

        return new XPage( );
    }

    /**
     * Do remove a shopping cart item
     * @param request The request
     * @return An XPage
     */
    @Action( ACTION_REMOVE_ITEM )
    public XPage removeItem( HttpServletRequest request )
    {
        String strIdItem = request.getParameter( PARAMETER_ID_ITEM );
        if ( StringUtils.isNotEmpty( strIdItem ) && StringUtils.isNumeric( strIdItem ) )
        {
            int nIdItem = Integer.parseInt( strIdItem );
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
            ShoppingCartService.getInstance( ).removeShoppingCartItem( user == null ? null : user.getName( ), nIdItem,
                    true );
        }
        return redirectView( request, VIEW_MY_SHOPPING_CART );
    }
}