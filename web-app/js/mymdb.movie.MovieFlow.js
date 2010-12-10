
mymdb.movie.MovieManagerFlowPanel = Ext.extend( Ext.Panel, {
	layout:'card',
	activeItem:0,
    initComponent: function(){
        Ext.apply(this, {
        	items:[
        	       {xtype:'movieflow-entertile'},
        	       {xtype:'movieflow-fetchresults'},
        	       {xtype:'movieflow-finish'}
        	]        	
        });
        
        mymdb.movie.MovieManagerFlowPanel.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-panel', mymdb.movie.MovieManagerFlowPanel);

mymdb.movie.EnterTitleView = Ext.extend(Ext.form.FormPanel, {
    initComponent: function(){
        Ext.apply(this, {
            formUrl:'movie/enterTitle',
        	items:[
                {
                    xtype:'textfield',
                    fieldLabel:'Title',
                    name:'title'
                },
                { xtype:'movieflow-nextbutton', nextId:1 }
    		]        	
        });
        
        this.on('activate',mymdb.movie.FlowViewInitFunction);        
        
        mymdb.movie.EnterTitleView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-entertile', mymdb.movie.EnterTitleView);

mymdb.movie.FetchResultsView = Ext.extend(Ext.form.FormPanel, {
    initComponent: function(){
        Ext.apply(this, {
            formUrl:'movie/fetchResults',
        	items:[
    		      {
    		    	  xtype:'textfield',
    		    	  fieldLabel:'Title',
    		    	  name:'title',
    		    	  readOnly:true
    		      },
                { xtype:'movieflow-nextbutton', nextId:2, text:'Finish' },
                { xtype:'movieflow-nextbutton', nextId:0, text:'Previous' },
    		]        	
        });
        
        this.on('activate',mymdb.movie.FlowViewInitFunction);           
        
        mymdb.movie.FetchResultsView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-fetchresults', mymdb.movie.FetchResultsView);

mymdb.movie.FinishView = Ext.extend(Ext.form.FormPanel, {
    initComponent: function(){
        Ext.apply(this, {
            formUrl:'movie/finish',
            items:[
                  {
                      xtype:'textfield',
                      fieldLabel:'Title',
                      name:'title',
                      readOnly:true
                  },
                {
                    xtype:'button',
                    text:'Close',
                    handler:function(b,e){
                        b.findParentByType('window',false).close();
                    }
                }
            ]           
        });
        
        this.on('activate',mymdb.movie.FlowViewInitFunction);            
        
        mymdb.movie.FinishView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-finish', mymdb.movie.FinishView);

mymdb.movie.FlowNextButton = Ext.extend(Ext.Button, {
    text:'Next',
    handler:function(b,e){
        var formPanel = b.findParentByType('form');
        formPanel.getForm().submit({
            method:'POST', url:formPanel.formUrl,
            success:function(){
                formPanel.findParentByType(mymdb.movie.MovieManagerFlowPanel).getLayout().setActiveItem(b.nextId);
            },
            failure:function(){
                Ext.Msg.alert('Error','Unable to submit form');
            }
        });
    }
});
Ext.reg('movieflow-nextbutton', mymdb.movie.FlowNextButton);

mymdb.movie.FlowViewInitFunction = function(pnl){
    pnl.load({ method:'GET', url:pnl.formUrl });
};
