package fr.paris.lutece.plugins.shoppingcart.service.validator;

/**
 * Interface for validator services
 */
public interface IShoppingCartValidator
{
    /**
     * Get the unique id of the validator
     * @return The unique id of the validator
     */
    String getValidatorId( );

    /**
     * Get a short description of the validator
     * @return The description of the validator
     */
    String getValidatorDescription( );

    /**
     * Check if this validator is enabled
     * @return True if this validator is enabled
     */
    boolean getEnabled( );

    /**
     * Enable or disable this validator
     * @param bEnabled True to enable this validator, false to disable it
     */
    void setEnabled( boolean bEnabled );
}
