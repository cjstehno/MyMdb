
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
                {
                    xtype: 'radio',
                    name:'posterType',
                    inputValue:'URL',
                    hideLabel:true,
                    boxLabel:'URL:',
                    checked:true,
                    width:75,
                    handler:function(rdo, checked){
                        var ct = rdo.ownerCt;
                        var urlTxt = ct.getComponent('url-text');
                        if(checked){
                            urlTxt.enable();
                            ct.clearOtherRadios(rdo);
                        } else {
                            urlTxt.disable();
                        }
                    }
                },
                {
                    xtype:'textfield',
                    itemId:'url-text',
                    name:'url',
                    hideLabel:true,
                    value:'http://',
                    height:25,
                    width:400
                },
                {
                    xtype: 'radio',
                    name:'posterType',
                    inputValue:'FILE',
                    hideLabel:true,
                    boxLabel:'File:',
                    width:75,
                    handler:function(rdo, checked){
                        var ct = rdo.ownerCt;
                        var fileFld = ct.getComponent('file-text');
                        if(checked){
                            fileFld.enable();
                            ct.clearOtherRadios(rdo);
                        } else {
                            fileFld.disable();
                        }
                    }
                },
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
                {
                    xtype: 'radio',
                    name:'posterType',
                    inputValue:'EXISTING',
                    hideLabel:true,
                    boxLabel:'Existing:',
                    width:75,
                    handler:function(rdo, checked){
                        var ct = rdo.ownerCt;
                        if(checked){
                            // enable selector
                            ct.clearOtherRadios(rdo);
                        } else {
                            // disable selector
                        }
                    }
                },
                {
                    xtype:'button', 
                    text:'Select...',
                    height:25,
                    disabled:true
                },
                {
                    xtype: 'radio',
                    name:'posterType',
                    inputValue:'NONE',
                    hideLabel:true,
                    boxLabel:'None',
                    colspan:2,
                    width:75,
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
        alert('reloading image');
    }
});
Ext.reg('mymdb-imagepanel', Ext.ux.Image);
