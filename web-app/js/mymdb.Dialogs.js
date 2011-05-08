

mymdb.AboutDialog = Ext.extend( Ext.Window ,{
    autoShow:true,
    iconCls:'icon-about',
    closable:true,
    initHidden:false,
    modal:true,
    width:600,
    height:500,
    title:'About My Movie Database',
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'tabpanel',
                    activeTab:0,
                    items:[
                        {
                            title:'Info',
                            autoLoad:'browser/about',
                            closable:false
                        },
                        {
                            title:'Apache v2',
                            autoLoad:'apache-lic.html',
                            autoScroll:true,
                            closable:false
                        },
                        {
                            title:'GPL v3',
                            autoLoad:'gpl-3.0.html',
                            autoScroll:true,
                            closable:false
                        },
                        {
                            title:'Silk Icons',
                            autoLoad:'icons-license.html',
                            autoScroll:true,
                            closable:false
                        },
                        {
                            title:'TMDB',
                            autoLoad:'tmdb-panel.html',
                            autoScroll:true,
                            closable:false
                        }
                    ]
                }
            ]
        });
        mymdb.AboutDialog.superclass.initComponent.apply(this, arguments);
    }
});

/**
 * Self-closing message box useful for non-actionable success messages.
 *
 * Pass in a "data" array where the first element will be used as the message content.
 */
mymdb.Message = Ext.extend(Ext.Window, {
    width:300,
    height:50,
    closable:false,
    resizable:false,
    bodyBorder:false,
    y:50,
    tpl:"<div style='text-align:center;padding:6px;font-weight:bold;color:green;'>{0}</div>",
    listeners:{
        show:function(win){
            new Ext.util.DelayedTask(function(){
                win.close();
            }).delay(2500);
        }
    }
});
Ext.reg('mymdb-message',mymdb.Message);