
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
                    url:'/mymdb/poster/flow',
                    style:'margin:4px;',
                    border:true,
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
                    handler:function(btn){
                        // TODO: do ajax request to fetch image and stuff in DTO, then update image panle
//                        url:'' - stores in flow scope
//                        on success update image panel
                        // note: you dont have to fetch, next will also download and save as before (check for existing)

                        var urlText = btn.previousSibling().getValue();

                        Ext.Ajax.request({
                            url:'movie/fetchPosterUrl',
                            params: { url:urlText },
                            success:function(){
                                btn.findParentByType('movieflow-poster').findByType('mymdb-imagepanel').reload();
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
                    buttonText:'Select...'
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
            mymdb.movie.flow.DisableButtonFunction(p);
            mymdb.movie.flow.UpdateDialogTitleFunction(p, 'New Movie: Poster');
        });

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
                { xtype:'button', text:this.buttonText, height:23, flex:1, handler:this.handler }
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

//mymdb.ImagePanel = Ext.extend(Ext.Panel, {
//    autoLoad:false,
//    tpl:'<img src="/mymdb/poster/flow" />',
//    initComponent: function(){
//        Ext.apply(this, {});
//
//        mymdb.ImagePanel.superclass.initComponent.apply(this, arguments);
//    },
//    reload:function(){
//        this.load({
//            url:this.url,
//            nocache: false,
//            text:'Loading...',
//            timeout:30,
//            scripts:false
//        });
//    }
//});
//Ext.reg('mymdb-imagepanel', mymdb.ImagePanel);

Ext.ux.Image = Ext.extend(Ext.BoxComponent, {
    url  : Ext.BLANK_IMAGE_URL,  //for initial src value

    autoEl: {
        tag: 'img',
        src: Ext.BLANK_IMAGE_URL,
        cls: 'tng-managed-image'
    },

    initComponent : function(){
        Ext.ux.Image.superclass.initComponent.call(this);
        this.addEvents('load');
    },

    onRender: function() {
        Ext.ux.Image.superclass.onRender.apply(this, arguments);
        this.el.on('load', this.onLoad, this);
        if(this.url){
            this.setSrc(this.url);
        }
    },

    onLoad: function() {
        this.fireEvent('load', this);
    },

    setSrc: function(src) {
        this.el.dom.src = src;
    },

    reload:function(){
        onLoad();
    }
});
Ext.reg('mymdb-imagepanel', Ext.ux.Image);

