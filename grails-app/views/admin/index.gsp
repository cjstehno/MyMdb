<html>
    <head>
        <title>My Movie Database Admin</title>
		<meta name="layout" content="main" />
    </head>
    <body>
		<div id="body">
	        <h1>Admin Actions</h1>
			
			<ul>
			<li><g:link controller="movie" action="list">Manage Movies</g:link></li>
			<li><g:link controller="actor" action="list">Manage Actors</g:link></li>
			<li><g:link controller="genre" action="list">Manage Genres</g:link></li>
			</ul>

		</div>
    </body>
</html>