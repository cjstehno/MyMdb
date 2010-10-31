mymdb.GenreManagerDialog = Ext.extend( Ext.Window ,{
    autoShow:true,
    closable:true,
    initHidden:false,
    modal:true,
    width:400,
    height:300,
    title:'Genre Manager',
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            items:[ { xtype:'genremanagerpanel' } ],
            tbar:[ mymdb.NewGenreActionFactory('button'), ]
        });
        mymdb.GenreManagerDialog.superclass.initComponent.apply(this, arguments);
    }
});

mymdb.NewGenreActionFactory = function(xt){
    return {
        xtype:xt,
        text:'New Genre',
        icon:'/mymdb/images/icons/add.png',
        handler: function(){ new mymdb.GenreDialog(); }
    };
}

mymdb.GenreListView = Ext.extend( Ext.list.ListView, {
	emptyText: 'No Genres',
	reserveScrollOffset: true,
	hideHeaders:false,
	multiSelect:false,
	columns: [{header:'Genre', dataIndex:'lbl'}, {header:'# Movies',dataIndex:'cnt'}],

    initComponent: function(){
        mymdb.GenreListView.superclass.initComponent.apply(this, arguments);

        Ext.getBody().on("contextmenu", Ext.emptyFn, null, {preventDefault: true});

		this.on( 'contextmenu', function( dataView, idx, node, evt ){
            var popup = new Ext.menu.Menu({
                items:[
                    {
                        xtype:'menuitem',
                        text:'Edit',
                        icon:'/mymdb/images/icons/edit.png'
                    },
                    mymdb.NewGenreActionFactory('menuitem'),
                    {
                        xtype:'menuitem',
                        text:'Delete',
                        icon:'/mymdb/images/icons/delete.png'
                    }
                ]
            });
            popup.showAt( evt.getXY() );

//            var st = dataView.getStore();
//            var storeId = st.storeId;
//            var categoryId = st.getAt( idx ).data.cid;
//            var gridStore = Ext.getCmp('movieGridPanel').getStore();
//            gridStore.load({params:{ sid:storeId, cid:categoryId }});
		} );
    }
});

mymdb.GenreManagerPanel = Ext.extend( Ext.Panel, {
    autoScroll:true,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                new mymdb.GenreListView({
                    store:new Ext.data.JsonStore({
                        url:'genre/all',
                        autoLoad: true,
                        autoDestroy: true,
                        storeId: 'genre_manager_store',
                        root: 'items',
                        idProperty: 'cid',
                        fields: ['cid','lbl','cnt']
                    })
                })
            ]
        });
        mymdb.GenreManagerPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('genremanagerpanel', mymdb.GenreManagerPanel);

mymdb.GenreDialog = Ext.extend( Ext.Window ,{
    autoShow:true,
    closable:true,
    initHidden:false,
    modal:true,
    width:200,
    height:200,
    title:'Genre',
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            items:[ { xtype:'genreformpanel' } ]
        });
        mymdb.GenreDialog.superclass.initComponent.apply(this, arguments);
    }
});

mymdb.GenreFormPanel = Ext.extend( Ext.Panel, {
    html:'This will be an editor form'
});
Ext.reg('genreformpanel', mymdb.GenreFormPanel);