<%@ page import="com.stehno.mymdb.domain.Movie" %>
<%@ page import="com.stehno.mymdb.domain.Actor" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Catalog: Movies By Actor</title>
		<style type="text/css">
			div.list table {
				width:100%;
			}
			
			tr.movie-listing .info {
				padding: 10px;
			}
			
			tr.movie-listing .info .label {
				font-weight: bold;
			}
			
			tr.movie-listing .info .title {
				font-weight: bold;
				font-size: large;
				margin-bottom: 8px;
			}
			
			tr.movie-listing .info .detail {
				margin-bottom: 2px;
			}
			
			.listings {
				margin-bottom: 15px;
				text-align: center;
			}
		</style>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
			<span class="menuButton"><g:link controller="catalog" action="index">Catalogs</g:link></span>
        </div>
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
