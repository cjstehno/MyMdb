<%@ page import="com.stehno.mymdb.domain.Movie" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="catalogs" />
        <title>Catalog: Movies By Year</title>
    </head>
    <body>
        <div class="body">
            <h1>Movies By Release Year</h1>
			
			<div class="listings">
				<a href="year">All</a>
				<g:each in="${releaseYears}" var="yr">
					| <a href="year?year=${yr}">${yr}</a>
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
