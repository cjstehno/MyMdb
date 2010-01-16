
<%@ page import="com.stehno.mymdb.domain.Poster" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'poster.label', default: 'Poster')}" />
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
                            <g:sortableColumn property="name" title="${message(code: 'poster.name.label', default: 'Name')}" />
                            <g:sortableColumn property="data" title="${message(code: 'poster.data.label', default: 'Data')}" />
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${posterInstanceList}" status="i" var="posterInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td><g:link action="show" id="${posterInstance.id}">${fieldValue(bean: posterInstance, field: "name")}</g:link></td>
                            <td><img src="image/${posterInstance.id}" /></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${posterInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
