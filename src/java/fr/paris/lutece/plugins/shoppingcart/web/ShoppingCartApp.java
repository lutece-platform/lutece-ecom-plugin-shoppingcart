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

package fr.paris.lutece.plugins.shoppingcart.web;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItemDTO;
import fr.paris.lutece.plugins.shoppingcart.service.ShoppingCartLotService;
import fr.paris.lutece.plugins.shoppingcart.service.ShoppingCartService;
import fr.paris.lutece.plugins.shoppingcart.service.provider.ShoppingCartItemProviderManagementService;
import fr.paris.lutece.plugins.shoppingcart.service.validator.IShoppingCartValidator;
import fr.paris.lutece.plugins.shoppingcart.service.validator.ShoppingCartValidatorService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * This class provides a simple implementation of an XPage
 */
@Controller( xpageName = "shoppingcart", pageTitleProperty = ShoppingCartApp.DEFAULT_PAGE_TITLE, pagePathProperty = ShoppingCartApp.DEFAULT_PAGE_PATH_LABEL )
public class ShoppingCartApp extends MVCApplication
{
    /**
     * Default page title
     */
    public static final String DEFAULT_PAGE_TITLE = "shoppingcart.myshoppingCart.pageTitle";

    /**
     * Default page path label
     */
    public static final String DEFAULT_PAGE_PATH_LABEL = "shoppingcart.myshoppingCart.pagePathLabel";

    /**
     * Datastore key of the URL to redirect the user to after the validation of
     * its shopping cart
     */
    public static final String DATASTORE_KEY_URL_BACK = "shoppingcart.urlBackAfterValidation";

    private static final long serialVersionUID = 8047167849115970508L;

    private static final String TEMPLATE_MY_SHOPPING_CART = "/skin/plugins/shoppingcart/my_shoppingcart.html";
    private static final String TEMPLATE_VALIDATE_MY_SHOPPING_CART = "/skin/plugins/shoppingcart/validate_shoppingcart.html";
    private static final String TEMPLATE_MY_SHOPPING_CART_VALIDATED = "/skin/plugins/shoppingcart/shoppingcart_validated.html";
    private static final String TEMPLATE_PORTELT_SHOPPING_CART = "/skin/plugins/shoppingcart/portlet/portlet_shoppingcart.html";

    private static final String VIEW_MY_SHOPPING_CART = "myShoppingCart";
    private static final String VIEW_VALIDATE_SHOPPING_CART = "validateShoppingCart";
    private static final String VIEW_CONFIRM_REMOVE_ITEM = "confirmRemoveItem";
    private static final String VIEW_CONFIRM_EMPTY_SHOPPING_CART = "confirmEmptyShoppingCart";

    private static final String ACTION_REMOVE_ITEM = "removeItem";
    private static final String ACTION_EMPTY_SHOPPING_CART = "emptyShoppingCart";
    private static final String ACTION_DO_VALIDATE_SHOPPING_CART = "doValidateShoppingCart";

    private static final String MESSAGE_CONFIRM_REMOVE_ITEM = "shoppingcart.myshoppingCart.confirmRemoveItem";
    private static final String MESSAGE_CONFIRM_EMPTY_SHOPPING_CART = "shoppingcart.myshoppingCart.confirmEmptyShoppingCart";

    private static final String MESSAGE_VALIDATE_SHOPPING_CART_PAGE_TITLE = "shoppingcart.validate_shoppingcart.pageTitle";
    private static final String MESSAGE_VALIDATE_SHOPPING_CART_PAGE_PATH_LABEL = "shoppingcart.validate_shoppingcart.pagePathLabel";
    private static final String MESSAGE_SHOPPING_CART_VALIDATED_PAGE_TITLE = "shoppingcart.shoppingcart_validated.pageTitle";
    private static final String MESSAGE_SHOPPING_CART_VALIDATED_PAGE_PATH_LABEL = "shoppingcart.shoppingcart_validated.pagePathLabel";
    private static final String MESSAGE_SHOPPING_CART_MODIFIED = "shoppingcart.validate_shoppingcart.error.shoppingCartModified";

