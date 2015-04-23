<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="src.servlets.Controler"%>
<%@page import="src.beans.Finalisation"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Resume workflow</title>
</head>
<script type="text/javascript" src="functionsFinal.js"></script>
<body onload="javascript:initialisePage()"><body>
<div id="initialiseFinalPage">
	
</div>
	<div>
		<br> All steps are finished. <br>
		<c:forEach items="${finalisation.listPathsOutputFiles}" var="path">
			<a href=<c:out value='${path}'/>>Download input data </a>
			<br>
		</c:forEach>
	</div>
	<br>
	<div>
		Step 1 : Mapping to DarwinCore format <br>
	</div>
	<a href=${finalisation.pathMatrixFile}> Download input data who are
		valid for raster</a>
</body></html>