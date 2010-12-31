
mymdb.movie.flow.PosterView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/poster',
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
                    xtype:'label',
                    text:'Select movie poster to use:',
                    colspan:3
                },
                {
                    xtype: 'radio',
                    hideLabel:true,
                    boxLabel:'URL:',
                    checked:true,
                    width:75,
                    handler:function(rdo, checked){
                        if(checked){
                            // enable textfield
                            // deselect other radios
                        } else {
                            // disable textfield
                        }
                    }
                },
                {
                    xtype:'textfield',
                    hideLabel:true,
                    value:'http://',
                    height:25,
                    width:300
                },
                {
                    xtype:'label', text:'poster image', rowspan:2, width:125
                },
                {
                    xtype: 'radio',
                    hideLabel:true,
                    boxLabel:'File:',
                    width:75,
                    handler:function(rdo, checked){
                        if(checked){
                            // enable file
                            // deselect other radios
                        } else {
                            // disable file
                        }
                    }
                },
                {
                    xtype:'textfield',
                    inputType:'file',
                    hideLabel:true,
                    height:25,
                    width:275,
                    disabled:true
                },
                {
                    xtype: 'radio',
                    hideLabel:true,
                    boxLabel:'Existing:',
                    width:75,
                    handler:function(rdo, checked){
                        if(checked){
                            // enable selector
                            // deselect other radios
                        } else {
                            // disable selector
                        }
                    },
                    disabled:true
                },
                {
                    xtype:'button', 
                    text:'Select...',
                    colspan:2,
                    height:25,
                    disabled:true
                },
                {
                    xtype: 'radio',
                    hideLabel:true,
                    boxLabel:'None',
                    colspan:3,
                    width:75,
                    handler:function(rdo, checked){
                        if(checked){
                            // deselect other radios
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
    }
});
Ext.reg('movieflow-poster', mymdb.movie.flow.PosterView);