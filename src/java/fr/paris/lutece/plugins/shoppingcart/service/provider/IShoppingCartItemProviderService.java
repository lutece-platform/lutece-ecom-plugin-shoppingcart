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
package fr.paris.lutece.plugins.shoppingcart.service.provider;

/**
 * Interface for shopping cart item provider services
 */
public interface IShoppingCartItemProviderService
{
    /**
     * Notify the provider that the resource will be removed from the
     * shopping cart of the user
     * @param strResourceType The type of the removed resource
     * @param strResourceId The id of the removed resource
     */
    void notifyItemRemoval( String strResourceType, String strResourceId );

    /**
     * Notify the provider that the resource has been successfully validated
     * @param strResourceType The type of the validated resource
     * @param strResourceId The id of the validated resource
     */
    void notifyItemValidation( String strResourceType, String strResourceId );

    /**
     * Get the unique identifier of this provider service
     * @return The unique identifier of this provider service
     */
    String getProviderId( );

    /**
     * Get the description of a resource. The description can be HTML code.
     * @param strResourceType The type of
     * @param strResourceId The id of the resource to get the description of
     * @return The description of the resource
     */
    String getItemDescription( String strResourceType, String strResourceId );

    /**
     * Get the URL to modify a resource if it can be modified
     * @param strResourceType The type of the resource to modify
     * @param strResourceId The id of the resource to modify
     * @return The URL to modify the resource, or null if the resource can not
     *         be modified
     */
    String getItemModificationUrl( String strResourceType, String strResourceId );
}
