package fr.paris.lutece.plugins.shoppingcart.service.validator;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 * Service to manage shopping cart validators
 */
public class ShoppingCartValidatorManagementService
{
    private static final String DATASTORE_KEY_SHOPPING_CART_VALIDATOR_ORDER = "shoppingcart.validator.order.";
    private static final String DATASTORE_KEY_SHOPPING_CART_VALIDATOR_ENABLE = "shoppingcart.validator.enable.";

    private static ShoppingCartValidatorManagementService _instance = new ShoppingCartValidatorManagementService( );
    private volatile List<IShoppingCartValidator> _listValidators;

    /**
     * Default constructor
     */
    private ShoppingCartValidatorManagementService( )
    {
        // Default constructor
    }

    /**
     * Get the instance of the service
     * @return The instance of the service
     */
    public static ShoppingCartValidatorManagementService getInstance( )
    {
        return _instance;
    }

    /**
     * Get the list of validators. The list is sorted by their order.
     * @return The list of validators sorted by their order.
     */
    public List<IShoppingCartValidator> getValidatorlist( )
    {
        if ( _listValidators == null )
        {
            generateValidatorList( );
        }
        return _listValidators;
    }

    /**
     * Change the order of a validator, and updates other validators to fit with
     * the new order
     * @param strValidatorId The id of the validator to change to order of
     * @param nNewOrder The new value of the order of the validator
     */
    public synchronized void modifyValidatorOrder( String strValidatorId, int nNewOrder )
    {
        // We get the current list of validators. This list is sorted by validators's order
        List<IShoppingCartValidator> listValidators = getValidatorlist( );
        String strOldOrder = DatastoreService.getDataValue( DATASTORE_KEY_SHOPPING_CART_VALIDATOR_ORDER
                + strValidatorId, null );
        int nOldOrder;
        if ( StringUtils.isEmpty( strOldOrder ) || !StringUtils.isNumeric( strOldOrder ) )
        {
            AppLogService.error( "Order of service not found : " + strValidatorId
                    + ", assuming it is the last item of the list" );
            nOldOrder = listValidators.size( ) + 1;
        }
        else
        {
            nOldOrder = Integer.parseInt( strOldOrder );
        }
        // We check that we don't try to change an order by the same one
        if ( nNewOrder != nOldOrder )
        {
            // We get every order, ans we update orders between the old one and the new one
            ReferenceList refListOrder = DatastoreService
                    .getInstanceDataByPrefix( DATASTORE_KEY_SHOPPING_CART_VALIDATOR_ORDER );
            if ( nNewOrder > nOldOrder )
            {
                for ( ReferenceItem refItem : refListOrder )
                {
                    int nOrder = Integer.parseInt( refItem.getName( ) );
                    if ( nOrder <= nNewOrder && nOrder > nOldOrder )
                    {
                        DatastoreService.setDataValue( refItem.getCode( ), Integer.toString( nOrder - 1 ) );
                    }
                }
            }
            else
            {
                for ( ReferenceItem refItem : refListOrder )
                {
                    int nOrder = Integer.parseInt( refItem.getName( ) );
                    if ( nOrder >= nNewOrder && nOrder < nOldOrder )
                    {
                        DatastoreService.setDataValue( refItem.getCode( ), Integer.toString( nOrder + 1 ) );
                    }
                }
            }
            // We now update the new order of the validator
            DatastoreService.setDataValue( DATASTORE_KEY_SHOPPING_CART_VALIDATOR_ORDER + strValidatorId,
                    Integer.toString( nNewOrder ) );
            // We generate the new validator list
            generateValidatorList( );
        }
    }

    /**
     * Enable or disable a validator. Disabled validators will not be used to
     * validate shopping carts of users
     * @param strValidatorId The id of the validator the enable or disable
     * @param bEnable True to enable the validator, false to disable it
     */
    public synchronized void changeValidatorEnabling( String strValidatorId, boolean bEnable )
    {
        // We get the current list of validators
        List<IShoppingCartValidator> listValidators = SpringContextService
                .getBeansOfType( IShoppingCartValidator.class );
        for ( IShoppingCartValidator validator : listValidators )
        {
            if ( StringUtils.equals( strValidatorId, validator.getValidatorId( ) ) )
            {
                DatastoreService.setDataValue( DATASTORE_KEY_SHOPPING_CART_VALIDATOR_ENABLE + strValidatorId,
                        Boolean.toString( bEnable ) );
                break;
            }
        }
        generateValidatorList( );
    }

