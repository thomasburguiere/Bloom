<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body onload="javascript:initialise()">
	<p>Welcome to the clean data workflow !
	<form action="controler" method="post" enctype="multipart/form-data" name="formulaire" id="formulaire" accept-charset=utf-8>
		<script type="text/javascript" src="functions.js">
			
		</script>
		<br> 
			<input id="compteur_inp" type="hidden" name="compteur_inp" value=1 /> 
			<input id="addFile" type="button" name="add" value="Add a new file"
				onclick="javascript:addField('compteur_inp', 'synonyms', 'inp')"
				style="color: rgb(204, 0, 153); border-color: rgb(255, 255, 255); border-style: solid;" size="51">
			<input id="delFile" type="button" value="Delete file"
				onclick="javascript:deleteField('compteur_inp', 'inp')"
				style="color: rgb(204, 0, 153); border-color: rgb(255, 255, 255); border-style: solid;" size="51" />
		<br>
			<input id="inp0" type="file" name="inp0" style="color: rgb(51, 153, 255); border-color: rgb(255, 255, 255); 
				border-style: solid;" size="100" /> 
		<br>	
			<input id="synonyms" type="checkbox" name="synonyms" value="synonyms"> Include synonyms taxons 
		<br>
			<input id="compteur_raster" type="hidden" name="compteur_raster" value=0 /> 
			<input id="compteur_header" type="hidden" name="compteur_header" value=0 />
			<input type="checkbox" id="raster" name="raster" value="raster" onclick="addDeleteRasterFile()">
				Check data in raster cells <br>
			<input type="checkbox" id="tdwg4" name="tdwg4" value="tdwg4"> Botanic data (check tdwg4 code)
		<br>

		<font color="white"><input type="submit" value="Upload input file"></font>
	</form>
</body>
</html>