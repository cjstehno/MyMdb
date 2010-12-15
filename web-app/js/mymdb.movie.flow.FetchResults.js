
mymdb.movie.flow.FetchResultsView = Ext.extend(Ext.form.FormPanel, {
    initComponent: function(){
        Ext.apply(this, {
            formUrl:'movie/fetchResults',
            items:[
                { xtype:'textfield', fieldLabel:'Title', name:'title', readOnly:true },
                { xtype:'movieflow-nextbutton', text:'Next', nextId:2 },
                { xtype:'movieflow-nextbutton', text:'Previous', nextId:0 },
            ]
        });

        this.on('activate',mymdb.movie.flow.FlowViewInitFunction);

        mymdb.movie.flow.FetchResultsView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-fetchresults', mymdb.movie.flow.FetchResultsView);