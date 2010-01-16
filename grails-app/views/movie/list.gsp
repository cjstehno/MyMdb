
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
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'movie.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="title" title="${message(code: 'movie.title.label', default: 'Title')}" />
                        
                            <g:sortableColumn property="description" title="${message(code: 'movie.description.label', default: 'Description')}" />
                        
                            <g:sortableColumn property="releaseYear" title="${message(code: 'movie.releaseYear.label', default: 'Release Year')}" />
                        
                            <g:sortableColumn property="lastUpdate" title="${message(code: 'movie.lastUpdate.label', default: 'Last Update')}" />
                        
                            <g:sortableColumn property="dateCreated" title="${message(code: 'movie.dateCreated.label', default: 'Date Created')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${movieInstanceList}" status="i" var="movieInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${movieInstance.id}">${fieldValue(bean: movieInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: movieInstance, field: "title")}</td>
                        
                            <td>${fieldValue(bean: movieInstance, field: "description")}</td>
                        
                            <td>${fieldValue(bean: movieInstance, field: "releaseYear")}</td>
                        
                            <td><g:formatDate date="${movieInstance.lastUpdate}" /></td>
                        
                            <td><g:formatDate date="${movieInstance.dateCreated}" /></td>
                        
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
