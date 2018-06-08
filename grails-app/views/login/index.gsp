<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Login</title>
</head>
<body>
<div class="login-form">
    <form method="POST" action="${resource(file: '/login/authenticate')}">
        <h4 class="login-title">Member Login</h4><br>
        <div class="form-group">
            <i class="fa fa-user"></i>
            <input name="username" type="text" class="form-control" placeholder="Username" required="true">
        </div>
        <div class="form-group">
            <i class="fa fa-lock"></i>
            <input name="password" type="password" class="form-control" placeholder="Password" required="true">
        </div>
        <div class="form-group">
            <button type='submit' class="btn btn-primary btn-block">Log in</button>
        </div>
        <p class="text-center"><a href="#registerModal" data-toggle="modal">Create an Account</a></p>
        <div id="loginFormMsg">${loginMsg}</div>
    </form>
</div>

<!-- Register User Modal -->
<div id="registerModal" class="modal fade">
    <div class="modal-dialog modal-register">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Register User</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <g:form method="POST" url="[action:'saveUser', controller:'login']" id="saveUserForm">
                    <div class="form-group">
                        <i class="fa fa-user"></i>
                        <input name="registerUsername" type="text" class="form-control" placeholder="Enter Username" required="true">
                    </div>
                    <div class="form-group">
                        <i class="fa fa-lock"></i>
                        <input name="registerPassword" type="password" class="form-control" placeholder="Enter Password" required="true">
                    </div>
                    <div class="form-group">
                        <input id="saveUser" type="submit" class="btn btn-primary btn-block btn-lg" value="Register">
                    </div>
                </g:form>

            </div>
        </div>
    </div>
</div>
</body>
</html>