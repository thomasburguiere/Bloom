 <%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
	<link href="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/css/bootstrap-editable.css" rel="stylesheet"/>
	<link href="css/HomePage.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.7/css/jquery.dataTables.css">
	<link rel="Stylesheet" href="css/watable.css" />
	<title>Insert title here</title>
</head>
<body onload="javascript:initialise()">
	<div class="container">
		<div class="title"> 
			Biodiversity
			Linked
			Organisms
			Occurrences
			Megadatasets
		</div>
			<!--  enctype="multipart/form-data"  -->
		<div id="divBody" class="row">
			<div class="col-lg-4">
				<img src="images/colibri_bis.jpg"/>
			</div>
			<div class="col-lg-7">
				<form action="controler" method="post" enctype="multipart/form-data"
					name="formulaire" id="formulaire" accept-charset=utf-8>		
					<div class="bloc" id="bloc-intro" style="text-align:center">
						<p>Workflow begining</p>
					</div>		
					<div class="bloc col-lg-12" id="bloc-inputs">
						<p> Select inputs files </p>
						<input id="compteur_inp" type="hidden" name="compteur_inp" value=1 />
						<div class="col-lg-3">
							<input id="addFile" type="button" name="addFile" value="Add a new file"
								onclick="javascript:addField('compteur_inp', 'synonyms', 'inp')"/>
						</div>
						<div class="col-lg-3">
							<input id="delFile" name="delFile" type="button" value="Delete file"
								onclick="javascript:deleteField('compteur_inp', 'inp')"/>
						</div>
						<div id="divAddLoad_0" class="col-lg-12 addLoad">
							<div id="divAdd_0" class="col-lg-4">
								<input id="inp_0" type="file" name="inp_0" onChange="loadInputFile('0', 'change')"/>
							</div>
							<div id="divLoad_0" class="col-lg-4">
							</div>
							<div class="col-lg-4" id="divReconcile_0">
							</div>
						</div>
						<div id="divMapping_0" class="col-lg-12 mapping">
						</div>
						<div id="divSubmitMapping_0" class="col-lg-12 submitMapping">
						</div>
						<div id="divTableReconcile_0" class="col-lg-12 dataTable" style="width:auto">
							<!--  <input name="reconcileActive_0" id="reconcileActive_0" type="hidden" value="">-->
						</div>
						<div id="divReconciliationCheck_0" class="col-lg-12">
						</div>
						
						<div id="divSubmitReconcile_0" class="col-lg-12">
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12">
						</div>
					</div>					
					<div class="col-lg-12 bloc" id="bloc-synonym">
						<input id="synonyms" type="checkbox" name="synonyms" value="synonyms"> Include synonyms taxons
					</div>
					<div class="row">
						<div class="col-lg-12">
						</div>
					</div>
					<div class="col-lg-12 bloc" id="bloc-divRaster">
						<input id="compteur_raster" type="hidden" name="compteur_raster" value=0 />
						<input id="compteur_header" type="hidden" name="compteur_header" value=0 />
						<input type="checkbox" id="raster" name="raster" value="raster" onclick="addDeleteRasterFile()"> Check data in raster cells
						<div id="divRasterFiles" style="margin-left: 40px">
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12">
						</div>
					</div>
					<div class="col-lg-12 bloc" id="bloc-tdwg4">
						<input type="checkbox" id="tdwg4" name="tdwg4" value="tdwg4"> Botanic data (check tdwg4 code)
					</div>
					<div class="row">
						<div class="col-lg-12">
						</div>
					</div>
					<div class="col-lg-12 bloc" id="bloc-introduce">
						<input id="establishment" type="checkbox" name="establishment" value="establishment" onclick="established()"> How biological individual(s) is
							represented in the Occurrence : became established at the location.
							("establishmentMeans" DarwinCore term)
					
						<div style="margin-left: 40px">
							<input id="native" type="checkbox" name="native" value="native" onclick="checkEstablishment()"> Native 
							<br> 
							<input id="introduced" type="checkbox" name="introduced" value="introduced" onclick="checkEstablishment()"> Introduced 
							<br>
							<input id="naturalised" type="checkbox" name="naturalised" value="naturalised" onclick="checkEstablishment()"> Naturalised 
							<br>
							<input id="invasive" type="checkbox" name="invasive" value="invasive" onclick="checkEstablishment()">Invasive
							<br>
							<input id="managed" type="checkbox" name="managed" 	value="managed" onclick="checkEstablishment()"> Managed
							<br>
							<input id="uncertain" type="checkbox" name="uncertain" value="uncertain" onclick="checkEstablishment()"> Uncertain
							<br>
							<input id="others" type="checkbox" name="others" value="others" onclick="checkEstablishment()"> Others (blank, tags no represented, ...)
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12">
						</div>
					</div>
					<br id="brBeforeSubmit">
					<div class="foot">
						<input type="submit" value="Launch workflow" id="workflowLaunch" name="workflowLaunch" class="button">
					</div>
				</form>
			</div>
		</div>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
		<script src="bootstrap/js/bootstrap.min.js"></script>
		<script src="js/jquery.watable.js"></script>
		<script type="text/javascript" src="js/ReconciliationFunctions.js"></script>
		<script type="text/javascript" src="js/functions.js"></script>
		
		<script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.7/js/jquery.dataTables.js"></script>
	  	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
	  	<script src="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/js/bootstrap-editable.min.js"></script>
		<script type="text/javascript">
			
			
			
		</script>
	</div>
</body>
</html>