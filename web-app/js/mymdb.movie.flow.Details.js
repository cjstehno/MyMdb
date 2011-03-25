
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
                    hiddenName:'mpaaRating',
                    mode:'local',
                    width:100,
                    editable:false,
                    forceSelection:true,
                    allowBlank:false,
                    typeAhead:false,
                    lazyInit:false,
                    triggerAction:'all',
                    disableKeyFiltering:true,
                    store:new Ext.data.ArrayStore({
                        idIndex:0,
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
                    hiddenName:'format',
                    mode:'local',
                    width:100,
                    editable:false,
                    forceSelection:true,
                    allowBlank:false,
                    typeAhead:false,
                    lazyInit:false,
                    triggerAction:'all',
                    disableKeyFiltering:true,
                    store:new Ext.data.ArrayStore({
                        idIndex:0,
                        fields:[ 'fid', 'flabel' ],
                        data:[ ['UNKNOWN','Unknown'], ['DVD','DVD'], ['BLURAY','BluRay'], ['DVD_R','DVD-R'], ['VCD','VCD'] ]
                    }),
                    valueField:'fid',
                    displayField:'flabel'
                },

                {
                    xtype:'combo',
                    fieldLabel:'Broadcast',
                    name:'broadcast',
                    hiddenName:'broadcast',
                    mode:'local',
                    width:100,
                    editable:false,
                    forceSelection:true,
                    allowBlank:false,
                    typeAhead:false,
                    lazyInit:false,
                    triggerAction:'all',
                    disableKeyFiltering:true,
                    store:new Ext.data.ArrayStore({
                        idIndex:0,
                        fields:[ 'bid', 'blabel' ],
                        data:[ ['UNKNOWN','Unknown'], ['MOVIE','Movie'], ['TV_MOVIE','TV Movie'], ['TV_SPECIAL','TV Special'], ['TV_SERIES','TV Series'], ['OTHER','Other'] ]
                    }),
                    valueField:'bid',
                    displayField:'blabel'
                },                    

                {
                    xtype:'combo',
                    fieldLabel:'Storage',
                    name:'storageId',
                    hiddenName:'storageId',
                    mode:'local',
                    width:250,
                    editable:false,
                    forceSelection:true,
                    allowBlank:false,
                    typeAhead:false,
                    lazyInit:false,
                    triggerAction:'all',
                    disableKeyFiltering:true,
                    store:new Ext.data.JsonStore({
                        url:'movie/details/storage' + (this.movieId ? '?movieId=' + this.movieId : ''),
                        autoLoad:true,
                        root: 'items',
                        idProperty: 'id',
                        fields: ['id','label']
                    }),
                    valueField:'id',
                    displayField:'label'
                },

                { xtype:'htmleditor', fieldLabel:'Description', name:'description', anchor:'100%', height:260, allowBlank:false }
            ]
        });

        this.on('activate',function(p){
            this.disableNavButtons( ['prev-btn'] );
            this.setDialogTitle('New Movie: Details');

            this.find('name','storageId')[0].getStore().reload();
        },this);

        mymdb.movie.flow.DetailsView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-details', mymdb.movie.flow.DetailsView);