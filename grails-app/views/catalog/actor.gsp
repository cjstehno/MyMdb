<%@ page import="com.stehno.mymdb.domain.Movie" %>
<%@ page import="com.stehno.mymdb.domain.Actor" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="catalogs" />
        <title>Catalog: Movies By Actor</title>
    </head>
    <body>
        <div class="body">
            <h1>Movies By Actor</h1>
			
			<div class="listings">
				<g:each in="${Actor.list()}" var="actor">
					| <a href="actor?actor=${actor.id}">${actor.firstName} ${actor.middleName} ${actor.lastName}</a>
				</g:each> |
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