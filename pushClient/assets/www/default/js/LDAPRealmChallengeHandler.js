
/* JavaScript content from js/LDAPRealmChallengeHandler.js in folder common */
var realmName = "LDAPRealm";
var LDAPRealmChallengeHandler = WL.Client.createChallengeHandler(realmName);

LDAPRealmChallengeHandler.isCustomResponse = function(response) {
    if (!response || !response.responseText) {
        return false;
    }
    
    var idx = response.responseText.indexOf("j_security_check");
    
    if (idx >= 0){ 
    	return true;
    }
    
    return false;
};

LDAPRealmChallengeHandler.handleChallenge = function(response) {
		$("#popupLogin").popup("open", {transition: "turn"});
		$("#passwordInputField").val("");
		$("#usernameInputField").val("");
};

$("#loginButton").bind("click", function () {
	loginName = $("#usernameInputField").val();
    var reqURL = "/j_security_check";
    var options = {};
    options.parameters = {
    		j_username : $("#usernameInputField").val(),
    		j_password : $("#passwordInputField").val()
    };
    options.headers = {};
    LDAPRealmChallengeHandler.submitLoginForm(reqURL, options, LDAPRealmChallengeHandler.submitLoginFormCallback);
});

$("#cancelButton").bind("click", function () {
	$("#popupLogin").popup("close");
	LDAPRealmChallengeHandler.submitFailure();
});

LDAPRealmChallengeHandler.submitLoginFormCallback = function(response) {
    var isLoginFormResponse = LDAPRealmChallengeHandler.isCustomResponse(response);
    if (isLoginFormResponse){
    	LDAPRealmChallengeHandler.handleChallenge(response);
    } else {
    	$("#popupLogin").popup("close");
    	LDAPRealmChallengeHandler.submitSuccess();
    }
};