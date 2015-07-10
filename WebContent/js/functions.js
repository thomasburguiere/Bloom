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

	var divAddLoad = document.createElement('div');
	divAddLoad.setAttribute('id', "divAddLoad_" + nb_inp);
	divAddLoad.setAttribute("class", "col-lg-12 addLoad");

	//Create a new input file
	var inp = document.createElement('input');
	inp.setAttribute('type', 'file');
	inp.setAttribute('id', typeInput + "_" + nb_inp);
	inp.setAttribute('name', typeInput + "_" + nb_inp);

	if(typeInput == "inp"){
		inp.setAttribute('onchange', 'loadInputFile('+nb_inp+',\"change\")');

		var divAdd = document.createElement('div');
		divAdd.setAttribute('id', "divAdd_" + nb_inp);
		divAdd.setAttribute('class', "col-lg-4");

		var divLoad = document.createElement('div');
		divLoad.setAttribute('id', "divLoad_" + nb_inp);
		divLoad.setAttribute('class', "col-lg-4");

		var divReconcile = document.createElement('div');
		divReconcile.setAttribute('id', "divReconcile_" + nb_inp);
		divReconcile.setAttribute('class', "col-lg-4");

		var bloc_inputs = document.getElementById('bloc-inputs');

		// add all new elements to the formulary
		var divMapping = document.createElement('div');
		divMapping.setAttribute('id', "divMapping_" + nb_inp);
		divMapping.setAttribute('class', "col-lg-12 mapping");

		var divSubmitMapping = document.createElement('div');
		divSubmitMapping.setAttribute('id', "divSubmitMapping_" + nb_inp);
		divSubmitMapping.setAttribute('class', "col-lg-12 submitMapping");
		
		var divReconciliationCheck = document.createElement('div');
		divReconciliationCheck.setAttribute('id', "divReconciliationCheck_" + nb_inp);
		divReconciliationCheck.setAttribute('class', "col-lg-12");
		
		var divSubmitReconcile = document.createElement('div');
		divSubmitReconcile.setAttribute('id', "divSubmitReconcile_" + nb_inp);
		divSubmitReconcile.setAttribute('class', "col-lg-12");
		
		var divTableReconcile = document.createElement('div');
		divTableReconcile.setAttribute('id', "divTableReconcile_" + nb_inp);
		divTableReconcile.setAttribute('class', "col-lg-12");
		divTableReconcile.style.display = "inline-block";
		//divTableReconcile.style="width:auto";
		
		divAdd.appendChild(inp);
		divAddLoad.appendChild(divAdd);
		divAddLoad.appendChild(divLoad);
		divAddLoad.appendChild(divReconcile);

		bloc_inputs.appendChild(divAddLoad);
		bloc_inputs.appendChild(divMapping);
		bloc_inputs.appendChild(divSubmitMapping);
		bloc_inputs.appendChild(divReconciliationCheck);
		bloc_inputs.appendChild(divSubmitReconcile);
		bloc_inputs.appendChild(divTableReconcile);
		

	}
	if(typeInput == "raster"){
		//inp.setAttribute("class", "");
		var tableRaster = document.getElementById("rasterTable");
		var nb_header = document.getElementById("compteur_header").value;
		var header = document.createElement('input');
		header.setAttribute('type', 'file');
		header.setAttribute('id', 'header_' + nb_header);
		header.setAttribute('name', 'header_' + nb_header);
		//header.setAttribute('style', 'color:rgb(51,153,255); border-color:rgb(255,255,255); border-style:solid;" size="100"');

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
	//var formulaire = document.getElementById('formulaire');
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
			var bloc_inputs = document.getElementById("bloc-inputs");
			var divAddLoad = document.getElementById("divAddLoad_" + id);
			var divAdd = document.getElementById("divAdd_" + id);
			var divMapping = document.getElementById("divMapping_" + id);
			var divSubmitMapping = document.getElementById("divSubmitMapping_" + id);

			//divAdd.removeChild(inp);
			//divAddLoad.removeChild(divAdd);

			bloc_inputs.removeChild(divAddLoad);
			bloc_inputs.removeChild(divMapping);
			bloc_inputs.removeChild(divSubmitMapping);

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
				addRasterButton.class = "btn btn-default dropdown-toggle";
				//addRasterButton.style = "color:rgb(204,0,153); border-color:rgb(255,255,255); border-style:solid;";
				addRasterButton.size="51";
				addRasterButton.value = "Add a new raster file";
				addRasterButton.setAttribute("onclick", "addField(\'compteur_raster\',\'tdwg4\',\'raster\')");

				var delRasterButton = document.createElement("input");
				delRasterButton.id = "delRaster";
				delRasterButton.name = "delRaster";
				delRasterButton.type = "button";
				//delRasterButton.style = "color:rgb(204,0,153); border-color:rgb(255,255,255); border-style:solid;";
				delRasterButton.size = "51";
				delRasterButton.value = "Delete a raster file";
				delRasterButton.setAttribute("onclick","deleteField(\'compteur_raster\',\'raster\')");

				var tableRaster = document.createElement("table");
				tableRaster.id = "rasterTable";
				tableRaster.name = "rasterTable";
				tableRaster.setAttribute("class", "table table-striped");
				divRasterFiles.appendChild(tableRaster);

				this.addLine(addRasterButton,delRasterButton, "row_raster_0");
				this.addLine("Raster file","Header file","row_raster_1");				


			}
		}
	}
	else{
		divRasterFiles.style.display = "none";
	}
}

