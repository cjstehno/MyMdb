<html>
  <head>
    <meta name="layout" content="browser" />
    <style type="text/css">
      .icon-about { background-image: url('${fam.icon(name:'information')}')!important; }
      .icon-help { background-image: url('${fam.icon(name:'help')}')!important; }

      .icon-genre { background-image: url('${fam.icon(name:'package')}')!important; }
      .icon-add-genre { background-image: url('${fam.icon(name:'package_add')}')!important; }
      .icon-edit-genre { background-image: url('${fam.icon(name:'package')}')!important; }
      .icon-delete-genre { background-image: url('${fam.icon(name:'package_delete')}')!important; }

      .icon-actor { background-image: url('${fam.icon(name:'group')}')!important; }
      .icon-add-actor { background-image: url('${fam.icon(name:'group_add')}')!important; }
      .icon-edit-actor { background-image: url('${fam.icon(name:'group_edit')}')!important; }
      .icon-delete-actor { background-image: url('${fam.icon(name:'group_delete')}')!important; }

      .icon-add-movie { background-image: url('${fam.icon(name:'film_add')}')!important; }
      .icon-edit-movie { background-image: url('${fam.icon(name:'film_edit')}')!important; }
      .icon-delete-movie { background-image: url('${fam.icon(name:'film_delete')}')!important; }
      .icon-movie { background-image: url('${fam.icon(name:'film')}')!important; }

      .icon-add-site { background-image: url('${fam.icon(name:'world_add')}')!important; }
      .icon-edit-site { background-image: url('${fam.icon(name:'world_edit')}')!important; }
      .icon-delete-site { background-image: url('${fam.icon(name:'world_delete')}')!important; }

      .icon-storage { background-image: url('${fam.icon(name:'brick')}')!important; }
      .icon-add-storage { background-image: url('${fam.icon(name:'brick_add')}')!important; }
      .icon-edit-storage { background-image: url('${fam.icon(name:'brick_edit')}')!important; }
      .icon-delete-storage { background-image: url('${fam.icon(name:'brick_delete')}')!important; }

      .icon-user { background-image: url('${fam.icon(name:'user')}')!important; }
      .icon-add-user { background-image: url('${fam.icon(name:'user_add')}')!important; }
      .icon-edit-user { background-image: url('${fam.icon(name:'user_edit')}')!important; }
      .icon-delete-user { background-image: url('${fam.icon(name:'user_delete')}')!important; }

      .icon-right { background-image: url('${fam.icon(name:'arrow_right')}')!important; }
      .icon-left { background-image: url('${fam.icon(name:'arrow_left')}')!important; }

      .x-list-selected { background-color:#F5ECC6; border: 1px dashed gray; }
      .x-view-selected { background-color:#F5ECC6; border: 1px dashed gray; }
    </style>
    <g:javascript library="mymdb.app" />
    <g:javascript library="mymdb.Dialogs" />
    <g:javascript library="mymdb.genre.GenreManager" />
    <g:javascript library="mymdb.actor.ActorManager" />
    <g:javascript library="mymdb.storage.StorageManager" />
    <g:javascript library="mymdb.admin.UserManager" />
    <g:javascript library="mymdb.movie.flow.MovieFlow" />
    <g:javascript library="mymdb.movie.flow.FetchResults" />
    <g:javascript library="mymdb.movie.flow.Details" />
    <g:javascript library="mymdb.movie.flow.Poster" />
    <g:javascript library="mymdb.movie.flow.Genres" />
    <g:javascript library="mymdb.movie.flow.Actors" />
    <g:javascript library="mymdb.movie.flow.WebSites" />
    <g:javascript library="mymdb.movie.flow.Summary" />
    <g:javascript library="mymdb.ContentPanel" />
    <g:javascript library="mymdb.CategoriesPanel" />
    <script type="text/javascript">Ext.onReady(mymdb.app.init, mymdb.app);</script>
  </head>
  <body></body>
</html>
