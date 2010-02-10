<%@ page import="com.stehno.mymdb.domain.Movie" %>
<%@ page import="com.stehno.mymdb.domain.Genre" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="catalogs" />
        <title>Catalog: Movies By Genre</title>
    </head>
    <body>
        <div class="body">
            <h1>Movies By Release Genre: ${theGenre.name}</h1>
			
			<div class="listings">
				<a href="genre">All</a>
				<g:each in="${Genre.list()}" var="genre">
					| <a href="genre?genre=${genre.id}">${genre.name}</a>
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
