
mymdb.movie.flow.ActorsView = Ext.extend(Ext.form.FormPanel, {
    initComponent: function(){
        Ext.apply(this, {
            formUrl:'movie/actor',
            items:[
                { xtype:'label', text:'Actors' },
                { xtype:'movieflow-nextbutton', text:'Next', nextId:6 },
                { xtype:'movieflow-nextbutton', text:'Previous', nextId:4 },
            ]
        });

        this.on('activate',mymdb.movie.flow.FlowViewInitFunction);

        mymdb.movie.flow.ActorsView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-actor', mymdb.movie.flow.ActorsView);