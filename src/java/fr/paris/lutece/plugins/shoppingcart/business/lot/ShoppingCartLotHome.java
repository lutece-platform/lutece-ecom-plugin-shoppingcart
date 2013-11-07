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
package fr.paris.lutece.plugins.shoppingcart.business.lot;

import fr.paris.lutece.plugins.shoppingcart.service.ShoppingCartPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;


/**
 * Home for shopping cart lot
 */
public final class ShoppingCartLotHome
{
    private static IShoppingCartLotDAO _dao = SpringContextService.getBean( "shoppingcart.shoppingCartLoDAO" );
    private static Plugin _plugin = PluginService.getPlugin( ShoppingCartPlugin.PLUGIN_NAME );

    /**
     * Private constructor
     */
    private ShoppingCartLotHome( )
    {
        // Do nothing
    }

    /**
     * Get the last id lot used by a user
     * @param strUserName The name of the user to get the lot id of
     * @return The last id lot used by a user, or 0 if the user is not
     *         associated with any lot
     */
    public static int getLastIdlotOfUser( String strUserName )
    {
        return _dao.getLastIdlotOfUser( strUserName, _plugin );
    }

    /**
     * Get a new id lot for a user. The returned id lot will be reserved for the
     * given user and can not be used by any other user
     * @param strUserName The name of the user to get the lot id for
     * @return The id lot
     */
    public static int getNewIdLotForUser( String strUserName )
    {
        return _dao.getNewIdLotForUser( strUserName, _plugin );
    }

    /**
     * Get the name of the user associated with a given lot
     * @param nIdLot The id of the lot to get the name of the user associated
     *            with
     * @return The name of the user, or null if the lot was not found
     */
    public static String getUserNameFromIdLot( int nIdLot )
    {
        return _dao.getUserNameFromIdLot( nIdLot, _plugin );
    }
}
