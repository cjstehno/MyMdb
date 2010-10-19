
mymdb.CategoryListView = Ext.extend( Ext.list.ListView, {
	multiSelect: true,
	emptyText: 'No images to display',
	reserveScrollOffset: true,
	hideHeaders:true,
	multiSelect:false,		
	store:new Ext.data.ArrayStore ({
		autoDestroy: true,
		storeId: 'myStore',
		idIndex: 0, 
		fields: [ 'name' ],
		data: [
			['A'],['B'],['C'],['D'],['E'],['F'],['G'],['H'],['I'],['J'],['K'],['L'],['M'],
			['N'],['O'],['P'],['Q'],['R'],['S'],['T'],['U'],['V'],['W'],['X'],['Y'],['Z']
		]
	}),
	columns: [{
		header: 'Title',
		dataIndex: 'name'
	}],
			
    initComponent: function(){
        mymdb.CategoryListView.superclass.initComponent.apply(this, arguments);
 
		this.on( 'click', function( dataView, idx, node, evt ){
			alert( dataView.getStore().getAt( idx ).data.name );
		} );
    }
});

mymdb.CategoriesPanel = Ext.extend( Ext.Panel, {
	collapsible:true,
	title:'Categories',
	width:200,
	layout:'accordion',
	defaults: {
		// applied to each contained panel
		autoScroll:true
	},
	layoutConfig: {
		// layout-specific configs go here
		titleCollapse: true,
		animate: true,
		activeOnTop: false,
		fill:true
	},
    initComponent: function(){
        Ext.apply(this, {
			items: [
				{ title:'Titles', items:[ new mymdb.CategoryListView() ] },
				{ title:'Genres', html: '<p>Panel content!</p>' },
				{ title:'Actors', html: '<p>Panel content!</p>' },
				{ title:'Release Year', html: '<p>Panel content!</p>' },
				{ title:'Storage', html: '<p>Panel content!</p>' },
				{ title:'Lists', html: '<p>Panel content!</p>' }
			]
        });
        mymdb.CategoriesPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('categoriespanel', mymdb.CategoriesPanel);	