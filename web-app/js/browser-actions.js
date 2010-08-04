var somethingOneAction = new Ext.Action({
	text: 'Something One',
	handler: function(){
		new Ext.Window({
			autoShow: true,
			closable: true,
			initHidden: false,
			modal: true,
			title: 'A Dialog',
			html: 'You have opened a dialog'
		});
	},
	itemId: 'something1'
});

var somethingTwoAction = new Ext.Action({
	text: 'Something Two',
	handler: function(){
		Ext.Msg.alert('Action', 'You have done something!');
	},
	itemId: 'something2'
});	