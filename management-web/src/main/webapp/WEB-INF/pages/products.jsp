<%@include file="../commons/header.jsp" %>
<%@include file="../commons/navigation.jsp" %>
<table class="table table-striped">
    <caption>Products</caption>
    <thead>
    <tr>
        <th>Name</th>
        <th>Model</th>
        <th>Price</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${products}" var="product">
        <tr>
            <td>${product.name}</td>
            <td>${product.model}</td>
            <td>${product.price}</td>
        </tr>
    </c:forEach>

    </tbody>
</table>
<%@include file="../commons/footer.jsp" %>