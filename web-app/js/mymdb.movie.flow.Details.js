
mymdb.movie.flow.DetailsView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/details',
    nextId:3,
    previousId:1,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'textfield', fieldLabel:'Title', name:'title' },
                { xtype:'numberfield', fieldLabel:'Release Year', name:'releaseYear' },
                { xtype:'textfield', fieldLabel:'Storage Name', name:'storageName' },
                { xtype:'numberfield', fieldLabel:'Storage Index', name:'storageIndex' },
                { xtype:'htmleditor', fieldLabel:'Description', name:'description', anchor:'100%', height:200 }
            ]
        });

        this.on('activate',function(p){
            mymdb.movie.flow.DisableButtonFunction(p);
            mymdb.movie.flow.UpdateDialogTitleFunction(p, 'New Movie: Details');
        });

        mymdb.movie.flow.DetailsView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-details', mymdb.movie.flow.DetailsView);