package fr.paris.lutece.plugins.shoppingcart.service.validator;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.portal.service.security.LuteceUser;

import java.util.List;
import java.util.Locale;
import java.util.Map;


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
    String getValidatorDescription( Locale locale );

    /**
     * Validate items of the shopping cart of a user.
     * @param user The user associated with the shopping cart. May be null if
     *            the user has not logged in
     * @param listItemsToValidate The list of items to validate
     * @param mapRequestParameters The map containing request parameters if the
     *            validator needs to display a form, or null if the validator
     *            does not need to display a form
     * @return Null if items are valid and contain no errors, or a i18n key of a
     *         message that describes the error if items could not be validated
     */
    String validateShoppingCart( LuteceUser user, List<ShoppingCartItem> listItemsToValidate,
            Map<String, String[]> mapRequestParameters );

    /**
     * Check if this validator needs to display a form before validating data
     * @return True if it needs to display a form, false otherwise
     */
    boolean hasValidationForm( );

    /**
     * Get the HTML content of the form of this validator to display to users
     * before they can validate their shopping cart. Names of parameters
     * generated by this validator should begin with the name of the validator
     * to avoid conflicts
     * @param listItemsToValidate The list of items that will be displayed
     * @param locale The locale to use
     * @return The HTML content to display, or null if the validator does not
     *         needs to display a form
     */
    String getValidationFormHtml( List<ShoppingCartItem> listItemsToValidate, Locale locale );

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

    /**
     * Get a unique security key of the given list for this validator. The
     * security key should be unique for each list of items and for each
     * validator<br />
     * <br />
     * This key is used to make sur that the list of items has not changed from
     * the first validator to the last one
     * @param listItems The list of items
     * @return The security key of the list of items.
     */
    String getSecurityKeyForItems( List<ShoppingCartItem> listItems );
}
