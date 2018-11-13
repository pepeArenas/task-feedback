<%@include file="../commons/header.jsp" %>
<%@include file="../commons/navigation.jsp" %>
<form:form method="post" commandName="product">
    <fieldset class="form-group">
        <form:label path="name">Name:</form:label>
        <form:input path="name" type="text" class="form-control"
                    required="required"/>
        <form:errors path="name" cssClass="text-warning"></form:errors>

        <form:label path="model">Model:</form:label>
        <form:input path="model" type="text" class="form-control"
                    required="required"/>
        <form:errors path="model" cssClass="text-warning"></form:errors>

        <form:label path="price">Price:</form:label>
        <form:input path="price" type="number" min="0" value="0" step=".01" class="form-control"/>

        <form:input path="UUID" type="hidden" value="${suuid}" class="form-control"/>
    </fieldset>
    <button class="btn btn-success" type="submit" >Add</button>

</form:form>
<%@include file="../commons/footer.jsp" %>
