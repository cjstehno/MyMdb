<html>
    <head>
    <meta name='layout' content='mobile' />
    <title>Login</title>
    </head>
    <body>
        <h1>MyMdb Login</h1>

        <g:form controller="auth" action="signIn">
            <div><label for="username">Username: <g:textField name="username" /></label></div>
            <div><label for="password">Password: <g:passwordField name="password" /></label></div>
            <g:submitButton name="Login" value="login" />
        </g:form>

    </body>
</html>

