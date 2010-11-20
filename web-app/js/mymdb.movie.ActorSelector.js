mymdb.movie.MovieFormActorsPanel = Ext.extend( Ext.Panel, {
    title:'Actors',
    bodyStyle:'padding:5px',
    autoScroll:true,
    initComponent: function(){
        Ext.apply(this, {
//            tbar:[ { xtype:'movie-opengenreselector-item' } ],
//            items:[ { xtype:'movieformgenreslist' } ]
        });
        mymdb.movie.MovieFormActorsPanel.superclass.initComponent.apply(this, arguments);
    }    
});
Ext.reg('movie-form-actorspanel', mymdb.movie.MovieFormActorsPanel);