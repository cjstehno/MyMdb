<%@ page import="com.stehno.mymdb.domain.Movie" %>
<%@ page import="com.stehno.mymdb.domain.Genre" %>
<%@ page import="com.stehno.mymdb.domain.Actor" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'movie.label', default: 'Movie')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
				<div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${movieInstance}">
				<div class="errors">
					<g:renderErrors bean="${movieInstance}" as="list" />
				</div>
            </g:hasErrors>
            <g:form action="save" method="post" >
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
									<label for="genres">Genres:</label>
								</td>
								<td valign="top" class="value ${hasErrors(bean:movieInstance,field:'genres','errors')}">
									<g:each var="genre" in="${Genre.list()}">
										<div class="genre-list-item">
											<label><input type="checkbox" name="genres" value="${genre.id}" <g:if test="${movieInstance?.genres?.contains(genre)}">checked</g:if>/> ${genre.name}</label>
										</div>
									</g:each>
								</td>
							</tr> 	
							
							<tr class="prop">
								<td valign="top" class="name">
									<label for="actors">Actors:</label>
								</td>
								<td valign="top" class="value ${hasErrors(bean:movieInstance,field:'actors','errors')}">
									<g:each var="actor" in="${Actor.list()}">
										<div class="actor-list-item">
											<label><input type="checkbox" name="actors" value="${actor.id}" <g:if test="${movieInstance?.actors?.contains(actor)}">checked</g:if>/> ${actor.firstName} ${actor.middleName} ${actor.lastName}</label>
										</div>
									</g:each>
								</td>
							</tr> 								

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="storage"><g:message code="movie.storage.label" default="Storage" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: movieInstance, field: 'storage', 'errors')}">
									Box: <input type="text" id="storage.name" name="storage.name" value="${movieInstance?.storage?.name}" size="4" />
									Index: <input type="text" id="storage.index" name="storage.index" value="${movieInstance?.storage?.index}" size="4" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
