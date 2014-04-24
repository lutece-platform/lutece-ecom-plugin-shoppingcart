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

package fr.paris.lutece.plugins.shoppingcart.business;

import fr.paris.lutece.test.LuteceTestCase;


/**
 * Test class for business package
 */
public class ShoppingItemBusinessTest extends LuteceTestCase
{
    private final static int IDITEM1 = 1;
    private final static int IDITEM2 = 2;
    private final static String IDPROVIDER1 = "IdProvider1";
    private final static String IDPROVIDER2 = "IdProvider2";
    private final static int IDLOT1 = 1;
    private final static int IDLOT2 = 2;
    private final static String IDUSER1 = "1";
    private final static String IDUSER2 = "2";
    private final static String RESOURCETYPE1 = "ResourceType1";
    private final static String RESOURCETYPE2 = "ResourceType2";
    private final static String IDRESOURCE1 = "IdResource1";
    private final static String IDRESOURCE2 = "IdResource2";
    private final static Double ITEMPRICE1 = 1d;
    private final static Double ITEMPRICE2 = 2d;

    /**
     * Test the business package
     */
    public void testBusiness( )
    {
        // Initialize an object
        ShoppingCartItem shoppingItem = new ShoppingCartItem( );
        shoppingItem.setIdItem( IDITEM1 );
        shoppingItem.setIdProvider( IDPROVIDER1 );
        shoppingItem.setIdLot( IDLOT1 );
        shoppingItem.setIdUser( IDUSER1 );
        shoppingItem.setResourceType( RESOURCETYPE1 );
        shoppingItem.setIdResource( IDRESOURCE1 );
        shoppingItem.setItemPrice( ITEMPRICE1 );

        // Create test
        ShoppingCartItemHome.create( shoppingItem );
        ShoppingCartItem shoppingItemStored = ShoppingCartItemHome.findByPrimaryKey( shoppingItem.getIdItem( ) );
        assertEquals( shoppingItemStored.getIdItem( ), shoppingItem.getIdItem( ) );
        assertEquals( shoppingItemStored.getIdProvider( ), shoppingItem.getIdProvider( ) );
        assertEquals( shoppingItemStored.getIdLot( ), shoppingItem.getIdLot( ) );
        assertEquals( shoppingItemStored.getIdUser( ), shoppingItem.getIdUser( ) );
        assertEquals( shoppingItemStored.getResourceType( ), shoppingItem.getResourceType( ) );
        assertEquals( shoppingItemStored.getIdResource( ), shoppingItem.getIdResource( ) );
        assertEquals( shoppingItemStored.getItemPrice( ), shoppingItem.getItemPrice( ) );

        // Update test
        shoppingItem.setIdItem( IDITEM2 );
        shoppingItem.setIdProvider( IDPROVIDER2 );
        shoppingItem.setIdLot( IDLOT2 );
        shoppingItem.setIdUser( IDUSER2 );
        shoppingItem.setResourceType( RESOURCETYPE2 );
        shoppingItem.setIdResource( IDRESOURCE2 );
        shoppingItem.setItemPrice( ITEMPRICE2 );
        ShoppingCartItemHome.update( shoppingItem );
        shoppingItemStored = ShoppingCartItemHome.findByPrimaryKey( shoppingItem.getIdItem( ) );
        assertEquals( shoppingItemStored.getIdItem( ), shoppingItem.getIdItem( ) );
        assertEquals( shoppingItemStored.getIdProvider( ), shoppingItem.getIdProvider( ) );
        assertEquals( shoppingItemStored.getIdLot( ), shoppingItem.getIdLot( ) );
        assertEquals( shoppingItemStored.getIdUser( ), shoppingItem.getIdUser( ) );
        assertEquals( shoppingItemStored.getResourceType( ), shoppingItem.getResourceType( ) );
        assertEquals( shoppingItemStored.getIdResource( ), shoppingItem.getIdResource( ) );
        assertEquals( shoppingItemStored.getItemPrice( ), shoppingItem.getItemPrice( ) );

        // List test
        ShoppingCartItemHome.getShoppingItemsList( );

        // Delete test
        ShoppingCartItemHome.remove( shoppingItem.getIdItem( ) );
        shoppingItemStored = ShoppingCartItemHome.findByPrimaryKey( shoppingItem.getIdItem( ) );
        assertNull( shoppingItemStored );

    }

}