
mymdb.movie.flow.SummaryView = Ext.extend(Ext.form.FormPanel, {
    initComponent: function(){
        Ext.apply(this, {
            formUrl:'movie/finish',
            items:[
                { xtype:'label', text:'Summary' },
                { xtype:'movieflow-nextbutton', text:'Next', nextId:6 },
                { xtype:'movieflow-nextbutton', text:'Previous', nextId:4 },
            ]
        });

        this.on('activate',mymdb.movie.flow.FlowViewInitFunction);

        mymdb.movie.flow.SummaryView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-summary', mymdb.movie.flow.SummaryView);