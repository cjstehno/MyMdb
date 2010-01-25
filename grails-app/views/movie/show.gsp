
<%@ page import="com.stehno.mymdb.domain.Movie" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'movie.label', default: 'Movie')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="movie.title.label" default="Title" /></td>
                            <td valign="top" class="value">${fieldValue(bean: movieInstance, field: "title")}</td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="movie.description.label" default="Description" /></td>
                            <td valign="top" class="value">${fieldValue(bean: movieInstance, field: "description")}</td>
                        </tr>
						
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="movie.poster.label" default="Poster" /></td>
                            <td valign="top" class="value"><img src="../poster/${movieInstance.id}"/></td>
                        </tr>						
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="movie.releaseYear.label" default="Release Year" /></td>
                            <td valign="top" class="value">${movieInstance.releaseYear}</td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="movie.storage.label" default="Storage" /></td>
                            <td valign="top" class="value">${movieInstance.storage.name}-${movieInstance.storage.index}</td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="movie.genres.label" default="Genres" /></td>
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${movieInstance.genres}" var="g">
                                    <span class="genre-list-item">
										<g:link controller="genre" action="show" id="${g.id}">${g?.name}</g:link>
									</span>
                                </g:each>
                                </ul>
                            </td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="movie.actors.label" default="Actors" /></td>
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${movieInstance.actors}" var="a">
                                    <span class="actor-list-item">
										<g:link controller="actor" action="show" id="${a.id}">
											${a?.firstName} ${a?.middleName} ${a?.lastName}
										</g:link>
									</span>
                                </g:each>
                                </ul>
                            </td>
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${movieInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
