
mymdb.MovieDetailsTab = Ext.extend( Ext.Panel, {
    title:'Movie',
    closable:true
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
		fields: ['mid','ti','yr','bx', 'ge']
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
            {header: 'Storage', dataIndex: 'bx'}
        ]
    }),

    initComponent: function(){
        Ext.apply(this, {
        });

        mymdb.MovieGridPanel.superclass.initComponent.apply(this, arguments);

        this.on( 'rowclick', function(g,idx,evt){
            var selectedMovie = g.store.getAt(idx);
            var cmp = Ext.getCmp('contentPanel');
            cmp.add( new mymdb.MovieDetailsTab({ title:selectedMovie.data.ti, autoLoad:'browser/details?mid=' + selectedMovie.data.mid }) );
            cmp.activate(cmp.items.length-1);
        });
        
        Ext.getBody().on("contextmenu", Ext.emptyFn, null, {preventDefault: true});

        this.on( 'rowcontextmenu', function( grid, idx, evt ){
        	mymdb.MovieListContextPopupFactory( grid,idx).showAt( evt.getXY() );
        });        
    }
});

mymdb.MovieListContextPopupFactory = function( grid, idx ){
    return new Ext.menu.Menu({
        items:[
            { 
            	xtype:'menuitem',
            	text:'New Movie',
            	icon:'/mymdb/images/icons/add.png'
            },
            {
                xtype:'menuitem',
                text:'Edit Movie',
                icon:'/mymdb/images/icons/edit.png',
            	handler:function(){
            	    var movieId = grid.getStore().getAt( idx ).data.mid;
            	    var dialog = new mymdb.movie.MovieDialog({autoShow:false});
            	    dialog.get(0).getForm().load({
            	        url: 'movie/edit',
            	        params:{ id:movieId },
            	        method:'GET',
            	        failure: function(form, action) {
            	            Ext.Msg.alert('Load Failure', action.result.errorMessage);
            	        }
            	    });
            	    
            	    dialog.show();
            	}
            },            
            {
                xtype:'menuitem',
                text:'Delete Movie',
                icon:'/mymdb/images/icons/delete.png',
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
}

mymdb.ContentPanel = Ext.extend( Ext.TabPanel, {
    id:'contentPanel',
	title:'Content',
	activeTab:0,
    autoHeight:true,

    initComponent: function(){
        var movieGrid = new mymdb.MovieGridPanel();
        var movieGridTab = new Ext.Panel({
            title:'Movies',
            closable:false,
            autoHeight:true,
            items:[movieGrid]
        });

        Ext.apply(this, {
			items: [ movieGridTab ]
        });

        mymdb.ContentPanel.superclass.initComponent.apply(this, arguments);

        this.on('resize',function( comp, adjWidth, adjHeight, rawWidth, rawHeight){
            movieGrid.setSize( rawWidth, rawHeight-25 );
        });
    }
});
Ext.reg('contentpanel', mymdb.ContentPanel);
