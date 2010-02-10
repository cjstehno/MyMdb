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
            <h1>Movies By Actor: ${theActor.firstName} ${theActor.middleName} ${theActor.lastName}</h1>
			
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
