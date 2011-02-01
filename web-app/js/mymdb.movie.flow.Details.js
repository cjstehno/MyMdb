
mymdb.movie.flow.DetailsView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/details',
    nextId:2,
    previousId:0,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'textfield', fieldLabel:'Title', name:'title', allowBlank:false, minLength:1 },
                { xtype:'numberfield', fieldLabel:'Release Year', name:'releaseYear', allowBlank:false, allowDecimals:false, allowNegative:false, minValue:1930, maxValue:2020 },
                { xtype:'textfield', fieldLabel:'Storage Name', name:'storageName' },
                { xtype:'numberfield', fieldLabel:'Storage Index', name:'storageIndex', allowDecimals:false, allowNegative:false, minValue:1, maxValue:120 },
                { xtype:'htmleditor', fieldLabel:'Description', name:'description', anchor:'100%', height:260, allowBlank:false }
            ]
        });

        this.on('activate',function(p){
            this.disableNavButtons( ['prev-btn'] );
            this.setDialogTitle('New Movie: Details');
        },this);

        mymdb.movie.flow.DetailsView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-details', mymdb.movie.flow.DetailsView);