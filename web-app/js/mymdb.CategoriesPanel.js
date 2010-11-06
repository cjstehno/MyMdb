
mymdb.CategoryListView = Ext.extend( Ext.list.ListView, {
	emptyText: 'No Items',
	reserveScrollOffset: true,
	hideHeaders:true,
	multiSelect:false,		
	columns: [{ header:'Title', dataIndex:'label' }],
			
    initComponent: function(){
        mymdb.CategoryListView.superclass.initComponent.apply(this, arguments);
 
		this.on( 'click', function( dataView, idx, node, evt ){
            var st = dataView.getStore();
            var storeId = st.storeId;
            var categoryId = st.getAt( idx ).data.id;
            var gridStore = Ext.getCmp('movieGridPanel').getStore();
            gridStore.load({params:{ sid:storeId, cid:categoryId }});
		} );
    }
});

mymdb.CategoryListViewFactory = function(dataUrl,categoryId){
    return new mymdb.CategoryListView({
        store:new Ext.data.JsonStore({
            url:dataUrl,
            autoLoad: true,
            autoDestroy: true,
            storeId:categoryId,
            root: 'items',
            idProperty: 'id',
            fields: ['id','label']
        })
    });
};

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
				{ 
                    title:'Titles',
                    items:[ mymdb.CategoryListViewFactory('browser/titles','title_store') ]
                },
				{
                    title:'Genres',
                    items:[ mymdb.CategoryListViewFactory('browser/genres', 'genre_store') ]
                },
				{
                    title:'Actors',
                    items:[ mymdb.CategoryListViewFactory('browser/actors', 'actor_store') ]
                },
				{
                    title:'Release Years',
                    items:[ mymdb.CategoryListViewFactory('browser/releaseYears', 'year_store') ]
                },
				{
                    title:'Storage',
                    items:[ mymdb.CategoryListViewFactory('browser/storage', 'box_store') ]
                },
				{ title:'Lists', html:'Not supported yet.' }
			]
        });
        mymdb.CategoriesPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('categoriespanel', mymdb.CategoriesPanel);	