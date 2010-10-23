<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="mobile" />
  </head>
  <body>
    <h1>${movie.title} (${movie.releaseYear})</h1>

    <img src='<g:createLink controller="poster" action="image" params="[id:movie.id]" />' style="margin:5px;border:1px solid gray;" />

    <div><b>Box:</b> <g:link controller="mobile" action="box" params="[id:movie.storage.name]">${movie.storage.name}</g:link>-${movie.storage.index}</div>

    <div><b>Genre:</b>
      <g:each in="${movie.genres}" var="g">
        <g:link controller="mobile" action="genre" params="[id:g.id]">${g.name}</g:link>,
      </g:each></div>

    <div><b>Actors:</b>
      <g:each in="${movie.actors}" var="a">
        <g:link controller="mobile" action="actor" params="[id:a.id]">${a?.firstName} ${a?.middleName} ${a?.lastName}</g:link>,
      </g:each></div>

    <p>${movie.description}</p>
    
  </body>
</html>
