<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="shoppingCartPortletJspBean" scope="session" class="fr.paris.lutece.plugins.shoppingcart.web.portlet.ShoppingCartPortletJspBean" />

<%
	shoppingCartPortletJspBean.init( request, shoppingCartPortletJspBean.RIGHT_MANAGE_ADMIN_SITE );
	response.sendRedirect( shoppingCartPortletJspBean.doModify( request ) );
%>

