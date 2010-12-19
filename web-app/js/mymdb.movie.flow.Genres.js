
mymdb.movie.flow.GenresView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/genre',
    nextId:4,
    previousId:2,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'label', text:'Genres' }
            ]
        });

        this.on('activate',function(p){
            mymdb.movie.flow.DisableButtonFunction(p);
            mymdb.movie.flow.UpdateDialogTitleFunction(p, 'New Movie: Genres');
        });

        mymdb.movie.flow.GenresView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-genre', mymdb.movie.flow.GenresView);