
mymdb.movie.flow.ActorsView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/actor',
    nextId:6,
    previousId:4,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'label', text:'Actors' }
            ]
        });

        this.on('activate',function(p){
            mymdb.movie.flow.DisableButtonFunction(p);
            mymdb.movie.flow.UpdateDialogTitleFunction(p, 'New Movie: Actors');
        });

        mymdb.movie.flow.ActorsView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-actor', mymdb.movie.flow.ActorsView);