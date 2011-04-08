
mymdb.movie.flow.GenresView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/genres',
    nextId:4,
    previousId:2,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'label', text:'Select genres for movie:' },
                { xtype:'movieflow-itemselector', url:'movie/genres/list' },
                { 
                    xtype:'button',
                    text:'New Genre',
                    style:{padding:'4px'},
                    iconCls:'icon-add-genre',
                    handler: function(b,e){
                        new mymdb.genre.GenreDialog({
                            onSave:function(){
                                b.findParentByType('movieflow-genre').findByType('movieflow-itemselector')[0].store.reload();
                            }
                        });
                    }
                }
            ]
        });

        this.on('activate',function(p){
            this.disableNavButtons( [] );
            this.setDialogTitle('New Movie: Genres');

            this.findByType('movieflow-itemselector')[0].store.reload();
        },this);

        mymdb.movie.flow.GenresView.superclass.initComponent.apply(this, arguments);
    },
    next:function(){
        var panel = this.findParentByType(mymdb.movie.flow.MovieManagerFlowPanel);
        var nid = this.nextId;

        var selectedRecs = (this.findByType('movieflow-itemselector')[0]).getSelectedRecords();
        var genreIds = [];
        Ext.each(selectedRecs, function(it){
            genreIds.push(it.data.id);
        });

        this.getForm().submit({
            method:'POST', url:this.formUrl,
            params:{
                genres:genreIds
            },
            success:function(){
                panel.getLayout().setActiveItem(nid);
            },
            failure:function(){
                Ext.Msg.alert('Error','Unable to submit form');
            }
        });
    },
    finish:function(){
        var thePanel = this;
        var theForm = this.getForm();
        var theUrl = this.formUrl;

        var selectedRecs = (this.findByType('movieflow-itemselector')[0]).getSelectedRecords();
        var genreIds = [];
        Ext.each(selectedRecs, function(it){
            genreIds.push(it.data.id);
        });

        theForm.submit({
            url:theUrl,
            params:{ finish:true, genres:genreIds },
            clientValidation: true,
            method:'POST',
            success:function(form,action){
               Ext.Msg.alert('Success', 'Movie saved successfully', function(){
                   thePanel.findParentByType('window').close();
                   Ext.StoreMgr.lookup('gridData').load();
               });
            },
            failure:function(form,action){
                Ext.Msg.alert('Failure', action.result.msg);
            }
        });
    }
});
Ext.reg('movieflow-genre', mymdb.movie.flow.GenresView);



