<%@ page import="com.stehno.mymdb.domain.Movie" %>
<%@ page import="com.stehno.mymdb.domain.Storage" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="catalogs" />
        <title>Catalog: Movies By Storage</title>
    </head>
    <body>
        <div class="body">
            <h1>Movies By Storage: ${theStorage}</h1>
			
			<div class="listings">
				<a href="storage">All</a>
				<g:each in="${storageBoxes}" var="storage">
					| <a href="storage?box=${storage}">${storage}</a>
				</g:each>
			</div>	
			
            <div class="list">
				<table>
					<g:render template="movie" collection="${movieInstanceList}" />
				</table>
            </div>
			
            <div class="paginateButtons">
                <g:paginate total="${movieInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
