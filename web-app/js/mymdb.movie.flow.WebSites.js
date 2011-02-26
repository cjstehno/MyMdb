/*
 * Copyright (c) 2011 Christopher J. Stehno (chris@stehno.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


mymdb.movie.flow.WebSitesView = Ext.extend(mymdb.movie.flow.ViewPanel, {
    formUrl:'movie/websites',
    nextId:6,
    previousId:4,
    initComponent: function(){
        Ext.apply(this, {
            items:[
                { xtype:'label', text:'Select web sites for movie:' },
                {
                    xtype:'movieflow-websites-listpanel'
                }
            ]
        });

        this.on('activate',function(p){
            this.disableNavButtons( [] );
            this.setDialogTitle('New Movie: Web Sites');

            this.findByType('movieflow-websites-listpanel')[0].reload();
        },this);

        mymdb.movie.flow.WebSitesView.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-websites', mymdb.movie.flow.WebSitesView);

mymdb.movie.flow.WebSitesListPanel = Ext.extend(Ext.Panel, {
    layout:'fit',
    height:425,
    buttonAlign:'left',
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'listview',
                    singleSelect:true,
                    columns:[ { header:'Name', dataIndex:'label' }, { header:'Url', dataIndex:'url' } ],
                    listeners:{
                        contextmenu:function( dataView, idx, node, evt ){
                            new mymdb.movie.flow.WebSiteListContextMenu({target:dataView.ownerCt}).showAt(evt.getXY());
                        }
                    },
                    store:new Ext.data.JsonStore({
                        url: 'movie/websites/sites',
                        root: 'sites',
                        fields: [
                            'label', 'url'
                        ],
                        writer:new Ext.data.JsonWriter({
                            encode:true,
                            writeAllFields:true
                        })
                    })
                }
            ],
            buttons:[
                {
                    text:'Add Site',
                    iconCls:'icon-add-site',
                    scope:this,
                    handler:function(){this.addSite();}
                }
            ]
        });

        mymdb.movie.flow.WebSitesListPanel.superclass.initComponent.apply(this, arguments);
    },
    reload:function(){
        this.findByType('listview')[0].getStore().load();
    },
    addSite:function(){
        var store = this.findByType('listview')[0].getStore();

        new mymdb.movie.flow.WebSiteDialog({ onSave:function(lbl,url){
            var WebSiteRecord = Ext.data.Record.create([ 'label','url' ]);
            store.add([ new WebSiteRecord({ label:lbl, url:url }) ])
        } }).show();
    },
    editSite:function(){
        var listView = this.findByType('listview')[0];
        if( listView.getSelectionCount() > 0 ){
            var rec = listView.getSelectedRecords()[0];

            new mymdb.movie.flow.WebSiteDialog({
                siteData:{ label:rec.data.label, url:rec.data.url },
                onSave:function(lbl,url){
                    rec.beginEdit();
                    rec.set('label',lbl);
                    rec.set('url',url);
                    rec.endEdit();
                }
            }).show();
        }
    },
    deleteSite:function(){
        var listView = this.findByType('listview')[0];
        if( listView.getSelectionCount() > 0 ){
            var rec = listView.getSelectedRecords()[0];

            Ext.MessageBox.confirm('Delete Web Site','Are you sure you want to delete this Web Site?',function(btn){
                if(btn == 'Yes'){
                    listView.getStore().remove(rec);
                }
            }, this);
        }
    }
});
Ext.reg('movieflow-websites-listpanel', mymdb.movie.flow.WebSitesListPanel);


/**
 * Context menu for web site manager.
 */
mymdb.movie.flow.WebSiteListContextMenu = Ext.extend(Ext.menu.Menu, {
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'menuitem',
                    text:'Edit',
                    iconCls:'icon-edit-site',
                    scope:this.target,
                    handler:function(b){ this.editSite(); }
                },
                {
                    text:'Add Site',
                    iconCls:'icon-add-site',
                    scope:this.target,
                    handler:function(){ this.addSite(); }
                },
                {
                    xtype:'menuitem',
                    text:'Delete',
                    iconCls:'icon-delete-site',
                    scope:this.target,
                    handler:function(b){ this.deleteSite(); }
                }
            ]
        });

        mymdb.movie.flow.WebSiteListContextMenu.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-websites-listpanel-menu',mymdb.movie.flow.WebSiteListContextMenu);


mymdb.movie.flow.WebSiteDialog = Ext.extend(Ext.Window, {
    title:'Web Site',
    width:300,
    height:125,
    layout:'fit',
    siteData:{ label:'', url:'' },
    initComponent: function(){
        Ext.apply(this, {
            items:[
                {
                    xtype:'form',
                    labelWidth:50,
                    padding:4,
                    items:[
                        { xtype:'textfield', fieldLabel:'Label', name:'label', anchor:'100%', value:this.siteData.label },
                        { xtype:'textfield', fieldLabel:'Url', name:'url', anchor:'100%', value:this.siteData.url }
                    ]
                }
            ],
            buttons:[
                {
                    text:'Save',
                    scope:this,
                    handler:function(){
                        var form = this.findByType('form')[0].getForm();

                        var labelValue = form.findField('label').getValue();

                        var urlValue = form.findField('url').getValue();
                        if(urlValue.indexOf('http') == -1){
                            urlValue = 'http://' + urlValue;
                        }

                        this.onSave( labelValue, urlValue );
                        this.close();
                    }
                },
                { text:'Cancel', scope:this, handler:function(b){ this.close(); } }
            ]
        });

        mymdb.movie.flow.WebSiteDialog.superclass.initComponent.apply(this, arguments);
    }
});
Ext.reg('movieflow-website-dialog',mymdb.movie.flow.WebSiteDialog);

