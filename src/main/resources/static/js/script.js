console.log("this is script page");

const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	}
	else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
}

const deleteContact = (cid) => {
	swal({
		title: "Are you sure?",
		text: "You want to delete this contact!",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				window.location = "/user/delete/" + cid;
			} else {
				swal("Your contact is safe!");
			}
		});
}

const search = () => {
	let query = $('#search-input').val();
	console.log(query);

	if (query == "") {
		$('.search-result').hide();
	}
	else {

		let url = `http://localhost:9090/search/${query}`;
		fetch(url).then(response => {
			return response.json();
		}).then(data => {
			console.log(data);
			let text = `<div class='list-group'>`;
			data.forEach(contact => {
				text += `<a href='/user/${contact.cid}/contact_details' class='list-group-item list-group-action'>${contact.name}</a>`;
			})
			text += `</div>`;
			$('.search-result').html(text);
			$('.search-result').show();
		})
	}
}