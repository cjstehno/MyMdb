<div>
    <h1 style='margin-bottom:5px;border-bottom:1px dashed gray;padding:4px;'>${fieldValue(bean: movieInstance, field: "title")} (${movieInstance.releaseYear})</h1>

    <div style='float:left'><img src='<g:createLink controller="poster" action="show" params="[id:movieInstance.poster?.id]" />' style='margin:10px;border:1px solid gray;' width='160' /></div>

    <div>
        <div style='margin-bottom:8px;'><b>Location:</b> ${movieInstance.storage.name}-${movieInstance.storage.index}</div>

        <div style='margin-bottom:8px;'><b>Format:</b> ${movieInstance.format}</div>

        <div style='margin-bottom:8px;'><b>Runtime:</b> ${movieInstance.runtime} mins</div>

        <div style='margin-bottom:8px;'><b>Rating:</b> ${movieInstance.mpaaRating}</div>

        <div><b>Genre:</b></div>
        <div style='margin-bottom:8px;margin-left:8px;'>
            <g:each in="${movieInstance.genres}" var="g">
                <span>${g?.name}</span>,
            </g:each>
        </div>

        <div><b>Actors:</b></div>
        <div style='margin-bottom:8px;margin-left:8px;'>
            <g:each in="${movieInstance.actors}" var="a">
                <span>${a?.firstName} ${a?.middleName} ${a?.lastName}</span>,
            </g:each>
        </div>

        <div><b>Sites:</b></div>
        <div style='margin-bottom:8px;margin-left:8px;'>
            <ul>
            <g:each in="${movieInstance.sites}" var="s">
                <li><a href="${s.url}" target="_blank">${s.label}</a></li>
            </g:each>
            </ul>
        </div>

        <div><b>Description:</b></div>
        <div style='margin-left:8px;'>${movieInstance.description}</div>
    </div>
</div>
