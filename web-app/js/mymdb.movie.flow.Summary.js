
mymdb.movie.flow.SummaryView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/summary',
    nextId:6,
    previousId:4,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'panel', itemId:'summary', border:false }
            ]
        });

        this.on('activate',function(p){
            mymdb.movie.flow.DisableButtonFunction(p);
//            p.updateNextButtonText('Finish');
            mymdb.movie.flow.UpdateDialogTitleFunction(p, 'New Movie: Summary');
        });

        mymdb.movie.flow.SummaryView.superclass.initComponent.apply(this, arguments);

        this.un('activate');

        this.on('activate',function(p){
            p.getComponent('summary').load({url:p.formUrl});
        });
    },
    next:function(){
        var thePanel = this;
        var theForm = this.getForm();
        var theUrl = this.formUrl;
        theForm.submit({
            url:theUrl,
            clientValidation: true,
            method:'POST',
            success:function(form,action){
               Ext.Msg.alert('Success', 'Movie saved successfully', function(){
                   thePanel.findParentByType('window').close();
                   Ext.StoreMgr.lookup('gridData').load();
               });
            },
            failure:function(form,action){
                Ext.Msg.alert('Failure', action.result.msg);
            }
        });
    }
//    updateNextButtonText:function(newText){
//        var dia = this.findParentByType(mymdb.movie.MovieDialog);
//        Ext.each( dia.buttons, function(b){
//            if(b.itemId == 'next-btn'){
//                b.setText(newText);
//            }
//        });
//    }
});
Ext.reg('movieflow-summary', mymdb.movie.flow.SummaryView);
