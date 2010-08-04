Ext.BLANK_IMAGE_URL = 'rc/js/ext/resources/images/default/s.gif';

Ext.onReady(function(){
	var northPanel = new Ext.Panel({
		region: 'north',
		autoHeight: true,
		items: [
			{
				html: '<h1 class="x-panel-header">My Movie Database</h1>',
				border: false,
				margins: '0 0 5 0'
			},
			{
				xtype: 'toolbar',
				items: [ somethingOneAction, somethingTwoAction ]
			}
		]
	});
	
	var centerPanel = initializeGrid();
	centerPanel.region = 'center';
	centerPanel.split = true;

	var westPanel = new Ext.Panel({
		region: 'west',
		title: 'Categories',
		layout:'accordion',
		collapsible: true,
		split: true,
		width: '15%',
		defaults: {
			bodyStyle: 'padding:15px'
		},
		layoutConfig: {
			titleCollapse: false,
			activeOnTop: false,
			animate: true,
			titleCollapse: true
		},
		items: [{
			title: 'Title',
			autoLoad: resourceBase + '/browser/titles',
			autoScroll: true
		},{
			title: 'Genre',
			autoLoad: resourceBase + '/browser/genres',
			autoScroll: true
		},{
			title: 'Actor',
			html: '<p>Panel content!</p>',
			autoScroll: true
		},{
			title: 'Storage',
			html: '<p>Panel content!</p>',
			autoScroll: true
		},{
			title: 'Release Year',
			html: '<p>Panel content!</p>',
			autoScroll: true
		},{
			title: 'Tag',
			html: '<p>Panel content!</p>',
			autoScroll: true
		}
		]
	});	
	
	new Ext.Viewport({
		layout: 'border',
		items: [ northPanel, centerPanel, westPanel ]
	});
}); 