<html>
  <head>
    <meta name="layout" content="browser" />
    <style type="text/css">
      .icon-about { background-image: url(/mymdb/images/icons/info.png)!important; }
      .icon-help { background-image: url(/mymdb/images/icons/help.png)!important; }
      .icon-genre { background-image: url(/mymdb/images/icons/film.png)!important; }
      .icon-actor { background-image: url(/mymdb/images/icons/search_user.png)!important; }
      .icon-add-movie { background-image: url(/mymdb/images/icons/add.png)!important; }
      .icon-movie { background-image: url(/mymdb/images/icons/office_folders.png)!important; }

      .x-list-selected { background-color:#F5ECC6; border: 1px dashed gray; }
    </style>
    <g:javascript library="mymdb.app" />
    <g:javascript library="mymdb.Dialogs" />
    <g:javascript library="mymdb.genre.GenreManager" />
    <g:javascript library="mymdb.actor.ActorManager" />
    <g:javascript library="mymdb.movie.MovieManager" />
    <g:javascript library="mymdb.ContentPanel" />
    <g:javascript library="mymdb.CategoriesPanel" />
    <script type="text/javascript">Ext.onReady(mymdb.app.init, mymdb.app);</script>
  </head>
  <body></body>
</html>
