<div>
    <h1 style='margin-bottom:5px;border-bottom:1px dashed gray;padding:4px;'>${title} (${releaseYear})</h1>

    <div style='float:left'><img src='<g:createLink controller="poster" action="flow" />' style='margin:10px;border:1px solid gray;' width='160' /></div>

    <div style='float:right;width:400px;'>
        <div style='margin-bottom:8px;'><b>Location:</b> ${storage}</div>

        <div><b>Genre:</b></div>
        <div style='margin-bottom:8px;margin-left:8px;'>
            <g:each in="${genres}" var="g">
                <span>${g?.name}</span>,
            </g:each>
        </div>

        <div><b>Actors:</b></div>
        <div style='margin-bottom:8px;margin-left:8px;'>
            <g:each in="${actors}" var="a">
                <span>${a?.firstName} ${a?.middleName} ${a?.lastName}</span>,
            </g:each>
        </div>

        <div><b>Description:</b></div>
        <div style='margin-left:8px;'>${description}</div>
    </div>
</div>
