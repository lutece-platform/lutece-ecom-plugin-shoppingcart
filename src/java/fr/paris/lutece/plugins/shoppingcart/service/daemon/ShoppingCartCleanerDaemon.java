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
package fr.paris.lutece.plugins.shoppingcart.service.daemon;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItemFilter;
import fr.paris.lutece.plugins.shoppingcart.service.ShoppingCartService;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * Daemon to remove outdated items
 */
public class ShoppingCartCleanerDaemon extends Daemon
{
    /**
     * Datastore key of the parameter that contains the number of days to wait
     * before removing shopping cart items saved in the database
     */
    public static final String DATASTORE_KEY_NB_DAYS_BEFORE_CLEANING = "shoppingcart.nbHoursBeforeCleaning";

    private static final String MESSAGE_ITEMS_HAVE_INFINITE_LIFE_TIME = "No item to remove : items have an infinite life time";
    private static final String MESSAGE_NO_EXPIRED_ITEM_TO_REMOVE = "No expired item to remove";
    private static final String MESSAGE_ITEMS_HAS_BEEN_REMOVED = " expired items have been removed";

    private static final String CONSTANT_ZERO = "0";

    /**
     * Remove outdated items of user's shopping carts
     */
    @Override
    public void run( )
    {
        String strNbDaysBeforeCleaning = DatastoreService.getInstanceDataValue( DATASTORE_KEY_NB_DAYS_BEFORE_CLEANING,
                CONSTANT_ZERO );

        if ( StringUtils.isNumeric( strNbDaysBeforeCleaning ) )
        {
            int nNbDayBeforeCleaning = 0;
            if ( StringUtils.isNotEmpty( strNbDaysBeforeCleaning ) )
            {
                nNbDayBeforeCleaning = Integer.parseInt( strNbDaysBeforeCleaning );
            }
            if ( nNbDayBeforeCleaning > 0 )
            {
                Calendar calendar = GregorianCalendar.getInstance( );
                calendar.add( GregorianCalendar.DAY_OF_MONTH, nNbDayBeforeCleaning * -1 );
                ShoppingCartItemFilter filter = new ShoppingCartItemFilter( );
                filter.setDateCreationMax( calendar.getTime( ) );
                List<ShoppingCartItem> listItemsToRemove = ShoppingCartService.getInstance( )
                        .getShoppingCartItemsByFilter( filter );
                if ( listItemsToRemove != null && listItemsToRemove.size( ) > 0 )
                {
                    for ( ShoppingCartItem item : listItemsToRemove )
                    {
                        ShoppingCartService.getInstance( ).removeShoppingCartItem( item.getIdUser( ),
                                item.getIdItem( ), true );
                    }
                    setLastRunLogs( listItemsToRemove.size( ) + MESSAGE_ITEMS_HAS_BEEN_REMOVED );
                }
                else
                {
                    setLastRunLogs( MESSAGE_NO_EXPIRED_ITEM_TO_REMOVE );
                }
            }
            else
            {
                setLastRunLogs( MESSAGE_ITEMS_HAVE_INFINITE_LIFE_TIME );
            }
        }
        else
        {
            AppLogService.error( "Non-integer value for the number of days before items cleaning : "
                    + strNbDaysBeforeCleaning );
            setLastRunLogs( MESSAGE_ITEMS_HAVE_INFINITE_LIFE_TIME );
        }
    }

}
