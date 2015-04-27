<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
		<!--<c:forEach items="${finalisation.listPathsOutputFiles}" var="path">
			<a href=<c:out value='${path}'/>>Download input data </a>
			<br>
		</c:forEach>-->
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

	<br>
	<div id=divStep1 style="visibility: hidden">
		Step 1 : Mapping to DarwinCore format <br>
		<c:forEach items="${step1.mappedFilesAssociatedPath}" var="entry"> 
			${entry.key.originalName} : <a href=<c:out value='${entry.value}'/>> Download</a>
			<br>
		</c:forEach>
	</div>
	<div id=divStep2 style="visibility: hidden">
		Step 2 : Check coordinates <br> Number of occurrences found : <br>
	</div>
	<div id=divStep3 style="visibility: hidden">
		Step 3 : Check geospatial issue <br> Number of occurrences found
		: <br>
	</div>
	<div id=divStep4 style="visibility: hidden">
		Step 4 : Check taxonomy (KEW API) <br>
	</div>
	<div id=divStep5 style="visibility: hidden">
		Step 5 : Add synonyms <br> Number of occurrences involved : <br>
	</div>
	<div id=divStep6 style="visibility: hidden">
		Step 6 : Check TDWG code <br>
	</div>
	<div id=divStep7 style="visibility: hidden">
		Step 7 : Check if coordinates are equivalent to ISO2 code <br>
		Number of wrong occurrences : <br>
	</div>
	<div id=divStep8 style="visibility: hidden">
		Step 8 :Check coordinates in raster cells <br> Number of
		occurrences found : <br>
	</div>

	<!-- <a href=${finalisation.pathMatrixFile}> Download input data who are valid for raster</a> 
	
	<script>
function initialiseFinalPage(){
	var step1_involved = document.getElementById("step1_involved");
	alert("magna " + step1_involved);
	if(step1_involved){
		this.initialiseStep1();
	}
	
	var step2_involved = document.getElementById("step2_involved");
	if(step2_involved){
		this.initialiseStep1();
	}
	
	var step3_involved = document.getElementById("step3_involved");
	if(step3_involved){
		this.initialiseStep1();
	}
	
	var step4_involved = document.getElementById("step4_involved");
	if(step4_involved){
		this.initialiseStep1();
	}
	
	var step5_involved = document.getElementById("step5_involved");
	if(step5_involved){
		this.initialiseStep1();
	}
	
	var step6_involved = document.getElementById("step6_involved");
	if(step6_involved){
		this.initialiseStep1();
	}
	
	var step7_involved = document.getElementById("step7_involved");
	if(step7_involved){
		this.initialiseStep1();
	}
	
	var step8_involved = document.getElementById("step8_involved");
	if(step8_involved){
		this.initialiseStep1();
	}
}

function initialiseStep1(){
	var step1_ok = document.getElementById("step1_ok");
	var divStep1 = document.getElementById("divStep1");
	divStep1.setAttribute('style', "margin-left: 40px");
	divStep1.setAttribute("style", "visibility: visible");
	//divStep1.appendChild(divStep1Results);
}
</script>	-->
</body>
</html>