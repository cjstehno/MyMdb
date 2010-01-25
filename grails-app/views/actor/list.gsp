<%@ page import="com.stehno.mymdb.domain.Actor" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'actor.label', default: 'Actor')}" />
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
                            <g:sortableColumn property="firstName" title="${message(code: 'actor.firstName.label', default: 'First Name')}" />
                            <g:sortableColumn property="middleName" title="${message(code: 'actor.middleName.label', default: 'Middle Name')}" />
                            <g:sortableColumn property="lastName" title="${message(code: 'actor.lastName.label', default: 'Last Name')}" />
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${actorInstanceList}" status="i" var="actorInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td><g:link action="show" id="${actorInstance.id}">${fieldValue(bean: actorInstance, field: "firstName")}</g:link></td>
                            <td>${fieldValue(bean: actorInstance, field: "middleName")}</td>
                            <td>${fieldValue(bean: actorInstance, field: "lastName")}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${actorInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
