
mymdb.admin.SettingsDialog = Ext.extend( Ext.Window ,{
    title:'Settings',
    iconCls:'icon-settings',
    autoShow:true,
    closable:true,
    initHidden:false,
    modal:true,
    width:400,
    height:150,
    layout:'fit',
    autoScroll:true,
    initComponent: function(){
        Ext.apply(this, {
            buttons:[
                { text:'Cancel', scope:this, handler:function(){ this.close(); } },
                {
                    text:'Save',
                    scope:this,
                    handler:function(){
                        this.findByType('form')[0].getForm().submit({
                            url:'settings/save',
                            method:'POST',
                            success:function(){},
                            failure:function(){}
                        });
                    } 
                }
            ],
            items:[
                {
                    xtype:'form',
                    labelWidth:120,
                    frame:false,
                    bodyStyle:'padding:5px 5px 0',
                    items:[
                        { xtype:'textfield', fieldLabel:'TMDB Key', name:'tmdbApiKey', anchor:'100%' },
                        { xtype:'textfield', fieldLabel:'Rotten Tomatoes Key', name:'rottenApiKey', anchor:'100%' }
                    ],
                    listeners:{
                        render:{
                            scope:this,
                            fn:function(){
                                this.findByType('form')[0].getForm().load({
                                    url:'settings/show',
                                    method:'GET'
                                });
                            }
                        }
                    }
                }
            ]
        });
        mymdb.admin.SettingsDialog.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('settings-dialog', mymdb.admin.SettingsDialog);