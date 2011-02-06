
mymdb.movie.flow.FetchResultsView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/fetch',
    nextId:1,
    layout:'table',
    layoutConfig:{
        columns: 3
    },
    defaults:{
        height:30
    },
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'label', text:'Title:', style:'padding:4px' },
                {
                    xtype: 'textfield',
                    name:'title',
                    fieldLabel:'Title',
                    width:510
                },
                {
                    xtype:'button',
                    text:'Search',
                    style:'padding:4px',
                    scope:this,
                    handler:function(b,e){
                        var title = this.findByType('textfield')[0].getValue();
                        this.findByType('movieflow-fetchresults-grid')[0].searchFor(title);
                    }
                },
                    
                { xtype:'movieflow-fetchresults-grid', colspan:3, height:355 }
            ]
        });

        this.on('activate',function(p){
            this.disableNavButtons( ['prev-btn', 'finish-btn'] );
            this.setDialogTitle('New Movie: Fetch Results');
        }, this);

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
    columns:[
        {header:'Title', dataIndex:'title', width:0.4 },
        {header:'Year', dataIndex:'releaseYear', width:0.08 },
        {header:'Description', dataIndex:'description', width:0.52 }
    ],
    initComponent: function(){
        Ext.apply(this, {
            store:new Ext.data.JsonStore({
                url:'movie/fetch/search',
                autoLoad: false,
                autoDestroy: true,
                root: 'items',
                idProperty: 'movieId',
                fields: ['movieId','title', 'releaseYear', 'description']
            })
        });

        mymdb.movie.flow.ResultsView.superclass.initComponent.apply(this, arguments);
    },
    searchFor:function(title){
        this.getStore().load({ params:{ title:title } });
    }
});
Ext.reg('movieflow-fetchresults-grid', mymdb.movie.flow.ResultsView);