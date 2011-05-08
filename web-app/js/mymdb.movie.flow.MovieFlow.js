// mymdb.movie

mymdb.movie.MovieDialog = Ext.extend( Ext.Window, {
    id:'movieDialog',
    autoShow:true,
    iconCls:'icon-movie',
    closable:true,
    initHidden:false,
    modal:true,
    width:640,
    height:580,
    title:'New Movie',
    layout:'fit',
    initComponent: function(){
        Ext.apply(this, {
            tbar:[
                { xtype:'tbfill' },
                { xtype:'button', text:'Help', iconCls:'icon-help', handler:function(){ Ext.Msg.alert('Help', 'Alas, you are currently helpless!'); } }
            ],
            items:[ {xtype:'movieflow-panel', movieId:this.movieId} ],
            buttons:[
                {
                    xtype:'button',
                    itemId:'cancel-btn',
                    text:'Cancel',
                    handler:function(b){
                        b.findParentByType(mymdb.movie.MovieDialog).close();
                    }
                },
                {
                    xtype:'button',
                    itemId:'prev-btn',
                    text:'Previous',
                    handler:function(b,e){
                        var active = Ext.getCmp('movieDialog').findByType(mymdb.movie.flow.MovieManagerFlowPanel)[0].getLayout().activeItem;
                        active.previous();
                    }
                },
                {
                    xtype:'button',
                    itemId:'next-btn',
                    text:'Next',
                    handler:function(b,e){
                        var active = Ext.getCmp('movieDialog').findByType(mymdb.movie.flow.MovieManagerFlowPanel)[0].getLayout().activeItem;
                        active.next();
                    }
                },
                {
                    xtype:'button',
                    itemId:'finish-btn',
                    text:'Finish',
                    handler:function(b,e){
                        var active = Ext.getCmp('movieDialog').findByType(mymdb.movie.flow.MovieManagerFlowPanel)[0].getLayout().activeItem;
                        active.finish();
                    }
                }
            ]
        });
        mymdb.movie.MovieDialog.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movie-dialog', mymdb.movie.MovieDialog);

mymdb.movie.flow.MovieManagerFlowPanel = Ext.extend( Ext.Panel, {
    layout:'card',
    initComponent: function(){
        Ext.apply(this, {
            activeItem:this.movieId !== undefined && this.movieId !== null ? 1 : 0,
            items:[
                {xtype:'movieflow-fetchresults'},
                {xtype:'movieflow-details', movieId:this.movieId },
                {xtype:'movieflow-poster', movieId:this.movieId },
                {xtype:'movieflow-genre', movieId:this.movieId },
                {xtype:'movieflow-actor', movieId:this.movieId },
                {xtype:'movieflow-websites', movieId:this.movieId },
                {xtype:'movieflow-summary', movieId:this.movieId }
            ]
        });

        mymdb.movie.flow.MovieManagerFlowPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-panel', mymdb.movie.flow.MovieManagerFlowPanel);

/**
 * This is the base form panel from which the movie flow panels are descended.
 */
mymdb.movie.flow.ViewPanel = Ext.extend(Ext.form.FormPanel, {
    formUrl:'',
    nextId:0,
    previousId:0,
    bodyStyle:{ padding:'5px' },
    initComponent: function(){
        Ext.apply(this, {});

        mymdb.movie.flow.ViewPanel.superclass.initComponent.apply(this, arguments);

        this.on('activate',function(p){
            this.loadData();
        },this);
    },
    loadData:function(){
        var self = this;
        this.load({
            method:'GET',
            url:this.formUrl,
            params:{
                id:this.movieId
            },
            success:function(form,action){
                self.fireEvent('loaded',action);
            }
        });
    },
    next:function(){
        var panel = this.findParentByType(mymdb.movie.flow.MovieManagerFlowPanel);
        var nid = this.nextId;

        this.getForm().submit({
            method:'POST', url:this.formUrl,
            success:function(){
                panel.getLayout().setActiveItem(nid);
            },
            failure:function(){
                Ext.Msg.alert('Error','Unable to submit form');
            }
        });
    },
    previous:function(){
        this.findParentByType(mymdb.movie.flow.MovieManagerFlowPanel).getLayout().setActiveItem(this.previousId);
    },
    finish:function(){
        var thePanel = this;
        var theForm = this.getForm();
        var theUrl = this.formUrl;
        theForm.submit({
            url:theUrl,
            params:{ finish:true },
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
    },
    setDialogTitle:function( title ){
        var dia = this.findParentByType(mymdb.movie.MovieDialog);
        dia.setTitle(title);
    },
    disableNavButtons:function( itemIds ){
        var dia = this.findParentByType(mymdb.movie.MovieDialog);
        Ext.each( dia.buttons, function(b){
            if( itemIds.indexOf(b.itemId) != -1 ){
                b.disable();
            } else {
                b.enable();
            }
        });
    }
});
Ext.reg('movieflow-viewpanel', mymdb.movie.flow.ViewPanel);
