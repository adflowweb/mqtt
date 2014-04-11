
/* JavaScript content from js/toDo.js in folder common */
$("#btnSubmitTask").bind("click", addTaskToList);

function addTaskToList(){
	
	var title = $("#taskTitle").val();
	var cat = $("#taskCat").val();
	var due = $("#taskDue").val();
	var desc = $("#taskDesc").val();
	
	if(!(title.length > 0 && desc.length > 0 && cat.length > 0 && due.length > 0)){
		alert("각 항목의 값을 넣어주세요");
	}else{
	
		var htmlStr = $("#lvTodo").html();
	
		htmlStr = 
			"<li data-icon='false'><a href='#'><h3>" + title + "</h3>" +
			"<p><strong>" + cat + "</strong></p>" +
			"<p class='ui-li-aside'>" + due + "</p>" +
			"<p>" + desc + "</p></a>" +
			"</li>" +
			htmlStr;
	
		$("#lvTodo").html(htmlStr);
		$("#lvTodo").listview("refresh");
		
		$("#taskTitle").val("");
		$("#taskCat").val("");
		$("#taskDue").val("");
		$("#taskDesc").val("");
		
		window.history.back();
	}
}