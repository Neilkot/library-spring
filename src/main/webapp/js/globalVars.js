var timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
var locale = Intl.DateTimeFormat().resolvedOptions().locale;

function formatDates() {
	$(".table-column-date").each(function() {
		var currValue = $(this).html();
		if (currValue) {
			var formattedValue = formatAsLocalDate(currValue);
			$(this).html(formattedValue);
		}
	});
}

function formatAsLocalDate(utcDate) {
	var date = new Date(utcDate);
	date.toLocaleString(locale, { timeZone: timeZone })
	var formatted = formatDate(date)
	console.log(date + " => " + formatted)
	return formatted;
}

function formatDate(date_ob) {
	let day = date_ob.getDate();
	let month = date_ob.getMonth() + 1;
	let year = date_ob.getFullYear();
	let hours = date_ob.getHours();
	let minutes = date_ob.getMinutes();
	console.log("minutes=" + minutes)
	month = (month < 10 ? "0" : "") + month;
    day = (day < 10 ? "0" : "") + day;
    hours = (hours < 10 ? "0" : "") + hours;
    minutes = (minutes < 10 ? "0" : "") + minutes;
	return year + "-" + month + "-" + day + " " + hours + ":" + minutes;
}

function getCurrPageSuffix() {
	var pathName = window.location.pathname;
	if (pathName.includes("/")) {
		return pathName.substring(pathName.lastIndexOf("/"));
	}
}

		
