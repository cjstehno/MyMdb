
mymdb.movie.flow.PosterView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/poster',
    nextId:3,
    previousId:1,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'radiogroup',
                    columns:1,
                    items:[
                        {
                            xtype:'compositefield',
                            items:[
                                {
                                    xtype: 'radio',
                                    fieldLabel:'URL'
                                }
                            ]
                        },
                        {
                            xtype:'compositefield',
                            items:[
                                {
                                    xtype: 'radio',
                                    fieldLabel:'Upload'
                                }
                            ]
                        }
                    ]
                }
            ]
        });

        this.on('activate',function(p){
            mymdb.movie.flow.DisableButtonFunction(p);
            mymdb.movie.flow.UpdateDialogTitleFunction(p, 'New Movie: Poster');
        });

        mymdb.movie.flow.PosterView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-poster', mymdb.movie.flow.PosterView);