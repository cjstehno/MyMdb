
mymdb.MovieDetailsTab = Ext.extend( Ext.Panel, {
    closable:true,
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'panel',
                    autoHeight:true,
                    autoLoad:'browser/details?mid=' + this.movieId,
                    tbar:[
                        {
                            xtype:'button',
                            text:'Edit Movie',
                            iconCls:'icon-edit-movie',
                            handler:function(){
                                new mymdb.movie.MovieDialog({ movieId:this.movieId });
                            }
                        },
                        {
                            text:'Delete'
                        }
                    ]
                }
            ]
        });

        mymdb.MovieDetailsTab.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movietab', mymdb.MovieDetailsTab);

mymdb.MovieGridPanel = Ext.extend( Ext.grid.GridPanel, {
    id:'movieGridPanel',
    loadMask:true,
    frame: true,
    store: new Ext.data.JsonStore({
		autoLoad: true,
		autoDestroy: true,
		url: 'browser/list',
		storeId: 'gridData',
		root: 'movies',
		idProperty: 'mid',
		fields: ['mid','ti','yr','bx', 'ge'/*,'fav'*/]
	}),
    sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
    viewConfig: {
        forceFit: true
    },
    colModel: new Ext.grid.ColumnModel({
        defaults: {sortable: true},
        columns: [
            {header: 'Title', dataIndex: 'ti'},
            {header: 'Genres', dataIndex: 'ge', sortable:false},
            {header: 'Year', dataIndex: 'yr'},
            {header: 'Storage', dataIndex: 'bx'}/*,
            {header:'Favorite', dataIndex:'fav' }*/
        ]
    }),

    initComponent: function(){
        Ext.apply(this, {
        });

        mymdb.MovieGridPanel.superclass.initComponent.apply(this, arguments);

        this.on( 'rowdblclick', function(g,idx,evt){
            var selectedMovie = g.store.getAt(idx);
            var cmp = Ext.getCmp('contentPanel');
            cmp.add( new mymdb.MovieDetailsTab({ title:selectedMovie.data.ti, movieId:selectedMovie.data.mid }) );
            cmp.activate(cmp.items.length-1);
        });
        
        Ext.getBody().on("contextmenu", Ext.emptyFn, null, {preventDefault: true});

        this.on( 'rowcontextmenu', function( grid, idx, evt ){
            mymdb.MovieListContextPopupFactory( grid,idx).showAt( evt.getXY() );
        });        
    }
});
Ext.reg('movie-grid', mymdb.MovieGridPanel);

mymdb.MovieListContextPopupFactory = function( grid, idx ){
//    var fav = grid.getStore().getAt( idx ).data.fav
    return new Ext.menu.Menu({
        items:[
//            {
//                xtype:'menuitem',
//                text:( fav ? 'Unmark Favorite' : 'Mark Favorite'),
//                iconCls:( fav ? 'icon-unmark-favorite' : 'icon-mark-favorite'),
//                handler:function(){
//                    Ext.Ajax.request({
//                        url:'foo.php',
//                        method:'POST',
//                        params: { foo: 'bar' },
//                        success: function(){},
//                        failure: function(){}
//                    });
//                }
//            },
            mymdb.app.openNewMovieAction,
            {
                xtype:'menuitem',
                text:'Edit Movie',
                iconCls:'icon-edit-movie',
                handler:function(){
                    new mymdb.movie.MovieDialog({ movieId:grid.getStore().getAt( idx ).data.mid });
                }
            },
            {
                xtype:'menuitem',
                text:'Delete Movie',
                iconCls:'icon-delete-movie',
                handler:function(b,e){
                    var movieStore = grid.getStore();
                    var itemData = movieStore.getAt( idx ).data;
                    Ext.MessageBox.confirm('Confirm Deletion','Are you sure you want to delete "' + itemData.ti + '"?', function(sel){
                        if( sel == 'yes' ){
                            Ext.Ajax.request({
                               url: 'movie/delete',
                               method:'POST',
                               params: { id:itemData.mid },
                               success: function(resp,opts){
                                   Ext.Msg.alert('Success','Movie successfully deleted.',function(){
                                       movieStore.load();
                                   });
                               },
                               failure: function(resp,opts){
                                   Ext.Msg.alert('Delete Failure','Unable to deleted selected movie.');
                               }
                            });
                        }
                    });
                }
            }
        ]
    });
};

mymdb.ContentPanel = Ext.extend( Ext.TabPanel, {
    id:'contentPanel',
	title:'Content',
	activeTab:0,
    autoHeight:true,

    initComponent: function(){
        Ext.apply(this, {
			items: [
                {
                    xtype:'panel',
                    title:'Movies',
                    closable:false,
                    autoHeight:true,
                    items:[
                        { xtype:'movie-grid' }
                    ]
                }
            ]
        });

        mymdb.ContentPanel.superclass.initComponent.apply(this, arguments);

        this.on('resize',function( comp, adjWidth, adjHeight, rawWidth, rawHeight){
            this.findByType('movie-grid')[0].setSize( rawWidth, rawHeight-25 );
        },this);
    }
});
Ext.reg('contentpanel', mymdb.ContentPanel);
