<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="printable" />
        <title>Movie Catalog: Storage</title>
    </head>
    <body>
	
		<h2>Movies by Box</h2>
		<div class="list">
			<table cellspacing="0">
				<col width="50%" />
				<col width="50%" />
				<g:each in="${storage}" status="i" var="movie">
					<tr class="${i % 2 == 0 ? '' : 'odd'}">
						<td>${movie.storage.name}-${movie.storage.index}</td>
						<td align="right">${movie.title}</td>
					</tr>
				</g:each>
			</table>
		</div>
		
    </body>
</html>
