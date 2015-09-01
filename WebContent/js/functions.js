function initialise() {
	document.getElementById('compteur_inp').value = 1;
	document.getElementById('compteur_raster').value = 0;
	document.getElementById('compteur_header').value = 0;
}
//var xhr = getXMLHttpRequest();

//add a new input file
function addField(compteur, idAdd, typeInput) {
	//count inputs number
	var nb_inp = document.getElementById(compteur).value;

	var divAddLoad = document.createElement('div');
	divAddLoad.setAttribute('id', "divAddLoad_" + nb_inp);
	divAddLoad.setAttribute("class", "col-lg-12 center");
    //divAddLoad.setAttribute("style", "margin-top: 10px; border-bottom-width: 10px; border-style:solid; border-width:1px; border-radius:5px; border-color:LightGray");
    //divAddLoad.setAttribute("role", "group");
    
	//Create a new input file
	var inp = document.createElement('input');
	inp.setAttribute('type', 'file');
	inp.setAttribute('id', typeInput + "_" + nb_inp);
	inp.setAttribute('name', typeInput + "_" + nb_inp);

	if(typeInput == "inp"){
        var globalInput = document.createElement("div");
        globalInput.setAttribute('class', "col-lg-12 global");
        globalInput.setAttribute('id', "globalInput_" + nb_inp);
        
        this.createInputSpecial(nb_inp, typeInput, divAddLoad);
        /*
		inp.setAttribute('onchange', 'loadInputFile('+nb_inp+',\"change\")');
    
        var spanInput = document.createElement('span');
        spanInput.innerHTML = "Browse";
       // spanInput.setAttribute('class', "btn btn-file");
        
        var divBTN = document.createElement('div');
        divBTN.setAttribute('class', "btn");
        
        divBTN.appendChild(spanInput);
        divBTN.appendChild(inp);
        
        var divAdd = document.createElement('div');
		divAdd.setAttribute('id', "divAdd_" + nb_inp);
		divAdd.setAttribute('class', "file-field input-field col-lg-4");
        divAdd.setAttribute('role', "group");
        
        divAdd.appendChild(divBTN);
        
        
        
        var divPathWrapper = document.createElement('div');
        divPathWrapper.setAttribute('class', "file-path-wrapper");
        
        var inputText = document.createElement('input');
        inputText.type = "text";
        inputText.id = "inputText_" + nb_inp;
        inputText.setAttribute('class', "file-path validate");
        
        divPathWrapper.appendChild(inputText);
        
        divAdd.appendChild(divPathWrapper);
        */
		var divLoad = document.createElement('div');
		divLoad.setAttribute('id', "divLoad_" + nb_inp);
		divLoad.setAttribute('class', "col-lg-4 marges");
        //divLoad.setAttribute('role', "group");
                             
		var divReconcile = document.createElement('div');
		divReconcile.setAttribute('id', "divReconcile_" + nb_inp);
		divReconcile.setAttribute('class', "col-lg-4 marges");
        //divReconcile.setAttribute('role', "group");

		var bloc_inputs = document.getElementById('bloc-inputs');

		// add all new elements to the formulary
		var divMapping = document.createElement('div');
		divMapping.setAttribute('id', "divMapping_" + nb_inp);
		divMapping.setAttribute('class', "col-lg-12");

		var divSubmitMapping = document.createElement('div');
		divSubmitMapping.setAttribute('id', "divSubmitMapping_" + nb_inp);
		
		
		var divReconciliationCheck = document.createElement('div');
		divReconciliationCheck.setAttribute('id', "divReconciliationCheck_" + nb_inp);
		divReconciliationCheck.setAttribute('class', "col-lg-12");
		
		var divSubmitReconcile = document.createElement('div');
		divSubmitReconcile.setAttribute('id', "divSubmitReconcile_" + nb_inp);
		divSubmitReconcile.setAttribute('class', "col-lg-12 center");
		
		var divTableReconcile = document.createElement('div');
		divTableReconcile.setAttribute('id', "divTableReconcile_" + nb_inp);
		divTableReconcile.setAttribute('class', "col-lg-12 dataTable");
		divTableReconcile.style.display = "inline-block";
		
        
		//divAdd.appendChild(spanInput);
		//divAddLoad.appendChild(divAdd);
		divAddLoad.appendChild(divLoad);
		divAddLoad.appendChild(divReconcile);
        
        bloc_inputs.appendChild(globalInput);
        globalInput.appendChild(divAddLoad);
        
		globalInput.appendChild(divMapping);
		globalInput.appendChild(divSubmitMapping);
        divSubmitMapping.setAttribute('class', "col-lg-12 center");
        //divSubmitMapping.setAttribute('role', "group");
        
		globalInput.appendChild(divTableReconcile);
		globalInput.appendChild(divReconciliationCheck);
		globalInput.appendChild(divSubmitReconcile);
		//globalInput.setAttribute("style","margin-top: 10px; border-bottom-width: 10px; border-style:solid; border-width:1px; border-radius:5px; border-color:LightGray; display:inline-block;");
		

	}
	if(typeInput == "raster"){
		//inp.setAttribute("class", "");
		var tableRaster = document.getElementById("rasterTable");
		var nb_header = document.getElementById("compteur_header").value;
		    
		var rowInputRaster = tableRaster.insertRow(-1);
		var rowNb = parseInt(nb_header) + 2;
		rowInputRaster.id = "row_raster_" + rowNb;
		rowInputRaster.name = "row_raster_" + rowNb;
        
		var cellInputRaster = rowInputRaster.insertCell(0);
		
        this.createInputSpecial(nb_header, typeInput, cellInputRaster);
        
        var cellInputHeader = rowInputRaster.insertCell(1);
        this.createInputSpecial(nb_header, "header", cellInputHeader);
        
		document.getElementById("compteur_header").value ++;
	}	

	//increment input counter
	document.getElementById(compteur).value ++;
}

