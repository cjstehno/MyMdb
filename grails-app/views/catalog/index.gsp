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
				<!--
				<h2>Search</h2>
				<div class="sub-section">
					<form action="search" method="post">
					Query: <input type="text" name="q" /><button>Search</button>
					</form>
				</div>
				-->
				
				<h2>Views</h2>
				<div class="sub-section">
					<h3>Movies by Title</h3>
					<div class="listings">
						<g:link controller="catalog" action="title">All</g:link>
						<g:each in="${'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.chars}" var="val">
							| <g:link controller="catalog" action="title" params="[letter:val]">${val}</g:link>
						</g:each>
					</div>
					
					<h3>Movies by Genre</h3>
					<div class="listings">
						<g:each in="${Genre.list()}" var="genre">
							<g:link controller="catalog" action="genre" id="${genre.id}">${genre.name}</g:link>,
						</g:each>
					</div>
					
					<h3>Movies by Actor</h3>
					<div class="listings">
						<div id="actor-tabs">
							<ul>
								<g:each in="${(0..<actorTabs.size())}" var="t">
									<li><a href="#tab${t}">${t*30 + 1}-${t*30 + 30}</a></li>
								</g:each>
							</ul>
							<g:each in="${actorTabs}" var="tab" status="t">
								<div id="tab${t}" style="height:160px;">
									<g:each var="actor" in="${tab}">
										<div class="actor-list-item">
											<g:link controller="catalog" action="actor" id="${actor.id}">${actor.lastName}, ${actor.firstName} ${actor.middleName}</g:link>
										</div>
									</g:each>
								</div>
							</g:each>
						</div>
					</div>				
					
					<h3>Movies by Box</h3>
					<div class="listings">
						<g:link controller="catalog" action="storage">All</g:link>, 
						<g:each in="${storageBoxes}" var="storage">
							<g:link controller="catalog" action="storage" id="${storage}">${storage}</g:link>,
						</g:each>
					</div>				
					
					<h3>Movies by Release Year</h3>
					<div class="listings">
						<g:link controller="catalog" action="year">All</g:link>,
						<g:each in="${releaseYears}" var="yr">
							<g:link controller="catalog" action="year" params="[year:yr]">${yr}</g:link>,
						</g:each>
					</div>						
				</div>
				
				<h2>Printables</h2>
				<div class="sub-section">
					<h3>Printable Catalog</h3>
					<div class="listings">
						<g:link controller="printable" action="catalog" target="_blank">Print</g:link>
					</div>

					<h3>Printable Box Book</h3>
					<div class="listings">
						<g:each in="${storageBoxes}" var="storage">
							<g:link controller="printable" action="book" params="[box:storage]" target="_blank">${storage}</g:link>,
						</g:each>
					</div>	

					<h3>Printable Box Drawer</h3>
					<div class="listings">
						<g:each in="${storageBoxes}" var="storage">
							<g:link controller="printable" action="drawer" params="[box:storage]" target="_blank">${storage}</g:link>,
						</g:each>
					</div>	
				</div>
            </div>
        </div>
    </body>
</html>
