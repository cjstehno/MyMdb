Ext.BLANK_IMAGE_URL = 'js/ext/resources/images/default/s.gif';
 
Ext.namespace('mymdb');
 
mymdb.app = function() {
    // do NOT access DOM from here; elements don't exist yet
 
    // private variables
 
    // private functions
 
    // public space
    return {
        // public properties, e.g. strings to translate
 
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

mymdb.HeaderPanel = Ext.extend( Ext.Panel, {
	html: '<h1 class="x-panel-header">My Movie Database</h1>',
	autoHeight: true,
	border: false,
	margins: '0 0 5 0',
	bbar:[ { text:'foo', xtype:'button' } ]
});
Ext.reg('headerpanel', mymdb.HeaderPanel);

mymdb.FooterPanel = Ext.extend( Ext.Panel, {
	html: '<div style="text-align:center;font-size:small;">Copyright &copy; 2010 Christopher J. Stehno</div>',
	autoHeight: true,
	border: false,
	margins: '0 0 5 0'
});
Ext.reg('footerpanel', mymdb.FooterPanel);
