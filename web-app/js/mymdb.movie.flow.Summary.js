
mymdb.movie.flow.SummaryView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/finish',
    nextId:6,
    previousId:4,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'label', text:'Summary' }
            ]
        });

        this.on('activate',function(p){
            mymdb.movie.flow.DisableButtonFunction(p);
            mymdb.movie.flow.UpdateDialogTitleFunction(p, 'New Movie: Summary');
        });

        mymdb.movie.flow.SummaryView.superclass.initComponent.apply(this, arguments);
    },
    next:function(){
        alert('submit the final data');
    }
});
Ext.reg('movieflow-summary', mymdb.movie.flow.SummaryView);