    private static final String PARAMETER_ID_ITEM = "idItem";
    private static final String PARAMETER_VALIDATOR_ID = "validatorId";
    private static final String PARAMETER_KEY = "key";
    private static final String PARAMETER_CANCEL = "cancel";
    private static final String PARAMETER_REFERER = "referer";

    private static final String MARK_LIST_ITEMS = "list_items";
    private static final String MARK_HAS_PRICE = "has_price";
    private static final String MARK_VALIDATOR_CONTENT = "validator_content";
    private static final String MARK_URL_BACK = "url_back";
    private static final String MARK_REFERER = "shoppingcart.referer";

    private static final String PATH_PORTAL = "jsp/site/";

    /**
     * Returns the content of the shopping cart page.
     * @param request The HTTP request
     * @return The view
     */
    @View( value = VIEW_MY_SHOPPING_CART, defaultView = true )
    public XPage getViewMyShoppingCart( HttpServletRequest request )
    {
        XPage xpage = new XPage( );
        xpage.setContent( getHtmlViewMyShoppingCart( request, false ) );
        xpage.setPathLabel( I18nService.getLocalizedString( DEFAULT_PAGE_PATH_LABEL, request.getLocale( ) ) );
        xpage.setTitle( I18nService.getLocalizedString( DEFAULT_PAGE_TITLE, request.getLocale( ) ) );
        return xpage;
    }

    /**
     * Get the confirmation message before removing an item from the shopping
     * cart of the user
     * @param request The request
     * @return An XPage
     * @throws SiteMessageException The message to display
     */
    @View( VIEW_CONFIRM_REMOVE_ITEM )
    public XPage getConfirmRemoveItem( HttpServletRequest request ) throws SiteMessageException
    {
        String strIdItem = request.getParameter( PARAMETER_ID_ITEM );
        if ( StringUtils.isEmpty( strIdItem ) || !StringUtils.isNumeric( strIdItem ) )
        {
            return redirectView( request, VIEW_MY_SHOPPING_CART );
        }

        // We save the referer in session to redirect the user to it
        if ( StringUtils.isBlank( (String) request.getSession( ).getAttribute( MARK_REFERER ) ) )
        {
            request.getSession( ).setAttribute( MARK_REFERER, request.getHeader( PARAMETER_REFERER ) );
        }
        UrlItem url = new UrlItem( PATH_PORTAL + getActionUrl( ACTION_REMOVE_ITEM ) );
        url.addParameter( PARAMETER_ID_ITEM, strIdItem );
        SiteMessageService.setMessage( request, MESSAGE_CONFIRM_REMOVE_ITEM, SiteMessage.TYPE_CONFIRMATION,
                url.getUrl( ) );

        return null;
    }

    /**
     * Do remove a shopping cart item
     * @param request The request
     * @return An XPage
     */
    @Action( ACTION_REMOVE_ITEM )
    public XPage doRemoveItem( HttpServletRequest request )
    {
        String strIdItem = request.getParameter( PARAMETER_ID_ITEM );
        if ( StringUtils.isNotEmpty( strIdItem ) && StringUtils.isNumeric( strIdItem ) )
        {
            int nIdItem = Integer.parseInt( strIdItem );
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
            ShoppingCartService.getInstance( ).removeShoppingCartItem( user == null ? null : user.getName( ), nIdItem,
                    true );
        }
        String strReferer = (String) request.getSession( ).getAttribute( MARK_REFERER );
        if ( StringUtils.isNotBlank( strReferer ) )
        {
            request.getSession( ).removeAttribute( MARK_REFERER );
            return redirect( request, strReferer );
        }

        return redirectView( request, VIEW_MY_SHOPPING_CART );
    }

