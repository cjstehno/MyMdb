
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
                {
                    text:'New Storage',
                    iconCls:'icon-add-storage',
                    scope:this,
                    handler:function(){
                        this.showStorageDialog();
                    }
                }
            ]
        });
        mymdb.storage.StorageManagerDialog.superclass.initComponent.apply(this, arguments);
    },
    showStorageDialog:function( storageId ){
        var dialog = new mymdb.storage.StorageDialog();

        if( storageId != undefined && storageId != null ){
            dialog.findByType('form')[0].getForm().load({
                url:'storage/edit',
                params:{ id:storageId },
                method:'GET',
                failure: function(form, action) {
                    Ext.Msg.alert('Load Failure', action.result.errorMessage);
                }
            });
        }

        dialog.show();
    }
});
Ext.reg('storage-manager', mymdb.storage.StorageManagerDialog);

mymdb.storage.StorageListView = Ext.extend( Ext.list.ListView, {
    emptyText:'No Storage Units',
    loadingText:'Loading...',
    reserveScrollOffset: true,
    hideHeaders:false,
    multiSelect:false,
    singleSelect:true,
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
                    fn:function(dataView,idx,node,evt){
                        if( this.getSelectionCount() > 0){
                            var rec = this.getSelectedRecords()[0];
                            this.findParentByType('storage-manager').showStorageDialog(rec.data.id);
                        }
                    }
                },
                contextmenu:{
                    scope:this,
                    fn:function( dataView, idx, node, evt ){
                        new Ext.menu.Menu({
                            items:[
                                {
                                    xtype:'menuitem',
                                    text:'Edit Unit',
                                    iconCls:'icon-edit-storage',
                                    scope:this,
                                    handler:function(){
                                        var rec = dataView.getStore().getAt( idx );
                                        this.findParentByType('storage-manager').showStorageDialog(rec.data.id);
                                    }
                                },
                                {
                                    text:'New Unit',
                                    iconCls:'icon-add-storage',
                                    scope:this,
                                    handler: function(b,e){
                                        this.findParentByType('storage-manager').showStorageDialog();
                                    }
                                },
                                {
                                    xtype:'menuitem',
                                    text:'Delete Unit',
                                    iconCls:'icon-delete-storage',
                                    scope:this,
                                    handler:function(b,e){
                                        var rec = dataView.getStore().getAt( idx );
                                        Ext.MessageBox.confirm('Confirm Deletion','Are you sure you want to delete "' + rec.data.name + '"?', function(sel){
                                            if( sel == 'yes' ){
                                                mymdb.app.storageStore.removeAt(idx);
                                            }
                                        });
                                    }
                                }
                            ]
                        }).showAt( evt.getXY() );
                    }
                }
            },
            store:mymdb.app.storageStore
        });
        
        mymdb.storage.StorageListView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('storage-listview', mymdb.storage.StorageListView);

mymdb.storage.StorageDialog = Ext.extend( Ext.Window ,{
    title:'Storage',
    iconCls:'icon-storage',
    autoShow:false,
    closable:true,
    resizable:false,
    initHidden:false,
    modal:true,
    width:250,
    height:160,
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'form',
                    labelWidth:50,
                    frame:false,
                    bodyStyle:'padding:5px 5px 0',
                    items: [
                        { name:'id', xtype:'hidden' },
                        { name:'version', xtype:'hidden' },
                        { name:'name', xtype:'textfield', fieldLabel:'Name', allowBlank:false, minLength:1, maxLength:20 },
                        { name:'indexed', xtype:'checkbox', fieldLabel:'Indexed' },
                        { name:'capacity', xtype:'numberfield', fieldLabel:'Capacity', allowBlank:false, minValue:0 }
                    ],
                    buttons: [
                        {
                            text: 'Save',
                            scope:this,
                            handler:function(b,e){
                                var dialog = this;
                                var form = this.findByType('form')[0].getForm();
                                var storageId = form.findField('id').getValue();

                                form.submit({
                                    url: 'storage/' + ( storageId === null || storageId === '' ? 'save' : 'update'),
                                    method:'POST',
                                    success: function(form, action) {
                                        mymdb.app.storageStore.load();
                                        dialog.close();
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
                                               break;
                                       }
                                    }
                                });
                            }
                        },
                        {
                            text: 'Cancel',
                            scope:this,
                            handler:function(){ this.close(); }
                        }
                    ]
                }
            ]
        });
        mymdb.storage.StorageDialog.superclass.initComponent.apply(this, arguments);
    }
});
