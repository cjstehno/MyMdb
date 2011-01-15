
mymdb.movie.flow.ActorsView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/actor',
    nextId:5,
    previousId:3,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'label', text:'Select actors for movie:' },
                { xtype:'movieflow-itemselector', availableUrl:'actor/list' },
                {
                    xtype:'button',
                    text:'New Actor',
                    style:{padding:'4px'},
                    icon:'/mymdb/images/icons/add.png',
                    handler: function(b,e){
                        new mymdb.actor.ActorDialog({
                            onSave:function(){
                                (b.findParentByType('movieflow-actor').find('itemId', 'selector-available')[0]).getStore().load();
                            }
                        });
                    }
                }
            ]
        });

        this.on('activate',function(p){
            this.disableNavButtons( [] );
            this.setDialogTitle('New Movie: Actors');
        },this);

        mymdb.movie.flow.ActorsView.superclass.initComponent.apply(this, arguments);
    },
    next:function(){
        var panel = this.findParentByType(mymdb.movie.flow.MovieManagerFlowPanel);
        var nid = this.nextId;

        var selectedRecs = (this.find('itemId','selector-selected')[0]).getStore().getRange();
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
    }
});
Ext.reg('movieflow-actor', mymdb.movie.flow.ActorsView);

