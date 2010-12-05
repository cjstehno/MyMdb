// mymdb.movie

mymdb.movie.MovieDialog = Ext.extend( Ext.Window, {
	id:'movieDialog',
    autoShow:true,
    iconCls:'icon-movie',
    closable:true,
    initHidden:false,
    modal:true,
    width:625,
    height:590,
    title:'New Movie',
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
        	tbar:[ 
                { xtype:'button',text:'Fetch' }, 
                { xtype:'tbfill' }, 
                { xtype:'button',text:'Help', iconCls:'icon-help' } 
            ],
            items:[ { xtype:'movieformpanel' } ],
        });
        mymdb.movie.MovieDialog.superclass.initComponent.apply(this, arguments);
    }	
});

mymdb.movie.MovieFormPanel = Ext.extend( Ext.FormPanel, {
    id:'movie-formpanel',
    fileUpload:true,
    labelWidth:100,
    autoScroll:true,
    frame:false,
    bodyStyle:'padding:5px 5px 0',
    initComponent: function(){
        Ext.apply(this, {
            items: [
                { name:'id', xtype:'hidden' },
                { name:'version', xtype:'hidden' },
                {
                	xtype:'textfield',
                    fieldLabel:'Title',
                    name: 'title',
                    allowBlank:false,
                    maxLength:40
                },
                {
                	xtype:'textfield',
                    fieldLabel:'Release Year',
                    name: 'releaseYear',
                    allowBlank:false,
                    maxLength:4
                },
                {
                    xtype: 'compositefield',
                    fieldLabel: 'Storage',
                    msgTarget : 'side',
                    anchor    : '-20',
                    defaults: { flex: 1 },
                    items: [
                        {
                            xtype     : 'textfield',
                            name      : 'storage.name',
                            fieldLabel: 'Name'
                        },
                        {
                            xtype     : 'textfield',
                            name      : 'storage.index',
                            fieldLabel: 'Index'
                        }
                    ]
                },
                {
                	xtype:'textfield',
                    inputType:'file',
                    fieldLabel:'Poster',
                    name: 'poster',
                    allowBlank:false
                },
                new Ext.ux.form.SuperBoxSelect({
                    allowBlank:false,
                    id:'genre-selector',
                    xtype:'superboxselect',
                    fieldLabel: 'Genres',
                    emptyText: 'Select genres for movie.',
                    resizable: true,
                    name: 'genres',
                    anchor:'100%',
                    store:new Ext.data.JsonStore({
                        url:'genre/list',
                        autoLoad: true,
                        autoDestroy: true,
                        storeId: 'genre_form_store',
                        root: 'items',
                        idProperty: 'id',
                        fields: ['id','label','count']
                    }),
                    mode: 'local',
                    displayField: 'label',
                    displayFieldTpl: '{label}',
                    valueField: 'id',
                    forceSelection : true
                }),
                {
                    title:'Description',
                    items:[
                       {
                           xtype:'htmleditor',
                           id:'description',
                           height:347,
                           width:597,
                           hideLabel:true,
                           anchor:'100%'
                       }                              
                    ]
                }
            ],
            buttons: [
                { xtype:'movie-form-savebutton' },
                {
                    text:'Cancel',
                    handler:function(b,e){ Ext.getCmp('movieDialog').close(); }
                }
            ]
        });
        mymdb.movie.MovieFormPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieformpanel', mymdb.movie.MovieFormPanel);

mymdb.movie.MovieSaveButton = Ext.extend( Ext.Button, {
    text: 'Save',
    handler:function(b,e){
        var theForm = Ext.getCmp('movie-formpanel').getForm();
        var idValue = theForm.findField('id').getValue();
        
        theForm.submit({
            clientValidation: true,
            url: 'movie/' + ( idValue == null || idValue == '' ? 'save' : 'update'),
            method:'POST',
            success: function(form, action) {
               Ext.Msg.alert('Success', 'Move saved successfully', function(){
                   // FIXME: turn these into event fires so listeners can handle these operations
                   //   rather than having to force the issue this way
                   Ext.getCmp('movieDialog').close();
                   Ext.StoreMgr.lookup('gridData').load();
               });
            },
            failure: function(form, action) {
                Ext.Msg.alert('Failure', action.result.msg);
            }
        });
    }    
});
Ext.reg('movie-form-savebutton', mymdb.movie.MovieSaveButton);