
/*
 * Copyright (c) 2011 Christopher J. Stehno (chris@stehno.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

mymdb.admin.UserManagerDialog = Ext.extend( Ext.Window ,{
    title:'User Manager',
    iconCls:'icon-user',
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
                { xtype:'user-listview' }
            ],
            tbar:[
                {
                    text:'New User',
                    iconCls:'icon-add-user',
                    scope:this,
                    handler:function(){
                        this.showUserDialog();
                    }
                }
            ]
        });
        mymdb.admin.UserManagerDialog.superclass.initComponent.apply(this, arguments);
    },
    showUserDialog:function( userId ){
        var dialog = new mymdb.admin.UserDialog();

        if( userId != undefined && userId != null ){
            dialog.findByType('form')[0].getForm().load({
                url:'user/edit',
                params:{ id:userId },
                method:'GET',
                failure: function(form, action) {
                    Ext.Msg.alert('Load Failure', action.result.errorMessage);
                }
            });
        }

        dialog.show();
    }
});
Ext.reg('user-manager', mymdb.admin.UserManagerDialog);

mymdb.admin.UserListView = Ext.extend( Ext.list.ListView, {
    emptyText:'No Users',
    loadingText:'Loading...',
    reserveScrollOffset: true,
    hideHeaders:false,
    multiSelect:false,
    singleSelect:true,
    columns: [
        {header:'Username', dataIndex:'username'}
    ],

    initComponent: function(){
        Ext.apply(this, {
            listeners:{
                dblclick:{
                    scope:this,
                    fn:function(dataView,idx,node,evt){
                        if( this.getSelectionCount() > 0){
                            var rec = this.getSelectedRecords()[0];
                            this.findParentByType('user-manager').showUserDialog(rec.data.id);
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
                                    text:'Edit User',
                                    iconCls:'icon-edit-user',
                                    scope:this,
                                    handler:function(){
                                        var rec = dataView.getStore().getAt( idx );
                                        this.findParentByType('user-manager').showUserDialog(rec.data.id);
                                    }
                                },
                                {
                                    text:'New User',
                                    iconCls:'icon-add-user',
                                    scope:this,
                                    handler: function(b,e){
                                        this.findParentByType('user-manager').showUserDialog();
                                    }
                                },
                                {
                                    xtype:'menuitem',
                                    text:'Delete User',
                                    iconCls:'icon-delete-user',
                                    scope:this,
                                    handler:function(b,e){
                                        var rec = dataView.getStore().getAt( idx );
                                        Ext.MessageBox.confirm('Confirm Deletion','Are you sure you want to delete "' + rec.data.name + '"?', function(sel){
                                            if( sel == 'yes' ){
                                                this.getStore().removeAt(idx);
                                            }
                                        });
                                    }
                                }
                            ]
                        }).showAt( evt.getXY() );
                    }
                }
            },
            store:new Ext.data.JsonStore({
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
                fields: ['id','username']
            })
        });
        
        mymdb.admin.UserListView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('user-listview', mymdb.admin.UserListView);

mymdb.admin.UserDialog = Ext.extend( Ext.Window ,{
    title:'User',
    iconCls:'icon-user',
    autoShow:false,
    closable:true,
    resizable:false,
    initHidden:false,
    modal:true,
    width:275,
    height:160,
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'form',
                    labelWidth:100,
                    frame:false,
                    bodyStyle:'padding:5px 5px 0',
                    items: [
                        { name:'id', xtype:'hidden' },
                        { name:'version', xtype:'hidden' },
                        { name:'username', xtype:'textfield', fieldLabel:'Username', allowBlank:false, minLength:1, maxLength:20 },
                        { name:'password1', xtype:'textfield', inputType:'password', fieldLabel:'Password', allowBlank:false, minLength:1, maxLength:20 },
                        { name:'password2', xtype:'textfield', inputType:'password', fieldLabel:'Confirm Password', allowBlank:false, minLength:1, maxLength:20 }
                    ],
                    buttons: [
                        {
                            text: 'Save',
                            scope:this,
                            handler:function(b,e){
                                var dialog = this;
                                var form = this.findByType('form')[0].getForm();
                                var userId = form.findField('id').getValue();

                                form.submit({
                                    url: 'user/' + ( userId === null || userId === '' ? 'save' : 'update'),
                                    method:'POST',
                                    success: function(form, action) {
                                        // mymdb.app.storageStore.load(); //FIXME: connect to store!
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
        mymdb.admin.UserDialog.superclass.initComponent.apply(this, arguments);
    }
});
