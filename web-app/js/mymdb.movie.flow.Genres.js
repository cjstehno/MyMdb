
mymdb.movie.flow.GenresView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/genre',
    nextId:4,
    previousId:2,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'label', text:'Select genres for movie:' },
                {
                    xtype:'panel',
                    border:false,
                    layout:'hbox',
                    items:[
                        { xtype:'movieflow-genre-available' },
                        { xtype:'movieflow-genre-buttons' },
                        { xtype:'movieflow-genre-selected' },
                    ]
                }
            ]
        });

        this.on('activate',function(p){
            mymdb.movie.flow.DisableButtonFunction(p);
            mymdb.movie.flow.UpdateDialogTitleFunction(p, 'New Movie: Genres');
        });

        mymdb.movie.flow.GenresView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-genre', mymdb.movie.flow.GenresView);

mymdb.movie.flow.AvailableGenresPanel = Ext.extend( Ext.Panel, {
    width:275,
    height:300,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'label', text:'Available:' },
                { xtype:'button', text:'New Genre' }
            ]
        });

        mymdb.movie.flow.AvailableGenresPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-genre-available', mymdb.movie.flow.AvailableGenresPanel);

mymdb.movie.flow.GenreSelectorButtonPanel = Ext.extend( Ext.Panel, {
    width:45,
    height:300,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'button', text:'->' },
                { xtype:'button', text:'<-' }
            ]
        });

        mymdb.movie.flow.GenreSelectorButtonPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-genre-buttons', mymdb.movie.flow.GenreSelectorButtonPanel);

mymdb.movie.flow.SelectedGenresPanel = Ext.extend( Ext.Panel, {
    width:275,
    height:300,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'label', text:'Selected:' },
            ]
        });

        mymdb.movie.flow.SelectedGenresPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-genre-selected', mymdb.movie.flow.SelectedGenresPanel);