package fr.paris.lutece.plugins.shoppingcart.service.validator;

public abstract class AbstractShoppingCartValidator implements IShoppingCartValidator
{
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
}
