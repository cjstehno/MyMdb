
mymdb.movie.flow.PosterView = Ext.extend(Ext.form.FormPanel, {
    initComponent: function(){
        Ext.apply(this, {
            formUrl:'movie/poster',
            items:[
                { xtype:'label', text:'Poster' },
                { xtype:'movieflow-nextbutton', text:'Next', nextId:4 },
                { xtype:'movieflow-nextbutton', text:'Previous', nextId:2 },
            ]
        });

        this.on('activate',mymdb.movie.flow.FlowViewInitFunction);

        mymdb.movie.flow.PosterView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-poster', mymdb.movie.flow.PosterView);