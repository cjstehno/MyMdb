<html>
    <head>
        <title>My Movie Database</title>
		<meta name="layout" content="main" />
    </head>
    <body>
		<div id="body">
	        <h1>Let's Watch a Movie!</h1>
			
			<div><g:link controller="catalog" action="index">Catalogs</g:link></div>
			
			<h2>Admin Stuff</h2>
			
			<div><g:link controller="movie" action="list">Manage Movies</g:link></div>
			<div><g:link controller="actor" action="list">Manage Actors</g:link></div>
			<div><g:link controller="genre" action="list">Manage Genres</g:link></div>

		</div>
    </body>
</html>