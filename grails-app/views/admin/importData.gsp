<html>
    <head>
        <title>My Movie Database Admin</title>
		<meta name="layout" content="main" />
    </head>
    <body>
		<div id="body">
	        <h1>Import</h1>
			
			<h2>Genres</h2>
			<g:each in="${genres}" var="genre">
				<div>${genre.name}</div>
			</g:each>
			
			<h2>Actors</h2>
			<g:each in="${actors}" var="actor">
				<div>${actor.firstName} ${actor.middleName} ${actor.lastName}</div>
			</g:each>

			<h2>Movies</h2>
			<g:each in="${movies}" var="movie">
				<div>${movie.title}</div>
			</g:each>				
			
		</div>
    </body>
</html>