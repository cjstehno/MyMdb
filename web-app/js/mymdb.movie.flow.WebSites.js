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
                    handler:function(b){
                        var store = this.findByType('listview')[0].getStore();

                        var WebSiteRecord = Ext.data.Record.create([ 'label','url' ]);

                        store.add([ new WebSiteRecord({ label:'Foo', url:'http://foo.com' }) ])
                    }
                }
            ]
        });

        mymdb.movie.flow.WebSitesListPanel.superclass.initComponent.apply(this, arguments);
    },
    reload:function(){
        this.findByType('listview')[0].getStore().load();
    }
});
Ext.reg('movieflow-websites-listpanel', mymdb.movie.flow.WebSitesListPanel);



