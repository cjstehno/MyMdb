<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="printable" />
        <title>Movie Catalog: Titles</title>
    </head>
    <body>
	
		<h2>Movies by Title</h2>
		<div class="list">
			<table>
				<g:each in="${movies}" var="mov">
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

    </body>
</html>
