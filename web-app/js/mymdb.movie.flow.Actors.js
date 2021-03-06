
mymdb.movie.flow.ActorsView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/actors',
    nextId:5,
    previousId:3,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'label', text:'Select actors for movie:' },
                { xtype:'movieflow-itemselector', url:'movie/actors/list' },
                {
                    xtype:'button',
                    text:'New Actor',
                    style:{padding:'4px'},
                    iconCls:'icon-add-actor',
                    handler: function(b,e){
                        new mymdb.actor.ActorDialog({
                            onSave:function(){
                                b.findParentByType('movieflow-actor').findByType('movieflow-itemselector')[0].store.reload();
                            }
                        });
                    }
                }
            ]
        });

        this.on('activate',function(p){
            this.disableNavButtons( [] );
            this.setDialogTitle('New Movie: Actors');

            this.findByType('movieflow-itemselector')[0].store.reload();
        },this);

        mymdb.movie.flow.ActorsView.superclass.initComponent.apply(this, arguments);
    },
    next:function(){
        var panel = this.findParentByType(mymdb.movie.flow.MovieManagerFlowPanel);
        var nid = this.nextId;

        var selectedRecs = (this.findByType('movieflow-itemselector')[0]).getSelectedRecords();
        var actorIds = [];
        Ext.each(selectedRecs, function(it){
            actorIds.push(it.data.id);
        });

        this.getForm().submit({
            method:'POST', url:this.formUrl,
            params:{
                actors:actorIds
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
        var actorIds = [];
        Ext.each(selectedRecs, function(it){
            actorIds.push(it.data.id);
        });

        theForm.submit({
            url:theUrl,
            params:{ finish:true, actors:actorIds },
            clientValidation: true,
            method:'POST',
            success:function(form,action){
                new mymdb.Message({data:['Movie saved successfully']}).show();
                thePanel.findParentByType('window').close();
                Ext.StoreMgr.lookup('gridData').load();
            },
            failure:function(form,action){
                Ext.Msg.alert('Failure', action.result.msg);
            }
        });
    }
});
Ext.reg('movieflow-actor', mymdb.movie.flow.ActorsView);

