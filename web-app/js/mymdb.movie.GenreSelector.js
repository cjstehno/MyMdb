mymdb.movie.MovieGenreSelector = Ext.extend( Ext.Window ,{
    id:'movieGenreSelector',
    autoShow:true,
    iconCls:'icon-movie',
    closable:true,
    initHidden:false,
    modal:true,
    width:400,
    height:300,
    title:'Select Genres',
    layout:'fit',
    autoScroll:true,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'movie-genreselector-listview', preSelected:this.preSelected }
            ],
            tbar:[ mymdb.genre.NewGenreActionFactory('button'), ],
            buttons:[
                {
                    text:'Ok',
                    handler:function(){
                        var genreList = Ext.getCmp('genreListView');
                        var selectedGenres = genreList.getSelectedRecords();
                        
                        var formStore = Ext.StoreMgr.lookup('genre_form_store');
                        formStore.removeAll();
                        formStore.add( selectedGenres );
                        
                        Ext.getCmp('movieGenreSelector').close();
                    }
                },
                {
                    text:'Cancel',
                    handler:function(){
                        Ext.getCmp('movieGenreSelector').close();
                    }
                }
            ]
        });
        mymdb.movie.MovieGenreSelector.superclass.initComponent.apply(this, arguments);
    }
});

mymdb.movie.GenreSelectedHandler = function(){
	
};

mymdb.movie.GenreSelectorListView = Ext.extend( Ext.list.ListView, {
    id:'genreListView',
    emptyText: 'No Genres',
    loadingText:'Loading...',
    reserveScrollOffset: true,
    hideHeaders:false,
    multiSelect:true,
    columns: [{header:'Genre', dataIndex:'label'}],
    
    initComponent: function(){
        Ext.apply(this, {
            store:new Ext.data.JsonStore({
                url:'genre/list',
                autoLoad: false,
                autoDestroy: true,
                storeId: 'genre_selector_store',
                root: 'items',
                idProperty: 'id',
                fields: ['id','label']
            })
        });

        mymdb.movie.GenreSelectorListView.superclass.initComponent.apply(this, arguments);
        
        this.getStore().on( 'load', function(store, records, opts){
        	if( this.preSelected != undefined ){
        		var recs = [];
        		Ext.each( this.preSelected, function(it){
        			var idx = store.find( 'id', it );
        			if( idx != -1 ){
        				recs.push( store.getAt(idx) );
        			}
        		});
        		this.select( recs, false, true );
        	}
        }, this);
        
        this.getStore().load();
    }
});
Ext.reg('movie-genreselector-listview', mymdb.movie.GenreSelectorListView);