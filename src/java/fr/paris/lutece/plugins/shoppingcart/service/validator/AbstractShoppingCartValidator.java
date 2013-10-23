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
