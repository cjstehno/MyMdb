<%@ page import="com.stehno.mymdb.domain.Genre" %>
<div>All</div>
<g:each in="${Genre.list([sort:'name'])}" var="genre">
	<div>${genre.name}</div>
</g:each>