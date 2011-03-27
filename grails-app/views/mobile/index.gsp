<html>
    <head>
        <meta name="layout" content="mobile" />
        <style type="text/css">
            a {
                color:white;
                text-decoration:none;
            }
            body {
                margin-top: 0px;
                margin-bottom: 0px;
                margin-left: 0px;
                margin-right: 0px;
            }
            div {
                width:100%;
                font-size:50pt;
                padding: 4px
            }

            .item {
                border-bottom: 1px dashed gray;
            }

        </style>

        <g:javascript library="mymdb.mobile.app" />
    </head>
    <body>

        <div>
            <div style="background-color:blue;color:white"><a href='index'>Catgories:</a> ${ categoryName ?: ''}</div>
            <g:each in="${listItems}" var="category">
                <div class="item" onclick="openCategory('${category.category}')">${category.label}</div>
            </g:each>

        </div>

    </body>
</html>
