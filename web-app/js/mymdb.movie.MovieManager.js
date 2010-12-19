// mymdb.movie

mymdb.movie.MovieDialog = Ext.extend( Ext.Window, {
    id:'movieDialog',
    autoShow:true,
    iconCls:'icon-movie',
    closable:true,
    initHidden:false,
    modal:true,
    width:625,
    height:500,
    title:'New Movie',
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            tbar:[
                { xtype:'tbfill' }, 
                { xtype:'button',text:'Help', iconCls:'icon-help', handler:function(){ Ext.Msg.alert('Help', 'Alas, you are currently helpless!'); } }
            ],
            items:[ {xtype:'movieflow-panel'} ],
            buttons:[
                { 
                    xtype:'button',
                    itemId:'cancel-btn',
                    text:'Cancel',
                    handler:function(b){
                        b.findParentByType(mymdb.movie.MovieDialog).close();
                    }
                },
                { 
                    xtype:'button',
                    itemId:'prev-btn',
                    text:'Previous',
                    handler:function(b,e){
                        var active = Ext.getCmp('movieDialog').findByType(mymdb.movie.flow.MovieManagerFlowPanel)[0].getLayout().activeItem;
                        active.previous();
                    }
                },
                {
                    xtype:'button',
                    itemId:'next-btn',
                    text:'Next',
                    handler:function(b,e){
                        var active = Ext.getCmp('movieDialog').findByType(mymdb.movie.flow.MovieManagerFlowPanel)[0].getLayout().activeItem;
                        active.next();
                    }
                }
            ]
        });
        mymdb.movie.MovieDialog.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movie-dialog', mymdb.movie.MovieDialog);

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
                    maxLength:40,
                    width:200
                },
                {
                	xtype:'numberfield',
                    fieldLabel:'Release Year',
                    name: 'releaseYear',
                    allowBlank:false,
                    maxValue:2200,
                    minValue:1900,
                    width:75
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
                            fieldLabel: 'Name',
                            width:100
                        },
                        {
                            xtype     : 'textfield',
                            name      : 'storage.index',
                            fieldLabel: 'Index',
                            width:100
                        }
                    ]
                },
                {
                	xtype:'textfield',
                    inputType:'file',
                    fieldLabel:'Poster',
                    name: 'poster',
                    allowBlank:true
                },
                {
                    xtype:'superboxselect',
                    allowBlank:false,
                    id:'genre-selector',
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
                },
                {
                    xtype:'superboxselect',
                    allowBlank:false,
                    id:'actor-selector',
                    fieldLabel: 'Actors',
                    emptyText: 'Select actors for movie.',
                    resizable: true,
                    name: 'actors',
                    anchor:'100%',
                    store:new Ext.data.JsonStore({
                        url:'actor/list',
                        autoLoad: true,
                        autoDestroy: true,
                        storeId: 'actor_form_store',
                        root: 'items',
                        idProperty: 'id',
                        fields: ['id','label','count']
                    }),
                    mode: 'local',
                    displayField: 'label',
                    displayFieldTpl: '{label}',
                    valueField: 'id',
                    forceSelection : true
                },                
                {
                    xtype:'htmleditor',
                    id:'description',
                    hideLabel:true,
                    anchor:'100%'
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