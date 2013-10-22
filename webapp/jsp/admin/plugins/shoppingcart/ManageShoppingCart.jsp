<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="manageshoppingcart" scope="session" class="fr.paris.lutece.plugins.shoppingcart.web.ManageShoppingCartJspBean" />

<%
    manageshoppingcart.init( request, manageshoppingcart.RIGHT_MANAGE_SHOPPING_CART );
	String strContent = manageshoppingcart.processController( request, response );
	if ( strContent != null )
	{
%>


<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
<%
	}
%>
