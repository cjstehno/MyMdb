<%@ page import="com.stehno.mymdb.domain.Movie" %>
<%@ page import="com.stehno.mymdb.domain.Storage" %>
<%@ page import="com.stehno.mymdb.domain.Genre" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="printable" />
        <title>Movie Catalog</title>
    </head>
    <body>
	
		<h1>Movies Catlog</h1>
		
		<h2>Movies by Title</h2>
		<div class="list">
			<table>
				<g:each in="${moviesByTitle}" var="mov">
					<tr class="movie-listing">
						<td class="poster" valign="top" width="130"><img src="../movie/poster/${mov.id}" width="125" /></td>
						<td class="info" valign="top">
							<div class="title">${fieldValue(bean:mov, field:'title')}</div>
							<div class="detail">
								<span class="label">Released:</span> ${mov.releaseYear}
							</div>
							<div class="detail">
								<span class="label">Storage:</span> ${mov.storage.name}-${mov.storage.index}
							</div>
							<div class="detail">
								<span class="label">Genres:</span>
								<g:each in="${mov.genres}" var="g">
									<span>${g?.name}</span>,
								</g:each>
							</div>
							<div class="detail">
								<span class="label">Actors:</span>
								<g:each in="${mov.actors}" var="a">
									<span>${a?.firstName} ${a?.middleName} ${a?.lastName}</span>,
								</g:each>
							</div>
							<div class="detail">${fieldValue(bean:mov, field:'description')}</div>
						</td>
					</tr>	
				</g:each>
			</table>
		</div>
		
		<br class="page" />
		
		<h2>Movies by Box</h2>
		<div class="list">
			<table cellspacing="0">
				<col width="50%" />
				<col width="50%" />
				<g:each in="${moviesByBox}" status="i" var="movie">
					<tr class="${i % 2 == 0 ? '' : 'odd'}">
						<td>${movie.storage.name}-${movie.storage.index}</td>
						<td align="right">${movie.title}</td>
					</tr>
				</g:each>
			</table>
		</div>
		
		<br class="page" />
		
		<h2>Movies by Genre</h2>
		<div class="list">
			<g:each in="${genres}" var="genre">
				<div class="grouping">${genre.name}</div>
				<table cellspacing="0">
					<col width="50%" />
					<col width="50%" />				
					<g:each in="${genre.movies}" var="m">
						<tr>
							<td class="indent">${m.title}</td>
							<td align="right">${m.storage.name}-${m.storage.index}</td>
						</tr>
					</g:each>
				</table>
			</g:each>
		</div>
		
		<br class="page" />

		<h2>Movies by Actor</h2>
		<div class="list">
			<g:each in="${actors}" var="actor">
				<div class="grouping">${actor.lastName}, ${actor.firstName} ${actor.middleName}</div>
				<table cellspacing="0">
					<col width="50%" />
					<col width="50%" />	
					<g:each in="${actor.movies}" var="m">
						<tr>
							<td class="indent">${m.title}</td>
							<td align="right">${m.storage.name}-${m.storage.index}</td>
						</tr>
					</g:each>
				</table>
			</g:each>
		</div>					
			
    </body>
</html>
