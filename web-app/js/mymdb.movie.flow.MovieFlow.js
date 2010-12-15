
mymdb.movie.flow.MovieManagerFlowPanel = Ext.extend( Ext.Panel, {
	layout:'card',
	activeItem:0,
        initComponent: function(){
        Ext.apply(this, {
            items:[
               {xtype:'movieflow-entertile'},
               {xtype:'movieflow-fetchresults'},
               {xtype:'movieflow-details'},
               {xtype:'movieflow-poster'},
               {xtype:'movieflow-genre'},
               {xtype:'movieflow-actor'},
               {xtype:'movieflow-summary'}
            ]
        });
        
        mymdb.movie.flow.MovieManagerFlowPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-panel', mymdb.movie.flow.MovieManagerFlowPanel);

mymdb.movie.flow.EnterTitleView = Ext.extend(Ext.form.FormPanel, {
    initComponent: function(){
        Ext.apply(this, {
            formUrl:'movie/enterTitle',
            nextId:1,
            items:[
                { xtype:'textfield', fieldLabel:'Title', name:'title' }
            ]  
        });

        this.on('activate',function(p){
            mymdb.movie.flow.FlowViewInitFunction(p);
            mymdb.movie.flow.DisableButtonFunction(p, 'prev-btn');
        });
        
        mymdb.movie.flow.EnterTitleView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-entertile', mymdb.movie.flow.EnterTitleView);

mymdb.movie.flow.DisableButtonFunction = function( p, buttonId ){
    var dia = p.findParentByType(mymdb.movie.MovieDialog);
    Ext.each( dia.buttons, function(b){
        if(b.itemId == buttonId){
            b.disable();
        }
    });
};

mymdb.movie.flow.FlowNextButton = Ext.extend(Ext.Button, {
    text:'Next',
    handler:function(b,e){
        var formPanel = b.findParentByType('form');
        formPanel.getForm().submit({
            method:'POST', url:formPanel.formUrl,
            success:function(){
                formPanel.findParentByType(mymdb.movie.flow.MovieManagerFlowPanel).getLayout().setActiveItem(b.nextId);
            },
            failure:function(){
                Ext.Msg.alert('Error','Unable to submit form');
            }
        });
    }
});
Ext.reg('movieflow-nextbutton', mymdb.movie.flow.FlowNextButton);

mymdb.movie.flow.FlowViewInitFunction = function(pnl){
    pnl.load({method:'GET', url:pnl.formUrl});
};
