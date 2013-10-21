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

import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * ManageShoppingCartValidators JSP Bean
 */
@Controller( controllerJsp = "ManageShoppingCart.jsp", controllerPath = "jsp/admin/plugins/shoppingcart/", right = ManageShoppingCartJspBean.RIGHT_MANAGE_SHOPPING_CART )
public class ManageShoppingCartJspBean extends MVCAdminJspBean
{

    // Right
    /**
     * Right associated with the JspBean
     */
    public static final String RIGHT_MANAGE_SHOPPING_CART = "SHOPPINGCART_MANAGEMENT";

    private static final long serialVersionUID = 1958567840099955905L;

    private static final String PROPERTY_PAGE_TITLE_MANAGE_SHOPPINGCART = "shoppingcart.manage_shoppingcart.pageTitle";

    // Templates
    private static final String TEMPLATE_MANAGE_SHOPPINGCART = "admin/plugins/shoppingcart/manage_shoppingcart.html";

    /**
     * Get the manage shopping cart page
     * @param request The request
     * @return The HTML content to display
     */
    public String getManageShoppingCart( HttpServletRequest request )
    {

        Map<String, Object> model = new HashMap<String, Object>( );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_SHOPPINGCART, TEMPLATE_MANAGE_SHOPPINGCART, model );
    }
}
