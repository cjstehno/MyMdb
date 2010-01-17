<%@ page import="com.stehno.mymdb.domain.Genre" %>
<%@ page import="com.stehno.mymdb.domain.Actor" %>
<%@ page import="com.stehno.mymdb.domain.Storage" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="catalogs" />
        <title>Catalog</title>
    </head>
    <body>
        <div class="body">
            <h1>Catalog</h1>
            <div>
				<h2>Search</h2>
				<div class="sub-section">
					<form action="search" method="post">
					Query: <input type="text" name="q" /><button>Search</button>
					</form>
				</div>
				
				<h2>Views</h2>
				<div class="sub-section">
					<h3>Movies by Title</h3>
					<div class="listings">
						<a href="title">All</a>
						<g:each in="${'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.chars}" var="letter">
							| <a href="title?letter=${letter}">${letter}</a>
						</g:each>
					</div>
					
					<h3>Movies by Genre</h3>
					<div class="listings">
						<g:each in="${Genre.list()}" var="genre">
							| <a href="genre?genre=${genre.id}">${genre.name}</a>
						</g:each> |
					</div>
					
					<h3>Movies by Actor</h3>
					<div class="listings">
						<g:each in="${Actor.list()}" var="actor">
							| <a href="actor?actor=${actor.id}">${actor.firstName} ${actor.middleName} ${actor.lastName}</a>
						</g:each> |
					</div>				
					
					<h3>Movies by Box</h3>
					<div class="listings">
						<a href="storage">All</a>
						<g:each in="${storageBoxes}" var="storage">
							| <a href="storage?box=${storage}">${storage}</a>
						</g:each>
					</div>				
					
					<h3>Movies by Release Year</h3>
					<div class="listings">
						<a href="year">All</a>
						<g:each in="${releaseYears}" var="yr">
							| <a href="year?year=${yr}">${yr}</a>
						</g:each>
					</div>						
				</div>
				
				<h2>Printables</h2>
				<div class="sub-section">
					<h3>Printable Catalog</h3>
					<div class="listings">
						Print
					</div>

					<h3>Printable Box Book</h3>
					<div class="listings">
						All
						<g:each in="${Storage.list()}" var="storage">
							| ${storage.name}
						</g:each>
					</div>	

					<h3>Printable Box Drawer</h3>
					<div class="listings">
						All
						<g:each in="${Storage.list()}" var="storage">
							| ${storage.name}
						</g:each>
					</div>	
				</div>
            </div>
        </div>
    </body>
</html>
