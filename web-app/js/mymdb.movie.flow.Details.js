
mymdb.movie.flow.DetailsView = Ext.extend(Ext.form.FormPanel, {
    initComponent: function(){
        Ext.apply(this, {
            formUrl:'movie/details',
            items:[
                { xtype:'textfield', fieldLabel:'Title', name:'title' },
                { xtype:'numberfield', fieldLabel:'Release Year', name:'releaseYear' },
                { xtype:'textfield', fieldLabel:'Storage Name', name:'storageName' },
                { xtype:'numberfield', fieldLabel:'Storage Index', name:'storageIndex' },
                { xtype:'htmleditor', fieldLabel:'Description', name:'description', anchor:'100%', height:200 },
                { xtype:'movieflow-nextbutton', text:'Next', nextId:3 },
                { xtype:'movieflow-nextbutton', text:'Previous', nextId:1 },
            ]
        });

        this.on('activate',mymdb.movie.flow.FlowViewInitFunction);

        mymdb.movie.flow.DetailsView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-details', mymdb.movie.flow.DetailsView);