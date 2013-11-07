/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reseimport fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.util.CryptoService;

import java.util.List;
 above copyright notice
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
package fr.paris.lutece.plugins.shoppingcart.service.validator;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.util.CryptoService;

import java.util.List;


/**
 * Abstract implementation of shopping cart validators
 */
public abstract class AbstractShoppingCartValidator implements IShoppingCartValidator
{
    private static final String CONSTANT_CRYPTO_ALGORITHM = AppPropertiesService.getProperty(
            "shoppingcart.security.keyEncryptionAlgorithm", "md5" );

    private boolean _bEnabled;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getEnabled( )
    {
        return _bEnabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled( boolean bEnabled )
    {
        this._bEnabled = bEnabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSecurityKeyForItems( List<ShoppingCartItem> listItems )
    {
        return CryptoService.encrypt( getValidatorId( ) + CryptoService.getCryptoKey( ) + listItems.hashCode( ),
                CONSTANT_CRYPTO_ALGORITHM );
    }
}
