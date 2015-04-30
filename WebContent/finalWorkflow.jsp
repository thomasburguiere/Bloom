<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="src.servlets.Controler"%>
<%@page import="src.beans.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Resume workflow</title>
<script type="text/javascript" src="js/functionsFinal.js"></script>
</head>
<body onload="initialiseFinalPage()">
	<div>
		<br> All steps are finished. <br>
	</div>
	<div id="hiddenResults">
		<input type="hidden" id="step1_involved" value="${step1.involved}" />
		<input type="hidden" id="step1_ok" value="${step1.step1_ok}" />
		<!--<input type="hidden" id="step1_filesDwc" value="${step1.mappedFilesAssociatedPath}"/>  -->

		<input type="hidden" id="step2_involved" value="${step2.involved}" />
		<input type="hidden" id="step2_ok" value="${step2.step2_ok}" /> <input
			type="hidden" id="step2_nbFound" value="${step2.nbFound}" /> <input
			type="hidden" id="step2_path" value="${step2.pathWrongCoordinates}" />

		<input type="hidden" id="step3_involved" value="${step3.involved}" />
		<input type="hidden" id="step3_ok" value="${step3.step3_ok}" /> <input
			type="hidden" id="step3_nbFound" value="${step3.nbFound}" /> <input
			type="hidden" id="step3_path" value="${step3.pathWrongGeoIssue}" />
		<input type="hidden" id="step4_involved" value="${step4.involved}" />
		<input type="hidden" id="step4_ok" value="${step4.step4_ok}" /> <input
			type="hidden" id="step5_involved" value="${step5.involved}" /> <input
			type="hidden" id="step5_ok" value="${step5.step5_ok}" /> <input
			type="hidden" id="step5_nbFound" value="${step5.nbFound}" /> <input
			type="hidden" id="step6_involved" value="${step6.involved}" /> <input
			type="hidden" id="step6_ok" value="${step6.step6_ok}" /> <input
			type="hidden" id="step7_involved" value="${step7.involved}" /> <input
			type="hidden" id="step7_ok" value="${step7.step7_ok}" /> <input
			type="hidden" id="step7_nbFound" value="${step7.nbFound}" /> <input
			type="hidden" id="step7_path" value="${step7.pathWrongIso2}" /> <input
			type="hidden" id="step8_involved" value="${step8.involved}" /> <input
			type="hidden" id="step8_ok" value="${step8.step8_ok}" /> <input
			type="hidden" id="step8_nbFound" value="${step8.nbFound}" /> <input
			type="hidden" id="step8_path" value="${step8.pathWrongRaster}" />
	</div>

	<div id=divStep1 style="display: none">
		<p id="p_ok1">Step 1 : Mapping to DarwinCore format</p>
		<c:forEach items="${step1.mappedFilesAssociatedPath}" var="entry"> 
			${entry.key.originalName} : <a href=<c:out value='${entry.value}'/>>
				Download</a>
			<br>
		</c:forEach>
	</div>
	<div id=divStep2 style="display: none">
		<p id="p_ok2">Step 2 : Check coordinates</p>
		Number of occurrences found : ${step2.nbFound}
	</div>
	<div id=divStep3 style="display: none">
		<p id="p_ok3">Step 3 : Check geospatial issue</p>
		Number of occurrences found : ${step3.nbFound}
	</div>
	<div id=divStep4 style="display: none">
		<p id="p_ok4">Step 4 : Check taxonomy (KEW API)</p>
	</div>
	<div id=divStep5 style="display: none">
		<p id="p_ok5">Step 5 : Add synonyms</p>
		Number of occurrences involved : ${step5.nbFound}
	</div>
	<div id=divStep6 style="display: none">
		<p id="p_ok6">Step 6 : Check TDWG code</p>
	</div>
	<div id=divStep7 style="display: none">
		<p id="p_ok7">Step 7 : Check if coordinates are equivalent to ISO2
			code</p>
		Number of wrong occurrences : ${step7.nbFound}
	</div>
	<div id=divStep8 style="display: none">
		<p id="p_ok8">Step 8 :Check coordinates in raster cells</p>
		Number of occurrences found : "${step8.nbFound}"
	</div>
	<br>
	<div id="downloadFinalFiles">
	<p> These files can be downloaded : </p>
		<c:forEach items="${finalisation.listPathsOutputFiles}" var="path">
			<c:set var="name" value="${fn:split(path, '/')}" />
			<c:set var="length" value="${fn:length(name)}"/>
 			${name[length-1]}
			<a href=<c:out value='${path}'/>>Download clean data </a>
			<br>
		</c:forEach>
	</div>
</body>
</html>