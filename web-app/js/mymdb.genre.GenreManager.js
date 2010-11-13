// mymdb.genre

mymdb.genre.GenreManagerDialog = Ext.extend( Ext.Window ,{
    autoShow:true,
    iconCls:'icon-genre',
    closable:true,
    initHidden:false,
    modal:true,
    width:400,
    height:300,
    title:'Genre Manager',
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            items:[ { xtype:'genremanagerpanel' } ],
            tbar:[ mymdb.genre.NewGenreActionFactory('button'), ]
        });
        mymdb.genre.GenreManagerDialog.superclass.initComponent.apply(this, arguments);
    }
});

mymdb.genre.NewGenreActionFactory = function(xt){
    return {
        xtype:xt,
        text:'New Genre',
        icon:'/mymdb/images/icons/add.png',
        handler: function(){ new mymdb.genre.GenreDialog(); }
    };
}

mymdb.genre.OpenGenreEditDialogHandler = function(dataView,idx){
    var genreId = dataView.getStore().getAt( idx ).data.id;
    var dialog = new mymdb.genre.GenreDialog({autoShow:false});
    dialog.get(0).getForm().load({
        url: 'genre/edit',
        params:{ id:genreId },
        method:'GET',
        failure: function(form, action) {
            Ext.Msg.alert('Load Failure', action.result.errorMessage);
        }
    });
    dialog.show();
}

mymdb.genre.GenreListView = Ext.extend( Ext.list.ListView, {
    id:'genreListView',
	emptyText: 'No Genres',
	loadingText:'Loading...',
	reserveScrollOffset: true,
	hideHeaders:false,
	multiSelect:false,
	columns: [{header:'Genre', dataIndex:'label'}, {header:'# Movies',dataIndex:'count'}],

    initComponent: function(){
        mymdb.genre.GenreListView.superclass.initComponent.apply(this, arguments);

        Ext.getBody().on("contextmenu", Ext.emptyFn, null, {preventDefault: true});

        this.on( 'dblclick', function(dataView,idx,node,evt){
            mymdb.genre.OpenGenreEditDialogHandler(dataView,idx);
        });

		this.on( 'contextmenu', function( dataView, idx, node, evt ){
            var popup = new Ext.menu.Menu({
                items:[
                    {
                        xtype:'menuitem',
                        text:'Edit',
                        icon:'/mymdb/images/icons/edit.png',
                        handler:function(){
                            mymdb.genre.OpenGenreEditDialogHandler(dataView,idx);
                        }
                    },
                    mymdb.genre.NewGenreActionFactory('menuitem'),
                    {
                        xtype:'menuitem',
                        text:'Delete',
                        icon:'/mymdb/images/icons/delete.png',
                        handler:function(b,e){
                            var itemData = dataView.getStore().getAt( idx ).data;
                            Ext.MessageBox.confirm('Confirm Deletion','Are you sure you want to delete "' + itemData.label + '"?', function(sel){
                                if( sel == 'yes' ){
                                    Ext.Ajax.request({
                                       url: 'genre/delete',
                                       method:'POST',
                                       params: { id:itemData.id },
                                       success: function(resp,opts){
                                           Ext.Msg.alert('Success','Genre successfully deleted.',function(){
                                               Ext.getCmp('genreListView').getStore().load();
                                           });
                                       },
                                       failure: function(resp,opts){
                                           Ext.Msg.alert('Delete Failure','Unable to deleted selected genre.');
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

mymdb.genre.GenreManagerPanel = Ext.extend( Ext.Panel, {
    autoScroll:true,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                new mymdb.genre.GenreListView({
                    store:new Ext.data.JsonStore({
                        url:'genre/list',
                        autoLoad: true,
                        autoDestroy: true,
                        storeId: 'genre_manager_store',
                        root: 'items',
                        idProperty: 'id',
                        fields: ['id','label','count']
                    })
                })
            ]
        });
        mymdb.genre.GenreManagerPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('genremanagerpanel', mymdb.genre.GenreManagerPanel);

mymdb.genre.GenreDialog = Ext.extend( Ext.Window ,{
    id:'genreFormDialog',
    iconCls:'icon-genre',
    autoShow:true,
    closable:true,
    resizable:false,
    initHidden:false,
    modal:true,
    width:250,
    height:110,
    title:'Genre',
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            items:[ {xtype:'genreformpanel'} ]
        });
        mymdb.genre.GenreDialog.superclass.initComponent.apply(this, arguments);
    }
});

mymdb.genre.GenreFormPanel = Ext.extend( Ext.FormPanel, {
    labelWidth:50,
    frame:false,
    bodyStyle:'padding:5px 5px 0',
    defaultType: 'textfield',
    initComponent: function(){
        var formPanel = this;

        Ext.apply(this, {
            items: [
                {
                    name:'id',
                    xtype:'hidden'
                },
                {
                    name:'version',
                    xtype:'hidden'
                },
                {
                    fieldLabel: 'Name',
                    name: 'name',
                    allowBlank:false,
                    minLength:2,
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
                            url: 'genre/' + ( idValue == null || idValue == '' ? 'save' : 'update'),
                            method:'POST',
                            success: function(form, action) {
                               Ext.Msg.alert('Success', 'Genre saved successfully', function(){
                                   Ext.getCmp('genreFormDialog').close();
                                   Ext.getCmp('genreListView').getStore().load();
                               });
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
                        Ext.getCmp('genreFormDialog').close();
                    }
                }
            ]
        });
        mymdb.genre.GenreFormPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('genreformpanel', mymdb.genre.GenreFormPanel);
