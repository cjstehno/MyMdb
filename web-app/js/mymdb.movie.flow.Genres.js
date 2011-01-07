
mymdb.movie.flow.GenresView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/genre',
    nextId:4,
    previousId:2,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'label', text:'Select genres for movie:' },
                { xtype:'movieflow-itemselector', availableUrl:'genre/list' },
                { 
                    xtype:'button',
                    text:'New Genre',
                    style:{padding:'4px'},
                    icon:'/mymdb/images/icons/add.png',
                    handler: function(b,e){
                        new mymdb.genre.GenreDialog({
                            onSave:function(){
                                (b.findParentByType('movieflow-genre').find('itemId', 'selector-available')[0]).getStore().load();
                            }
                        });
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

        var selectedRecs = (this.find('itemId','selector-selected')[0]).getStore().getRange();
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

mymdb.movie.flow.ItemSelector = Ext.extend( Ext.Panel, {
    border:false,
    layout:'hbox',
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'movieflow-itemselector-listpanel',
                    items:[
                        {
                            xtype:'movieflow-selector-itemslist',
                            itemId:'selector-available',
                            columns: [{header:'Available', dataIndex:'label'}],
                            store:new Ext.data.JsonStore({
                                url:this.availableUrl,
                                autoLoad: true,
                                autoDestroy: true,
                                root: 'items',
                                idProperty: 'id',
                                fields: ['id','label']
                            })
                        }
                    ]
                },
                {
                    xtype:'panel',
                    width:45,
                    height:300,
                    border:false,
                    items:[
                        {
                            xtype:'button',
                            icon:'/mymdb/images/icons/next.png',
                            style:{padding:'11px'},
                            handler:function(b,e){
                                b.findParentByType('movieflow-itemselector').moveSelectedItems(true);
                            }
                        },
                        {
                            xtype:'button',
                            icon:'/mymdb/images/icons/back.png',
                            style:{padding:'11px'},
                            handler:function(b,e){
                                b.findParentByType('movieflow-itemselector').moveSelectedItems(false);
                            }
                        }
                    ]
                },
                {
                    xtype:'movieflow-itemselector-listpanel',
                    items:[
                        {
                            xtype:'movieflow-selector-itemslist',
                            itemId:'selector-selected',
                            columns: [{header:'Selected', dataIndex:'label'}],
                            moveRight:false,
                            store: []
                        }
                    ]
                }
            ]
        });

        mymdb.movie.flow.ItemSelector.superclass.initComponent.apply(this, arguments);
    },
    moveSelectedItems:function(moveRight){
        var fromList = this.find('itemId', 'selector-available')[0];
        var toList = this.find('itemId', 'selector-selected')[0];

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
    }
});
Ext.reg('movieflow-itemselector', mymdb.movie.flow.ItemSelector);

mymdb.movie.flow.ItemSelectorListPanel = Ext.extend(Ext.Panel, {
    width:275,
    height:350,
    autoScroll:true,
    initComponent: function(){
        Ext.apply(this, {});

        mymdb.movie.flow.ItemSelectorListPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-itemselector-listpanel', mymdb.movie.flow.ItemSelectorListPanel);

mymdb.movie.flow.ItemSelectorItemsList = Ext.extend( Ext.list.ListView, {
    emptyText: 'None available.',
    loadingText:'Loading...',
    reserveScrollOffset: true,
    hideHeaders:false,
    multiSelect:true,
    moveRight:true,
    initComponent: function(){
        mymdb.movie.flow.ItemSelectorItemsList.superclass.initComponent.apply(this, arguments);

        this.on( 'dblclick', function(dataView,idx,node,evt){
            dataView.findParentByType('movieflow-itemselector').moveSelectedItems(this.moveRight);
        });
    }
});
Ext.reg('movieflow-selector-itemslist',mymdb.movie.flow.ItemSelectorItemsList);

