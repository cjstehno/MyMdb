mymdb.movie.flow.ItemSelector = Ext.extend( Ext.Panel, {
    border:false,
    layout:'hbox',
    initComponent: function(){
        Ext.apply(this, {
            store:new Ext.data.JsonStore({
                url:this.url,
                autoLoad: false,
                autoDestroy: true,
                root: 'items',
                idProperty: 'id',
                fields: ['id','label',{name:'selected', type:'boolean'}],
                listeners:{
                    load:{
                        scope:this,
                        fn:function(sto, recs, opts){
                            var availSto = this.find('itemId','selector-available')[0].getStore();
                            var selecSto = this.find('itemId','selector-selected')[0].getStore();

                            availSto.removeAll();
                            selecSto.removeAll();

                            Ext.each( recs, function(r){
                                if( r.data.selected ){
                                    selecSto.add( [r] );
                                } else {
                                    availSto.add( [r] );
                                }
                            });
                        }
                    }
                }
            }),
            items:[
                {
                    xtype:'movieflow-itemselector-listpanel',
                    items:[
                        {
                            xtype:'movieflow-selector-itemslist',
                            itemId:'selector-available',
                            columns: [{header:'Available', dataIndex:'label'}],
                            store:[]
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
                            iconCls:'icon-right',
                            style:{padding:'11px'},
                            handler:function(b,e){
                                b.findParentByType('movieflow-itemselector').moveSelectedItems(true);
                            }
                        },
                        {
                            xtype:'button',
                            iconCls:'icon-left',
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
                            store:[]
                        }
                    ]
                }
            ]
        });

        mymdb.movie.flow.ItemSelector.superclass.initComponent.apply(this, arguments);
    },
    getSelectedRecords:function(){
        return (this.find('itemId','selector-selected')[0]).getStore().getRange();
    },
    setSelectedItems:function( items ){
        console.log('setSelectedItems: ' + items);

        var fromStore = this.find('itemId', 'selector-available')[0].getStore();

        var recs = [];
        Ext.each(items,function(it){
            var rec = fromStore.getById(it.toString());
            if(rec){
                recs.push(rec);
            }
        });

        this.moveItems(true, recs);
    },
    // TODO: see if I can refactor these into one function
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
    },
    moveItems:function( moveRight, items ){
        var fromList = this.find('itemId', 'selector-available')[0];
        var toList = this.find('itemId', 'selector-selected')[0];

        if(!moveRight){
            var tmp = fromList;
            fromList = toList;
            toList = tmp;
        }

        if( items.length > 0 ){
            fromList.getStore().remove(items);
            toList.getStore().add(items);
        }
    }
});
Ext.reg('movieflow-itemselector', mymdb.movie.flow.ItemSelector);

mymdb.movie.flow.ItemSelectorListPanel = Ext.extend(Ext.Panel, {
    width:275,
    height:350,
    autoScroll:true,
    initComponent: function(){
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