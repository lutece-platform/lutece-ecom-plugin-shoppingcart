package fr.paris.lutece.plugins.shoppingcart.service;

import fr.paris.lutece.plugins.shoppingcart.service.persistence.SessionPersistenceService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.web.LocalVariables;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Filter to transfer items from the anonymous shopping cart of a user to his
 * regular shopping cart when he logs in
 */
public class AnonymousShoppingCartFilter implements Filter
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void init( FilterConfig filterConfig ) throws ServletException
    {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException,
            ServletException
    {
        if ( request instanceof HttpServletRequest )
        {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession session = httpRequest.getSession( false );
            if ( session != null )
            {
                LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( httpRequest );
                if ( user != null )
                {
                    Boolean bHasItemsInSession = (Boolean) session
                            .getAttribute( SessionPersistenceService.SESSION_ATTRIBUTE_HAS_SESSION_ITEMS );
                    if ( bHasItemsInSession != null && bHasItemsInSession )
                    {
                        LocalVariables.setLocal( null, httpRequest, (HttpServletResponse) response );
                        ShoppingCartService.getInstance( ).checkAnonymousShoppingCart( user.getName( ) );
                    }
                }
            }
        }
        chain.doFilter( request, response );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy( )
    {
        // Do nothing
    }

}
