// mymdb.actor

mymdb.actor.ActorManagerDialog = Ext.extend( Ext.Window ,{
    autoShow:true,
    iconCls:'icon-actor',
    closable:true,
    initHidden:false,
    modal:true,
    width:400,
    height:300,
    title:'Actor Manager',
    layout:'fit',
    autoScroll:true,
    initComponent: function(){
        Ext.apply(this, {
            buttons:[
                { text:'Close', scope:this, handler:function(){ this.close(); } }
            ],
            items:[
                new mymdb.actor.ActorListView({
                    store:new Ext.data.JsonStore({
                        url:'actor/list',
                        autoLoad: true,
                        autoDestroy: true,
                        storeId: 'actor_manager_store',
                        root: 'items',
                        idProperty: 'id',
                        fields: ['id','label','count']
                    })
                })
            ],
            tbar:[
                {
                    text:'New Actor',
                    iconCls:'icon-add-actor',
                    handler:this.showActorDialog
                }
            ]
        });
        mymdb.actor.ActorManagerDialog.superclass.initComponent.apply(this, arguments);
    },
    showActorDialog:function(){
        new mymdb.actor.ActorDialog();
    }
});

mymdb.actor.OpenActorEditDialogHandler = function(dataView,idx){
    var actorId = dataView.getStore().getAt( idx ).data.id;
    var dialog = new mymdb.actor.ActorDialog({autoShow:false});
    dialog.get(0).getForm().load({
        url: 'actor/edit',
        params:{ id:actorId },
        method:'GET',
        failure: function(form, action) {
            Ext.Msg.alert('Load Failure', action.result.errorMessage);
        }
    });
    dialog.show();
};

mymdb.actor.ActorListView = Ext.extend( Ext.list.ListView, {
    id:'actorListView',
	emptyText: 'No Actors',
	reserveScrollOffset: true,
	loadingText:'Loading...',
	hideHeaders:false,
	multiSelect:false,
	columns: [{header:'Actor', dataIndex:'label'}, {header:'# Movies',dataIndex:'count'}],

    initComponent: function(){
        mymdb.actor.ActorListView.superclass.initComponent.apply(this, arguments);

        Ext.getBody().on("contextmenu", Ext.emptyFn, null, {preventDefault: true});

        this.on( 'dblclick', function(dataView,idx,node,evt){
            mymdb.actor.OpenActorEditDialogHandler(dataView,idx);
        });

		this.on( 'contextmenu', function( dataView, idx, node, evt ){
            var popup = new Ext.menu.Menu({
                items:[
                    {
                        xtype:'menuitem',
                        text:'Edit',
                        iconCls:'icon-edit-actor',
                        handler:function(){
                            mymdb.actor.OpenActorEditDialogHandler(dataView,idx);
                        }
                    },
                    {
                        text:'New Actor',
                        iconCls:'icon-add-actor',
                        handler: function(){ new mymdb.actor.ActorDialog(); }
                    },
                    {
                        xtype:'menuitem',
                        text:'Delete',
                        iconCls:'icon-delete-actor',
                        handler:function(b,e){
                            var itemData = dataView.getStore().getAt( idx ).data;
                            Ext.MessageBox.confirm('Confirm Deletion','Are you sure you want to delete "' + itemData.label + '"?', function(sel){
                                if( sel == 'yes' ){
                                    Ext.Ajax.request({
                                       url: 'actor/delete',
                                       method:'POST',
                                       params: { id:itemData.id },
                                       success: function(resp,opts){
                                           Ext.getCmp('actorListView').getStore().load();
                                       },
                                       failure: function(resp,opts){
                                           Ext.Msg.alert('Delete Failure','Unable to deleted selected actor.');
                                       }
                                    });
                                }
                            });
                        }
                    }
                ]
            });
            popup.showAt( evt.getXY() );
		} );
    }
});

mymdb.actor.ActorDialog = Ext.extend( Ext.Window ,{
    id:'actorFormDialog',
    iconCls:'icon-actor',
    autoShow:true,
    closable:true,
    resizable:false,
    initHidden:false,
    modal:true,
    width:275,
    height:160,
    title:'Actor',
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            items:[ {xtype:'actorformpanel'} ]
        });
        mymdb.actor.ActorDialog.superclass.initComponent.apply(this, arguments);
    },
    onSave:function(){
        Ext.getCmp('actorListView').getStore().load();
    }
});

mymdb.actor.ActorFormPanel = Ext.extend( Ext.FormPanel, {
    labelWidth:100,
    frame:false,
    bodyStyle:'padding:5px 5px 0',
    defaultType: 'textfield',
    initComponent: function(){
        var formPanel = this;

        Ext.apply(this, {
            items: [
                { name:'id', xtype:'hidden' },
                { name:'version', xtype:'hidden' },
                {
                    fieldLabel: 'First Name',
                    name: 'firstName',
                    allowBlank:true,
                    maxLength:40
                },
                {
                    fieldLabel: 'Middle Name',
                    name: 'middleName',
                    maxLength:40
                },
                {
                    fieldLabel: 'Last Name',
                    name: 'lastName',
                    maxLength:40
                }
            ],
            buttons: [
                {
                    text: 'Save',
                    handler:function(b,e){
                        var theForm = formPanel.getForm();
                        var idValue = theForm.findField('id').getValue();
                        theForm.submit({
                            clientValidation: true,
                            url: 'actor/' + ( idValue === null || idValue === '' ? 'save' : 'update'),
                            method:'POST',
                            success: function(form, action) {
                                var dia = Ext.getCmp('actorFormDialog');
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
                    handler:function(b,e){
                        Ext.getCmp('actorFormDialog').close();
                    }
                }
            ]
        });
        mymdb.actor.ActorFormPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('actorformpanel', mymdb.actor.ActorFormPanel);
