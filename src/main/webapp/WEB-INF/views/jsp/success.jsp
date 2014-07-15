<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Success</title>
<script type="text/javascript">
	function processAuthParams(input) {
		var params = {}, queryString = input;
		var regex = /([^&=]+)=([^&]*)/g;
		while (m = regex.exec(queryString)) {
			params[m[1]] = m[2];
		}
		return params;
	}
	function init() {
		var params = processAuthParams(document.location.hash.substring(1));
		window.opener.postMessage(params, '*');
		window.close();
	}
</script>
</head>
<body onload="init()">
</body>
</html>