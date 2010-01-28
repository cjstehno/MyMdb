
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
            <g:form method="post" enctype="multipart/form-data">
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
                                    <g:textField name="title" maxlength="100" style="width:50%;" value="${movieInstance?.title}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="description"><g:message code="movie.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: movieInstance, field: 'description', 'errors')}">
                                    <g:textArea name="description" cols="40" rows="5" style="width:100%" value="${movieInstance?.description}" />
                                </td>
                            </tr>
							
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="poster">Poster</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: movieInstance, field: 'poster', 'errors')}">
									<div><img src="poster/${movieInstance.id}" /></div>
									<input type="file" id="poster" name="poster" />
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
									<label for="genres"><g:message code="movie.genres.label" default="Genres" /></label>
								</td>
								<td valign="top" class="value ${hasErrors(bean:movieInstance,field:'genres','errors')}">
									<g:each var="genre" in="${genres}">
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
									<div id="actor-tabs">
										<ul>
											<g:each in="${tabRange}" var="t">
												<li><a href="#tab${t}">${t*30 + 1}-${t*30 + 30}</a></li>
											</g:each>
										</ul>
										<g:each in="${tabs}" var="tab" status="t">
											<div id="tab${t}" style="height:160px;">
												<g:each var="actor" in="${tab}">
													<div class="actor-list-item">
														<label><input type="checkbox" name="actors" value="${actor.id}" <g:if test="${movieInstance?.actors?.contains(actor)}">checked</g:if>/> ${actor.lastName}, ${actor.firstName} ${actor.middleName}</label>
													</div>
												</g:each>
											</div>
										</g:each>
									</div>
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
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
