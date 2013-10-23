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

import fr.paris.lutece.plugins.shoppingcart.service.daemon.ShoppingCartCleanerDaemon;
import fr.paris.lutece.plugins.shoppingcart.service.validator.IShoppingCartValidator;
import fr.paris.lutece.plugins.shoppingcart.service.validator.ShoppingCartValidatorService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


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

    private static final String VIEW_MANAGE_SHOPPING_CART = "manageShoppingCart";
    private static final String ACTION_DO_MODIFY_VALIDATOR_ORDER = "doModifyValidatorOrder";
    private static final String ACTION_DO_MODIFY_VALIDATOR_ENABLING = "doModifyValidatorEnabling";
    private static final String ACTION_DO_MODIFY_SHOPPING_CART_PARAMETERS = "doModifyShoppingCartParameters";

    private static final String PROPERTY_PAGE_TITLE_MANAGE_SHOPPINGCART = "shoppingcart.manage_shoppingcart.pageTitle";

    private static final String MESSAGE_ITEMS_LIFE_TIME_NOT_INTEGER = "shoppingcart.manage_shoppingcart.error.itemsLifeTimeNotInteger";

    private static final String MARK_NB_DAYS_BEFORE_CLEANING = "nbDaysBeforeCleaning";
    private static final String MARK_VALIDATORS = "validators";
    private static final String MARK_LIFE_TIME = "lifeTime";
    private static final String MARK_BACK_URL = "back_url";

    private static final String PARAMETER_VALIDATOR_ID = "validatorId";
    private static final String PARAMETER_NEW_ORDER = "newOrder";
    private static final String PARAMETER_ENABLE = "enable";
    private static final String PARAMETER_LIFE_TIME = "lifeTime";

    // Templates
    private static final String TEMPLATE_MANAGE_SHOPPINGCART = "admin/plugins/shoppingcart/manage_shoppingcart.html";

    /**
     * Get the manage shopping cart page
     * @param request The request
     * @return The HTML content to display
     */
    @View( value = VIEW_MANAGE_SHOPPING_CART, defaultView = true )
    public String getManageShoppingCart( HttpServletRequest request )
    {
        String strNbDaysBeforeCleaning = DatastoreService.getInstanceDataValue(
                ShoppingCartCleanerDaemon.DATASTORE_KEY_NB_HOURS_BEFORE_CLEANING, "" );
        List<IShoppingCartValidator> listValidators = ShoppingCartValidatorService.getInstance( ).getValidatorlist( );

        String strLifeTime = DatastoreService.getDataValue(
                ShoppingCartCleanerDaemon.DATASTORE_KEY_NB_HOURS_BEFORE_CLEANING, StringUtils.EMPTY );

        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_NB_DAYS_BEFORE_CLEANING, strNbDaysBeforeCleaning );
        model.put( MARK_VALIDATORS, listValidators );
        model.put( MARK_LIFE_TIME, strLifeTime );
        model.put( MARK_BACK_URL,
                DatastoreService.getDataValue( ShoppingCartApp.DATASTORE_KEY_URL_BACK, StringUtils.EMPTY ) );
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_SHOPPINGCART, TEMPLATE_MANAGE_SHOPPINGCART, model );
    }

    /**
     * Do modify the order of a validator
     * @param request The request
     * @return The next URL to redirect to
     */
    @Action( value = ACTION_DO_MODIFY_VALIDATOR_ORDER )
    public String doModifyValidatorOrder( HttpServletRequest request )
    {
        String strValidatorId = request.getParameter( PARAMETER_VALIDATOR_ID );
        String strOrder = request.getParameter( PARAMETER_NEW_ORDER );

        if ( StringUtils.isNotEmpty( strValidatorId ) && StringUtils.isNotEmpty( strOrder )
                && StringUtils.isNumeric( strOrder ) )
        {
            int nNewOrder = Integer.parseInt( strOrder );
            ShoppingCartValidatorService.getInstance( ).modifyValidatorOrder( strValidatorId, nNewOrder );
        }

        return redirectView( request, VIEW_MANAGE_SHOPPING_CART );
    }

    /**
     * Do modify the order of a validator
     * @param request The request
     * @return The next URL to redirect to
     */
    @Action( value = ACTION_DO_MODIFY_VALIDATOR_ENABLING )
    public String doModifyValidatorEnabling( HttpServletRequest request )
    {
        String strValidatorId = request.getParameter( PARAMETER_VALIDATOR_ID );
        boolean bEnable = Boolean.valueOf( request.getParameter( PARAMETER_ENABLE ) );

        if ( StringUtils.isNotEmpty( strValidatorId ) )
        {
            ShoppingCartValidatorService.getInstance( ).changeValidatorEnabling( strValidatorId, bEnable );
        }
        return redirectView( request, VIEW_MANAGE_SHOPPING_CART );
    }

    /**
     * Do modify the life time of shopping cart items saved in the database
     * @param request The request
     * @return The next URL to redirect to
     */
    @Action( ACTION_DO_MODIFY_SHOPPING_CART_PARAMETERS )
    public String doModifyShoppingCartParameters( HttpServletRequest request )
    {
        String strLifeTime = request.getParameter( PARAMETER_LIFE_TIME );

        if ( !StringUtils.isNumeric( strLifeTime ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_ITEMS_LIFE_TIME_NOT_INTEGER,
                    AdminMessage.TYPE_STOP ) );
        }
        if ( StringUtils.isEmpty( strLifeTime ) )
        {
            strLifeTime = Integer.toString( 0 );
        }
        DatastoreService.setDataValue( ShoppingCartCleanerDaemon.DATASTORE_KEY_NB_HOURS_BEFORE_CLEANING, strLifeTime );

        String strUrlBack = request.getParameter( MARK_BACK_URL );
        DatastoreService.setDataValue( ShoppingCartApp.DATASTORE_KEY_URL_BACK, strUrlBack );

        return redirectView( request, VIEW_MANAGE_SHOPPING_CART );
    }
}
