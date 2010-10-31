

mymdb.AboutDialog = Ext.extend( Ext.Window ,{
    autoShow:true,
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
                            autoLoad:'apache-lic.txt',
                            autoScroll:true,
                            closable:false
                        },
                        {
                            title:'GPL v3',
                            autoLoad:'gpl-3.0.txt',
                            autoScroll:true,
                            closable:false
                        },
                        {
                            title:'DryIcons Free',
                            autoLoad:'dryicons-license.txt',
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