    /**
     * Get the confirmation message before removing every items of a shopping
     * cart
     * @param request The request
     * @return An empty XPage
     * @throws SiteMessageException A site message exception to display a site
     *             message
     */
    @View( VIEW_CONFIRM_EMPTY_SHOPPING_CART )
    public XPage getConfirmEmptyShoppingCart( HttpServletRequest request ) throws SiteMessageException
    {
        // We save the referer in session to redirect the user to it
        if ( StringUtils.isBlank( (String) request.getSession( ).getAttribute( MARK_REFERER ) ) )
        {
            request.getSession( ).setAttribute( MARK_REFERER, request.getHeader( PARAMETER_REFERER ) );
        }
        SiteMessageService.setMessage( request, MESSAGE_CONFIRM_EMPTY_SHOPPING_CART, SiteMessage.TYPE_CONFIRMATION,
                PATH_PORTAL + getActionUrl( ACTION_EMPTY_SHOPPING_CART ) );
        return null;
    }

    /**
     * Do empty the shopping cart of the current user
     * @param request The request
     * @return The XPage to display
     */
    @Action( ACTION_EMPTY_SHOPPING_CART )
    public XPage doEmptyShoppingCart( HttpServletRequest request )
    {
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        ShoppingCartService.getInstance( ).emptyShoppingCartOfUser( user != null ? user.getName( ) : null, true );

        String strReferer = (String) request.getSession( ).getAttribute( MARK_REFERER );
        if ( StringUtils.isNotBlank( strReferer ) )
        {
            request.getSession( ).removeAttribute( MARK_REFERER );
            return redirect( request, strReferer );
        }

        return redirectView( request, VIEW_MY_SHOPPING_CART );
    }

