function initialise(){
	document.getElementById('compteur_inp').value = 1;
	document.getElementById('compteur_raster').value = 0;
	document.getElementById('compteur_header').value = 0;
}

//add a new input file
function addField(compteur, idAdd, typeInput){
	//count inputs number
	var nb_inp = document.getElementById(compteur).value;

	//Create a new input file 
	var inp = document.createElement('input');
	inp.setAttribute('type', 'file');
	inp.setAttribute('id', typeInput + nb_inp);
	inp.setAttribute('name', typeInput + nb_inp);
	inp.setAttribute('style', 'color:rgb(51,153,255); border-color:rgb(255,255,255); border-style:solid;" size="100"');
	//créé un retour charriot
	var br = document.createElement('br');
	br.setAttribute('id', 'br_' + typeInput + "_" + nb_inp);

	// add all new elements to the eformulary
	var buttonADD = document.getElementById(idAdd);
	var formulaire = document.getElementById('formulaire');
	formulaire.insertBefore(inp, buttonADD);
	
	if(typeInput == "raster"){
		addHeaderFile();
	}
	if(typeInput == "inp"){
		//formulaire.insertBefore(convert,buttonADD);
		
		var div = document.createElement('div');
		div.setAttribute('id', 'divText_' + nb_inp);
		div.setAttribute('style', "margin-left: 40px");
		var checkbox = document.createElement('input');
		checkbox.type = "checkbox";
		checkbox.name = "convert_" + nb_inp;
		checkbox.value = "value";
		checkbox.id = "convert_" + nb_inp ;
		checkbox.setAttribute('onclick', "addButtonLoad(" + nb_inp + ")");

		var label = document.createElement('label');
		label.htmlFor = "text" + nb_inp;
		label.appendChild(document.createTextNode('Convert to DwC format'));

		div.appendChild(checkbox);
		div.appendChild(label);
		
		formulaire.insertBefore(div, buttonADD);
		//document.getElementById('divText' + nb_inp).innerHTML = "Convert to DwC format !";
		
	}
	formulaire.insertBefore(br, buttonADD);

	//increment input counter
	document.getElementById(compteur).value ++;
}

// delete a input file 
function deleteField(compteur, typeInput)
{
	// count inputs number
	var nb_inp = document.getElementById(compteur).value;

	// if inputs exist => delete all
	var min = 0;
	if(typeInput == "inp"){
		min = 1;
	}
	if(nb_inp > min){
		
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
		
		if(typeInput == 'inp'){
			var divText = document.getElementById('divText_' + id);
			formulaire.removeChild(divText);
		}
		//decrement input counter
		document.getElementById(compteur).value --;
	}
}

// add or delete raster files
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
        
	    // if inputs exist => delete all
	    while(nb_raster != 0){
	    	var id = nb_raster - 1;
	        var raster = document.getElementById('raster' + id);
	        var header = document.getElementById('header' + id);
	        var br = document.getElementById('br_raster_' + id);

	        formulaire.removeChild(raster);
	        formulaire.removeChild(header);
	        formulaire.removeChild(br);
	     
	        //decrement input counter
	        document.getElementById('compteur_raster').value --;
	        document.getElementById('compteur_header').value --;
	        nb_header--;
	        nb_raster--;
	    }
	 	// delete buttons "add" and "delete"
	    //document.getElementById('compteur_raster').value = 0;
	    formulaire.removeChild(brAfter);
	    formulaire.removeChild(add);
	    formulaire.removeChild(del);
	}
}

// add a new input file : raster or dataset
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

// initialise all establishmentMeans option to "false" if main checkbox doesn't checked
function established() {
	var establishmentIsChecked = document.getElementById("establishment").checked;
	if(!establishmentIsChecked){
		
		document.getElementById("native").checked = false;
		document.getElementById("introduced").checked = false;
		document.getElementById("naturalised").checked = false;
		document.getElementById("invasive").checked = false;
		document.getElementById("managed").checked = false;
		document.getElementById("uncertain").checked = false;
	}
}

// check option establishmentMeans
function checkEstablishment(){
	
	//var establishmentIsChecked = document.getElementById("establishment").checked;
	var native = document.getElementById("native").checked;
	var introduced = document.getElementById("introduced").checked;
	var naturalised = document.getElementById("naturalised").checked;
	var invasive = document.getElementById("invasive").checked;
	var managed = document.getElementById("managed").checked;
	var uncertain = document.getElementById("uncertain").checked;
	var others = document.getElementById("others").checked;
	
	if(native || introduced || naturalised || invasive || managed || uncertain || others){
		document.getElementById("establishment").checked = true;
	}
	if(!native && !introduced && !naturalised && !invasive && !managed && !uncertain && !others){
		document.getElementById("establishment").checked = false;
	}
}

// add a new element with all parameters
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

// when click on load button submit => not necessary now
function mappingTable(div) {
	//var formulaire = document.getElementById('formulaire');
	var mappingTable = document.createElement('table');
	mappingTable.id = "mappingTable";
	var divConvert = document.getElementById(div);
	divConvert.appendChild(mappingTable);
	var arrayLignes = document.getElementById("mappingTable").rows; //l'array est stocké dans une variable
	var longueur = arrayLignes.length;//on peut donc appliquer la propriété length
	var i=0; //on définit un incrémenteur qui représentera la clé

	while(i<longueur)
	{
		if(i % 2 == 0)//si la clé est paire
		{
			arrayLignes[i].style.backgroundColor = "#bdcbf5";
		}
		else //elle est impaire
		{
			arrayLignes[i].style.backgroundColor = "#829eeb";
		}
		i++;
	}
}

// when click on "convert" checkbox
function addButtonLoad(counter){
	var div = "divText_" + counter;
	var divConvert = document.getElementById(div);
	var loadExist = document.getElementById('loadFile_' + counter);
	var convert = document.getElementById('convert_' + counter).cheked;
	
	if(loadExist == null){

		var buttonLoad = document.createElement('input');
		buttonLoad.type = "submit";
		buttonLoad.id = "loadFile_" + counter;
		buttonLoad.name = "loadFile_" + counter;
		buttonLoad.class = "button";
		buttonLoad.value = "Load file for mapping";
		buttonLoad.onclick="javascript:mappingTable(" + div + ")";

		var br = document.createElement('br');
		br.id = "brLoad_" + counter;
		divConvert.appendChild(br);
		divConvert.appendChild(buttonLoad);
		//formulaire.insertBefore(div, buttonADD);
	}
	else{
		if(!convert){
			
			var buttonLoad = document.getElementById("loadFile_"+counter);
			divConvert.removeChild(buttonLoad);
			var brLoad = document.getElementById("brLoad_"+counter);
			divConvert.removeChild(brLoad);
		}
	}
}