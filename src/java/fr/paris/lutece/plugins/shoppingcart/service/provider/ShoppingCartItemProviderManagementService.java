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
package fr.paris.lutece.plugins.shoppingcart.service.provider;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.commons.lang.StringUtils;


/**
 * Management class for shopping cart item providers
 */
public final class ShoppingCartItemProviderManagementService
{
    /**
     * Default constructor
     */
    private ShoppingCartItemProviderManagementService( )
    {
        // Do nothing
    }

    /**
     * Notify the provider of the item that the item will be removed from the
     * shopping cart of the user
     * @param item The item that will be removed
     */
    public static void notifyItemRemoval( ShoppingCartItem item )
    {
        for ( IShoppingCartItemProviderService providerService : SpringContextService
                .getBeansOfType( IShoppingCartItemProviderService.class ) )
        {
            if ( StringUtils.equals( item.getIdProvider( ), providerService.getProviderId( ) ) )
            {
                providerService.notifyItemRemoval( item.getResourceType( ), item.getIdResource( ) );
                return;
            }
        }
    }

    /**
     * Notify the provider of the item that the item has been successfully
     * validated.
     * @param item The validated item
     */
    public static void notifyItemValidation( ShoppingCartItem item )
    {
        for ( IShoppingCartItemProviderService providerService : SpringContextService
                .getBeansOfType( IShoppingCartItemProviderService.class ) )
        {
            if ( StringUtils.equals( item.getIdProvider( ), providerService.getProviderId( ) ) )
            {
                providerService.notifyItemValidation( item.getResourceType( ), item.getIdResource( ) );
                return;
            }
        }
    }

    /**
     * Get the description of a resource of the shopping cart
     * @param strProvider The provider of the resource
     * @param strResourceType The type of the resource
     * @param strResourceId The id of the resource
     * @return The description of the resource, or null if the provider service
     *         was not found
     */
    public static String getItemDescription( String strProvider, String strResourceType, String strResourceId )
    {
        for ( IShoppingCartItemProviderService providerService : SpringContextService
                .getBeansOfType( IShoppingCartItemProviderService.class ) )
        {
            if ( StringUtils.equals( strProvider, providerService.getProviderId( ) ) )
            {
                return providerService.getItemDescription( strResourceType, strResourceId );
            }
        }
        AppLogService.error( "Shopping cart provider service not found : " + strProvider + " for resource of type "
                + strResourceType + " and with id " + strResourceId, new AppException( ) );
        return null;
    }

    /**
     * Get the modification URL of a resource of the shopping cart
     * @param strProvider The provider of the resource
     * @param strResourceType The type of the resource
     * @param strResourceId The id of the resource
     * @return The modification URL of the resource, or null if the resource can
     *         not if be modified or provider service was not found
     */
    public static String getItemModificationUrl( String strProvider, String strResourceType, String strResourceId )
    {
        for ( IShoppingCartItemProviderService providerService : SpringContextService
                .getBeansOfType( IShoppingCartItemProviderService.class ) )
        {
            if ( StringUtils.equals( strProvider, providerService.getProviderId( ) ) )
            {
                return providerService.getItemModificationUrl( strResourceType, strResourceId );
            }
        }
        AppLogService.error( "Shopping cart provider service not found : " + strProvider + " for resource of type "
                + strResourceType + " and with id " + strResourceId );
        return null;
    }

}
