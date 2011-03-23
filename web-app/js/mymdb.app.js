Ext.QuickTips.init();

Ext.namespace('mymdb','mymdb.actor','mymdb.admin','mymdb.genre', 'mymdb.storage','mymdb.movie', 'mymdb.movie.flow');
 
mymdb.app = function() {
    // do NOT access DOM from here; elements don't exist yet
    // private variables
    // private functions
    // public space
    return {
        // public properties, e.g. strings to translate
        openAboutAction: new Ext.Action({
            itemId: 'openAbout',
            text: 'About',
            iconCls:'icon-about',
            handler: function(){ new mymdb.AboutDialog(); }
        }),
        
        openHelpAction: new Ext.Action({
            itemId: 'openHelp',
            text: 'Help',
            iconCls:'icon-help',
            handler: function(){ Ext.MessageBox.alert('Help','You are currently helpless.'); }
        }),        

        openGenreManagerAction: new Ext.Action({
            itemId:'openGenreManager',
            text:'Genre Manager',
            iconCls:'icon-genre',
            handler: function(){ new mymdb.genre.GenreManagerDialog(); }
        }),

        openActorManagerAction: new Ext.Action({
            itemId:'openActorManager',
            text:'Actor Manager',
            iconCls:'icon-actor',
            handler: function(){ new mymdb.actor.ActorManagerDialog(); }
        }),
        
        openNewMovieAction: new Ext.Action({
            itemId:'openNewMovie',
            text:'New Movie',
            iconCls:'icon-add-movie',
            handler: function(){ new mymdb.movie.MovieDialog(); }
        }),

        openStorageManagerAction: new Ext.Action({
            itemId:'openStorageManager',
            text:'Storage Manager',
            iconCls:'icon-add-storage',
            handler: function(){ new mymdb.storage.StorageManagerDialog(); }
        }),

        openUserManagerAction: new Ext.Action({
            itemId:'openUserManager',
            text:'User Manager',
            iconCls:'icon-add-user',
            handler: function(){ new mymdb.admin.UserManagerDialog(); }
        }),

        storageStore:new Ext.data.JsonStore({
            storeId:'box_store',    // TODO: remove this when category panels refactored
            proxy:new Ext.data.HttpProxy({
                api:{
                    read:{ url:'storage/list', method:'GET' },
                    destroy:{ url:'storage/delete', method:'POST' }
                }
            }),
            writer:new Ext.data.JsonWriter({
                encode:true,
                writeAllFields:true
            }),
            autoLoad:true,
            root: 'items',
            idProperty: 'id',
            fields: ['id','name','indexed','capacity','count']
        }),

        // public methods
        init: function() {
			new Ext.Viewport({
				layout: 'border',
				items: [ 
					{ region:'north', xtype:'headerpanel' },
					{ region:'center', xtype:'contentpanel'},
					{ region:'west', xtype:'categoriespanel', collapsible:true },
					{ region:'south', xtype:'footerpanel' }
				]
			});		

            Ext.getBody().on("contextmenu", Ext.emptyFn, null, {preventDefault: true});
        }
    };
}();

mymdb.HeaderPanel = Ext.extend( Ext.Panel, {
	html: '<h1 class="x-panel-header">My Movie Database</h1>',
	autoHeight: true,
	border: false,
	margins: '0 0 5 0',
	bbar:[
        mymdb.app.openUserManagerAction,
        { xtype:'tbseparator' },
        mymdb.app.openGenreManagerAction,
        mymdb.app.openActorManagerAction,
        mymdb.app.openStorageManagerAction,
        { xtype:'tbseparator' },
        mymdb.app.openNewMovieAction,
        { xtype:'tbfill' },
        mymdb.app.openAboutAction,
        mymdb.app.openHelpAction
    ]
});
Ext.reg('headerpanel', mymdb.HeaderPanel);

mymdb.FooterPanel = Ext.extend( Ext.Panel, {
	html: '<div style="text-align:center;font-size:small;">Copyright &copy; 2010 Christopher J. Stehno</div>',
	autoHeight: true,
	border: false,
	margins: '0 0 5 0'
});
Ext.reg('footerpanel', mymdb.FooterPanel);