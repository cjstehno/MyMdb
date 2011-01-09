// mymdb.movie

mymdb.movie.MovieDialog = Ext.extend( Ext.Window, {
    id:'movieDialog',
    autoShow:true,
    iconCls:'icon-movie',
    closable:true,
    initHidden:false,
    modal:true,
    width:625,
    height:500,
    title:'New Movie',
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            tbar:[
                { xtype:'tbfill' }, 
                { xtype:'button',text:'Help', iconCls:'icon-help', handler:function(){ Ext.Msg.alert('Help', 'Alas, you are currently helpless!'); } }
            ],
            items:[ {xtype:'movieflow-panel', movieId:this.movieId} ],
            buttons:[
                { 
                    xtype:'button',
                    itemId:'cancel-btn',
                    text:'Cancel',
                    handler:function(b){
                        b.findParentByType(mymdb.movie.MovieDialog).close();
                    }
                },
                { 
                    xtype:'button',
                    itemId:'prev-btn',
                    text:'Previous',
                    handler:function(b,e){
                        var active = Ext.getCmp('movieDialog').findByType(mymdb.movie.flow.MovieManagerFlowPanel)[0].getLayout().activeItem;
                        active.previous();
                    }
                },
                {
                    xtype:'button',
                    itemId:'next-btn',
                    text:'Next',
                    handler:function(b,e){
                        var active = Ext.getCmp('movieDialog').findByType(mymdb.movie.flow.MovieManagerFlowPanel)[0].getLayout().activeItem;
                        active.next();
                    }
                }
            ]
        });
        mymdb.movie.MovieDialog.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movie-dialog', mymdb.movie.MovieDialog);
