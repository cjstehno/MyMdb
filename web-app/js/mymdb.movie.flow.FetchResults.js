
mymdb.movie.flow.FetchResultsView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/fetchResults',
    nextId:2,
    previousId:0,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'textfield', fieldLabel:'Title', name:'title', readOnly:true }
            ]
        });

        this.on('activate',function(p){
            mymdb.movie.flow.DisableButtonFunction(p);
            mymdb.movie.flow.UpdateDialogTitleFunction(p, 'New Movie: Fetch Results');
        });

        mymdb.movie.flow.FetchResultsView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-fetchresults', mymdb.movie.flow.FetchResultsView);