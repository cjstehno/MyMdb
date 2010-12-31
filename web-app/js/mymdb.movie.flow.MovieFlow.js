
mymdb.movie.flow.MovieManagerFlowPanel = Ext.extend( Ext.Panel, {
    layout:'card',
    activeItem:0,
    initComponent: function(){
        Ext.apply(this, {
            items:[
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

mymdb.movie.flow.ViewPanel = Ext.extend(Ext.form.FormPanel, {
    formUrl:'',
    nextId:0,
    previousId:0,
    bodyStyle:{ padding:'5px' },
    initComponent: function(){
        Ext.apply(this, {});

        mymdb.movie.flow.ViewPanel.superclass.initComponent.apply(this, arguments);

        this.on('activate',function(p){
            p.load({method:'GET', url:p.formUrl});
        });
    },
    next:function(){
        var panel = this.findParentByType(mymdb.movie.flow.MovieManagerFlowPanel);
        var nid = this.nextId;

        this.getForm().submit({
            method:'POST', url:this.formUrl,
            success:function(){
                panel.getLayout().setActiveItem(nid);
            },
            failure:function(){
                Ext.Msg.alert('Error','Unable to submit form');
            }
        });
    },
    previous:function(){
        this.findParentByType(mymdb.movie.flow.MovieManagerFlowPanel).getLayout().setActiveItem(this.previousId);
    }
});
Ext.reg('movieflow-viewpanel', mymdb.movie.flow.ViewPanel);

mymdb.movie.flow.DisableButtonFunction = function( p, buttonId ){
    var dia = p.findParentByType(mymdb.movie.MovieDialog);
    Ext.each( dia.buttons, function(b){
        if(b.itemId == buttonId){
            b.disable();
        } else {
            b.enable();
        }
    });
};

mymdb.movie.flow.UpdateDialogTitleFunction = function( p, title ){
    var dia = p.findParentByType(mymdb.movie.MovieDialog);
    dia.setTitle(title);
};
