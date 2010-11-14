// mymdb.movie

mymdb.movie.MovieDialog = Ext.extend( Ext.Window, {
	id:'movieDialog',
    autoShow:true,
    iconCls:'icon-movie',
    closable:true,
    initHidden:false,
    modal:true,
    width:625,
    height:650,
    title:'New Movie',
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
        	tbar:[ { xtype:'button',text:'Fetch' }, { xtype:'tbfill' }, { xtype:'button',text:'Help' } ],
            items:[ { xtype:'movieformpanel' } ],
        });
        mymdb.movie.MovieDialog.superclass.initComponent.apply(this, arguments);
    }	
});

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
                	xtype:'fieldset',
                	items:[
					   {
					   		xtype:'checkboxgroup',
							fieldLabel:'Genres',
							itemCls:'x-check-group-alt',
							columns:4,
							items:[
								{boxLabel:'Horror', name:'cb-col-1'},
								{boxLabel:'Action', name:'cb-col-2', checked: true},
								{boxLabel:'Drama', name:'cb-cold-3'},
								{boxLabel:'Comedy', name:'cb-colf-1'},
								{boxLabel:'Porn', name:'cb-colg-2', checked: true},
								{boxLabel:'Family', name:'cb-col-e3'},
								{boxLabel:'Musical', name:'cb-cols-1'},
								{boxLabel:'Indy', name:'cb-col-u2', checked: true},
								{boxLabel:'Snuff', name:'cb-col-3d'}  						
							]
					   },
					   {
						   xtype:'button',
						   text:'Select Genres',
                           handler:function(){ new mymdb.movie.MovieGenreSelector(); }
					   }
                	]
                },
                
                {
                	xtype:'fieldset',
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
					   },
					   {
						   xtype:'button',
						   text:'Select Actors'
					   }
                	]
                },
                

                       {
                           xtype:'htmleditor',
                           id:'desctiption',
                           height:200,
                           hideLabel:true,
                           anchor:'100%'
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

mymdb.movie.MovieGenreSelector = Ext.extend( Ext.Window ,{
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
                new mymdb.movie.GenreSelectorListView({
                    store:new Ext.data.JsonStore({
                        url:'genre/list',
                        autoLoad: true,
                        autoDestroy: true,
                        storeId: 'genre_selector_store',
                        root: 'items',
                        idProperty: 'id',
                        fields: ['id','label']
                    })
                })
            ],
            tbar:[ mymdb.genre.NewGenreActionFactory('button'), ],
            buttons:[
                {
                    text:'Ok'
                },
                {
                    text:'Cancel'
                }
            ]
        });
        mymdb.movie.MovieGenreSelector.superclass.initComponent.apply(this, arguments);
    }
});

mymdb.movie.GenreSelectorListView = Ext.extend( Ext.list.ListView, {
    id:'genreListView',
	emptyText: 'No Genres',
	loadingText:'Loading...',
	reserveScrollOffset: true,
	hideHeaders:false,
	multiSelect:true,
	columns: [{header:'Genre', dataIndex:'label'}],

    initComponent: function(){
        mymdb.movie.GenreSelectorListView.superclass.initComponent.apply(this, arguments);

//        Ext.getBody().on("contextmenu", Ext.emptyFn, null, {preventDefault: true});

//        this.on( 'dblclick', function(dataView,idx,node,evt){
//            mymdb.genre.OpenGenreEditDialogHandler(dataView,idx);
//        });

//		this.on( 'contextmenu', function( dataView, idx, node, evt ){
//            mymdb.genre.ContextPopupFactory(dataView,idx).showAt( evt.getXY() );
//		} );
    }
});
