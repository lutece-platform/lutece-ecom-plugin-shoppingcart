package fr.paris.lutece.plugins.shoppingcart.service.validator;

import fr.paris.lutece.plugins.shoppingcart.business.ShoppingCartItem;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 * Validator that check that the user has logged in before validating its
 * shopping cart
 */
public class LoggedInUserValidator extends AbstractShoppingCartValidator
{
    private static final String VALIDATOR_ID = "shoppingcart.loggedInUserValidator";

    private static final String MESSAGE_LOGGED_IN_USER_VALIDATOR_DESCRIPTION = "shoppingcart.validator.loggedInUser.description";
    private static final String MESSAGE_ERROR_USER_NOT_LOGGED_IN = "shoppingcart.validator.loggedInUser.errorUserNotLoggedIn";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValidatorId( )
    {
        return VALIDATOR_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValidatorDescription( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_LOGGED_IN_USER_VALIDATOR_DESCRIPTION, locale );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validateShoppingCart( LuteceUser user, List<ShoppingCartItem> listItemsToValidate,
            Map<String, String[]> mapRequestParameters )
    {
        if ( user == null || StringUtils.equals( user.getName( ), LuteceUser.ANONYMOUS_USERNAME ) )
        {
            return MESSAGE_ERROR_USER_NOT_LOGGED_IN;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * @return false
     */
    @Override
    public boolean hasValidationForm( )
    {
        return false;
    }

    /**
     * {@inheritDoc}
     * @return null
     */
    @Override
    public String getValidationFormHtml( List<ShoppingCartItem> listItemsToValidate, Locale locale )
    {
        // No validation form
        return null;
    }
}
