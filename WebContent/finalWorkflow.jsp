<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="src.servlets.Controler"%>
<%@page import="src.beans.*"%>


<!DOCTYPE html PUBLIC "-//W4C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
	<link href="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/css/bootstrap-editable.css" rel="stylesheet"/>
	<link href="css/HomePage.css" rel="stylesheet">
<title>Resume workflow</title>
<script type="text/javascript" src="js/functionsFinal.js"></script>
</head>
<body onload="initialiseFinalPage()">
    <div class="title"> 
			Biodiversity
			Linked
			Organisms
			Occurrences
			Megadatasets
    </div>
	<div>
		<br> All steps are finished. <br>
	</div>
<!-- 
	<div id="hiddenResults">
		<input type="hidden" id="step1_involved" value="${step1.involved}" />
		<input type="hidden" id="step1_ok" value="${step1.step1_ok}" />
		
		<input type="hidden" id="step2_involved" value="${step2.involved}" />
		<input type="hidden" id="step2_ok" value="${step2.step2_ok}" />
		
		<input type="hidden" id="step3_involved" value="${step3.involved}" />
		<input type="hidden" id="step3_ok" value="${step3.step3_ok}" />
		<input type="hidden" id="step3_nbFound" value="${step3.nbFound}" />
		<input type="hidden" id="step3_path" value="${step3.pathWrongCoordinates}" />

		<input type="hidden" id="step4_involved" value="${step4.involved}" />
		<input type="hidden" id="step4_ok" value="${step4.step4_ok}" /> <input
			type="hidden" id="step4_nbFound" value="${step4.nbFound}" /> <input
			type="hidden" id="step4_path" value="${step4.pathWrongGeoIssue}" />
			
		<input type="hidden" id="step5_involved" value="${step5.involved}" />
		<input type="hidden" id="step5_ok" value="${step5.step5_ok}" /> 
		
		<input
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
-->
    <table data-toggle="table" data-card-view="true" class="table table-hover" style="margin-top: 0px;" id=divStep1>
        <thead>
            <td id="p_ok1">
                Step 1 : Mapping to DarwinCore format
            </td>
        </thead>
        <tbody>
            <c:set var="count1" value="0" scope="page" />
            <c:forEach items="${step1.mappedFilesAssociatedPath}" var="entry">
                <tr data-index=<c:out value="${count1}" />>
                    <td colspan="6">
                        <div class="card-view">
                            <span class="name">Filename</span>
                            <span class="value"><c:out value='${entry.key.filename}'/></span>
                        </div>
                        <div class="card-view">
                            <span class="sucess">Sucess</span>
                            <span class="value">Yes/No</span>
                        </div>
                        <div class="card-view">
                            <span class="downloadLink">Download link</span>
                            <span class="value"><a href=<c:out value='${entry.value}'/>>Download</a></span>
                        </div>
                    </td>
                </tr>
                <c:set var="count1" value="${count1 + 1}" scope="page"/>
            </c:forEach>
        </tbody>
    </table>
    <!-- 
        <div id=divStep1 style="display: none">
            <div id=divStep1Involved style="display: none">
                <p id="p_ok1">Step 1 : Mapping to DarwinCore format</p>
                <c:forEach items="${step1.mappedFilesAssociatedPath}" var="entry"> 
                    Input with mapping DWC : <a href=<c:out value='${entry.value}'/>>
                        Download</a>
                    <br>
                </c:forEach>
            </div>
            <div id=divStep1NotInvolved style="display: none">
                <p id="step1NotInvolved">Step 1 isn't involved in process</p>
            </div>
        </div>
        -->
    <table data-toggle="table" data-card-view="true" class="table table-hover" style="margin-top: 0px;" id=divStep2 >
        <thead>
            <td id="p_ok2">
                Step 2 : Reconciliation Service
            </td>
        </thead>
        <tbody>
            <c:set var="count2" value="0" scope="page" />
            <c:forEach items="${step2.reconciledFilesAssociatedPath}" var="entry">
                <tr data-index=<c:out value="${count2}" />>
                    <td colspan="6">
                        <div class="card-view">
                            <span class="name">Filename</span>
                            <span class="value"><c:out value='${entry.key.filename}'/></span>
                        </div>
                        <div class="card-view">
                            <span class="sucess">Sucess</span>
                            <span class="value">Yes/No</span>
                        </div>
                        <div class="card-view">
                            <span class="downloadLink">Download link</span>
                            <span class="value"><a href=<c:out value='${entry.value}'/>>Download</a></span>
                        </div>
                    </td>
                </tr>
                <c:set var="count2" value="${count2 + 1}" scope="page"/>
            </c:forEach>
        </tbody>
    </table>
    <!--
        <div id=divStep2 style="display: none">
            <div id=divStep2Involved style="display: none">
                <p id="p_ok2">Step 2 : Reconciliation Service</p>
                <c:forEach items="${step2.reconciledFilesAssociatedPath}" var="entry"> 
                    Input with reconcile service : <a href=<c:out value='${entry.value}'/>>
                        Download</a>
                    <br>
                </c:forEach>
            </div>
            <div id=divStep2NotInvolved style="display: none">
                <p id="step2NotInvolved">Step 2 isn't involved in process</p>
            </div>
        </div>
    -->
   <table data-toggle="table" data-card-view="true" class="table table-hover" style="margin-top: 0px;" id=divStep3>
        <thead>
            <td id="p_ok3">
                Step 3 : Check coordinates
            </td>
        </thead>
        <tbody>
            <tr data-index=0 />>
                <td colspan="6">
                    <div class="card-view">
                        <span class="errorNumber">Occurrences number deleted</span>
                        <span class="value"><c:out value='${step3.nbFound}'/></span>
                    </div>
                    <div class="card-view">
                        <span class="downloadLink">Download link</span>
                        <span class="value"><a href=<c:out value='${step3.pathWrongCoordinates}'/>>Download wrong coordinates file</a></span>
                    </div>
                </td>
            </tr>   
        </div>
    </table>
    <table data-toggle="table" data-card-view="true" class="table table-hover" style="margin-top: 0px;" id=divStep4>
      <thead>
            <td id="p_ok4">
                Step 4 : Check geospatial issue
            </td>
        </thead>
        <tbody>
            <tr data-index=0 />
                <td colspan="6">
                    <div class="card-view">
                        <span class="errorNumber">Occurrences number deleted</span>
                        <span class="value"><c:out value='${step3.nbFound}'/></span>
                    </div>
                    <div class="card-view">
                        <span class="downloadLink">Download link</span>
                        <span class="value"><a href=<c:out value='${step4.pathWrongGeoIssue}'/>>Download wrong geospatial issue file</a></span>
                    </div>
                </td>
            </tr>   
        </div>
    </table>
    <table data-toggle="table" data-card-view="true" class="table table-hover" style="margin-top: 0px;" id=divStep5>
        <div id=divStep5 style="display: none">
            <div id=divStep5Involved style="display: none">
                <p id="p_ok5">Step 5 : Add synonyms</p>
                Number of occurrences involved : ${step5.nbFound}
            </div>
            <div id=divStep5NotInvolved style="display: none">
                <p id="step5NotInvolved">Step 5 isn't involved in process</p>
            </div>
        </div>
    </table>
    <table data-toggle="table" data-card-view="true" class="table table-hover" style="margin-top: 0px;" id=divStep6>
        <div id=divStep6 style="display: none">
            <div id=divStep6Involved style="display: none">
                <p id="p_ok6">Step 6 : Check TDWG code</p>
            </div>
            <div id=divStep6NotInvolved style="display: none">
                <p id="step6NotInvolved">Step 6 isn't involved in process</p>
            </div>
        </div>
    </table>
    <table data-toggle="table" data-card-view="true" class="table table-hover" style="margin-top: 0px;" id=divStep7>
        <div id=divStep7 style="display: none">
            <div id=divStep7Involved style="display: none">
                <p id="p_ok7">Step 7 : Check if coordinates are equivalent to ISO2 code</p>
                Number of wrong occurrences : ${step7.nbFound}
            </div>
            <div id=divStep7NotInvolved style="display: none">
                <p id="step7NotInvolved">Step 7 isn't involved in process</p>
            </div>
        </div>
    </table>
    <table data-toggle="table" data-card-view="true" class="table table-hover" style="margin-top: 0px;" id=divStep8>
        <div id=divStep8 style="display: none">
            <div id=divStep8Involved style="display: none">
                <p id="p_ok8">Step 8 :Check coordinates in raster cells</p>
                Number of occurrences found : "${step8.nbFound}"
            </div>
            <div id=divStep8NotInvolved style="display: none">
                <p id="step8NotInvolved">Step 8 isn't involved in process</p>
            </div>
        </div>
    </table>
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

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>
	<script src="js/jquery.watable.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script type="text/javascript"></script>
</body>
</html>