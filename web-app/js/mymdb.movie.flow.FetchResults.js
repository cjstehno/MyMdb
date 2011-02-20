
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
                    width:510,
                    listeners:{
                        specialkey:{
                            scope:this,
                            fn:function( field, e ){
                                if(e.getKey() == 13) this.doSearch( field.getValue() );
                            }
                        }
                    }
                },
                {
                    xtype:'button',
                    text:'Search',
                    style:'padding:4px',
                    scope:this,
                    handler:function(b,e){
                        this.doSearch( this.findByType('textfield')[0].getValue() );
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
    },
    doSearch:function(value){
        this.findByType('movieflow-fetchresults-grid')[0].searchFor(value);
    },
    next:function(){
        var selectedId = null;
        var providerId = null;

        var grid = this.findByType('movieflow-fetchresults-grid')[0];
        if(grid.getSelectionCount() > 0){
            var rec = grid.getSelectedRecords()[0];
            selectedId = rec.data.id;
            providerId = rec.data.providerId;
        }
                
        var panel = this.findParentByType(mymdb.movie.flow.MovieManagerFlowPanel);
        var nid = this.nextId;

        this.getForm().submit({
            method:'POST', url:this.formUrl,
            params:{
                selectedId:selectedId,
                providerId:providerId
            },
            success:function(){
                panel.getLayout().setActiveItem(nid);
            },
            failure:function(){
                Ext.Msg.alert('Error','Unable to submit form');
            }
        });
    }
});
Ext.reg('movieflow-fetchresults', mymdb.movie.flow.FetchResultsView);

/**
 * List view where movie fetch results are displayed for selection.
 */
mymdb.movie.flow.ResultsView = Ext.extend( Ext.list.ListView, {
    emptyText: 'No results found.',
    loadingText:'Searching...',
    reserveScrollOffset: true,
    hideHeaders:false,
    multiSelect:false,
    singleSelect:true,
    columns:[
        {header:'Title', dataIndex:'title', width:0.3 },
        {header:'Year', dataIndex:'releaseYear', width:0.08 },
        {header:'Provider', dataIndex:'providerId', width:0.12 },
        {header:'Description', dataIndex:'description', width:0.50 }
    ],
    initComponent: function(){
        Ext.apply(this, {
            store:new Ext.data.JsonStore({
                url:'movie/fetch/search',
                autoLoad: false,
                autoDestroy: true,
                root: 'items',
                idProperty: 'id',
                fields: ['id','title', 'releaseYear', 'description', 'providerId']
            })
        });

        this.on('dblclick', function( view, index, node, e ){
            var selRec = view.getSelectedRecords()[0];
            var preview = new mymdb.movie.flow.ResultPreview({ movieId:selRec.data.id, providerId:selRec.data.providerId });
            preview.show();
        });

        mymdb.movie.flow.ResultsView.superclass.initComponent.apply(this, arguments);
    },
    searchFor:function(title){
        this.getStore().load({ params:{ title:title } });
    }
});
Ext.reg('movieflow-fetchresults-grid', mymdb.movie.flow.ResultsView);

/**
 * Dialog window used to display the preview of a selected fetched movie's data.
 */
mymdb.movie.flow.ResultPreview = Ext.extend(Ext.Window, {
    title:'Movie Preview',
    modal:true,
    width:600,
    height:500,
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            buttons:[
                {
                    text:'Close',
                    scope:this,
                    handler:function(b,e){ this.close(); }
                }
            ],
            items:[
                {
                    xtype:'panel',
                    autoLoad:'movie/fetch/preview/' + this.providerId + '/' + this.movieId
                }
            ]
        });

        mymdb.movie.flow.ResultPreview.superclass.initComponent.apply(this, arguments);
    }
});