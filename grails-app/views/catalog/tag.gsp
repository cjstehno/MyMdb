<%@ page import="com.stehno.mymdb.domain.Movie" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="catalogs" />
        <title>Catalog: Movies By Tag</title>
    </head>
    <body>
        <div class="body">
            <h1>Movies By Tag: ${theTag}</h1>

			<div class="listings">
				<a href=".">All</a>
				<g:each in="${allTags}" var="t">
					| <a href="${t}">${t}</a>
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
