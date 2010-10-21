
mymdb.CategoryListView = Ext.extend( Ext.list.ListView, {
	emptyText: 'No Items',
	reserveScrollOffset: true,
	hideHeaders:true,
	multiSelect:false,		
	columns: [{ header:'Title', dataIndex:'lbl' }],
			
    initComponent: function(){
        mymdb.CategoryListView.superclass.initComponent.apply(this, arguments);
 
		this.on( 'click', function( dataView, idx, node, evt ){
            var st = dataView.getStore();
            var storeId = st.storeId;
            var categoryId = st.getAt( idx ).data.cid;
            var gridStore = Ext.getCmp('movieGridPanel').getStore();
            gridStore.load({params:{ sid:storeId, cid:categoryId }});
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
				{ 
                    title:'Titles',
                    items:[
                        new mymdb.CategoryListView({
                            store:new Ext.data.JsonStore({
                                url:'browser/titles',
                                autoLoad: true,
                                autoDestroy: true,
                                storeId: 'title_store',
                                root: 'items',
                                idProperty: 'cid',
                                fields: ['cid','lbl']
                            })
                        })
                    ]
                },
				{
                    title:'Genres',
                    items:[
                        new mymdb.CategoryListView({
                            store:new Ext.data.JsonStore({
                                url:'browser/genres',
                                autoLoad: true,
                                autoDestroy: true,
                                storeId: 'genre_store',
                                root: 'items',
                                idProperty: 'cid',
                                fields: ['cid','lbl']
                            })
                        })
                    ]
                },
				{
                    title:'Actors',
                    items:[
                        new mymdb.CategoryListView({
                            store:new Ext.data.JsonStore({
                                url:'browser/actors',
                                autoLoad: true,
                                autoDestroy: true,
                                storeId: 'actor_store',
                                root: 'items',
                                idProperty: 'cid',
                                fields: ['cid','lbl']
                            })
                        })
                    ]
                },
				{
                    title:'Release Years',
                    items:[
                        new mymdb.CategoryListView({
                            store:new Ext.data.JsonStore({
                                url:'browser/releaseYears',
                                autoLoad: true,
                                autoDestroy: true,
                                storeId: 'year_store',
                                root: 'items',
                                idProperty: 'cid',
                                fields: ['cid','lbl']
                            })
                        })
                    ]
                },
				{
                    title:'Storage',
                    items:[
                        new mymdb.CategoryListView({
                            store:new Ext.data.JsonStore({
                                url:'browser/storage',
                                autoLoad: true,
                                autoDestroy: true,
                                storeId: 'box_store',
                                root: 'items',
                                idProperty: 'cid',
                                fields: ['cid','lbl']
                            })
                        })
                    ]
                },
				{ title:'Lists', html:'Not supported yet.' }
			]
        });
        mymdb.CategoriesPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('categoriespanel', mymdb.CategoriesPanel);	