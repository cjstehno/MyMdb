
mymdb.movie.flow.GenresView = Ext.extend(Ext.form.FormPanel, {
    initComponent: function(){
        Ext.apply(this, {
            formUrl:'movie/genre',
            items:[
                { xtype:'label', text:'Genres' },
                { xtype:'movieflow-nextbutton', text:'Next', nextId:5 },
                { xtype:'movieflow-nextbutton', text:'Previous', nextId:3 },
            ]
        });

        this.on('activate',mymdb.movie.flow.FlowViewInitFunction);

        mymdb.movie.flow.GenresView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-genre', mymdb.movie.flow.GenresView);