Ext.BLANK_IMAGE_URL = 'js/ext/resources/images/default/s.gif';
 
Ext.namespace('mymdb');
 
mymdb.app = function() {
    // do NOT access DOM from here; elements don't exist yet
 
    // private variables
 
    // private functions
 
    // public space
    return {
        // public properties, e.g. strings to translate
        openAboutAction: new Ext.Action({
            itemId: 'openAbout',
            text: 'About',
            handler: function(){
                new mymdb.AboutDialog();
            }
        }),
 
        // public methods
        init: function() {
			new Ext.Viewport({
				layout: 'border',
				items: [ 
					{ region:'north', xtype:'headerpanel' },
					{ region:'center', xtype:'contentpanel'},
					{ region:'west', xtype:'categoriespanel', collapsible:true },
					// { region:'east', xtype:'detailspanel', collapsible:true},
					{ region:'south', xtype:'footerpanel' }
				]
			});		
		
        }
    };
}();

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
                        }
                    ]
                }
            ]
        });
        mymdb.AboutDialog.superclass.initComponent.apply(this, arguments);
    }
});

mymdb.HeaderPanel = Ext.extend( Ext.Panel, {
	html: '<h1 class="x-panel-header">My Movie Database</h1>',
	autoHeight: true,
	border: false,
	margins: '0 0 5 0',
	bbar:[ { xtype:'tbfill' }, mymdb.app.openAboutAction ]
});
Ext.reg('headerpanel', mymdb.HeaderPanel);

mymdb.FooterPanel = Ext.extend( Ext.Panel, {
	html: '<div style="text-align:center;font-size:small;">Copyright &copy; 2010 Christopher J. Stehno</div>',
	autoHeight: true,
	border: false,
	margins: '0 0 5 0'
});
Ext.reg('footerpanel', mymdb.FooterPanel);