    /**
     * Generate the list of validators sorted by their order. Disabled
     * validators are not included in the list. The list is saved in the
     * attribute {@link #_listValidators}.
     */
    private synchronized void generateValidatorList( )
    {
        List<IShoppingCartValidator> listValidators = SpringContextService
                .getBeansOfType( IShoppingCartValidator.class );

        Map<String, Integer> mapOrder = new HashMap<String, Integer>( listValidators.size( ) );
        ReferenceList refListOrder = DatastoreService
                .getInstanceDataByPrefix( DATASTORE_KEY_SHOPPING_CART_VALIDATOR_ORDER );
        ReferenceList refListEnabling = DatastoreService
                .getInstanceDataByPrefix( DATASTORE_KEY_SHOPPING_CART_VALIDATOR_ENABLE );

        List<IShoppingCartValidator> listMissingOrderValidators = new ArrayList<IShoppingCartValidator>( );
        List<IShoppingCartValidator> listMissingEnablingValidators = new ArrayList<IShoppingCartValidator>( );
        int nMaxOrder = 0;
        for ( IShoppingCartValidator validator : listValidators )
        {
            String strOrderKey = DATASTORE_KEY_SHOPPING_CART_VALIDATOR_ORDER + validator.getValidatorId( );
            String strEnablingKey = DATASTORE_KEY_SHOPPING_CART_VALIDATOR_ENABLE + validator.getValidatorId( );
            boolean bOrderFound = false;
            boolean bEnablingFound = false;
            boolean bEnabled = true;
            // We get the order of the validator
            for ( ReferenceItem refItem : refListEnabling )
            {
                if ( StringUtils.equals( strEnablingKey, refItem.getCode( ) ) )
                {
                    bEnabled = Boolean.valueOf( refItem.getName( ) );
                    bEnablingFound = true;
                    refListEnabling.remove( refItem );
                    break;
                }
            }
            validator.setEnabled( bEnabled );
            // If the value was not found, we assume that is is enabled
            if ( !bEnablingFound )
            {
                listMissingEnablingValidators.add( validator );
            }
            for ( ReferenceItem refItem : refListOrder )
            {
                if ( StringUtils.equals( strOrderKey, refItem.getCode( ) ) )
                {
                    // We get the order, and save the maximum order encountered
                    int nOrder = Integer.valueOf( refItem.getName( ) );
                    if ( nOrder > nMaxOrder )
                    {
                        nMaxOrder = nOrder;
                    }
                    mapOrder.put( validator.getValidatorId( ), nOrder );
                    refListOrder.remove( refItem );
                    bOrderFound = true;
                    break;
                }
            }

            if ( !bOrderFound )
            {
                // If the service was not found, we mark it to be added
                listMissingOrderValidators.add( validator );
            }
        }

        // Every validator that was not removed of this list does not exist anymore and should therefore be removed
        for ( ReferenceItem refItem : refListOrder )
        {
            DatastoreService.removeData( refItem.getCode( ) );
        }
        for ( ReferenceItem refItem : refListEnabling )
        {
            DatastoreService.removeData( refItem.getCode( ) );
        }

        // We add missing enabling of validators
        for ( IShoppingCartValidator validator : listMissingEnablingValidators )
        {
            DatastoreService.setDataValue( DATASTORE_KEY_SHOPPING_CART_VALIDATOR_ENABLE + validator.getValidatorId( ),
                    Boolean.TRUE.toString( ) );
        }

        // We add missing order of validators. Default order is Spring order
        for ( IShoppingCartValidator validator : listMissingOrderValidators )
        {
            nMaxOrder++;
            DatastoreService.setDataValue( DATASTORE_KEY_SHOPPING_CART_VALIDATOR_ORDER + validator.getValidatorId( ),
                    Integer.toString( nMaxOrder ) );
            mapOrder.put( validator.getValidatorId( ), nMaxOrder );
        }
        ValidatorComparator comparator = new ValidatorComparator( mapOrder );
        Collections.sort( listValidators, comparator );
        _listValidators = listValidators;
    }

    /**
     * Comparator of validators. This class is used to sort them by their order
     */
    private static class ValidatorComparator implements Comparator<IShoppingCartValidator>
    {
        private Map<String, Integer> _mapOrder;

        /**
         * Creates a new validator comparator
         * @param mapOrder The order of validators to use
         */
        public ValidatorComparator( Map<String, Integer> mapOrder )
        {
            this._mapOrder = mapOrder;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare( IShoppingCartValidator o1, IShoppingCartValidator o2 )
        {
            return _mapOrder.get( o1.getValidatorId( ) ) - _mapOrder.get( o2.getValidatorId( ) );
        }
    }
}
