
mymdb.movie.flow.GenresView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/genre',
    nextId:4,
    previousId:2,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'label', text:'Select genres for movie:' },
                {
                    xtype:'panel',
                    border:false,
                    layout:'hbox',
                    items:[
                        { xtype:'movieflow-selector-availablepanel' },
                        { xtype:'movieflow-selector-buttons' },
                        { xtype:'movieflow-selector-selectedpanel' },
                    ]
                },
                { 
                    xtype:'button',
                    text:'New Genre',
                    style:{padding:'4px'},
                    icon:'/mymdb/images/icons/add.png',
                    handler: function(){ 
                        new mymdb.genre.GenreDialog({reloadTarget:'available-items'});
                    }
                }
            ]
        });

        this.on('activate',function(p){
            mymdb.movie.flow.DisableButtonFunction(p);
            mymdb.movie.flow.UpdateDialogTitleFunction(p, 'New Movie: Genres');
        });

        mymdb.movie.flow.GenresView.superclass.initComponent.apply(this, arguments);
    },
    next:function(){
        var panel = this.findParentByType(mymdb.movie.flow.MovieManagerFlowPanel);
        var nid = this.nextId;

        var selectedRecs = (this.findByType('movieflow-selector-selectedlist')[0]).getStore().getRange();
        var genreIds = [];
        Ext.each(selectedRecs, function(it){
            genreIds.push(it.data.id);
        });

        this.getForm().submit({
            method:'POST', url:this.formUrl,
            params:{
                genres:genreIds
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
Ext.reg('movieflow-genre', mymdb.movie.flow.GenresView);

mymdb.movie.flow.AvailableItemsPanel = Ext.extend( Ext.Panel, {
    width:275,
    height:350,
    autoScroll:true,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'movieflow-selector-availablelist',
                    store:new Ext.data.JsonStore({
                        url:'genre/list',
                        autoLoad: true,
                        autoDestroy: true,
                        storeId: 'movie-flow-genres-avail',
                        root: 'items',
                        idProperty: 'id',
                        fields: ['id','label']
                    })
                }
            ]
        });

        mymdb.movie.flow.AvailableItemsPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-selector-availablepanel', mymdb.movie.flow.AvailableItemsPanel);

mymdb.movie.flow.SelectorButtonPanel = Ext.extend( Ext.Panel, {
    width:45,
    height:300,
    border:false,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'button',
                    icon:'/mymdb/images/icons/next.png',
                    style:{padding:'11px'},
                    handler:function(b,e){
                        mymdb.movie.flow.MoveItemFunction(b,true);
                    }
                },
                { 
                    xtype:'button',
                    icon:'/mymdb/images/icons/back.png',
                    style:{padding:'11px'},
                    handler:function(b,e){
                        mymdb.movie.flow.MoveItemFunction(b,false);
                    }
                }
            ]
        });

        mymdb.movie.flow.SelectorButtonPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-selector-buttons', mymdb.movie.flow.SelectorButtonPanel);

mymdb.movie.flow.MoveItemFunction = function(ctx,moveRight){
    var parent = ctx.findParentByType(mymdb.movie.flow.GenresView);
    var fromList = parent.findByType('movieflow-selector-availablelist')[0];
    var toList = parent.findByType('movieflow-selector-selectedlist')[0];

    if(!moveRight){
        var tmp = fromList;
        fromList = toList;
        toList = tmp;
    }

    if( fromList.getSelectionCount() > 0 ){
        var recs = fromList.getSelectedRecords();
        fromList.getStore().remove(recs);

        toList.getStore().add(recs);
    }
};

mymdb.movie.flow.SelectedItemsPanel = Ext.extend( Ext.Panel, {
    width:275,
    height:350,
    autoScroll:true,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { 
                    xtype:'movieflow-selector-selectedlist',
                    store: []
                }
            ]
        });

        mymdb.movie.flow.SelectedItemsPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-selector-selectedpanel', mymdb.movie.flow.SelectedItemsPanel);

mymdb.movie.flow.AvailableItemsList = Ext.extend( Ext.list.ListView, {
    id:'available-items',
    emptyText: 'None available.',
    loadingText:'Loading...',
    reserveScrollOffset: true,
    hideHeaders:false,
    multiSelect:true,
    columns: [{header:'Available', dataIndex:'label'}],

    initComponent: function(){
        mymdb.movie.flow.AvailableItemsList.superclass.initComponent.apply(this, arguments);

        this.on( 'dblclick', function(dataView,idx,node,evt){
            mymdb.movie.flow.MoveItemFunction(dataView,true);
        });
    }
});
Ext.reg('movieflow-selector-availablelist', mymdb.movie.flow.AvailableItemsList);

mymdb.movie.flow.SelectedItemsList = Ext.extend( Ext.list.ListView, {
    emptyText: 'None selected.',
    loadingText:'Loading...',
    reserveScrollOffset: true,
    hideHeaders:false,
    multiSelect:true,
    columns: [{header:'Selected', dataIndex:'label'}],

    initComponent: function(){
        mymdb.movie.flow.SelectedItemsList.superclass.initComponent.apply(this, arguments);

        this.on( 'dblclick', function(dataView,idx,node,evt){
            mymdb.movie.flow.MoveItemFunction(dataView,false);
        });
    }
});
Ext.reg('movieflow-selector-selectedlist', mymdb.movie.flow.SelectedItemsList);