function createInputSpecial(nbInput, typeInput, container){
    var divContainer = document.createElement('div');
    
    
    var divBtn = document.createElement('div');   
    divBtn.setAttribute('class', 'btn');
    var span = document.createElement('span');
    span.innerHTML = "Browse";
    divBtn.appendChild(span);
    
    var inputLoad = document.createElement('input');
    inputLoad.setAttribute('type', 'file');
    inputLoad.setAttribute('id', typeInput + "_" + nbInput);
    inputLoad.setAttribute('name', typeInput + "_" + nbInput);
    divBtn.appendChild(inputLoad);
    
    var divFilePathWrapper = document.createElement('div');
    divFilePathWrapper.setAttribute('class','file-path-wrapper');
    var inputText = document.createElement('input');
    inputText.setAttribute('type', 'text');
    inputText.setAttribute('id', "text_" + typeInput + '_' + nbInput);
    inputText.setAttribute('name', "text_" + typeInput + '_' + nbInput);
    inputText.setAttribute('class', 'file-path validate');
    
    if(typeInput == "raster" || typeInput == "header"){
        divContainer.setAttribute('class', 'file-field input-field');
        inputLoad.setAttribute('onchange', "loadInputRaster(\"" + nbInput + "\",\"" + typeInput + "\")");
    }
    else if(typeInput == "inp"){
        divContainer.setAttribute('class', "file-field input-field col-lg-4");
        divContainer.setAttribute('id', "divAdd_" + nbInput);
        divContainer.setAttribute('role', "group");
        inputLoad.setAttribute('onchange', 'loadInputFile('+nbInput+',\"change\")');
    }
    
    divFilePathWrapper.appendChild(inputText);
    
    divContainer.appendChild(divBtn);
    divContainer.appendChild(divFilePathWrapper);
    
    container.appendChild(divContainer);
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

			var divReconciliationCheck = document.getElementById("divReconciliationCheck_" + id);
			var divTableReconcile = document.getElementById("divTableReconcile_" + id);
			var divSubmitReconcile = document.getElementById("divSubmitReconcile_" + id);
			
            var globalInput = document.getElementById("globalInput_" + id);
			//divAdd.removeChild(inp);
			//divAddLoad.removeChild(divAdd);
            bloc_inputs.removeChild(globalInput);
			//bloc_inputs.removeChild(divAddLoad);
			//bloc_inputs.removeChild(divMapping);
			//bloc_inputs.removeChild(divSubmitMapping);

			 //bloc_inputs.removeChild(divReconciliationCheck);
			//bloc_inputs.removeChild(divTableReconcile);
			//bloc_inputs.removeChild(divSubmitReconcile);
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
				addRasterButton.setAttribute('class',"btn btn-default waves-effect waves-light font-small-button dropdown-toggle");
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
                delRasterButton.setAttribute('class', 'btn btn-default waves-effect waves-light font-small-button');
				delRasterButton.setAttribute("onclick","deleteField(\'compteur_raster\',\'raster\')");

				var tableRaster = document.createElement("table");
				tableRaster.id = "rasterTable";
				tableRaster.name = "rasterTable";
				tableRaster.setAttribute("class", "table table-marges");
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
		buttonConvert = document.createElement('button');
		buttonConvert.type = "button";
		buttonConvert.id = "convert_" + counter;
		buttonConvert.name = "convert_" + counter;
		//buttonConvert.value = "Load file for mapping";
		buttonConvert.setAttribute("onclick" , "loadInputFile(" + counter + ",\"load\")");
        buttonConvert.setAttribute('class', "btn btn-default waves-effect waves-light btn-large font-medium-button");
		divLoad.appendChild(buttonConvert);
		$("#convert_" + counter).text('Load file for mapping');
        divAddLoad.appendChild(divLoad);
        
	}
	else{
		buttonConvert = document.getElementById("convert_" + counter);
		var divMapping = document.getElementById("divMapping_" + counter);
		var tableMapping = document.getElementById("mappingTable_" + counter);
		var divTableReconcile = document.getElementById("divTableReconcile_" + counter);
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
		var tableReconcile = document.getElementById("tableReconcile_" + counter + "_wrapper");
		var tablePrepareReconcile = document.getElementById("tablePrepareReconcile_" + counter);
		var divUrlTaxo = document.getElementById("divUrlTaxo_" + counter);
		
		var divMessageReconcileCancelled  = document.getElementById("divMessageReconcileCancelled_" + counter);
		var divMessageReconcileSaved = document.getElementById("divMessageReconcileSaved_" + counter);
	
		if(change_load_reconcile == "load"){
			if(tableMapping){
				divMapping.style.display = "block";
				//tableMapping.style.display="block";
				divSubmitMapping.style.display = "block";
				divSubmitMappingOK.style.display = "block";
				divSubmitMappingCancel.style.display ="block";
				divMessageSaved.style.display = "none";
				divMessagedCancelled.style.display = "none";
				mappingActive.value = "false";
			}
		}
		if(change_load_reconcile == "change"){
			if(tableMapping){
			    divMapping.removeChild(tableMapping);
			    divMapping.removeChild(mappingActive);

				divSubmitMapping.removeChild(divSubmitMappingOK);
				divSubmitMapping.removeChild(divSubmitMappingCancel);
				divSubmitMapping.removeChild(divMessageSaved);
				divSubmitMapping.removeChild(divMessagedCancelled);
			}
			   
			if(tablePrepareReconcile){
				divReconciliationCheck.removeChild(tablePrepareReconcile);
				divReconciliationCheck.removeChild(divUrlTaxo);
			}
			
			if(divTableReconcile){
				divMessageReconcileSaved.style.display = "none";
				divMessageReconcileCancelled.style.display = "none";
			}
			
			if(divButtonCancelReconciliation){
			    divSubmitReconcile.removeChild(divButtonCancelReconciliation);
			}
			    
			if(divButtonStartReconciliation){
				divSubmitReconcile.removeChild(divButtonStartReconciliation);
			}
			
			if(divButtonValidReconciliation){
			    divSubmitReconcile.removeChild(divButtonValidReconciliation);
			}
			
			if(tableReconcile){
			    divTableReconcile.removeChild(tableReconcile);
			}
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
    var filename = fileExist.value;
    var inputText = document.getElementById("text_inp_" + counter);
    inputText.value = filename;
	if(fileExist.value != null){
		var columns = this.mappingDWC(counter, change_load_reconcile);
		taxonReconciliation(columns, counter, change_load_reconcile);
	}	
}

function loadInputRaster(counter, typeInput){
    var fileExist = document.getElementById(typeInput + '_' + counter);
    //console.log(typeInput + '_' + counter);
    var filename = fileExist.value;
    //console.log("filename : " + filename);
    var inputText = document.getElementById("text_" + typeInput + "_" + counter);
    inputText.value = filename;
}

function findSeparator(contentFile){
    var previous = 0;
    var lines = contentFile.split('\n');
    var separators = [',','\t',';'];
    var isGoodCandidate = new Boolean(false);
    var reste = [];
    
    for(var s = 0; s < separators.length; s++){
        var sep = separators[s];
        for(var l = 0; l < lines.length; l++){
            var line = lines[l];
            if(line != ""){
                var count = this.countSeparators(line, sep);
                console.log("************\n" + line + "    " + count);
                if (count == 0) {
                    // no separator in this line
                    isGoodCandidate = false;
                    break;
                }
                if (count != previous && previous != 0) {
                   // not the same number that the line before
                   isGoodCandidate = false;
                   break;
                }

                previous = count;
                isGoodCandidate = true;
            }
        }
        if (isGoodCandidate) {
            reste.push(sep);
        }
    }
    
    if (reste.length == 0) {
        // no one separator found
        console.log("No separator found !");
    }
    else if (reste.length > 1) {
        // too many separators found
        console.log("Too many separators found");
    }	
    else{
        console.log("separator : " + reste[0]);
        return reste[0];
    }
}

function countSeparators(line, separator) {
        var number = 0;
        var pos = line.indexOf(separator);
        while (pos != -1) {
            number++;
            line = line.substring(pos + 1);
            pos = line.indexOf(separator);
        }
        return number;
    }


function readInputFile(contentFile, nbInput){
    var separator = this.findSeparator(contentFile);
	var length = contentFile.split('\n')[0].split(separator).length;
	var firstLine = contentFile.split('\n')[0].split(separator);
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
        
        var tbody = document.createElement("tbody");
        mappingTable.appendChild(tbody);
        
		for(var i = 0 ; i < firstLineInput.length; i++){
			var tagInput = firstLineInput[i];
			var row = mappingTable.insertRow(-1); // insert last line
			row.id = "row_" + i;
			row.name = "row_" + i;
            tbody.appendChild(row);
			var cellInput = row.insertCell(0);
			cellInput.innerHTML = tagInput;

			var dropdownDwC = document.createElement("select");
            dropdownDwC.style.display = "block";
			dropdownDwC.name = "dropdownDwC_" + i;
            var cellDwC = row.insertCell(1);
			
            //tbody.appendChild(cellDwC);
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
			

		}

		divMapping.appendChild(mappingTable);
        mappingTable.setAttribute('class', "centered table-marges");

        var mapping = document.createElement("input");
		mapping.id = "mappingActive_" + nbInput;
		mapping.name = "mappingActive_" + nbInput;
		mapping.value = "false";
		mapping.type = "hidden";
		divMapping.appendChild(mapping);

		var buttonOK = document.createElement('button');
		buttonOK.id = "okMapping_" + nbInput;
		buttonOK.name = "okMapping_" + nbInput;
        //buttonOK.value = "OK";
		buttonOK.type = "button";
		var activeMapping = new Boolean(true);
		buttonOK.setAttribute("onclick", "activeMapping(" + activeMapping +"," + nbInput +")");
        buttonOK.setAttribute('class', "btn btn-default waves-effect waves-light font-small-button");

		var buttonCancel = document.createElement('button');
		buttonCancel.id = "cancelMapping_" + nbInput;
		buttonCancel.name = "cancelMapping_" + nbInput;
        //buttonCancel.value = "Cancel conversion";
		buttonCancel.type = "button";
		buttonCancel.setAttribute('onclick', "deleteMapping("+ nbInput +")");
        buttonCancel.setAttribute('class', "btn btn-default waves-effect waves-light font-small-button");
        
		var divSubmitOK = document.createElement('div');
		divSubmitOK.setAttribute('id', "divSubmitMappingOK_" + nbInput);
		divSubmitOK.setAttribute('class', "col-lg-6");
       // divSubmitOK.setAttribute('role',"group");	
		divSubmitOK.setAttribute('value', 'false');
		divSubmitOK.appendChild(buttonOK);
        
        
		var divSubmitMappingCancel = document.createElement('div');
		divSubmitMappingCancel.setAttribute('id', "divSubmitMappingCancel_" + nbInput);
		divSubmitMappingCancel.setAttribute('class', "col-lg-6");
        //divSubmitMappingCancel.setAttribute('role',"group");	
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
        $("#okMapping_" + nbInput).text('Valid mapping DarwinCore');
		divSubmitMapping.appendChild(divSubmitMappingCancel);
         $("#cancelMapping_" + nbInput).text('Cancel mapping DarwinCore');
		divSubmitMapping.appendChild(divMessageSaved);
		divSubmitMapping.appendChild(divMessageCancelled);

        divSubmitMapping.setAttribute('class', "col-lg-12 center");
        //divSubmitMapping.setAttribute('role', "group");
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

function activeRunning(){
    var divRunning = document.getElementById('running');   
    //var submitButton = document.getElementById('workflowLaunch');
    console.log("running");
    var divBody = document.getElementById('divBody');
    //submitButton.addEventListener("click", function(){
        divRunning.style.display = 'block';
        divBody.style.display = 'none';
    //});
    
}