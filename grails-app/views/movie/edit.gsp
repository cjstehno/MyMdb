
<%@ page import="com.stehno.mymdb.domain.Movie" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'movie.label', default: 'Movie')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${movieInstance}">
            <div class="errors">
                <g:renderErrors bean="${movieInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${movieInstance?.id}" />
                <g:hiddenField name="version" value="${movieInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="title"><g:message code="movie.title.label" default="Title" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: movieInstance, field: 'title', 'errors')}">
                                    <g:textField name="title" maxlength="100" value="${movieInstance?.title}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="description"><g:message code="movie.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: movieInstance, field: 'description', 'errors')}">
                                    <g:textArea name="description" cols="40" rows="5" value="${movieInstance?.description}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="releaseYear"><g:message code="movie.releaseYear.label" default="Release Year" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: movieInstance, field: 'releaseYear', 'errors')}">
                                    <g:select name="releaseYear" from="${1930..2020}" value="${fieldValue(bean: movieInstance, field: 'releaseYear')}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="lastUpdate"><g:message code="movie.lastUpdate.label" default="Last Update" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: movieInstance, field: 'lastUpdate', 'errors')}">
                                    <g:datePicker name="lastUpdate" precision="day" value="${movieInstance?.lastUpdate}" noSelection="['': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dateCreated"><g:message code="movie.dateCreated.label" default="Date Created" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: movieInstance, field: 'dateCreated', 'errors')}">
                                    <g:datePicker name="dateCreated" precision="day" value="${movieInstance?.dateCreated}" noSelection="['': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="storage"><g:message code="movie.storage.label" default="Storage" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: movieInstance, field: 'storage', 'errors')}">
                                    
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="genres"><g:message code="movie.genres.label" default="Genres" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: movieInstance, field: 'genres', 'errors')}">
                                    <g:select name="genres" from="${com.stehno.mymdb.domain.Genre.list()}" multiple="yes" optionKey="id" size="5" value="${movieInstance?.genres}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="actors"><g:message code="movie.actors.label" default="Actors" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: movieInstance, field: 'actors', 'errors')}">
                                    <g:select name="actors" from="${com.stehno.mymdb.domain.Actor.list()}" multiple="yes" optionKey="id" size="5" value="${movieInstance?.actors}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
