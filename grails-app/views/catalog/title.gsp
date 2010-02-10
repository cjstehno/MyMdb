<%@ page import="com.stehno.mymdb.domain.Movie" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="catalogs" />
        <title>Catalog: Movies By Title</title>
    </head>
    <body>
        <div class="body">
            <h1>Movies By Title: ${letter}</h1>
			
			<div class="listings">
				<a href="title">All</a>
				<g:each in="${'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.chars}" var="letter">
					| <a href="title?letter=${letter}">${letter}</a>
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
