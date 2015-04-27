<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body onload="javascript:initialise()">
	<p>Welcome to the clean data workflow !
	<!--  enctype="multipart/form-data"  -->
	<form action="controler" method="post" enctype="multipart/form-data" 
		name="formulaire" id="formulaire" accept-charset=utf-8>
		<br> <input id="compteur_inp" type="hidden" name="compteur_inp" value=1/>
			<input id="addFile" type="button" name="addFile" value="Add a new file"
			onclick="javascript:addField('compteur_inp', 'synonyms', 'inp')" style="color: rgb(204, 0, 153);
			border-color: rgb(255, 255, 255); border-style: solid;" size="51"> 
			<input id="delFile" name ="delFile" type="button" value="Delete file"
			onclick="javascript:deleteField('compteur_inp', 'inp')"
			style="color: rgb(204, 0, 153); border-color: rgb(255, 255, 255); border-style: solid;"
			size="51"/>
		<br> <input id="inp_0" type="file" name="inp_0" onChange="loadInputFile('0')"
			style="color: rgb(51, 153, 255); border-color: rgb(255, 255, 255); border-style: solid;"
			size="100"/>
		<div id="divLoad_0"  style="margin-left: 40px">
			<!--init : ${initialisation.convertDwC}
			<input id="convert_0" type="submit" name="convert_0" value="Convert to DwC format" />-->
			<!--<c:out value="${initialisation.convertDwC}"/> 
			<input id="loadFile0" type="submit" name="loadFile" class="button" value="Load file for mapping" onclick="javascript:mappingTable('divText0')"/>-->
		</div>
		<br> <input id="synonyms" type="checkbox"
			name="synonyms" value="synonyms" onclick="handleClick(this)"> Include synonyms taxons
		<br>
		<div id="divRaster">
			<input id="compteur_raster" type="hidden" name="compteur_raster"
				value=0/>
			<input id="compteur_header" type="hidden"
				name="compteur_header" value=0 /> 
			<input type="checkbox" id="raster" name="raster" value="raster"
			onclick="addDeleteRasterFile()" > Check data in raster cells
			<div id="divRasterFiles" style="margin-left: 40px">
			</div>
		</div>
		<input type="checkbox" id="tdwg4" name="tdwg4" value="tdwg4" onclick="handleClick(this)"> Botanic data (check tdwg4 code)
		<br>
		<input id="establishment" type="checkbox" name="establishment"
			value="establishment" onclick="established()" onclick="handleClick(this)"> How biological
			individual(s) is represented in the Occurrence : became established at
			the location. ("establishmentMeans" DarwinCore term)
		<br>
		<div style="margin-left: 40px">
			<input id="native" type="checkbox" name="native" value="native"
				onclick="checkEstablishment()"> Native
			<br>
			<input id="introduced" type="checkbox" name="introduced" value="introduced"
				onclick="checkEstablishment()"> Introduced
			<br> <input id="naturalised" type="checkbox" name="naturalised"
				value="naturalised" onclick="checkEstablishment()"> Naturalised
			<br> <input id="invasive" type="checkbox" name="invasive" value="invasive" 
				onclick="checkEstablishment()"> Invasive
			<br> <input id="managed" type="checkbox" name="managed"
				value="managed" onclick="checkEstablishment()"> Managed
			<br> <input id="uncertain" type="checkbox" name="uncertain"
				value="uncertain" onclick="checkEstablishment()"> Uncertain
			<br> <input id="others" type="checkbox" name="others"
				value="others" onclick="checkEstablishment()"> Others (blank, tags no represented, ...)
		</div>
		<br	id="brBeforeSubmit"><font color="white">
		<input type="submit" value="Launch workflow" id="workflowLaunch" name="workflowLaunch" class="button" ></font>
	</form>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
	<script type="text/javascript" src="js/functions.js"></script>
</body>
</html>