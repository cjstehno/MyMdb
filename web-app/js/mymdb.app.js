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

        logoutAction: new Ext.Action({
            text:'Logout',
            iconCls:'icon-logout',
            handler: function(){
                Ext.MessageBox.confirm('Logout?','Are you sure you want to logout?', function(sel){
                    if( sel == 'yes' ){
                        location = 'auth/signOut';
                    }
                });
            }
        }),

        settingsAction: new Ext.Action({
            text:'Settings',
            iconCls:'icon-settings',
            handler: function(){ new mymdb.admin.SettingsDialog(); }
        }),

        exportAction: new Ext.Action({
            text:'Export',
            iconCls:'icon-export',
            handler: function(){
                Ext.MessageBox.confirm('Export Confirmation','Are you sure you want to export the entire collection?',function(answer){
                    if(answer=='yes'){
                        window.location = 'transfer/exportCollection';
                    }
                });
            }
        }),

        importAction: new Ext.Action({
            text:'Import',
            iconCls:'icon-import',
            handler: function(){
                new mymdb.ImportDialog().show();
            }
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

        userStore:new Ext.data.JsonStore({
            proxy:new Ext.data.HttpProxy({
                api:{
                    read:{ url:'user/list', method:'GET' },
                    destroy:{ url:'user/delete', method:'POST' }
                }
            }),
            writer:new Ext.data.JsonWriter({
                encode:true,
                writeAllFields:true
            }),
            autoLoad:true,
            root: 'items',
            idProperty: 'id',
            fields: ['id','username', 'roles']
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
        mymdb.app.settingsAction,
        { xtype:'tbseparator' },
        mymdb.app.exportAction,
        mymdb.app.importAction,
        { xtype:'tbseparator' },
        mymdb.app.openGenreManagerAction,
        mymdb.app.openActorManagerAction,
        mymdb.app.openStorageManagerAction,
        { xtype:'tbseparator' },
        mymdb.app.openNewMovieAction,
        { xtype:'tbfill' },
        mymdb.app.openAboutAction,
        mymdb.app.openHelpAction,
        mymdb.app.logoutAction
    ]
});
Ext.reg('headerpanel', mymdb.HeaderPanel);

mymdb.FooterPanel = Ext.extend( Ext.Panel, {
	html: '<div style="text-align:center;font-size:small;">Copyright &copy; 2011 Christopher J. Stehno</div>',
	autoHeight: true,
	border: false,
	margins: '0 0 5 0'
});
Ext.reg('footerpanel', mymdb.FooterPanel);

mymdb.ImportDialog = Ext.extend(Ext.Window, {
    title:'Import Collection',
    iconCls:'icon-import',
    width:400,
    height:130,
    modal:true,
    resizable:false,
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'form',
                    fileUpload:true,
                    frame:false,
                    monitorValid:true,
                    bodyStyle:'padding:5px 5px 0',
                    labelWidth:50,
                    items:[
                        {
                            xtype:'textfield',
                            fieldLabel:'File',
                            inputType:'file',
                            name:'file',
                            allowBlank:false
                        },
                        {
                            xtype:'checkbox',
                            fieldLabel:'Confirm',
                            name:'confirm',
                            boxLabel:'You will be deleting all existing content.'
                        }
                    ],
                    buttons:[
                        { text:'Cancel', scope:this, handler:function(){ this.close(); } },
                        {
                            text:'Ok',
                            scope:this,
                            formBind:true,
                            handler:function(){
                                var dia = this;
                                this.findByType('form')[0].getForm().submit({
                                    url:'transfer/importCollection',
                                    method:'POST',
                                    success:function(){
                                        dia.close();
                                    },
                                    failure:function(){
                                        Ext.MessageBox.alert('Error','Unable to import');
                                    }
                                });
                            }
                        }
                    ]
                }
            ]
        });

        mymdb.ImportDialog.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('import-dialog',mymdb.ImportDialog);