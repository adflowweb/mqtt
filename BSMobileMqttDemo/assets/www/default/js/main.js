/* JavaScript content from js/main.js in folder common */
function wlCommonInit() {

	/*
	 * Application is started in offline mode as defined by a connectOnStartup
	 * property in initOptions.js file. In order to begin communicating with
	 * Worklight Server you need to either:
	 * 
	 * 1. Change connectOnStartup property in initOptions.js to true. This will
	 * make Worklight framework automatically attempt to connect to Worklight
	 * Server as a part of application start-up. Keep in mind - this may
	 * increase application start-up time.
	 * 
	 * 2. Use WL.Client.connect() API once connectivity to a Worklight Server is
	 * required. This API needs to be called only once, before any other
	 * WL.Client methods that communicate with the Worklight Server. Don't
	 * forget to specify and implement onSuccess and onFailure callback
	 * functions for WL.Client.connect(), e.g:
	 * 
	 * WL.Client.connect({ onSuccess: onConnectSuccess, onFailure:
	 * onConnectFailure });
	 * 
	 */

	// Common initialization code goes here
}

function findEmployee() {

	var serialNumber = $("#inEmpLU").val();

	var invocationData = {
		adapter : 'MySQLAdapter',
		procedure : 'findEmployee',
		parameters : [ serialNumber ]
	};

	var options = {
		onSuccess : displayResult,
		onFailure : displayError,
		invocationContext : {}
	};

	WL.Client.invokeProcedure(invocationData, options);
}

function displayResult(result) {
	WL.Logger.debug("SQL Success");

	var formattedResult = "";
	var item = result.invocationResult.resultSet;

	if (item.length > 0) {
		formattedResult = formatResult(item);
	}

	$('#empResultDiv').html(formattedResult);

	$.mobile.changePage("#empResult", {
		role : "page",
		transition : "slide"
	});

}

function displayError(result) {
	WL.Logger.debug("SQL Failure");
}

function formatResult(item) {
	var htmlStr = "<fieldset class='container_16'>"
			+ "<div class='grid_5 picCentered'>"
			+ "<img width='80px' height='96px' src='data:image/jpeg;base64,"
			+ item[0].PHOTO + "'/>" + "</div>" + "<div class='grid_11'>"
			+ "<h1>" + item[0].LNAME + item[0].FNAME + "</h1>"
			+ "<p class='displayedInfo'>" + "<b>영업점: </b>" + item[0].LOCTN
			+ "<br/><b>내선번호: </b>" + item[0].EXT + "<br/><b>휴대전화: </b>"
			+ item[0].MPHONE + "<br/><b>이메일: </b>" + item[0].EMAIL + "</p>"
			+ "</div>" + "</fieldset>";
	return htmlStr;
}

function getFeeds() {

	var invocationData = {
		adapter : 'HttpFeedAdapter',
		procedure : 'getFeeds',
		parameters : []
	};

	var options = {
		onSuccess : loadFeedSuccess,
		onFailure : loadFeedFailure,
		invocationContext : {}
	};

	WL.Client.invokeProcedure(invocationData, options);

}

function loadFeedSuccess(result) {
	WL.Logger.debug("HTTP Success");

	var formattedFeed = "";
	var feed = result.invocationResult.rss.channel.item;

	if (feed.length > 0) {
		formattedFeed = formatFeed(feed);
	}

	$('#newsFeedDiv').html(formattedFeed);
	$('#feedListView').listview();

	// $.mobile.changePage( "#newsFeed", { role: "page", transition: "slide"} );
}

function loadFeedFailure(result) {
	WL.Logger.debug("HTTP Failure");
}

/*
 * <li><a href="index.html"> <h2>jQuery Team</h2> <p><strong>Boston
 * Conference Planning</strong></p> <p>In preparation for the upcoming
 * conference in Boston, we need to start gathering a list of sponsors and
 * speakers.</p> <p class="ui-li-aside"><strong>9:18</strong>AM</p> </a></li>
 */

function formatFeed(feed) {
	var htmlStr = "<ul id='feedListView' data-role='listview' data-inset='true'>";
	var i = 0;

	while (i < feed.length) {
		htmlStr = htmlStr + "<li><a href='" + feed[i].link + "'>" + "<h2>"
				+ feed[i].title + "</h2>" + "<p><strong>작성: " + feed[i].creator
				+ "</strong></p>" + "<p>" + feed[i].pubDate + "</p>"
				+ "</a></li>";
		i = i + 1;
	}
	return htmlStr;
}


$("#newsGuide").click(function(event) {
	event.preventDefault();
	getFeeds();
});
/* JavaScript content from js/main.js in folder android */
// This method is invoked after loading the main HTML and successful
// initialization of the Worklight runtime.
function wlEnvInit() {
	wlCommonInit();
	// Environment initialization code goes here
}

function loadEducation(image) {
	var content = '<div data-role="navbar" data-iconpos="left">'
			+ '<ul>'
			+ '<li><a href="#" data-icon="location"'
			+ ' class="ui-btn-active ui-state-persist">지도</a></li>'
			+ ' <li><a href="#" data-icon="calendar">일정</a></li>'
			+ ' <li><a href="#" data-icon="user">문의</a></li>'
			+ ' </ul>'
			+ '</div>'
			+ '<div id="educationDiv"><img src="data:image/jpg;base64,'
			+ image
			+ '" alt="British Blog Directory" width="100%" height="100%" /></div>';

	$('#educationDetail').html(content).trigger("create");
	$.mobile.changePage('#education', {
		transition : 'slide'
	});
}