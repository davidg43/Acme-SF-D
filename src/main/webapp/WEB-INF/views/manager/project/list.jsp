<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.list.label.code" path="code" width="30%"/>
	<acme:list-column code="manager.list.label.title" path="title" width="50%"/>
	<acme:list-column code="manager.list.label.publishable" path="publishable" width="10%"/>
		<acme:list-column code="manager.list.label.draft" path="isDraft" width="10%"/>
</acme:list>

<acme:button code="manager.list.label.create" action="/manager/project/create"/>


