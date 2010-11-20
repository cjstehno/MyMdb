
<div>
  <img src='<g:createLink controller="poster" action="image" params="[id:movieInstance.id]" />' style="float:left;margin:5px;border:1px solid gray;" />

  <h1 style="border-bottom:1px solid gray;">${fieldValue(bean: movieInstance, field: "title")} (${movieInstance.releaseYear})</h1>
  <div><b>Box:</b> ${movieInstance.storage.name}-${movieInstance.storage.index}</div>

  <div><b>Genre:</b>
  <g:each in="${movieInstance.genres}" var="g">
    <span>${g?.name}</span>, 
  </g:each></div>

  <div><b>Actors:</b>
  <g:each in="${movieInstance.actors}" var="a">
    <span style="border-bottom:1px dashed gray;">${a?.firstName} ${a?.middleName} ${a?.lastName}</span>, 
  </g:each></div>

  <p style="padding:10px;margin-left:15px;margin-right:15px;">${movieInstance.description}</p>

</div>
