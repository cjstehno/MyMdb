
mymdb.movie.flow.PosterView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/poster',
    fileUpload:true,
    nextId:3,
    previousId:1,
    layout:'table',
    layoutConfig:{
        columns: 4
    },
    defaults:{
        height:30
    },
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'panel', colspan:4, html:'Select movie poster to use:', border:false },
                    
                { xtype:'mymdb-imagepanel', url:'/mymdb/poster/flow', style:'margin:4px;', border:false, width:110, height:160, rowspan:4 },

                { xtype: 'movieflow-poster-radio', boxLabel:'URL:', inputValue:'URL', targetItemIds:['url-text','url-button'] },
                { xtype:'textfield', itemId:'url-text', name:'url', value:'http://', width:325, disabled:true },
                {
                    xtype:'button',
                    itemId:'url-button',
                    text:'Fetch',
                    width:70,
                    handler:function(b,e){
                        var urlText = b.previousSibling().getValue();
                        var imagePanel = b.findParentByType('movieflow-poster').findByType('mymdb-imagepanel')[0];

                        Ext.Ajax.request({
                            method:'GET',
                            url:'movie/poster/fetch',
                            params: { url:urlText },
                            success:function(){
                                imagePanel.reload();
                            },
                            failure:function(){
                            }
                        });
                    }
                },

                { xtype:'movieflow-poster-radio', boxLabel:'File:', inputValue:'FILE', targetItemIds:['file-text'] },
                { xtype:'textfield', itemId:'file-text', inputType:'file', name:'file', hideLabel:true, height:25, width:400, disabled:true, colspan:2 },

                { xtype:'movieflow-poster-radio', boxLabel:'Existing:', inputValue:'EXISTING', targetItemIds:['existing-text','existing-button'] },
                { xtype:'textfield', itemId:'existing-text', name:'posterName', width:325, disabled:true, readOnly:true },
                {
                    xtype:'button',
                    itemId:'existing-button',
                    text:'Select...',
                    width:70,
                    disabled:true,
                    handler:function(b,e){
                        var existingText = b.previousSibling();
                        var posterId = b.ownerCt.find('name','posterId')[0];
                        var preSelectedId = posterId.getValue();

                        new mymdb.movie.flow.PosterSelector({
                            preSelectedId:preSelectedId,
                            callback:function(sel){
                                existingText.setValue( sel.name );
                                posterId.setValue( sel.id );

                                var imagePanel = b.findParentByType('movieflow-poster').findByType('mymdb-imagepanel')[0];

                                Ext.Ajax.request({
                                    url:'movie/poster/select',
                                    params: { id:sel.id },
                                    success:function(){
                                        imagePanel.reload();
                                    },
                                    failure:function(){
                                    }
                                });

                            }
                        }).setVisible(true);
                    }
                },

                {
                    xtype:'movieflow-poster-radio',
                    boxLabel:'None:',
                    inputValue:'NONE',
                    colspan:3,
                    handler:function(rdo, checked){
                        if(checked){
                            rdo.ownerCt.clearOtherRadios(rdo);

                            var imagePanel = rdo.findParentByType('movieflow-poster').findByType('mymdb-imagepanel')[0];

                            Ext.Ajax.request({
                                method:'POST',
                                url:'movie/poster/clear',
                                success:function(){
                                    imagePanel.reload();
                                },
                                failure:function(){ }
                            });
                        }
                    }                    
                },

                { xtype:'hidden', itemId:'existing-id', name:'posterId', colspan:4 }
            ]
        });

        this.on('activate',function(p){
            this.disableNavButtons( ['finish-btn'] );   // TODO: filed as issue (MYMDB-59)
            this.setDialogTitle('New Movie: Poster');

            p.findByType('mymdb-imagepanel')[0].reload();
        },this);

        mymdb.movie.flow.PosterView.superclass.initComponent.apply(this, arguments);
    },
    clearOtherRadios:function(rdo){
        Ext.each( rdo.ownerCt.findByType('radio'), function(it){
            if( it != rdo ){
                it.setValue(false);
            }
        });
    }
});
Ext.reg('movieflow-poster', mymdb.movie.flow.PosterView);


