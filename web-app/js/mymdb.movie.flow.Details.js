
mymdb.movie.flow.DetailsView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/details',
    nextId:2,
    previousId:0,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'textfield', fieldLabel:'Title', name:'title', allowBlank:false, minLength:1, width:300 },
                { xtype:'numberfield', fieldLabel:'Release Year', name:'releaseYear', allowBlank:false, allowDecimals:false, allowNegative:false, minValue:1930, maxValue:2020 },

                { xtype:'numberfield', fieldLabel:'Runtime (min)', name:'runtime', allowBlank:false, allowDecimals:false, allowNegative:false, minValue:0, maxValue:500 },

                {
                    xtype:'combo',
                    fieldLabel:'MPAA Rating',
                    name:'mpaaRating',
                    mode:'local',
                    width:100,
                    editable:false,
                    autoSelect:true,
                    allowBlank:false,
                    lazyInit:false,
                    store:new Ext.data.ArrayStore({
                        fields:[ 'rid', 'rlabel' ],
                        data:[ ['UNKNOWN','Unknown'], ['G','G'], ['PG','PG'], ['PG_13','PG-13'], ['R','R'], ['NC_17','NC-17'], ['UNRATED','Unrated'] ]
                    }),
                    valueField:'rid',
                    displayField:'rlabel'
                },

                {
                    xtype:'combo',
                    fieldLabel:'Format',
                    name:'format',
                    mode:'local',
                    width:100,
                    editable:false,
                    autoSelect:true,
                    allowBlank:false,
                    lazyInit:false,
                    store:new Ext.data.ArrayStore({
                        fields:[ 'fid', 'flabel' ],
                        data:[ ['UNKNOWN','Unknown'], ['DVD','DVD'], ['BLUERAY','BlueRay'], ['DVD_R','DVD-R'], ['VCD','VCD'] ]
                    }),
                    valueField:'fid',
                    displayField:'flabel'
                },

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