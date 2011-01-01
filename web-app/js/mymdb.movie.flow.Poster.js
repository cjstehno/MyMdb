
mymdb.movie.flow.PosterView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/poster',
    fileUpload:true,
    nextId:3,
    previousId:1,
    layout:'table',
    layoutConfig:{
        columns: 2
    },
    defaults:{
        height:30
    },
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'panel',
                    padding:4,
                    html:'<div>Select movie poster to use:</div><div style="margin-top:4px;"><img src="/mymdb/images/noposter.jpg" width="80" height="122"/></div>',
                    border:false,
                    height:150,
                    colspan:2
                },
                {
                    xtype: 'radio',
                    name:'posterType',
                    inputValue:'url',
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
                    width:525
                },
                {
                    xtype: 'radio',
                    name:'posterType',
                    inputValue:'file',
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
                    inputValue:'existing',
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
                    },
                    disabled:true
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
                    inputValue:'none',
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