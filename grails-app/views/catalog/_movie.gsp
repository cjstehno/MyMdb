<tr class="movie-listing">
	<td class="poster" valign="top"><img src="../movie/poster/${it.id}" width="125" /></td>
	<td class="info" valign="top">
		<div class="title">${fieldValue(bean:it, field:'title')}</div>
		<div class="detail">
			<span class="label">Released:</span> ${it.releaseYear}
		</div>
		<div class="detail">
			<span class="label">Storage:</span> ${it.storage.name}-${it.storage.index}
		</div>
		<div class="detail">
			<span class="label">Genres:</span>
			<g:each in="${it.genres}" var="g">
				<span>
					<g:link controller="genre" action="show" id="${g.id}">${g?.name}</g:link>
				</span>
			</g:each>
		</div>
		<div class="detail">
			<span class="label">Actors:</span>
			<g:each in="${it.actors}" var="a">
				<span>
					<g:link controller="actor" action="show" id="${a.id}">
						${a?.firstName} ${a?.middleName} ${a?.lastName}
					</g:link>
				</span>
			</g:each>
		</div>
		<div class="detail">${fieldValue(bean:it, field:'description')}</div>
	</td>
</tr>