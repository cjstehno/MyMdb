<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="printable" />
        <title>Movie Catalog: Genres</title>
    </head>
    <body>
	
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
		
    </body>
</html>
