<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body onload="javascript:initialise()">
	<p>Welcome to the clean data workflow !
	<form action="soaprequestservlet" method="post">
		<label for="wsdl">Webservice endpoint</label> <input type="text"
			name="wsdl" size="120" id="wsdl"
			style="font-size: 11px; color: blue;" /> <label for="FormatXML">Format
			XML</label> <input id="FormatXML" type="checkbox" name="formatXML"
			value="formatXML" checked="checked" />
		<table cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td width="45%">
					<h3>SOAP Request</h3> <textarea
						style="font-size: 11px; color: blue;" name="soapmessage" cols="80"
						rows="40" title="SOAP MessageRequest" id="soaprequest"></textarea>
				</td>
				<td width="45%">
					<h3>SOAP Response</h3> <textarea
						style="font-size: 11px; color: blue;" name="soapmessageresponse"
						cols="80" rows="40" title="Soap Request" id="soapresponse"></textarea>
				</td>
			</tr>
		</table>
		<input type="submit" name="Test SOAP Request"
			value="Test SOAP Request" />
	</form>

	<form action="controler" method="post" enctype="multipart/form-data"
		name="formulaire" id="formulaire" accept-charset=utf-8>
		<script type="text/javascript" src="functions.js">
			
		</script>

		<br> <input id="compteur_inp" type="hidden" name="compteur_inp"
			value=1 /> <input id="addFile" type="button" name="add"
			value="Add a new file"
			onclick="javascript:addField('compteur_inp', 'synonyms', 'inp')"
			style="color: rgb(204, 0, 153); border-color: rgb(255, 255, 255); border-style: solid;"
			size="51"> <input id="delFile" type="button"
			value="Delete file"
			onclick="javascript:deleteField('compteur_inp', 'inp')"
			style="color: rgb(204, 0, 153); border-color: rgb(255, 255, 255); border-style: solid;"
			size="51" /> <br> <input id="inp0" type="file" name="inp0"
			style="color: rgb(51, 153, 255); border-color: rgb(255, 255, 255); border-style: solid;"
			size="100" /> <br> <input id="synonyms" type="checkbox"
			name="synonyms" value="synonyms"> Include synonyms taxons <br>
		<input id="compteur_raster" type="hidden" name="compteur_raster"
			value=0 /> <input id="compteur_header" type="hidden"
			name="compteur_header" value=0 /> <input type="checkbox" id="raster"
			name="raster" value="raster" onclick="addDeleteRasterFile()">
		Check data in raster cells <br> <input type="checkbox" id="tdwg4"
			name="tdwg4" value="tdwg4"> Botanic data (check tdwg4 code) <br>
		<input id="establishment" type="checkbox" name="establishment"
			value="establishment" onclick="established()"> How biological
		individual(s) is represented in the Occurrence : became established at
		the location. ("establishmentMeans" DarwinCore term) <br>
		<div style="margin-left: 40px">
			<input id="native" type="checkbox" name="native" value="native"
				onclick="checkEstablishment()"> Native <br> <input
				id="introduced" type="checkbox" name="introduced" value="introduced"
				onclick="checkEstablishment()"> Introduced <br> <input
				id="naturalised" type="checkbox" name="naturalised"
				value="naturalised" onclick="checkEstablishment()">
			Naturalised <br> <input id="invasive" type="checkbox"
				name="invasive" value="invasive" onclick="checkEstablishment()">
			Invasive <br> <input id="managed" type="checkbox" name="managed"
				value="managed" onclick="checkEstablishment()"> Managed <br>
			<input id="uncertain" type="checkbox" name="uncertain"
				value="uncertain" onclick="checkEstablishment()"> Uncertain
			<br> <input id="others" type="checkbox" name="others"
				value="others" onclick="checkEstablishment()"> Others
			(blank, tags no represented, ...)
		</div>
		<input id="iptToolkit" type="checkbox" name="iptToolkit"
			value="iptToolkit"> Convert your data to Darwin Core format <br
			id="brBeforeSubmit"> <font color="white"><input
			type="submit" value="Upload input file" id="submit"></font>
	</form>
</body>
</html>