    /**
     * Get the validation form of the next validator for the shopping cart of
     * the user. If the next validator has no form, then validates it and
     * display the form of the next one. If there is no more validator to
     * perform, display the validation success page
     * @param request The request
     * @return The XPage to display
     * @throws SiteMessageException If a site message needs to be displayed
     */
    @View( VIEW_VALIDATE_SHOPPING_CART )
    public XPage getValidateShoppingCart( HttpServletRequest request ) throws SiteMessageException
    {
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        List<ShoppingCartItem> listItems = ShoppingCartService.getInstance( ).getShoppingCartOfUser( user );

        // We save the referer in session to redirect the user to it
        if ( StringUtils.isBlank( (String) request.getSession( ).getAttribute( MARK_REFERER ) ) )
        {
            request.getSession( ).setAttribute( MARK_REFERER, request.getHeader( PARAMETER_REFERER ) );
        }

        if ( listItems == null || listItems.size( ) == 0 )
        {
            return redirectView( request, VIEW_MY_SHOPPING_CART );
        }

        String strValidatorId = request.getParameter( PARAMETER_VALIDATOR_ID );

        if ( StringUtils.isNotEmpty( strValidatorId ) )
        {
            IShoppingCartValidator oldValidator = ShoppingCartValidatorService.getInstance( ).getValidator(
                    strValidatorId );

            String strOldGeneratedKey = oldValidator.getSecurityKeyForItems( listItems );
            if ( strValidatorId != null )
            {
                String strProvidedKey = request.getParameter( PARAMETER_KEY );
                // If the provided key is not correct, we redirect the user to the shopping cart management page
                if ( !StringUtils.equals( strProvidedKey, strOldGeneratedKey ) )
                {
                    SiteMessageService.setMessage( request, MESSAGE_SHOPPING_CART_MODIFIED, SiteMessage.TYPE_STOP,
                            getViewFullUrl( VIEW_MY_SHOPPING_CART ) );
                }
            }
        }

        IShoppingCartValidator validator = ShoppingCartValidatorService.getInstance( )
                .getNextValidator( strValidatorId );
        while ( validator != null && !validator.hasValidationForm( ) )
        {
            validateItemList( user, validator, listItems, request );
            validator = ShoppingCartValidatorService.getInstance( ).getNextValidator( validator.getValidatorId( ) );
        }

        if ( validator == null )
        {
            // If there is no more validations to perform
            boolean bHasPrice = false;
            List<ShoppingCartItemDTO> listDto = new ArrayList<ShoppingCartItemDTO>( listItems.size( ) );
            int nLastIdLot = 1;
            String strUserName = user != null && user.getName( ) != null ? user.getName( )
                    : LuteceUser.ANONYMOUS_USERNAME;
            for ( ShoppingCartItem item : listItems )
            {
                // We check that the user has
                if ( item.getIdLot( ) == ShoppingCartItem.NEW_ID_LOT_FOR_ANONYMOUS_USER )
                {
                    nLastIdLot = ShoppingCartLotService.getInstance( ).getNewIdLotForUser( strUserName );
                    item.setIdLot( nLastIdLot );
                }
                else if ( item.getIdLot( ) == ShoppingCartItem.LAST_ID_LOT_FOR_ANONYMOUS_USER )
                {
                    item.setIdLot( nLastIdLot );
                }

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

                ShoppingCartItemProviderManagementService.notifyItemValidation( item );
                // We now remove the item from the database without notifying the provider of the removal
                ShoppingCartService.getInstance( ).removeShoppingCartItem( item.getIdUser( ), item.getIdItem( ), false );
            }

            String strUrlBack = DatastoreService.getInstanceDataValue( DATASTORE_KEY_URL_BACK,
                    getViewFullUrl( VIEW_MY_SHOPPING_CART ) );

            // If no return url is set, we redirect the user where he came from
            if ( StringUtils.isEmpty( strUrlBack ) )
            {
                strUrlBack = (String) request.getSession( ).getAttribute( MARK_REFERER );
                request.getSession( ).removeAttribute( MARK_REFERER );
            }

            Map<String, Object> model = new HashMap<String, Object>( );
            model.put( MARK_LIST_ITEMS, listDto );
            model.put( MARK_HAS_PRICE, bHasPrice );
            model.put( MARK_URL_BACK, strUrlBack );

            return getXPage( TEMPLATE_MY_SHOPPING_CART_VALIDATED, request.getLocale( ), model,
                    MESSAGE_SHOPPING_CART_VALIDATED_PAGE_TITLE, MESSAGE_SHOPPING_CART_VALIDATED_PAGE_PATH_LABEL );
        }

        String strGeneratedKey = validator.getSecurityKeyForItems( listItems );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( PARAMETER_KEY, strGeneratedKey );
        model.put( PARAMETER_VALIDATOR_ID, validator.getValidatorId( ) );
        model.put( MARK_VALIDATOR_CONTENT, validator.getValidationFormHtml( listItems, request.getLocale( ) ) );
        //        model.put( MARK_VALIDATORS_CONTENT, strValidatorsContent );
        return getXPage( TEMPLATE_VALIDATE_MY_SHOPPING_CART, request.getLocale( ), model,
                MESSAGE_VALIDATE_SHOPPING_CART_PAGE_TITLE, MESSAGE_VALIDATE_SHOPPING_CART_PAGE_PATH_LABEL );
    }