function addLine(val1,val2, idLine){

	// On créé le corps du tableau
	TBODY = document.createElement ("tbody");
	THEAD = document.createElement('thead');

	// On créé la ligne
	TR = document.createElement ("tr");
	TR.setAttribute("id", idLine);

	// On créé la premiere cellule
	TD1  = document.createElement ("td");
	if(idLine == "row_raster_1"){
		TXT1 = document.createTextNode (val1);
		TD1.appendChild (TXT1);
	}
	else{
		TD1.appendChild(val1);
	}


	// On créé la deuxieme cellule
	TD2  = document.createElement ("td");
	if(idLine == "row_raster_1"){
		TXT2 = document.createTextNode (val2);
		TD2.appendChild (TXT2);
	}
	else{
		TD2.appendChild(val2);
	}


	// On assemble les cellules a la ligne
	TR.appendChild(TD1);
	TR.appendChild(TD2);

	if(idLine != "row_raster_0"){
		// On assemble la ligne au corps du tableau
		TBODY.appendChild(TR);

		// On assemble le corps du tableau au tableau
		document.getElementById ('rasterTable').appendChild (TBODY);
	}
	else{
		THEAD.appendChild(TR);

		// On assemble le corps du tableau au tableau
		document.getElementById ('rasterTable').appendChild (THEAD);
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
var columns = "";
function mappingDWC(counter, change_load_reconcile){
	var convertButton = document.getElementById('convert_'+ counter);
	var divAddLoad = document.getElementById("divAddLoad_" + counter);
	var buttonConvert = null;
	if(!convertButton){
		var divLoad = document.getElementById("divLoad_" + counter);
		buttonConvert = document.createElement('input');
		buttonConvert.type = "button";
		buttonConvert.id = "convert_" + counter;
		buttonConvert.name = "convert_" + counter;
		buttonConvert.value = "Load file for mapping";
		buttonConvert.setAttribute("onclick" , "loadInputFile(" + counter + ",\"load\")");
		divLoad.appendChild(buttonConvert);
		divAddLoad.appendChild(divLoad);
	}
	else{
		buttonConvert = document.getElementById("convert_" + counter);
		var divMapping = document.getElementById("divMapping_" + counter);
		var tableMapping = document.getElementById("mappingTable_" + counter);
		var divSubmitMapping = document.getElementById("divSubmitMapping_" + counter);
		var divSubmitMappingOK = document.getElementById("divSubmitMappingOK_" + counter);
		var divSubmitMappingCancel = document.getElementById("divSubmitMappingCancel_" + counter);
		var mappingActive = document.getElementById("mappingActive_" + counter);
		var divMessageSaved = document.getElementById("divMessageSaved_" + counter);
		var divMessagedCancelled = document.getElementById("divMessageCancelled_" + counter);
		
		var divReconciliationCheck = document.getElementById("divReconciliationCheck_" + counter);
		var divSubmitReconcile = document.getElementById("divSubmitReconcile_" + counter);
		
		var divButtonStartReconciliation = document.getElementById("divButtonStartReconciliation_" + counter);
		var divButtonCancelReconciliation = document.getElementById("divButtonCancelReconciliation_" + counter);
		var divButtonValidReconciliation = document.getElementById("divButtonValidReconciliation_" + counter);
		
		
		var divTableReconcile = document.getElementById("divTableReconcile_" + counter);
		var tableReconcile = document.getElementById("tableReconcile_" + counter);
		var tablePreparationReconcile = document.getElementById("tablePreparationReconcile_" + counter);
		
		if(change_load_reconcile == "load"){
			if(tableMapping){
				divMapping.style.display = "block";
				tableMapping.style.display="block";
				divSubmitMapping.style.display = "block";
				divSubmitMappingOK.style.display = "block";
				divSubmitMappingCancel.style.display ="block";
				divMessageSaved.style.display = "none";
				divMessagedCancelled.style.display = "none";
			}
		}
		if(change_load_reconcile == "change"){
			divMapping.removeChild(tableMapping);
			divMapping.removeChild(mappingActive);

			divSubmitMapping.removeChild(divSubmitMappingOK);
			divSubmitMapping.removeChild(divSubmitMappingCancel);
			divSubmitMapping.removeChild(divMessageSaved);
			divSubmitMapping.removeChild(divMessagedCancelled);
			
			divReconciliationCheck.removeChild(tablePreparationReconcile);
			divSubmitReconcile.removeChild(divButtonCancelReconciliation);
			divSubmitReconcile.removeChild(divButtonStartReconciliation);
			divSubmitReconcile.removeChild(divButtonValidReconciliation);
			
			divTableReconcile.removeChild(tableReconcile);
		}

	}

	var fileInput = document.querySelector('#inp_' + counter);

	buttonConvert.addEventListener('click', function() {
		var reader = new FileReader();
		reader.addEventListener('load', function() {
			readInputFile(reader.result, counter);
		}, false);
		reader.readAsText(fileInput.files[0]);

	}, false);	
}

//when click on "convert" checkbox
function loadInputFile(counter, change_load_reconcile){

	var fileExist = document.getElementById('inp_' + counter);

	if(fileExist.value != null){
		var columns = this.mappingDWC(counter, change_load_reconcile);
		taxonReconciliation(columns, counter, change_load_reconcile);
	}	
}

function readInputFile(contentFile, nbInput){
	var length = contentFile.split('\n')[0].split(',').length;
	var firstLine = contentFile.split('\n')[0].split(',');
	//console.log(firstLine);
	//console.log(contentFile.split('\n')[0].split(','));
	//firstLine[length] = " ";
	var dwcTags = [" ","abstract","acceptedNameUsage","acceptedNameUsageID","accessRights","accrualMethod","accrualPeriodicity","accrualPolicy","alternative","associatedMedia","associatedOccurrences","associatedOrganisms","associatedReferences","associatedSequences","associatedTaxa","audience","available","basisOfRecord","bed","behavior","bibliographicCitation","catalogNumber","class","classKey","collectionCode","collectionID","conformsTo","continent","contributor","coordinateAccuracy","coordinatePrecision","coordinateUncertaintyInMeters","country","countryCode","county","coverage","created","creator","dataGeneralizations","datasetID","datasetKey","datasetName","date","dateAccepted","dateCopyrighted","dateIdentified","dateSubmitted","day","decimalLatitude","decimalLongitude","depth","depthAccuracy","description","disposition","distanceAboveSurface","distanceAboveSurfaceAccuracy","dynamicProperties","earliestAgeOrLowestStage","earliestEonOrLowestEonothem","earliestEpochOrLowestSeries","earliestEraOrLowestErathem","earliestPeriodOrLowestSystem","educationLevel","elevation","elevationAccuracy","endDayOfYear","establishmentMeans","event","eventDate","eventID","eventRemarks","eventTime","extent","family","familyKey","fieldNotes","fieldNumber","footprintSpatialFit","footprintSRS","footprintWKT","format","formation","gbifID","genericName","genus","genusKey","geodeticDatum","geologicalContext","geologicalContextID","georeferencedBy","georeferencedDate","georeferenceProtocol","georeferenceRemarks","georeferenceSources","georeferenceVerificationStatus","group","habitat","hasCoordinate","hasFormat","hasGeospatialIssues","hasPart","hasVersion","higherClassification","higherGeography","higherGeographyID","highestBiostratigraphicZone","identification","identificationID","identificationQualifier","identificationReferences","identificationRemarks","identificationVerificationStatus","identifiedBy","identifier","idFile","individualCount","individualID","informationWithheld","infraspecificEpithet","institutionCode","institutionID","instructionalMethod","isFormatOf","island","islandGroup","isPartOf","isReferencedBy","isReplacedBy","isRequiredBy","issue","issued","isVersionOf","kingdom","kingdomKey","language","lastCrawled","lastInterpreted","lastParsed","latestAgeOrHighestStage","latestEonOrHighestEonothem","latestEpochOrHighestSeries","latestEraOrHighestErathem","latestPeriodOrHighestSystem","license","lifeStage","lithostratigraphicTerms","livingSpecimen","locality","locationAccordingTo","locationID","locationRemarks","lowestBiostratigraphicZone","machineObservation","materialSample","materialSampleID","maximumDepthinMeters","maximumDistanceAboveSurfaceInMeters","maximumElevationInMeters","measurementAccuracy","measurementDeterminedBy","measurementDeterminedDate","measurementID","measurementMethod","measurementOrFact","measurementRemarks","measurementType","measurementUnit","mediator","mediaType","medium","member","minimumDepthinMeters","minimumDistanceAboveSurfaceInMeters","minimumElevationInMeters","modified","month","municipality","nameAccordingTo","nameAccordingToID","namePublishedIn","namePublishedInID","namePublishedInYear","nomenclaturalCode","nomenclaturalStatus","occurrence","occurrenceDetails","occurrenceID","occurrenceRemarks","occurrenceStatus","order","orderKey","organism","organismID","organismName","organismRemarks","organismScope","originalNameUsage","originalNameUsageID","otherCatalogNumbers","ownerInstitutionCode","parentNameUsage","parentNameUsageID","phylum","phylumKey","pointRadiusSpatialFit","preparations","preservedSpecimen","previousIdentifications","protocol","provenance","publisher","publishingCountry","recordedBy","recordNumber","references","relatedResourceID","relationshipAccordingTo","relationshipEstablishedDate","relationshipRemarks","relation","replaces","reproductiveCondition","requires","resourceID","resourceRelationship","resourceRelationshipID","rights","rightsHolder","samplingEffort","samplingProtocol","scientificName","scientificNameAuthorship","scientificNameID","sex","source","spatial","species","speciesKey","specificEpithet","startDayOfYear","stateProvince","subgenus","subgenusKey","subject","tableOfContents","taxon","taxonConceptID","taxonID","taxonKey","taxonomicStatus","taxonRank","taxonRemarks","temporal","title","type","typeStatus","typifiedName","valid","verbatimCoordinates","verbatimCoordinateSystem","verbatimDate","verbatimDepth","verbatimElevation","verbatimEventDate","verbatimLatitude","verbatimLocality","verbatimLongitude","verbatimSRS","verbatimTaxonRank","vernacularName","waterBody","year"];
	var presentTags = [];
	for(var i = 0; i < firstLine.length; i++){
		if(jQuery.inArray(firstLine[i], dwcTags)!=-1){
			presentTags[presentTags.length] = firstLine[i];
		}
	}
	
	createMapping(firstLine, dwcTags, presentTags, nbInput);
		
	

	return firstLine;
}


function createMapping(firstLineInput, dwcTags, presentTags, nbInput){
	var mappingTable = document.getElementById("mappingTable_" + nbInput);
	var divSubmitMapping = document.getElementById("divSubmitMapping_" + nbInput);
	var divMapping = document.getElementById("divMapping_" + nbInput);
	divMapping.style.display = "block";

	if(mappingTable == null){

		var mappingTable = document.createElement("table");
		mappingTable.id = "mappingTable_" + nbInput;
		mappingTable.name = "mappingTable_" + nbInput;
		mappingTable.border = "0";
		mappingTable.setAttribute('class', "tableMapping");

		for(var i = 0 ; i < firstLineInput.length; i++){
			var tagInput = firstLineInput[i];
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

		var divSubmitOK = document.createElement('div');
		divSubmitOK.setAttribute('id', "divSubmitMappingOK_" + nbInput);
		divSubmitOK.setAttribute('class', "col-lg-6");	
		divSubmitOK.setAttribute('value', 'false');
		divSubmitOK.appendChild(buttonOK);

		var divSubmitMappingCancel = document.createElement('div');
		divSubmitMappingCancel.setAttribute('id', "divSubmitMappingCancel_" + nbInput);
		divSubmitMappingCancel.setAttribute('class', "col-lg-6");
		divSubmitMappingCancel.setAttribute('value', "false");
		divSubmitMappingCancel.appendChild(buttonCancel);

		var divMessageSaved = document.createElement('div');
		divMessageSaved.setAttribute('id', "divMessageSaved_" + nbInput);
		divMessageSaved.setAttribute('class', "col-lg-12 text-success text-center");
		divMessageSaved.style.display = "none";
		var messageSaved = document.createElement('p');
		messageSaved.innerHTML = "Mapping saved";
		divMessageSaved.appendChild(messageSaved);

		var divMessageCancelled = document.createElement('div');
		divMessageCancelled.setAttribute('id', "divMessageCancelled_" + nbInput);
		divMessageCancelled.setAttribute('class', "col-lg-12 text-danger text-center");
		divMessageCancelled.style.display = "none";
		var messageCancelled = document.createElement('p');
		//messageCancelled.setAttribute('class', "has-warning");
		messageCancelled.innerHTML = "Mapping cancelled";
		divMessageCancelled.appendChild(messageCancelled);

		divSubmitMapping.appendChild(divSubmitOK);
		divSubmitMapping.appendChild(divSubmitMappingCancel);
		divSubmitMapping.appendChild(divMessageSaved);
		divSubmitMapping.appendChild(divMessageCancelled);

	}
}

function activeMapping(mappingIsActive, nbInput){
	var mappingActive = document.getElementById("mappingActive_" + nbInput);
	var divSubmitMappingOK = document.getElementById("divSubmitMappingOK_" + nbInput);
	var divSubmitMappingCancel = document.getElementById("divSubmitMappingCancel_" + nbInput);
	var divMessageSaved = document.getElementById("divMessageSaved_" + nbInput);
	var divMessageCancelled = document.getElementById("divMessageCancelled_" + nbInput);
	var divMapping = document.getElementById("divMapping_" + nbInput);

	if(mappingIsActive){
		mappingActive.value = "true";
		divSubmitMappingOK.value = "true";
		divSubmitMappingCancel.value = "true";
		divMessageSaved.style.display = "block";
		divMessageCancelled.style.display = "none";
		divMapping.style.display = "none";
		divSubmitMappingCancel.style.display = "none";
		divSubmitMappingOK.style.display = "none";
	}
	else{
		mappingActive.value = "false";
		divSubmitMappingOK.value = "false";
		divSubmitMappingCancel.value = "false";
		divMessageSaved.style.display = "none";
		divMessageCancelled.style.display = "block";
		divSubmitMappingCancel.style.display = "none";
		divSubmitMappingOK.style.display = "none";
	}
}

function deleteMapping(nbInput){

	var divMapping = document.getElementById("divMapping_" + nbInput);
	divMapping.style.display="none";

	var divSubmitMapping = document.getElementById("divSubmitMapping_" + nbInput);
	var divSubmitMappingOK = document.getElementById("divSubmitMappingOK_" + nbInput);
	var divSubmitMappingCancel = document.getElementById("divSubmitMappingCancel_" + nbInput);
	var divMessageCancelled = document.getElementById("divMessageCancelled_" + nbInput);
	//divSubmitMapping.style.display = "none";
	divSubmitMappingOK.style.display = "none";
	divSubmitMappingCancel.style.display = "none";
	divMessageCancelled.style.display = "block";
	activeMapping(false, nbInput);
}