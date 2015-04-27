function initialise(){
	document.getElementById('compteur_inp').value = 1;
	document.getElementById('compteur_raster').value = 0;
	document.getElementById('compteur_header').value = 0;
}
//var xhr = getXMLHttpRequest();

//add a new input file
function addField(compteur, idAdd, typeInput){
	//count inputs number
	var nb_inp = document.getElementById(compteur).value;

	//Create a new input file 
	var inp = document.createElement('input');
	inp.setAttribute('type', 'file');
	inp.setAttribute('id', typeInput + "_" + nb_inp);
	inp.setAttribute('name', typeInput + "_" + nb_inp);
	inp.setAttribute('style', 'color:rgb(51,153,255); border-color:rgb(255,255,255); border-style:solid;" size="100"');

	if(typeInput == "inp"){
		inp.setAttribute('onchange', 'loadInputFile('+nb_inp+')');

		// add all new elements to the formulary
		var buttonADD = document.getElementById(idAdd);
		var formulaire = document.getElementById('formulaire');
		formulaire.insertBefore(inp, buttonADD);

		var div = document.createElement('div');
		div.setAttribute('id', 'divLoad_' + nb_inp);
		div.setAttribute('style', "margin-left: 40px");

		formulaire.insertBefore(div, buttonADD);
	}
	if(typeInput == "raster"){

		var tableRaster = document.getElementById("rasterTable");
		var nb_header = document.getElementById("compteur_header").value;
		var header = document.createElement('input');
		header.setAttribute('type', 'file');
		header.setAttribute('id', 'header_' + nb_header);
		header.setAttribute('name', 'header_' + nb_header);
		header.setAttribute('style', 'color:rgb(51,153,255); border-color:rgb(255,255,255); border-style:solid;" size="100"');

		var rowInputRaster = tableRaster.insertRow(-1);
		var rowNb = parseInt(nb_header) + 2;
		rowInputRaster.id = "row_raster_" + rowNb;
		rowInputRaster.name = "row_raster_" + rowNb;

		var cellInputRaster = rowInputRaster.insertCell(0);
		cellInputRaster.appendChild(inp);
		var cellInputHeader = rowInputRaster.insertCell(1);
		cellInputHeader.appendChild(header);

		document.getElementById("compteur_header").value ++;
	}	

	//increment input counter
	document.getElementById(compteur).value ++;
}

//delete a input file 
function deleteField(compteur, typeInput){
	// count inputs number
	var nb_inp = document.getElementById(compteur).value;
	var formulaire = document.getElementById('formulaire');
	// if inputs exist => delete all
	var min = 0;
	if(typeInput == "inp"){
		min = 1;
	}
	if(nb_inp > min){
		var id = nb_inp - 1;
		var inp = document.getElementById(typeInput + "_" + id);

		if(typeInput == 'raster'){
			var tableRaster = document.getElementById("rasterTable");
			tableRaster.deleteRow(-1);
			document.getElementById('compteur_header').value --;
		}
		if(typeInput == 'inp'){
			formulaire.removeChild(inp);
			var divLoad = document.getElementById('divLoad_' + id);
			formulaire.removeChild(divLoad);
		}
		//decrement input counter
		document.getElementById(compteur).value --;
	}
}

//add or delete raster files
function addDeleteRasterFile(){
	var rasterOption = document.getElementById('raster').checked;
	var nb_raster = document.getElementById('compteur_raster').value;
	var divRasterFiles = document.getElementById("divRasterFiles");

	if(rasterOption){
		divRasterFiles.style.display = "block";
		if(nb_raster == 0){
			var tableRaster = document.getElementById("rasterTable");

			if(tableRaster == null){

				//divRasterFiles.id = "divRasterFiles";
				var addRasterButton = document.createElement("input");
				addRasterButton.id = "addRaster";
				addRasterButton.name = "addRaster";
				addRasterButton.type = "button";
				addRasterButton.style = "color:rgb(204,0,153); border-color:rgb(255,255,255); border-style:solid;";
				addRasterButton.size="51";
				addRasterButton.value = "Add a new raster file";
				addRasterButton.setAttribute("onclick", "addField(\'compteur_raster\',\'tdwg4\',\'raster\')");

				var delRasterButton = document.createElement("input");
				delRasterButton.id = "delRaster";
				delRasterButton.name = "delRaster";
				delRasterButton.type = "button";
				delRasterButton.style = "color:rgb(204,0,153); border-color:rgb(255,255,255); border-style:solid;";
				delRasterButton.size = "51";
				delRasterButton.value = "Delete a raster file";
				delRasterButton.setAttribute("onclick","deleteField(\'compteur_raster\',\'raster\')");

				var tableRaster = document.createElement("table");
				tableRaster.id = "rasterTable";
				tableRaster.name = "rasterTable";

				var row = tableRaster.insertRow(-1);
				row.id = "row_raster_0";
				row.name = "row_raster_0";

				var cell_0 = row.insertCell(0);
				cell_0.appendChild(addRasterButton);

				var cell_1 = row.insertCell(1);
				cell_1.appendChild(delRasterButton);

				tableRaster.appendChild(row);

				var rowInfo = tableRaster.insertRow(-1);
				rowInfo.id = "row_raster_1";
				rowInfo.name = "row_raster_1";

				var cell_2 = rowInfo.insertCell(0);
				cell_2.innerHTML = "Raster file";
				var cell_3 = rowInfo.insertCell(1);
				cell_3.innerHTML = "Header file";

				divRasterFiles.appendChild(tableRaster);
			}
		}
	}
	else{
		divRasterFiles.style.display = "none";
	}
}

