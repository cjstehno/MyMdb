
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

                { xtype: 'movieflow-poster-radio', boxLabel:'URL:', inputValue:'URL', targetItemIds:['url-text','url-button'], checked:true },
                { xtype:'textfield', itemId:'url-text', name:'url', value:'http://', width:325 },
                {
                    xtype:'button',
                    itemId:'url-button',
                    text:'Fetch',
                    width:70,
                    handler:function(b,e){
                        var urlText = b.previousSibling().getValue();
                        var imagePanel = b.findParentByType('movieflow-poster').findByType('mymdb-imagepanel')[0];

                        Ext.Ajax.request({
                            url:'movie/fetchPosterUrl',
                            params: { url:urlText },
                            success:function(){
                                imagePanel.reload();
                            },
                            failure:function(){
                            }
                        });
                    }
                },

                { xtype: 'movieflow-poster-radio', boxLabel:'File:', inputValue:'FILE', targetItemIds:['file-text'] },
                { xtype:'textfield', itemId:'file-text', inputType:'file', name:'file', hideLabel:true, height:25, width:400, disabled:true, colspan:2 },

                { xtype: 'movieflow-poster-radio', boxLabel:'Existing:', inputValue:'EXISTING', targetItemIds:['existing-text','existing-button'] },
                { xtype:'textfield', itemId:'existing-text', name:'existing', width:325, disabled:true },
                {
                    xtype:'button',
                    itemId:'existing-button',
                    text:'Select...',
                    width:70,
                    disabled:true,
                    handler:function(b,e){
                        new mymdb.movie.flow.PosterSelector().setVisible(true);
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
                        }
                    }                    
                }
            ]
        });

        this.on('activate',function(p){
            this.disableNavButtons( ['finish-btn'] );
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
    buttons:[
        {
            xtype:'button',
            text:'Cancel'
        },
        {
            xtype:'button',
            text:'OK'
        }
    ],
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'panel',
                    height:330,
                    autoScroll:true,
                    items:[
                        {
                            xtype:'dataview',
//                            width:150,
                            store: new Ext.data.JsonStore({
                                autoLoad:true,
                                url:'poster/list',
                                root: 'posters',
                                fields: ['name', 'id']
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
                }
            ]
        });

        mymdb.movie.flow.PosterSelector.superclass.initComponent.apply(this, arguments);
    }
});
