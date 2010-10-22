<tr class="movie-listing">
	<td class="poster" valign="top"><img class="movie-poster" src="${request.contextPath}/movie/poster/${it.id}" width="125" /></td>
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
					<g:link controller="catalog" action="genre" id="${g.id}">${g?.name}</g:link>
				</span>
			</g:each>
		</div>
		<div class="detail">
			<span class="label">Actors:</span>
			<g:each in="${it.actors}" var="a">
				<span>
					<g:link controller="catalog" action="actor" id="${a.id}">
						${a?.firstName} ${a?.middleName} ${a?.lastName}
					</g:link>
				</span>
			</g:each>
		</div>
        <div class="detail"><span class="label">Tags:</span>
          <g:each in="${it.tags}" var="tag">
            <span><g:link controller="catalog" action="tag" id="${tag}">${tag}</g:link>,</span>
          </g:each>
        </div>
		<div class="detail">${fieldValue(bean:it, field:'description')}</div>
		<div style="float:right;"><g:link controller="movie" action="edit" id="${it.id}">edit</g:link></div>
	</td>
</tr>