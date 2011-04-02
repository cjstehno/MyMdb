<html>
    <head>
    <meta name='layout' content='security' />
    <title>Login</title>
    <style type="text/css">
        img.bg {
            position:absolute;
            top:0px;
            left:0px;
            width:100%;
            height:100%;
            z-index:0;
        }

        div.title {
            position:absolute;
            top:100px;
            left:0px;
            text-align:center;
            width:100%;
            z-index:1000;
            font-family:sans-serif;
            font-size:30pt;
        }
    </style>
    <script type="text/javascript">
        Ext.QuickTips.init();
        Ext.ns('mymdb.security');

        mymdb.security.LoginDialog = Ext.extend( Ext.Window, {
            title:'Please Login',
            width:300,
            height:155,
            modal:true,
            layout:'fit',
            resizable:false,
            initComponent: function(){
                Ext.apply(this, {
                    items:[
                        {
                            xtype:'form',
                            url:'signIn',
                            method:'POST',
                            padding:8,
                            items:[
                                {
                                    xtype:'textfield',
                                    fieldLabel:'Username',
                                    name:'username',
                                    value:'${username}',
                                    allowBlank:false,
                                    minLength:5,
                                    maxLength:25
                                },
                                {
                                    xtype:'textfield',
                                    inputType:'password',
                                    fieldLabel:'Password',
                                    name:'password',
                                    allowBlank:false,
                                    minLength:5,
                                    maxLength:25,
                                    listeners:{
                                        specialkey:{
                                            scope:this,
                                            fn:function( field, e ){
                                                if(e.getKey() == 13) this.submitLogin();
                                            }
                                        }
                                    }
                                },
                                { xtype:'checkbox', fieldLabel:'Remember me', name:'rememberMe' }
                            ]
                        }
                    ],
                    buttons:[
                        {
                            text:'Login',
                            scope:this,
                            handler:function(){
                                this.submitLogin();
                            }
                        }
                    ]
                });

                mymdb.security.LoginDialog.superclass.initComponent.apply(this, arguments);
            },
            submitLogin:function(){
                var theForm = this.findByType('form')[0].getForm();
                theForm.submit({
                    success: function(form, action) {
                        window.location = '${request.contextPath}/browser';
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
                                Ext.Msg.show({
                                   title:'Login Failure',
                                   msg: action.result.errors.general,
                                   buttons: Ext.Msg.OK,
                                   icon: Ext.MessageBox.ERROR
                                });
                                break;
                       }
                    }
                });
            }
        });

        Ext.onReady(function(){
            new mymdb.security.LoginDialog().show();
        });
    </script>
</head>
<body><img src="${request.contextPath}/images/loginbg.png" class="bg" /><div class="title">My Movie Database</div></body>
</html>

