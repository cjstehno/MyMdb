function renderMovieCountText(cnt){
	return 'Found ' + cnt + " movies";
}

function initializeGrid(){

	var gridStore = new Ext.data.JsonStore({
		autoLoad: true,
		autoDestroy: true,
		url: 'browser/list',
		storeId: 'gridData',
		root: 'movies',
		idProperty: 'mid',
		fields: ['mid','ti','yr','bx']
	});

	var gridToolbar = new Ext.Toolbar({
		items: [
			{xtype:'tbtext', text:renderMovieCountText(gridStore.getCount())}
		]
	});
	
	gridStore.on( 'load', function(store, recs, opts){
		gridToolbar.items.first().setText( renderMovieCountText(gridStore.getCount()) );
	} );
		
	var grid = new Ext.grid.GridPanel({
		frame: true,
		store: gridStore,

		sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
		
		viewConfig: {
			forceFit: true,
		},
	
		colModel: new Ext.grid.ColumnModel({
			defaults: {
				sortable: true
			},
			columns: [
				{header: 'Title', dataIndex: 'ti'},
				{header: 'Genres', dataIndex: 'genres', sortable:false},
				{header: 'Year', dataIndex: 'yr'},
				{header: 'Storage', dataIndex: 'bx'}
			]
		}),
		
		bbar: gridToolbar
	});
	
	grid.on( 'rowclick', function(g,idx,evt){
		var selectedMovie = g.store.getAt(idx);
		browserWindow.setVisible( true );
		//Ext.Msg.alert('Status', 'You clicked: ' + selectedMovie.id + ", " + selectedMovie.data.ti);
	});
	
	return grid;
}
