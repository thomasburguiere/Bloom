<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<p>
	Welcome to the clean data workflow !
	<body onload="javascript:initialise()">
	<form action="controler" method="post" enctype="multipart/form-data" name="formulaire" id="formulaire">
	<br>
		<input id="inp0" type="file" name="inp0" style="color:rgb(51,153,255); border-color:rgb(255,255,255); border-style:solid;" size="100"/>
	<br>
		<input id="compteur_inp" type="hidden" name="compteur_inp" value=1 />
		<input id="addFile" type="button" name="add" value="Add a new file" onclick="javascript:addField()" style="color:rgb(204,0,153); border-color:rgb(255,255,255); border-style:solid;" size="51">
		<input id="delFile" type="button" value="Delete file" onclick="javascript:deleteField()" style="color:rgb(204,0,153); border-color:rgb(255,255,255); border-style:solid;" size="51"/>
	<br>
		<script type="text/javascript">
			function initialise(){
			    document.getElementById('compteur_inp').value = 1;
			}	
			function addField(){
				//compte le nombre d'input
			    nb_inp = document.getElementById('compteur_inp').value;
			     
			    //Créé un nouvel inputfile
			    var inp = document.createElement('input');
			    inp.setAttribute('type', 'file');
			    inp.setAttribute('id', 'inp' + nb_inp);
			    inp.setAttribute('name', 'inp' + nb_inp);
			  	inp.setAttribute('style', 'color:rgb(51,153,255); border-color:rgb(255,255,255); border-style:solid;" size="100"')
			  	//créé un retour charriot
			    var br = document.createElement('br');
			    br.setAttribute('id', 'br' + nb_inp);
			    
			    //ajoute tous les nouveaux éléments au formulaire
			    var bouton_valider = document.getElementById('addFile')
			    var formulaire = document.getElementById('formulaire')
			    formulaire.insertBefore(inp, bouton_valider);
			    formulaire.insertBefore(br, bouton_valider);
			 
			  	//incrément le compteur d'input
			    document.getElementById('compteur_inp').value ++;
			}
			function deleteField()
			{
			    //compte le nombre d'input
			    var nb_inp = document.getElementById('compteur_inp').value;
			     
			    //S'il reste des input on supprime le dernier
			    if(nb_inp > 0)
			    {
			        var id = nb_inp - 1;
			        var inp = document.getElementById('inp' + id);
			        var br = document.getElementById('br' + id);
			        var formulaire = document.getElementById('formulaire');
			        formulaire.removeChild(inp);
			        formulaire.removeChild(br);
			         
			        //décrémente le compteur d'input
			        document.getElementById('compteur_inp').value --;
			    }
			}
			
		</script>
		<p align="center"><font color="white"><input type="submit" value="Upload input file"></font></p>
	</form>
	</p>
</body>
</html>