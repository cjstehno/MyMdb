
mymdb.DetailsPanel = Ext.extend( Ext.Panel, {
	title: 'Details',
	html: 'Information goes here...',
	collapsible:true,
	width:200
});
Ext.reg('detailspanel', mymdb.DetailsPanel);