    /**
     * Do validate a shopping cart with a validator specified in a request
     * parameter
     * @param request The request
     * @return the XPage to display
     * @throws SiteMessageException If an error occurs during the validation
     */
    @Action( ACTION_DO_VALIDATE_SHOPPING_CART )
    public XPage doValidateShoppingCart( HttpServletRequest request ) throws SiteMessageException
    {
        if ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_CANCEL ) ) )
        {
            String strReferer = (String) request.getSession( ).getAttribute( MARK_REFERER );
            if ( StringUtils.isNotBlank( strReferer ) )
            {
                request.getSession( ).removeAttribute( MARK_REFERER );
                return redirect( request, strReferer );
            }
            return redirectView( request, VIEW_MY_SHOPPING_CART );
        }

        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        List<ShoppingCartItem> listItems = ShoppingCartService.getInstance( ).getShoppingCartOfUser( user );

        if ( listItems == null || listItems.size( ) == 0 )
        {
            return redirectView( request, VIEW_MY_SHOPPING_CART );
        }

        String strValidatorId = request.getParameter( PARAMETER_VALIDATOR_ID );
        String strProvidedKey = request.getParameter( PARAMETER_KEY );

        IShoppingCartValidator validator = ShoppingCartValidatorService.getInstance( ).getValidator( strValidatorId );

        String strGeneratedKey = validator.getSecurityKeyForItems( listItems );

        // We check that the key is still good, ie we check that items to validate have not changed since the first step of the validation
        if ( !StringUtils.equals( strProvidedKey, strGeneratedKey ) )
        {
            SiteMessageService.setMessage( request, MESSAGE_SHOPPING_CART_MODIFIED, SiteMessage.TYPE_STOP,
                    getViewFullUrl( VIEW_MY_SHOPPING_CART ) );
        }

        validateItemList( user, validator, listItems, request );

        // If the validation succeeded, we redirect the user to the next validator form
        UrlItem url = new UrlItem( getViewUrl( VIEW_VALIDATE_SHOPPING_CART ) );
        // We provide the id of the last succeeded validator
        url.addParameter( PARAMETER_VALIDATOR_ID, strValidatorId );
        // We authenticate the URL
        url.addParameter( PARAMETER_KEY, strGeneratedKey );

        return redirect( request, url.getUrl( ) );
    }

    /**
     * Get the HTML code to display the content of the shopping cart of a user
     * @param request The request
     * @param bUsePortletTemplate True to use the template of the my shopping
     *            cart portlet, false to use the XPage template. If the HTML
     *            will be displayed in another page, then the portlet template
     *            should be used
     * @return The HTML code to display
     */
    public static String getHtmlViewMyShoppingCart( HttpServletRequest request, boolean bUsePortletTemplate )
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

        HtmlTemplate template = AppTemplateService.getTemplate( bUsePortletTemplate ? TEMPLATE_PORTELT_SHOPPING_CART
                : TEMPLATE_MY_SHOPPING_CART, request.getLocale( ), model );
        return template.getHtml( );
    }

    /**
     * Returns a new XPage object with the content filled by a template using a
     * given model and for a given locale
     * @param strTemplate The template to display
     * @param locale The locale to use
     * @param model The model containing parameters of the template
     * @param strPageTitleProperty The i18n property of the title of the page
     * @param strPagePathLabelProperty The i18n property of the page path label
     * @return The XPage
     * @see #getXPage(String, Locale, Map)
     */
    protected XPage getXPage( String strTemplate, Locale locale, Map<String, Object> model,
            String strPageTitleProperty, String strPagePathLabelProperty )
    {
        XPage page = super.getXPage( strTemplate, locale, model );
        page.setTitle( I18nService.getLocalizedString( strPageTitleProperty, locale ) );
        page.setPathLabel( I18nService.getLocalizedString( strPagePathLabelProperty, locale ) );
        return page;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected XPage getXPage( String strTemplate, Locale locale, Map<String, Object> model )
    {
        return getXPage( strTemplate, locale, model, DEFAULT_PAGE_TITLE, DEFAULT_PAGE_PATH_LABEL );
    }

    /**
     * Validate a list of items
     * @param user The user
     * @param validator The validator to use
     * @param listItems The list of items to validate
     * @param request The request
     * @throws SiteMessageException Throw a {@link SiteMessageException} if an
     *             error occurs during the validation
     */
    private void validateItemList( LuteceUser user, IShoppingCartValidator validator, List<ShoppingCartItem> listItems,
            HttpServletRequest request ) throws SiteMessageException
    {
        String strErrorKey = validator.validateShoppingCart( user, listItems, request.getParameterMap( ) );
        if ( strErrorKey != null )
        {
            String strRedirectUrl = (String) request.getSession( ).getAttribute( MARK_REFERER );
            if ( StringUtils.isNotBlank( strRedirectUrl ) )
            {
                request.getSession( ).removeAttribute( MARK_REFERER );
            }
            else
            {
                strRedirectUrl = getViewFullUrl( VIEW_MY_SHOPPING_CART );
            }
            SiteMessageService.setMessage( request, strErrorKey, SiteMessage.TYPE_STOP, strRedirectUrl );
        }
    }
}