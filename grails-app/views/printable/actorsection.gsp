<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="printable" />
        <title>Movie Catalog: Actors</title>
    </head>
    <body>
	
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