mymdb.movie.flow.PosterTypeRadio = Ext.extend(Ext.form.Radio, {
    name:'posterType',
    hideLabel:true,
    width:75,
    handler:function(rdo, checked){
        var ct = rdo.ownerCt;
        Ext.each( rdo.targetItemIds, function(it){
            var item = ct.getComponent(it);
            if(checked){
                item.enable();
                ct.clearOtherRadios(rdo);
            } else {
                item.disable();
            }
        });
    }
});
Ext.reg('movieflow-poster-radio',mymdb.movie.flow.PosterTypeRadio);


mymdb.ImagePanel = Ext.extend(Ext.Panel, {
    tpl:'<img src="{src}?{x}" width="{w}" height="{h}" />',
    listeners:{
        activate:function(p){
            p.reload();
        }
    },
    initComponent: function(){
        Ext.apply(this, {
            data:{ src:this.url, w:this.width, h:this.height, x:new Date().getTime() }
        });

        mymdb.ImagePanel.superclass.initComponent.apply(this, arguments);
    },
    reload:function(){
        this.update( { src:this.url, w:this.width, h:this.height, x:new Date().getTime() } );
    }
});
Ext.reg('mymdb-imagepanel', mymdb.ImagePanel);


mymdb.movie.flow.PosterSelector = Ext.extend(Ext.Window, {
    title:'Select Poster',
    width:300,
    height:400,
    modal:true,
    initComponent: function(){
        Ext.apply(this, {
            buttons:[
                {
                    xtype:'button',
                    text:'Cancel',
                    handler:function(b,e){
                        b.ownerCt.close();
                    }
                },
                {
                    xtype:'button',
                    text:'OK',
                    scope:this,
                    handler:function(b,e){
                        var dv = this.findByType('dataview')[0];
                        if(dv.getSelectionCount() > 0){
                            var sel = dv.getSelectedRecords()[0];
                            this.onSelection({name:sel.data.name, id:sel.data.id});
                        }
                    }
                }
            ],
            items:[
                {
                    xtype:'movieflow-poster-posterlist',
                    listeners:{
                        loaded:{
                            scope:this,
                            fn:function(pl){
                                var preSel = this.preSelectedId;
                                if(preSel !== undefined && preSel !== null){
                                    var dv = pl.findByType('dataview')[0];
                                    var rec = dv.getStore().getById(preSel.toString());
                                    dv.select(rec);
                                }
                            }
                        }
                    }
                }
            ]
        });

        mymdb.movie.flow.PosterSelector.superclass.initComponent.apply(this, arguments);
    },
    onSelection:function( sel ){
        this.callback(sel);
        this.close();
    }
});

mymdb.movie.flow.PosterListPanel = Ext.extend(Ext.Panel, {
    height:330,
    autoScroll:true,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'dataview',
                    store: new Ext.data.JsonStore({
                        autoLoad:true,
                        url:'poster/list',
                        root: 'posters',
                        idProperty:'id',
                        fields: ['name', 'id'],
                        listeners:{
                            load:{
                                scope:this,
                                fn:function(){
                                    this.fireEvent('loaded',this);
                                }
                            }
                        }
                    }),
                    tpl: new Ext.XTemplate(
                        '<tpl for=".">',
                            '<div class="thumb-wrap" id="{id}" style="padding:4px;">',
                            '<div class="thumb"><img src="poster/show/{id}" title="{name}" width="120" style="border:1px solid gray;"></div>',
                            '<div class="poster-name">{name}</div>',
                            '</div>',
                        '</tpl>',
                        '<div class="x-clear"></div>'
                    ),
                    autoHeight:true,
                    multiSelect:false,
                    singleSelect:true,
                    overClass:'x-view-over',
                    itemSelector:'div.thumb-wrap',
                    emptyText: 'No images to display'
                }
            ]
        });

        mymdb.movie.flow.PosterListPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-poster-posterlist',mymdb.movie.flow.PosterListPanel);