function addCart(bookId) {

	var url = "/addCart?id=" + bookId;

	var totalCartItems = $("#totalCartItems").text();

	$.ajax({
		url: url,
		type: "GET",
		dataType: "json",
		data: 'json',
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

function doFavorite(index) {

	var url = null;
	var totalFavorite = $("#totalFavorite").text();

	if ($('.tg-btnaddtowishlist' + '.' + index + '.hiden-flagFavorite').hasClass("bookmark")) {
		url = "/doUnFavorite?id=" + index;
		$('.tg-btnaddtowishlist' + '.' + index).removeClass('st-hiden');
		$('.tg-btnaddtowishlist' + '.' + index + '.hiden-flagFavorite').addClass('st-hiden');
		$('.tg-btnaddtowishlist' + '.' + index + '.hiden-flagFavorite').removeClass('bookmark');
		$('.tg-btnaddtowishlist' + '.' + index + '.do-flagFavorite').removeClass('st-hiden');
		$("#totalFavorite").text("");
		totalFavorite = parseInt(totalFavorite) - 1;
		$("#totalFavorite").text(totalFavorite);

	} else {
		url = "/doFavorite?id=" + index;
		$('.tg-btnaddtowishlist' + '.' + index).addClass('st-hiden');
		$('.tg-btnaddtowishlist' + '.' + index + '.hiden-flagFavorite').addClass('bookmark');
		$('.tg-btnaddtowishlist' + '.' + index + '.hiden-flagFavorite').removeClass('st-hiden');
		$('.tg-btnaddtowishlist' + '.' + index + '.do-flagFavorite').addClass('st-hiden');
		$("#totalFavorite").text("");
		totalFavorite = parseInt(totalFavorite) + 1;
		$("#totalFavorite").text(totalFavorite);

	}

	$.ajax({
		url: url,
		type: "GET",
		dataType: "json",
		data: 'json',
		success: function(result) {



		},
		error: function(err) {
		}
	}); // ajax call

}


function doUnFavorite(bookId) {

	var url = "/doUnFavorite?id=" + bookId;

	var totalFavorite = $("#totalFavorite").text();

	$.ajax({
		url: url,
		type: "GET",
		dataType: "json",
		data: 'json',
		success: function(result) {
			$("#totalFavorite").text("");
			totalFavorite = parseInt(totalFavorite) - 1;
			$("#totalFavorite").text(totalFavorite);
			$('.tg-btnaddtowishlist' + '.doUnFavorite' + '.init' + "." + bookId).remove();
			$('.tg-btnaddtowishlist' + '.doFavorite ' + '.init' + "." + bookId).remove();
			$('.tg-btnaddtowishlist' + '.doFavorite ' + "." + bookId).addClass('st-hiden');
			$('.tg-btnaddtowishlist' + '.doUnFavorite ' + "." + bookId + '.st-hiden').removeClass('st-hiden');




		},
		error: function(err) {
			// check the err for error details
		}
	}); // ajax call

}