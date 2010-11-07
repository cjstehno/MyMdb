
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
    }
});

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
