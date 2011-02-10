<html>
  <head>
    <meta name="layout" content="browser" />
    <style type="text/css">
      .icon-about { background-image: url('${fam.icon(name:'information')}')!important; }
      .icon-help { background-image: url('${fam.icon(name:'help')}')!important; }
      .icon-genre { background-image: url('${fam.icon(name:'package')}')!important; }
      .icon-actor { background-image: url('${fam.icon(name:'group')}')!important; }
      .icon-add-movie { background-image: url('${fam.icon(name:'film_add')}')!important; }
      .icon-movie { background-image: url('${fam.icon(name:'film')}')!important; }

      .x-list-selected { background-color:#F5ECC6; border: 1px dashed gray; }
      .x-view-selected { background-color:#F5ECC6; border: 1px dashed gray; }
    </style>
    <g:javascript library="mymdb.app" />
    <g:javascript library="mymdb.Dialogs" />
    <g:javascript library="mymdb.genre.GenreManager" />
    <g:javascript library="mymdb.actor.ActorManager" />
    <g:javascript library="mymdb.movie.flow.MovieFlow" />
    <g:javascript library="mymdb.movie.flow.FetchResults" />
    <g:javascript library="mymdb.movie.flow.Details" />
    <g:javascript library="mymdb.movie.flow.Poster" />
    <g:javascript library="mymdb.movie.flow.Genres" />
    <g:javascript library="mymdb.movie.flow.Actors" />
    <g:javascript library="mymdb.movie.flow.Summary" />
    <g:javascript library="mymdb.ContentPanel" />
    <g:javascript library="mymdb.CategoriesPanel" />
    <script type="text/javascript">Ext.onReady(mymdb.app.init, mymdb.app);</script>
  </head>
  <body></body>
</html>
