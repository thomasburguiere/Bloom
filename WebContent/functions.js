function initialise(){
	document.getElementById('compteur_inp').value = 1;
	document.getElementById('compteur_raster').value = 0;
	document.getElementById('compteur_header').value = 0;
}	
function addField(compteur, idAdd, typeInput){
	//compte le nombre d'input
	var nb_inp = document.getElementById(compteur).value;

	//Créé un nouvel inputfile
	var inp = document.createElement('input');
	inp.setAttribute('type', 'file');
	inp.setAttribute('id', typeInput + nb_inp);
	inp.setAttribute('name', typeInput + nb_inp);
	inp.setAttribute('style', 'color:rgb(51,153,255); border-color:rgb(255,255,255); border-style:solid;" size="100"')
	//créé un retour charriot
	var br = document.createElement('br');
	br.setAttribute('id', 'br_' + typeInput + "_" + nb_inp);

	//ajoute tous les nouveaux éléments au formulaire
	var buttonADD = document.getElementById(idAdd);
	var formulaire = document.getElementById('formulaire');
	formulaire.insertBefore(inp, buttonADD);
	
	if(typeInput == "raster"){
		addHeaderFile();
	}
	formulaire.insertBefore(br, buttonADD);

	//incrément le compteur d'input
	document.getElementById(compteur).value ++;
}

function deleteField(compteur, typeInput)
{
	//compte le nombre d'input
	var nb_inp = document.getElementById(compteur).value;

	//S'il reste des input on supprime le dernier
	
	if(nb_inp > 0){
		
		var id = nb_inp - 1;
		var inp = document.getElementById(typeInput + id);
		var br = document.getElementById('br_' + typeInput + "_" + id);
		//alert('br_' + typeInput + "_" + id);
		var formulaire = document.getElementById('formulaire');
		//alert(typeInput + id);
		formulaire.removeChild(inp);
		
		formulaire.removeChild(br);
		
		if(typeInput == 'raster'){
			//alert(typeInput + id);
			var header = document.getElementById('header' + id);
			formulaire.removeChild(header);
			document.getElementById('compteur_header').value --;
		}
			
		//décrémente le compteur d'input
		document.getElementById(compteur).value --;
	}
}

function addDeleteRasterFile(){
	var rasterOption = document.getElementById('raster').checked;
	nb_raster = document.getElementById('compteur_raster').value;
	nb_header = document.getElementById('compteur_header').value;
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
	        var header = document.getElementById('header' + id);
	        var br = document.getElementById('br_raster_' + id);

	        formulaire.removeChild(raster);
	        formulaire.removeChild(header);
	        formulaire.removeChild(br);
	     
	        //décrémente le compteur d'input
	        document.getElementById('compteur_raster').value --;
	        document.getElementById('compteur_header').value --;
	        nb_header--;
	        nb_raster--;
	    }
	 	// supprimer les boutons "add" et "delete"
	    //document.getElementById('compteur_raster').value = 0;
	    formulaire.removeChild(brAfter);
	    formulaire.removeChild(add);
	    formulaire.removeChild(del);
	}
}

function addHeaderFile(){
	//compte le nombre d'input
	//var nb_raster = document.getElementById("compteur_raster").value;
	var nb_header = document.getElementById("compteur_header").value;
	
	//Créé un nouvel inputfile
	var header = document.createElement('input');
	header.setAttribute('type', 'file');
	header.setAttribute('id', 'header' + nb_header);
	header.setAttribute('name', 'header' + nb_header);
	header.setAttribute('style', 'color:rgb(51,153,255); border-color:rgb(255,255,255); border-style:solid;" size="100"');

	//ajoute tous les nouveaux éléments au formulaire
	var buttonADD = document.getElementById('tdwg4');
	var formulaire = document.getElementById('formulaire');
	formulaire.insertBefore(header, buttonADD);
	
	//incrément le compteur header
	document.getElementById("compteur_header").value ++;
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