
<%@ page import="com.stehno.mymdb.domain.Movie" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'movie.label', default: 'Movie')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
							<th>Storage</th>
                            <g:sortableColumn property="title" title="${message(code: 'movie.title.label', default: 'Title')}" />
							<th>Poster</th>
                            <g:sortableColumn property="description" title="${message(code: 'movie.description.label', default: 'Description')}" />
							<th>Genres</th>
							<th>Actors</th>
                            <g:sortableColumn property="releaseYear" title="${message(code: 'movie.releaseYear.label', default: 'Release Year')}" />
							<th>&nbsp;</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${movieInstanceList}" status="i" var="movieInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
							<td>${movieInstance.storage.name}-${movieInstance.storage.index}</td>
                            <td><g:link action="show" id="${movieInstance.id}">${fieldValue(bean: movieInstance, field: "title")}</g:link></td>
							<td><img src="poster/${movieInstance.id}" width="100" /></td>
                            <td>${fieldValue(bean: movieInstance, field: "description")}</td>
							<td>
								<g:each in="${movieInstance.genres}" var="g">
                                    <span>
										<g:link controller="genre" action="show" id="${g.id}">${g?.name}</g:link>
									</span>
								</g:each>
							</td>
							<td>
								<g:each in="${movieInstance.actors}" var="a">
                                    <span>
										<g:link controller="actor" action="show" id="${a.id}">
											${a?.firstName} ${a?.middleName} ${a?.lastName}
										</g:link>
									</span>
								</g:each>
							</td>
                            <td>${movieInstance.releaseYear}</td>
							<td><g:link controller="movie" action="edit" id="${movieInstance.id}">Edit</g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${movieInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
