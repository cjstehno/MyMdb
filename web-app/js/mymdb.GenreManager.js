mymdb.GenreManagerDialog = Ext.extend( Ext.Window ,{
    autoShow:true,
    closable:true,
    initHidden:false,
    modal:true,
    width:400,
    height:300,
    title:'Genre Manager',
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            items:[ new mymdb.GenreManagerPanel() ]
        });
        mymdb.GenreManagerDialog.superclass.initComponent.apply(this, arguments);
    },
    tbar:[
        {
            text:'New',
            handler: function(){ new mymdb.GenreDialog(); }
        }
    ]
});

mymdb.GenreManagerPanel = Ext.extend( Ext.Panel, {
    html:'This will have a list of genres',
    initComponent: function(){
        Ext.apply(this, {
            items:[ ]
        });
        mymdb.GenreManagerPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('genremanagerpanel', mymdb.GenreManagerPanel);

mymdb.GenreDialog = Ext.extend( Ext.Window ,{
    autoShow:true,
    closable:true,
    initHidden:false,
    modal:true,
    width:200,
    height:200,
    title:'Genre',
    layout:'fit',
    html:'This will be an editor form for genre',
    initComponent: function(){
        Ext.apply(this, {
            items:[]
        });
        mymdb.GenreDialog.superclass.initComponent.apply(this, arguments);
    }
});