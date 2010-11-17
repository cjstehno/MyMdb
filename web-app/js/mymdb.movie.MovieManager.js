// mymdb.movie

mymdb.movie.MovieDialog = Ext.extend( Ext.Window, {
	id:'movieDialog',
    autoShow:true,
    iconCls:'icon-movie',
    closable:true,
    initHidden:false,
    modal:true,
    width:625,
    height:590,
    title:'New Movie',
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
        	tbar:[ 
                { xtype:'button',text:'Fetch' }, 
                { xtype:'tbfill' }, 
                { xtype:'button',text:'Help', iconCls:'icon-help' } 
            ],
            items:[ { xtype:'movieformpanel' } ],
        });
        mymdb.movie.MovieDialog.superclass.initComponent.apply(this, arguments);
    }	
});

mymdb.movie.MovieGenreLoader = function(){
    var genreStore = new Ext.data.JsonStore({
        url:'genre/list',
        autoLoad: true,
        autoDestroy: true,
        storeId: 'movie_genre_store',
        root: 'items',
        idProperty: 'id',
        fields: ['id','label']
    });
    
    var genres = [];
    
    genreStore.on('load', function(store, recs, opts){
        Ext.each( recs, function(rec){
            alert(rec);
            genres.push( { boxLabel:rec.label, name:'genre', value:rec.id } );            
        });
    });
    
    return genres;
}

mymdb.movie.MovieFormPanel = Ext.extend( Ext.FormPanel, {
    labelWidth:100,
    autoScroll:true,
    frame:false,
    bodyStyle:'padding:5px 5px 0',
    initComponent: function(){
        Ext.apply(this, {
            items: [
                { name:'id', xtype:'hidden' },
                { name:'version', xtype:'hidden' },
                {
                	xtype:'textfield',
                    fieldLabel:'Title',
                    name: 'title',
                    allowBlank:false,
                    maxLength:40
                },
                {
                	xtype:'textfield',
                    fieldLabel:'Release Year',
                    name: 'releaseYear',
                    allowBlank:false,
                    maxLength:4
                },
                {
                    xtype: 'compositefield',
                    fieldLabel: 'Storage',
                    msgTarget : 'side',
                    anchor    : '-20',
                    defaults: { flex: 1 },
                    items: [
                        {
                            xtype     : 'textfield',
                            name      : 'storage_name',
                            fieldLabel: 'Name'
                        },
                        {
                            xtype     : 'textfield',
                            name      : 'storage_index',
                            fieldLabel: 'Index'
                        }
                    ]
                },
                {
                	xtype:'textfield',
                    fieldLabel:'Poster',
                    name: 'poster',
                    allowBlank:false,
                    maxLength:4
                },
                
                {
                    xtype:'tabpanel',
                    height:375,
                    activeItem:0,
                    items:[
                        {
                            title:'Genres',
                            bodyStyle:'padding:5px',
                            autoScroll:true,
                            items:[
                                {
                                    fieldLabel:'Genres',
                                    itemCls:'x-check-group-alt',
                                    columns:4,
                                    items:mymdb.movie.MovieGenreLoader()
                                }                         
                            ]
                        },
                        {
                            title:'Actors',
                            bodyStyle:'padding:5px',
                            autoScroll:true,
                            items:[
                               {
                                    xtype:'checkboxgroup',
                                    fieldLabel:'Actors',
                                    itemCls:'x-check-group-alt',
                                    columns:3,
                                    items:[
                                        {boxLabel:'Walken, Christoper', name:'xcb-col-1'},
                                        {boxLabel:'Channing, Stockard', name:'xcb-col-2', checked: true},
                                        {boxLabel:'Herman, Peewee', name:'xcb-cold-3'},
                                        {boxLabel:'Murrow, Edward R.', name:'xcb-colf-1'},
                                        {boxLabel:'Chabert, Lacy', name:'xcb-colg-2', checked: true},
                                        {boxLabel:'Jackson, Sammuel L.', name:'xcb-col-e3'},
                                        {boxLabel:'Bunker, Edith', name:'xcb-cols-1'},
                                        {boxLabel:'Cher', name:'xcb-col-u2', checked: true},
                                        {boxLabel:'Stooge, Curley M.', name:'xcb-col-3d'}                       
                                    ]
                               }
                            ]
                        },
                        {
                            title:'Description',
                            items:[
                               {
                                   xtype:'htmleditor',
                                   id:'desctiption',
                                   height:347,
                                   width:597,
                                   hideLabel:true,
                                   anchor:'100%'
                               }                              
                            ]
                        }
                    ]
                }
            ],
            buttons: [
                {
                    text:'Save',
                    handler:function(b,e){ alert('Saving'); }
                },
                {
                    text:'Cancel',
                    handler:function(b,e){ Ext.getCmp('movieDialog').close(); }
                }
            ]
        });
        mymdb.movie.MovieFormPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieformpanel', mymdb.movie.MovieFormPanel);
