
mymdb.storage.StorageManagerDialog = Ext.extend( Ext.Window ,{
    title:'Storage Manager',
    iconCls:'icon-storage',
    autoShow:true,
    closable:true,
    initHidden:false,
    modal:true,
    width:400,
    height:300,
    layout:'fit',
    autoScroll:true,
    initComponent: function(){
        Ext.apply(this, {
            buttons:[
                { text:'Close', scope:this, handler:function(){ this.close(); } }
            ],
            items:[
                { xtype:'storage-listview' }
            ],
            tbar:[
                { text:'New Storage', iconCls:'icon-add-storage', handler:this.showStorageDialog }
            ]
        });
        mymdb.storage.StorageManagerDialog.superclass.initComponent.apply(this, arguments);
    },
    showStorageDialog:function(){
        new mymdb.storage.StorageDialog();
    }
});

mymdb.storage.StorageListView = Ext.extend( Ext.list.ListView, {
    emptyText:'No Storage Units',
    loadingText:'Loading...',
    reserveScrollOffset: true,
    hideHeaders:false,
    multiSelect:false,
    columns: [
        {header:'Unit', dataIndex:'name'},
        {header:'Indexed', dataIndex:'indexed'},
        {header:'Capacity', dataIndex:'capacity'},
        {header:'# Movies',dataIndex:'count'}
    ],

    initComponent: function(){
        Ext.apply(this, {
            listeners:{
                dblclick:{
                    scope:this,
                    fn:function(){
                        Ext.Msg.alert('edit');
                    }
                },
                contextmenu:{
                    scope:this,
                    fn:function(){
                        Ext.Msg.alert('context');
                    }
                }
            },
            store:new Ext.data.JsonStore({
                url:'storage/list',
                autoLoad: true,
                autoDestroy: true,
                root: 'items',
                idProperty: 'id',
                fields: ['id','name','indexed','capacity','count']
            })
        });
        
        mymdb.storage.StorageListView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('storage-listview', mymdb.storage.StorageListView);

mymdb.storage.StorageDialog = Ext.extend( Ext.Window ,{
    title:'Storage',
    iconCls:'icon-storage',
    autoShow:true,
    closable:true,
    resizable:false,
    initHidden:false,
    modal:true,
    width:250,
    height:110,
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            items:[ {xtype:'storage-form' } ]
        });
        mymdb.genre.GenreDialog.superclass.initComponent.apply(this, arguments);
    }/*,
    onSave:function(){
        Ext.getCmp('genreListView').getStore().load();
    }*/
});
Ext.reg('genre-dialog', mymdb.genre.GenreDialog);

mymdb.genre.GenreFormPanel = Ext.extend( Ext.FormPanel, {
    labelWidth:50,
    frame:false,
    bodyStyle:'padding:5px 5px 0',
    defaultType: 'textfield',
    initComponent: function(){
        var formPanel = this;

        Ext.apply(this, {
            items: [
                { name:'id', xtype:'hidden' },
                { name:'version', xtype:'hidden' },
                { name: 'name', fieldLabel: 'Name', allowBlank:false, minLength:2, maxLength:40 }
            ],
            buttons: [
                {
                    text: 'Save',
                    handler:function(b,e){
                        var theForm = formPanel.getForm();
                        var idValue = theForm.findField('id').getValue();
                        theForm.submit({
                            clientValidation: true,
                            url: 'genre/' + ( idValue === null || idValue === '' ? 'save' : 'update'),
                            method:'POST',
                            success: function(form, action) {
                               var dia = formPanel.findParentByType('genre-dialog');
                               dia.close();
                               dia.onSave();
                            },
                            failure: function(form, action) {
                                switch (action.failureType) {
                                    case Ext.form.Action.CLIENT_INVALID:
                                        Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
                                        break;
                                    case Ext.form.Action.CONNECT_FAILURE:
                                        Ext.Msg.alert('Failure', 'Ajax communication failed');
                                        break;
                                    default:
                                       //Ext.Msg.alert('Failure', action.result.msg);
                                       break;
                               }
                            }
                        });
                    }
                },
                {
                    text: 'Cancel',
                    handler:function(b,e){ b.findParentByType('genre-dialog').close(); }
                }
            ]
        });
        mymdb.genre.GenreFormPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('genreformpanel', mymdb.genre.GenreFormPanel);