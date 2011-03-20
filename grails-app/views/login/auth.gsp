<head>
    <meta name='layout' content='security' />
    <title>Login</title>
    <script type="text/javascript">
        Ext.QuickTips.init();
        Ext.ns('mymdb.security');

        mymdb.security.LoginDialog = Ext.extend( Ext.Window, {
            title:'Please Login',
            width:300,
            height:155,
            modal:true,
            layout:'fit',
            initComponent: function(){
                Ext.apply(this, {
                    items:[
                        {
                            xtype:'form',
                            url:'${request.contextPath}/j_spring_security_check',
                            method:'POST',
                            padding:8,
                            items:[
                                { xtype:'textfield', fieldLabel:'Username', name:'j_username', allowBlank:false, minLength:5, maxLength:25 },
                                { xtype:'textfield', inputType:'password', fieldLabel:'Password', name:'j_password', allowBlank:false, minLength:5, maxLength:25 },
                                { xtype:'checkbox', fieldLabel:'Remember me', name:'_spring_security_remember_me'<g:if test='${hasCookie}'>, checked:true</g:if> }
                            ]
                        }
                    ],
                    buttons:[
                        {
                            text:'Login',
                            scope:this,
                            handler:function(){
                                this.findByType('form')[0].getForm().submit({
                                    success: function(form, action) {
                                        if(action.result.errors != undefined){
                                            Ext.Msg.alert('Failure', action.result.errors.global);
                                        }
                                    },
                                    failure: function(form, action) {
                                        switch (action.failureType) {
                                            case Ext.form.Action.CLIENT_INVALID:
                                                Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
                                                break;
                                            case Ext.form.Action.CONNECT_FAILURE:
                                                Ext.Msg.alert('Failure', 'Ajax communication failed');
                                                break;
                                            default:
                                               break;
                                       }
                                    }
                                });
                            }
                        }
                    ]
                });

                mymdb.security.LoginDialog.superclass.initComponent.apply(this, arguments);
            }
        });

        Ext.onReady(function(){
            new mymdb.security.LoginDialog().show();
        });
    </script>
</head>
<body>

<h1>My Movie Database</h1>

</body>
