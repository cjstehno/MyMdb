
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
            this.disableNavButtons( ['next-btn'] );
            this.setDialogTitle('New Movie: Summary');
        },this);

        mymdb.movie.flow.SummaryView.superclass.initComponent.apply(this, arguments);

        this.un('activate');

        this.on('activate',function(p){ // TODO: try using autoload instead and then reloading... that is the difference
            p.getComponent('summary').load({url:p.formUrl});
        });
    }
});
Ext.reg('movieflow-summary', mymdb.movie.flow.SummaryView);
