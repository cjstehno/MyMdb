<html>
    <head>
        <title><g:layoutTitle default="My Movie Database Admin" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
		<link rel="stylesheet" href="${resource(dir:'css',file:'mymdb.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="application" />
    </head>
    <body>
        <div id="grailsLogo" class="logo">My Movie Database</div>
        <div class="nav">
			<span class="menuButton"><g:link controller="catalog" action="index">Catalogs</g:link></span>
			<span class="menuButton"><g:link controller="admin" action="index">Admin</g:link></span>
        </div>
		<g:layoutBody />
    </body>
</html>