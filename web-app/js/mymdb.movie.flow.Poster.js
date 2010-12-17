
mymdb.movie.flow.PosterView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/poster',
    nextId:4,
    previousId:2,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'label', text:'Poster' }
            ]
        });

        this.on('activate',function(p){
            mymdb.movie.flow.DisableButtonFunction(p);
            mymdb.movie.flow.UpdateDialogTitleFunction(p, 'New Movie: Poster');
        });

        mymdb.movie.flow.PosterView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-poster', mymdb.movie.flow.PosterView);