//initialise all establishmentMeans option to "false" if main checkbox doesn't checked
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

//check option establishmentMeans
function checkEstablishment(){

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

//add a new element with all parameters
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

//when click on "convert" checkbox
function loadInputFile(counter){
	var divLoadName = "divLoad_" + counter;
	var divLoad = document.getElementById(divLoadName);
	var fileExist = document.getElementById('inp_' + counter);
	var convertButton = document.getElementById('convert_'+ counter);
	var buttonConvert = null;
	var fileChanging = null;
	if(fileExist.value != null){
		if(!convertButton){
			buttonConvert = document.createElement('input');
			buttonConvert.type = "button";
			buttonConvert.id = "convert_" + counter;
			buttonConvert.name = "convert_" + counter;
			buttonConvert.value = "Load file for mapping";
			buttonConvert.setAttribute("onclick" , "loadInputFile(" + counter + ")");
			fileChanging = "true";
			divLoad.appendChild(buttonConvert);
		}
		else{
			buttonConvert = document.getElementById("convert_" + counter);
		}
		
		var fileInput = document.querySelector('#inp_' + counter);
		buttonConvert.addEventListener('click', function() {
			var reader = new FileReader();
			reader.addEventListener('load', function() {
				readInputFile(reader.result, counter, fileChanging);
			}, false);
			reader.readAsText(fileInput.files[0]);
		}, false);
		

	}
}

function readInputFile(contentFile, nbInput, fileChanging){
	var length = contentFile.split('\n')[0].split(',').length;
	var firstLine = contentFile.split('\n')[0].split(',');
	console.log(firstLine);
	//console.log(contentFile.split('\n')[0].split(','));
	//firstLine[length] = " ";
	var dwcTags = [" ","abstract","acceptedNameUsage","acceptedNameUsageID","accessRights","accrualMethod","accrualPeriodicity","accrualPolicy","alternative","associatedMedia","associatedOccurrences","associatedOrganisms","associatedReferences","associatedSequences","associatedTaxa","audience","available","basisOfRecord","bed","behavior","bibliographicCitation","catalogNumber","class","classKey","collectionCode","collectionID","conformsTo","continent","contributor","coordinateAccuracy","coordinatePrecision","coordinateUncertaintyInMeters","country","countryCode","county","coverage","created","creator","dataGeneralizations","datasetID","datasetKey","datasetName","date","dateAccepted","dateCopyrighted","dateIdentified","dateSubmitted","day","decimalLatitude","decimalLongitude","depth","depthAccuracy","description","disposition","distanceAboveSurface","distanceAboveSurfaceAccuracy","dynamicProperties","earliestAgeOrLowestStage","earliestEonOrLowestEonothem","earliestEpochOrLowestSeries","earliestEraOrLowestErathem","earliestPeriodOrLowestSystem","educationLevel","elevation","elevationAccuracy","endDayOfYear","establishmentMeans","event","eventDate","eventID","eventRemarks","eventTime","extent","family","familyKey","fieldNotes","fieldNumber","footprintSpatialFit","footprintSRS","footprintWKT","format","formation","gbifID","genericName","genus","genusKey","geodeticDatum","geologicalContext","geologicalContextID","georeferencedBy","georeferencedDate","georeferenceProtocol","georeferenceRemarks","georeferenceSources","georeferenceVerificationStatus","group","habitat","hasCoordinate","hasFormat","hasGeospatialIssues","hasPart","hasVersion","higherClassification","higherGeography","higherGeographyID","highestBiostratigraphicZone","identification","identificationID","identificationQualifier","identificationReferences","identificationRemarks","identificationVerificationStatus","identifiedBy","identifier","idFile","individualCount","individualID","informationWithheld","infraspecificEpithet","institutionCode","institutionID","instructionalMethod","isFormatOf","island","islandGroup","isPartOf","isReferencedBy","isReplacedBy","isRequiredBy","issue","issued","isVersionOf","kingdom","kingdomKey","language","lastCrawled","lastInterpreted","lastParsed","latestAgeOrHighestStage","latestEonOrHighestEonothem","latestEpochOrHighestSeries","latestEraOrHighestErathem","latestPeriodOrHighestSystem","license","lifeStage","lithostratigraphicTerms","livingSpecimen","locality","locationAccordingTo","locationID","locationRemarks","lowestBiostratigraphicZone","machineObservation","materialSample","materialSampleID","maximumDepthinMeters","maximumDistanceAboveSurfaceInMeters","maximumElevationInMeters","measurementAccuracy","measurementDeterminedBy","measurementDeterminedDate","measurementID","measurementMethod","measurementOrFact","measurementRemarks","measurementType","measurementUnit","mediator","mediaType","medium","member","minimumDepthinMeters","minimumDistanceAboveSurfaceInMeters","minimumElevationInMeters","modified","month","municipality","nameAccordingTo","nameAccordingToID","namePublishedIn","namePublishedInID","namePublishedInYear","nomenclaturalCode","nomenclaturalStatus","occurrence","occurrenceDetails","occurrenceID","occurrenceRemarks","occurrenceStatus","order","orderKey","organism","organismID","organismName","organismRemarks","organismScope","originalNameUsage","originalNameUsageID","otherCatalogNumbers","ownerInstitutionCode","parentNameUsage","parentNameUsageID","phylum","phylumKey","pointRadiusSpatialFit","preparations","preservedSpecimen","previousIdentifications","protocol","provenance","publisher","publishingCountry","recordedBy","recordNumber","references","relatedResourceID","relationshipAccordingTo","relationshipEstablishedDate","relationshipRemarks","relation","replaces","reproductiveCondition","requires","resourceID","resourceRelationship","resourceRelationshipID","rights","rightsHolder","samplingEffort","samplingProtocol","scientificName","scientificNameAuthorship","scientificNameID","sex","source","spatial","species","speciesKey","specificEpithet","startDayOfYear","stateProvince","subgenus","subgenusKey","subject","tableOfContents","taxon","taxonConceptID","taxonID","taxonKey","taxonomicStatus","taxonRank","taxonRemarks","temporal","title","type","typeStatus","typifiedName","valid","verbatimCoordinates","verbatimCoordinateSystem","verbatimDate","verbatimDepth","verbatimElevation","verbatimEventDate","verbatimLatitude","verbatimLocality","verbatimLongitude","verbatimSRS","verbatimTaxonRank","vernacularName","waterBody","year"];
	var presentTags = [];
	for(var i = 0; i < firstLine.length; i++){
		if(jQuery.inArray(firstLine[i], dwcTags)!=-1){
			presentTags[presentTags.length] = firstLine[i];
		}
	}
	console.log(presentTags);
	createMapping(firstLine, dwcTags, presentTags, nbInput, fileChanging);
}

function createMapping(firstLineInput, dwcTags, presentTags, nbInput, fileChanging){
	var mappingTable = document.getElementById("divMapping_" + nbInput);

	if(mappingTable == null){

		var mappingTable = document.createElement("table");
		mappingTable.id = "mappingTable_" + nbInput;
		mappingTable.name = "mappingTable_" + nbInput;
		mappingTable.border = "0";

		var divLoad = document.getElementById("divLoad_"+nbInput);
		var divMapping = document.createElement("div");
		divMapping.id = "divMapping_" + nbInput;
		divMapping.name = "divMapping_" + nbInput;
		//console.log(firstLineInput);
		for(var i = 0 ; i < firstLineInput.length; i++){
			var tagInput = firstLineInput[i];
			console.log(tagInput);
			var row = mappingTable.insertRow(-1); // insert last line
			row.id = "row_" + i;
			row.name = "row_" + i;
			var cellInput = row.insertCell(0);
			cellInput.innerHTML = tagInput;

			var dropdownDwC = document.createElement("select");
			dropdownDwC.name = "dropdownDwC_" + i;

			var cellDwC = row.insertCell(1);

			for(var j = 0 ; j <  dwcTags.length ; j++){
				var dwcTag = dwcTags[j];
				var optionDwC = document.createElement('option');
				optionDwC.value = dwcTags[j];
				optionDwC.text = dwcTags[j];

				if(tagInput == dwcTag){
					optionDwC.selected = "selected";
				}
				dropdownDwC.appendChild(optionDwC);
			}

			cellDwC.appendChild(dropdownDwC);
			mappingTable.appendChild(row);
			mappingTable.appendChild(cellInput);
			mappingTable.appendChild(cellDwC);

		}

		divMapping.appendChild(mappingTable);

		var brMapping = document.createElement('br');
		brMapping.id = "brMapping_" + nbInput;
		brMapping.name = "brMapping_" + nbInput;
		divMapping.appendChild(brMapping);

		var mapping = document.createElement("input");
		mapping.id = "mappingActive_" + nbInput;
		mapping.name = "mappingActive_" + nbInput;
		mapping.value = "false";
		mapping.type = "hidden";
		divMapping.appendChild(mapping);

		var buttonOK = document.createElement('input');
		buttonOK.id = "okMapping_" + nbInput;
		buttonOK.name = "okMapping_" + nbInput;
		buttonOK.value = "OK";
		buttonOK.type = "button";
		var activeMapping = new Boolean(true);
		buttonOK.setAttribute("onclick", "activeMapping(" + activeMapping +"," + nbInput +")");

		var buttonCancel = document.createElement('input');
		buttonCancel.id = "cancelMapping_" + nbInput;
		buttonCancel.name = "cancelMapping_" + nbInput;
		buttonCancel.value = "Cancel conversion";
		buttonCancel.type = "button";
		buttonCancel.setAttribute('onclick', "deleteMapping("+ nbInput +")");

		divMapping.appendChild(buttonOK);
		divMapping.appendChild(buttonCancel);

		divLoad.appendChild(divMapping);
	}
	else{
		if(fileChanging == "true"){
			var divMapping = document.getElementById("divMapping_" + nbInput);
			var divLoad = document.getElementById("divLoad_" + nbInput);
			divLoad.removeChild(divMapping);
		}
	}
}

function activeMapping(mappingIsActive, nbInput){
	var mappingActive = document.getElementById("mappingActive_" + nbInput);
	if(mappingIsActive){
		mappingActive.value = "true";
	}
	else{
		mappingActive.value = "false";
	}
}
function deleteMapping(nbInput){

	var divMapping = document.getElementById("divMapping_" + nbInput);
	divMapping.style.display="none";
	activeMapping(false, nbInput);
}

/*
function request() {
	var jsonArray = {};
	var xhr = getXMLHttpRequest();
	xhr.open("GET", "inp0.json", true);
	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 200){
			jsonArray = JSON.parse(xhr.responseText);
			var presentTags = jsonArray.inp0[0].presentTags;
			var DwCTags = jsonArray.inp0[1].DwCTags;
			var noMappedTags = jsonArray.inp0[2].noMappedTags;

		}
	};
	xhr.send(null); 

}


function getXMLHttpRequest() {
	var xhr = null;

	if (window.XMLHttpRequest || window.ActiveXObject) {
		if (window.ActiveXObject) {
			try {
				xhr = new ActiveXObject("Msxml2.XMLHTTP");
			} catch(e) {
				xhr = new ActiveXObject("Microsoft.XMLHTTP");
			}
		} else {
			xhr = new XMLHttpRequest(); 
		}
	} else {
		alert("Votre navigateur ne supporte pas l'objet XMLHTTPRequest...");
		return null;
	}

	return xhr;
}

function request(nbInput){
	var fileInput = document.querySelector('#inp' + nbInput);
	var button = document.querySelector('#convert_' + nbInput);
	button.addEventListener('click', function() {
		var reader = new FileReader();
		reader.addEventListener('load', function() {
			alert('Contenu du fichier "' + fileInput.files[0].name + '" :\n\n' + reader.result);
		}, false);
		reader.readAsText(fileInput.files[0]);
	}, false);


	//var convertButton = document.getElementById(convert);
	//var xhr = getXMLHttpRequest();
	//xhr.addEventListener("readystatechange", stateChange, false);
	//xhr.open("POST", "controler", true);	
	//xhr.send(null);
}

*/