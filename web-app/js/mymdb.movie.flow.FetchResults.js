
mymdb.movie.flow.FetchResultsView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/fetchResults',
    nextId:1,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'compositefield',
                    items:[
                        {
                            xtype: 'textfield',
                            name:'title',
                            fieldLabel:'Title',
                            width:300
                        },
                        {
                            xtype:'button',
                            text:'Fetch',
                            handler:function(){ Ext.Msg.alert('Not Supported', 'Sorry, this feature is not supported yet.'); }
                        }
                    ]
                },
                { xtype:'movieflow-fetchresults-grid' }
            ]
        });

        this.on('activate',function(p){
            mymdb.movie.flow.DisableButtonFunction(p, 'prev-btn');
            mymdb.movie.flow.UpdateDialogTitleFunction(p, 'New Movie: Fetch Results');
        });

        mymdb.movie.flow.FetchResultsView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-fetchresults', mymdb.movie.flow.FetchResultsView);

mymdb.movie.flow.ResultsView = Ext.extend( Ext.list.ListView, {
    emptyText: 'No results found.',
    loadingText:'Searching...',
    reserveScrollOffset: true,
    hideHeaders:false,
    multiSelect:false,
    singleSelect:true,
    columns: [{header:'Title', dataIndex:'title'}],
    initComponent: function(){
        Ext.apply(this, {
            store:new Ext.data.ArrayStore({
                idIndex:0,
                autoLoad:false,
                fields:['title'],
                data:[] // FIXME: pull search result data on 'search' click
            })
        });

        mymdb.movie.flow.ResultsView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-fetchresults-grid', mymdb.movie.flow.ResultsView);