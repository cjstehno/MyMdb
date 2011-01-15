
mymdb.movie.flow.PosterView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/poster',
    fileUpload:true,
    nextId:3,
    previousId:1,
    layout:'table',
    layoutConfig:{
        columns: 3
    },
    defaults:{
        height:30
    },
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'panel',
                    colspan:3,
                    html:'Select movie poster to use:',
                    border:false
                },
                {
                    xtype:'mymdb-imagepanel',
                    id:'movieflow-poster',
                    url:'/mymdb/poster/flow',
                    style:'margin:4px;',
                    border:false,
                    width:110,
                    height:160,
                    rowspan:4
                },
                { xtype: 'movieflow-poster-radio', boxLabel:'URL:', inputValue:'URL', targetItemId:'url-text', checked:true },
                {
                    xtype:'movieflow-poster-fetch',
                    itemId:'url-text',
                    textName:'url',
                    textValue:'http://',
                    buttonText:'Fetch',
                    buttonHandler:function(b,e){
                        var urlText = b.previousSibling().getValue();

                        // TODO: figure out why this doesnt work
//                        var imagePanel = b.findParentByType('form').findByType('mymdb-imagepanel')[0];
                        var imagePanel = Ext.getCmp('movieflow-poster');

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
                { xtype: 'movieflow-poster-radio', boxLabel:'File:', inputValue:'FILE', targetItemId:'file-text' },
                {
                    xtype:'textfield',
                    itemId:'file-text',
                    inputType:'file',
                    name:'file',
                    hideLabel:true,
                    height:25,
                    width:400,
                    disabled:true
                },
                { xtype: 'movieflow-poster-radio', boxLabel:'Existing:', inputValue:'EXISTING', targetItemId:'existing-field' },
                {
                    xtype:'movieflow-poster-fetch',
                    itemId:'existing-field',
                    disabled:true,
                    textName:'title',
                    buttonText:'Select...',
                    buttonHandler:function(b,e){
//                        var urlText = b.previousSibling().getValue();
//
//                        // TODO: figure out why this doesnt work
////                        var imagePanel = b.findParentByType('form').findByType('mymdb-imagepanel')[0];
//                        var imagePanel = Ext.getCmp('movieflow-poster');
//
//                        Ext.Ajax.request({
//                            url:'movie/fetchPosterUrl',
//                            params: { url:urlText },
//                            success:function(){
//                                imagePanel.reload();
//                            },
//                            failure:function(){
//                            }
//                        });
                    }
                },
                {
                    xtype:'movieflow-poster-radio',
                    boxLabel:'None:',
                    inputValue:'NONE',
                    colspan:2,
                    handler:function(rdo, checked){
                        if(checked){
                            rdo.ownerCt.clearOtherRadios(rdo);
                        }
                    }                    
                }
            ]
        });

        this.on('activate',function(p){
            this.disableNavButtons( [] );
            this.setDialogTitle('New Movie: Poster');
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


mymdb.movie.flow.PosterFetchField = Ext.extend(Ext.form.CompositeField, {
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'textfield', name:this.textName, hideLabel:true, value:this.textValue, height:25, width:325 },
                {
                    xtype:'button',
                    text:this.buttonText,
                    height:23,
                    flex:1,
                    handler:this.buttonHandler
                }
            ]
        });

        mymdb.movie.flow.PosterFetchField.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-poster-fetch',mymdb.movie.flow.PosterFetchField);


mymdb.movie.flow.PosterTypeRadio = Ext.extend(Ext.form.Radio, {
    name:'posterType',
    hideLabel:true,
    width:75,
    handler:function(rdo, checked){
        var ct = rdo.ownerCt;
        var item = ct.getComponent(rdo.targetItemId);
        if(checked){
            item.enable();
            ct.clearOtherRadios(rdo);
        } else {
            item.disable();
        }
    }
});
Ext.reg('movieflow-poster-radio',mymdb.movie.flow.PosterTypeRadio);

mymdb.ImagePanel = Ext.extend(Ext.Panel, {
    tpl:'<img src="{src}" width="{w}" height="{h}" />',
    initComponent: function(){
        Ext.apply(this, {
            data:{ src:this.url + '?' + Math.random(), w:this.width, h:this.height }
        });

        mymdb.ImagePanel.superclass.initComponent.apply(this, arguments);
    },
    reload:function(){
        // TODO: see if there is a better way of not caching the urls
        // if nothing else, I should use unix date long value instead of random
        var dat = { src:this.url + '?' + Math.random(), w:this.width, h:this.height };
        this.update( dat );
    }
});
Ext.reg('mymdb-imagepanel', mymdb.ImagePanel);


