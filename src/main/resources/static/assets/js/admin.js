function showEditAuthor(authorId) {

	var url = "/editAuthor?id=" + authorId;

	var totalCartItems = $("#totalCartItems").text();

	$.ajax({
		url: url,
		type: "GET",
		dataType: "json",
		success: function(result) {
			$("#totalCartItems").text("");
			totalCartItems = parseInt(totalCartItems) + 1;
			$("#totalCartItems").text(totalCartItems);

		},
		error: function(err) {
			// check the err for error details
		}
	}); // ajax call

}
