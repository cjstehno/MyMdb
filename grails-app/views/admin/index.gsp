<html>
    <head>
        <title>My Movie Database Admin</title>
		<meta name="layout" content="main" />
    </head>
    <body>
		<div id="body">
	        <h1>Admin Actions</h1>
			
			<h2>Movie Data</h2>
			<ul>
				<li><g:link controller="movie" action="list">Manage Movies</g:link></li>
				<li><g:link controller="actor" action="list">Manage Actors</g:link></li>
				<li><g:link controller="genre" action="list">Manage Genres</g:link></li>
			</ul>

			<h2>Data Transfer</h2>
			<ul>
				<li><g:link controller="admin" action="exportData">Export</g:link></li>
				<li>
					<g:form controller="admin" action="importData" method="post" enctype="multipart/form-data">
						Import: <input type="file" name="data"/>
						<button>Submit</button>
					</g:form>
				</li>
				<li>
					<g:form controller="admin" action="batchAdd" method="post" enctype="multipart/form-data">
						Batch Add: <input type="file" name="data"/>
						<button>Submit</button>
					</g:form>
				</li>				
			</ul>
			
		</div>
    </body>
</html>