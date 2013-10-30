<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="shoppingCartPortletJspBean" scope="session" class="fr.paris.lutece.plugins.shoppingcart.web.portlet.ShoppingCartPortletJspBean" />

<% shoppingCartPortletJspBean.init( request, shoppingCartPortletJspBean.RIGHT_MANAGE_ADMIN_SITE ); %>
<%= shoppingCartPortletJspBean.getCreate( request ) %>

<%@ include file="../../AdminFooter.jsp" %>