<%@ page import="com.stehno.mymdb.domain.Movie" %>
<%@ page import="com.stehno.mymdb.domain.Actor" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="catalogs" />
        <title>Catalog: Search</title>
    </head>
    <body>
        <div class="body">
            <h1>Search</h1>
			
			<div class="listings">
				<form action="search" method="post">
				Query: <input type="text" name="q" value="${params.q?.trim()}" /><button>Search</button>
				</form>
			</div>		
			
            <div class="list">
				<table>
					<g:render template="movie" collection="${searchResult?.results}" />
				</table>
            </div>
			
        </div>
    </body>
</html>
