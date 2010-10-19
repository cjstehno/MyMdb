
mymdb.MovieGridPanel = Ext.extend( Ext.grid.GridPanel, {
    frame: true,
    store: new Ext.data.JsonStore({
		autoLoad: true,
		autoDestroy: true,
		url: 'browser/list',
		storeId: 'gridData',
		root: 'movies',
		idProperty: 'mid',
		fields: ['mid','ti','yr','bx']
	}),
    sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
    viewConfig: {
        forceFit: true
    },
    colModel: new Ext.grid.ColumnModel({
        defaults: { sortable: true },
        columns: [
            {header: 'Title', dataIndex: 'ti'},
            {header: 'Genres', dataIndex: 'genres', sortable:false},
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
            //browserWindow.setVisible( true );
            Ext.Msg.alert('Status', 'You clicked: ' + selectedMovie.id + ", " + selectedMovie.data.ti);
        });
    }
});

mymdb.ContentPanel = Ext.extend( Ext.TabPanel, {
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
			items: [
				movieGridTab,
				{
					title:'Something',
					html:'This will be a grid of the results...',
					closable:true
				},
				{
					title:'More',
					html:'This will be a grid of the results...',
					closable:true
				}
			]
        });

        mymdb.ContentPanel.superclass.initComponent.apply(this, arguments);

        this.on('resize',function( comp, adjWidth, adjHeight, rawWidth, rawHeight){
            movieGrid.setSize( rawWidth, rawHeight-25 );
        });
    }
});
Ext.reg('contentpanel', mymdb.ContentPanel);
