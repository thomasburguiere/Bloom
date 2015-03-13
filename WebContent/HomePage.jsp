<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<p>
	Welcome to the clean data workflow !
	<form action="controler" method="post" enctype="multipart/form-data" name="formulaire" id="formulaire">
		<br>
			<input id="inp0" type="file" name="inp0" style="color:rgb(51,153,255); border-color:rgb(255,255,255); border-style:solid;" size="100"/>
		<br>
			<input id="compteur_inp" type="hidden" name="compteur_inp" value=1 />
			<body onload="javascript:initialise()" />
			
			<input id="addFile" type="button" name="add" value="Add a new file" onclick="javascript:addField('compteur_inp', 'addFile', 'inp')" style="color:rgb(204,0,153); border-color:rgb(255,255,255); border-style:solid;" size="51">
			<input id="delFile" type="button" value="Delete file" onclick="javascript:deleteField('compteur_inp', 'inp')" style="color:rgb(204,0,153); border-color:rgb(255,255,255); border-style:solid;" size="51"/>
		<br>
			<script type="text/javascript">
				function initialise(){
				    document.getElementById('compteur_inp').value = 1;
				    document.getElementById('compteur_raster').value = 0;
				}	
				function addField(compteur, idAdd, typeInput){
					//compte le nombre d'input
				    nb_inp = document.getElementById(compteur).value;
				     
				    //Créé un nouvel inputfile
				    var inp = document.createElement('input');
				    inp.setAttribute('type', 'file');
				    inp.setAttribute('id', typeInput + nb_inp);
				    inp.setAttribute('name', typeInput + nb_inp);
				  	inp.setAttribute('style', 'color:rgb(51,153,255); border-color:rgb(255,255,255); border-style:solid;" size="100"')
				  	//créé un retour charriot
				    var br = document.createElement('br');
				    br.setAttribute('id', 'br' + nb_inp);
				    
				    //ajoute tous les nouveaux éléments au formulaire
				    var buttonADD = document.getElementById(idAdd);
				    var formulaire = document.getElementById('formulaire');
				    formulaire.insertBefore(inp, buttonADD);
				    formulaire.insertBefore(br, buttonADD);
				 
				  	//incrément le compteur d'input
				    document.getElementById(compteur).value ++;
				}
				function deleteField(compteur, typeInput)
				{
				    //compte le nombre d'input
				    var nb_inp = document.getElementById(compteur).value;
				     
				    //S'il reste des input on supprime le dernier
				    if(nb_inp > 0)
				    {
				        var id = nb_inp - 1;
				        var inp = document.getElementById(typeInput + id);
				        var br = document.getElementById('br' + id);
				        var formulaire = document.getElementById('formulaire');
				        formulaire.removeChild(inp);
				        formulaire.removeChild(br);
				         
				        //décrémente le compteur d'input
				        document.getElementById(compteur).value --;
				    }
				}
			</script>
			<br>
				<input type="checkbox" name="synonyms" value="synonyms"> Include synonyms taxons
			<br>
				<input id="compteur_raster" type="hidden" name="compteur_raster" value=0 />
				<input type="checkbox" id="raster" name="raster" value="raster" onclick="addDeleteRasterFile()"> Check data in raster cells
			<br>
				<input type="checkbox" id="tdwg4" name="tdwg4" value="tdwg4"> Botanic data (check tdwg4 code)
			<br>
			<script type="text/javascript">
				function addDeleteRasterFile(){
					var rasterOption = document.getElementById('raster').checked;
					nb_raster = document.getElementById('compteur_raster').value;
					if(rasterOption){
						
						if(nb_raster == 0){
							
							//id, typeElement, type, onclick, style, value, elementAfter
							this.addElement('addRaster', 'input', 'button', 'javascript:addField(\'compteur_raster\', \'tdwg4\', \'raster\')', 'color:rgb(204,0,153); border-color:rgb(255,255,255); border-style:solid;" size="51"', 'Add a new raster file', 'tdwg4');
							
							this.addElement('delRaster', 'input', 'button', 'javascript:deleteField(\'compteur_raster\', \'raster\')', 'color:rgb(204,0,153); border-color:rgb(255,255,255); border-style:solid;" size="51"', 'Delete a raster file', 'tdwg4');
				    		
							this.addElement('brAfterDelRaster', 'br', '', '', '', '', 'tdwg4');
						}
					}
					else{
						var formulaire = document.getElementById('formulaire');
						var add = document.getElementById('addRaster');
				        var del = document.getElementById('delRaster');
				        var brAfter = document.getElementById('brAfterDelRaster');
				        
					    //S'il reste des input on supprime le dernier
					    while(nb_raster != 0){
					    	var id = nb_raster - 1;
					        var raster = document.getElementById('raster' + id);
					        var br = document.getElementById('br' + id);
					        formulaire.removeChild(raster);
					        formulaire.removeChild(br);
					     
					        //décrémente le compteur d'input
					        document.getElementById('compteur_raster').value --;
					        nb_raster--;
					    }
					 	// supprimer les boutons "add" et "delete"
					    //document.getElementById('compteur_raster').value = 0;
					    formulaire.removeChild(brAfter);
					    formulaire.removeChild(add);
					    formulaire.removeChild(del);
					}
				}
				
				function addElement(id, typeElement, type, onclick, style, value, elementAfter){
					var element = document.createElement(typeElement);
					element.setAttribute('type', type);
					element.setAttribute('id', id);
					element.setAttribute('name', id);
					element.setAttribute('onclick', onclick);
					element.setAttribute('style', style );
					element.setAttribute('value', value);
    
				    var after = document.getElementById(elementAfter);
				    var formulaire = document.getElementById('formulaire');
		    		formulaire.insertBefore(element, after);
				}
			</script>
			<font color="white"><input type="submit" value="Upload input file"></font>
	</form>
</body>
</html>