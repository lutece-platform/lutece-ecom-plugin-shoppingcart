<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="manageshoppingcart" scope="session" class="fr.paris.lutece.plugins.shoppingcart.web.ManageShoppingCartJspBean" />

<%
    manageshoppingcart.init( request, manageshoppingcart.RIGHT_MANAGE_SHOPPING_CART );
%>
<%= manageshoppingcart.getManageShoppingCart( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
