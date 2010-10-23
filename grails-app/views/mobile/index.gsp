<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="mobile" />
  </head>
  <body>
    <h1>${categoryName?:'Categories'}</h1>

    <g:each var="item" in="${listItems}">
      <g:if test="${item.id}">
           <div><g:link controller="mobile" action="${item.category}" params="[id:item.id]">${item.label}</g:link></div>
      </g:if>
      <g:else>
           <div><g:link controller="mobile" action="${item.category}">${item.label}</g:link></div>
      </g:else>
    </g:each>

  </body>
</html>
