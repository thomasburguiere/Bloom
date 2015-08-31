var inputList = [];

function InputObject(nbInput, rows, cols, contentFile, presentTags, reconcileTags){
	this.id = nbInput;
	this.rows = rows;
	this.cols = cols;
	this.file = contentFile;
	this.tags = presentTags;
	this.tagsReconcile = reconcileTags;
}

function addInput(inputObject){
	inputList.push(inputObject);
}

function getInput(nbInput){
	var inputLength = inputList.length;
	for(var i = 0 ; i < inputLength ; i++){
		var input = inputList[i];
		if(nbInput == i){
			return input;
		}
	}
}

function getJson(nbInput){
	var inputLength = inputList.length;
	for(var i = 0 ; i < inputLength ; i++){
		var input = inputList[i];
		if(nbInput == i){
			return input.json;
		}
	}
}

function getTags(nbInput){
	var inputLength = inputList.length;
	for(var i = 0 ; i < inputLength ; i++){
		var input = inputList[i];
		if(nbInput == i){
			return input.tags;
		}
	}
}

function getReconcileTags(nbInput){
	var inputLength = inputList.length;
	for(var i = 0 ; i < inputLength ; i++){
		var input = inputList[i];
		if(nbInput == i){
			return input.tagsReconcile;
		}
	}
}

function getRows(nbInput){
	var inputLength = inputList.length;
	for(var i = 0 ; i < inputLength ; i++){
		var input = inputList[i];
		if(nbInput == i){
			return input.rows;
		}
	}
}

function getCols(nbInput){
	var inputLength = inputList.length;
	for(var i = 0 ; i < inputLength ; i++){
		var input = inputList[i];
		if(nbInput == i){
			return input.cols;
		}
	}
}

function getFile(nbInput){
	var inputLength = inputList.length;
	for(var i = 0 ; i < inputLength ; i++){
		var input = inputList[i];
		if(nbInput == i){
			return input.file;
		}
	}
}

function deleteInput(nbInput, newInput){
    inputList.splice(nbInput, 1, newInput);
}

function taxonReconciliation(fileReader, counter, changeOrLoad) {
	if(changeOrLoad != "load"){
		var divAddLoad = document.getElementById("divAddLoad_" + counter);
		var reconcileButton = document.getElementById("reconcileButton_" + counter);
		var buttonReconcile = null;
		var divSubmitReconcile = document.getElementById("divSubmitReconcile_" + counter);
		var divTableReconcile = document.getElementById("divTableReconcile_" + counter);

		if(!reconcileButton){
			var divReconcile = document.getElementById("divReconcile_" + counter);
			buttonReconcile = document.createElement('button');
			buttonReconcile.type = "button";
			buttonReconcile.id = "reconcileButton_" + counter;
			buttonReconcile.name = "reconcileButton_" + counter;
			//buttonReconcile.value = "Taxonomic validation";
			buttonReconcile.setAttribute("onclick" , "loadInputFile(" + counter + ",\"reconcile\")");
            buttonReconcile.setAttribute('class', "btn btn-default waves-effect waves-light btn-large font-medium-button");
			divReconcile.appendChild(buttonReconcile);
            $("#reconcileButton_" + counter).text("Taxonomic validation");
			divAddLoad.appendChild(divReconcile);
			/*var testSelect = document.createElement('a');
			var divReconciliationCheck = document.getElementById("divReconciliationCheck_" + counter);
			testSelect.id = "testSelect";
			testSelect.setAttribute('href',"#");
			divReconciliationCheck.appendChild(testSelect);*/

			var divMessageReconcileSaved = document.createElement('div');
			divMessageReconcileSaved.setAttribute('id', "divMessageReconcileSaved_" + counter);
			divMessageReconcileSaved.setAttribute('class', "col-lg-12 text-success text-center");
			divMessageReconcileSaved.style.display = "none";
			var messageReconcileSaved = document.createElement('p');
			messageReconcileSaved.innerHTML = "Reconciliation service saved";
			divMessageReconcileSaved.appendChild(messageReconcileSaved);

			var divMessageReconcileCancelled = document.createElement('div');
			divMessageReconcileCancelled.setAttribute('id', "divMessageReconcileCancelled_" + counter);
			divMessageReconcileCancelled.setAttribute('class', "col-lg-12 text-danger text-center");
			divMessageReconcileCancelled.style.display = "none";
			var messageReconcileCancelled = document.createElement('p');
			//messageCancelled.setAttribute('class', "has-warning");
			messageReconcileCancelled.innerHTML = "Reconciliation service cancelled";
			divMessageReconcileCancelled.appendChild(messageReconcileCancelled);

			divSubmitReconcile.appendChild(divMessageReconcileCancelled);
			divSubmitReconcile.appendChild(divMessageReconcileSaved);
			
			/*var reconcile = document.createElement("input");
			reconcile.id = "reconcileActive_" + counter;
			reconcile.name = "reconcileActive_" + counter;
			reconcile.value = "false";
			reconcile.type = "hidden";
			divTableReconcile.appendChild(reconcile);*/
		}
		else{
			console.log("changeOrLoad : " + changeOrLoad);
			var reconcileActive = document.getElementById("reconcileActive_" + counter);
			if(reconcileActive){
				var reconcileActiveValue = document.getElementById("reconcileActive_" + counter).getAttribute("value");
				console.log("value reconcileActive : " + reconcileActiveValue);
				if(reconcileActiveValue == "false"){
					var divReconciliationCheck = document.getElementById("divReconciliationCheck_" + counter);
					divReconciliationCheck.style.display ="block";


					divSubmitReconcile.style.display = "block";

					divTableReconcile.style.display = "none";

					var tableReconcile = document.getElementById("tableReconcile_" + counter + "_wrapper");
					if(tableReconcile != null){
						//alert(tableReconcile.id);
						divTableReconcile.removeChild(tableReconcile);
					}
				}	
				else{
					reconcileActive.value = "false";
					console.log("new value : " + reconcileActive.value);
				}
			}
			

		}

		buttonReconcile = document.getElementById("reconcileButton_" + counter);

		buttonReconcile.addEventListener('click', function() {
			var divReconciliationCheck = document.getElementById("divReconciliationCheck_" + counter);
			var urlTaxo = document.getElementById("urlAPItaxo_" + counter);
			if(urlTaxo == null){
                var divUrlTaxo = document.createElement("div");
                divUrlTaxo.setAttribute('class', "col-lg-12 url-div");
                divUrlTaxo.id = "divUrlTaxo_" + counter;
                
				var urlTaxo = document.createElement("input");
				urlTaxo.id = "urlAPItaxo_" + counter;
				urlTaxo.type = "url";
				urlTaxo.setAttribute('class', "col-lg-10");
				//urlTaxo.placeholder = "http://data1.kew.org/reconciliation/reconcile/IpniName";
                var labelUrl = document.createElement('label');
                labelUrl.id = "labelUrlTaxo_" + counter;
                labelUrl.setAttribute('class', "col-lg-2");
                labelUrl.value = "http://data1.kew.org/reconciliation/reconcile/IpniName";
                divUrlTaxo.appendChild(labelUrl);
                divUrlTaxo.appendChild(urlTaxo);
				divReconciliationCheck.appendChild(divUrlTaxo);
                $("#labelUrlTaxo_" + counter).text("Url reference");
			}
			if(changeOrLoad == "change"){
                console.log("change and read file");
				var fileInput = document.querySelector('#inp_' + counter);
				var reader = new FileReader();
				reader.addEventListener('load', function() {
                    
					readInputFileReconcile(reader.result, counter);

				}, false);
				reader.readAsText(fileInput.files[0]);
			}
			else if(changeOrLoad == "reconcile"){
                console.log("reconcile");
				var divMessageReconcileCancelled  = document.getElementById("divMessageReconcileCancelled_" + counter);
				var divMessageReconcileSaved = document.getElementById("divMessageReconcileSaved_" + counter);
				var divButtonStartReconciliation = document.getElementById("divButtonStartReconciliation_" + counter);
				var divButtonCancelReconciliation = document.getElementById("divButtonCancelReconciliation_" + counter);
				var reconcileActive = document.getElementById("reconcileActive_" + counter);
				var divReconciliationCheck = document.getElementById("divReconciliationCheck_" + counter);
				var divButtonValidReconciliation = document.getElementById("divButtonValidReconciliation_" + counter);

				reconcileActive.value = "false";
				divReconciliationCheck.style.display = "block";
				divMessageReconcileCancelled.style.display = "none";
				divMessageReconcileSaved.style.display = "none";
				if(divButtonStartReconciliation){
					divButtonStartReconciliation.style.display ="block";
					divButtonCancelReconciliation.style.display = "block";
					divButtonValidReconciliation.style.display ="none";
				}

			}
		}, false);
	}

}

function readInputFileReconcile(contentFile, nbInput){
	var firstLine = contentFile.split('\n')[0].split(',');
	this.createReconciliationPreparation(firstLine, nbInput);

	var lines = contentFile.split("\n");
	var headers = lines[0].split(",");
	var rows = this.createDataJSON(headers, lines);
	var cols = createTitleJSON(headers);

	var input = new InputObject(nbInput, rows, cols, contentFile, firstLine, []);
    
    this.deleteInput(nbInput, input);
    //add an input to the list
    //inputList.push(input);
    
    console.log(inputList);
}

function createReconciliationPreparation(presentTags, nbInput){

	var columnPrepare = ["Column to check","column for help 1","column for help 2","column for help 3","column for help 4"];
	var divReconciliationCheck = document.getElementById("divReconciliationCheck_" + nbInput);
	var divSubmitReconcile = document.getElementById("divSubmitReconcile_" + nbInput);
	var divTableReconcile = document.getElementById("divTableReconcile_" + nbInput);

	var tablePrepareReconcile = document.getElementById("tablePrepareReconcile_" + nbInput);

	if(tablePrepareReconcile == null){
		var tablePrepareReconcile = document.createElement("table");
		tablePrepareReconcile.id = "tablePrepareReconcile_" + nbInput;
		tablePrepareReconcile.name = "tablePrepareReconcile_" + nbInput;
		tablePrepareReconcile.border = "0";
		tablePrepareReconcile.setAttribute("class", "centered table-marges");
        var tbody = document.createElement("tbody");
        tablePrepareReconcile.appendChild(tbody);
        
		for(var i = 0 ; i < columnPrepare.length; i++){
			var row = tablePrepareReconcile.insertRow(-1);
			row.id = "rowReconcile_"+i;
			row.name = "rowReconcile_"+i;
            tbody.appendChild(row);
			var cellInput = row.insertCell(0);
			cellInput.innerHTML = columnPrepare[i];

			var dropdownReconcile = document.createElement("select");
			dropdownReconcile.name = "dropdownReconcile_" + nbInput + "_" + i;
			dropdownReconcile.id = "dropdownReconcile_" + nbInput + "_" + i;
            dropdownReconcile.style.display = "block";
			var cellReconcile = row.insertCell(1);
			
            for(var j = 0 ; j < presentTags.length; j++){
				var tag = presentTags[j];
				var optionTag = document.createElement('option');
				optionTag.value = tag;
				optionTag.text = tag;

				dropdownReconcile.appendChild(optionTag);
			}
			cellReconcile.appendChild(dropdownReconcile);
			//tablePrepareReconcile.appendChild(row);
			//tablePrepareReconcile.appendChild(cellInput);
			//tablePrepareReconcile.appendChild(cellReconcile);
		}

		divReconciliationCheck.appendChild(tablePrepareReconcile);

		var buttonStartReconciliation = document.createElement('button');
		buttonStartReconciliation.id = "buttonStartReconciliation_" + nbInput;
		buttonStartReconciliation.name = "buttonStartReconciliation_" + nbInput;
		//buttonStartReconciliation.value = "Start reconciliation";
		buttonStartReconciliation.type = "button";
		buttonStartReconciliation.setAttribute("onclick", "startReconciliation(" + nbInput + ")");
        buttonStartReconciliation.setAttribute('class', "btn btn-default waves-effect waves-light font-small-button");
        
		var divButtonStartReconciliation = document.createElement('div');
		divButtonStartReconciliation.id = "divButtonStartReconciliation_" + nbInput;
		divButtonStartReconciliation.setAttribute('class', "col-lg-6");
		divButtonStartReconciliation.appendChild(buttonStartReconciliation);
        
		divSubmitReconcile.appendChild(divButtonStartReconciliation);
        

		var buttonCancelReconciliation = document.createElement('button');
		buttonCancelReconciliation.id = "buttonCancelReconciliation_" + nbInput;
		buttonCancelReconciliation.name = "buttonCancelReconciliation_" + nbInput;
		//buttonCancelReconciliation.value = "Cancel reconciliation";
		buttonCancelReconciliation.type = "button";
		buttonCancelReconciliation.setAttribute("onclick", "cancelReconciliation(" + nbInput + ")");
        buttonCancelReconciliation.setAttribute('class', "btn btn-default waves-effect waves-light font-small-button");
        
		var divButtonCancelReconciliation= document.createElement('div');
		divButtonCancelReconciliation.id = "divButtonCancelReconciliation_" + nbInput;
		divButtonCancelReconciliation.setAttribute('class', "col-lg-6");
		divButtonCancelReconciliation.appendChild(buttonCancelReconciliation);
        

		divSubmitReconcile.appendChild(divButtonCancelReconciliation);
        $("#buttonCancelReconciliation_" + nbInput).text("Cancel reconciliation");
        $("#buttonStartReconciliation_" + nbInput).text("Start reconciliation");
        
		var buttonValidReconciliation = document.createElement('button');
		buttonValidReconciliation.id = "buttonValidReconciliation_" + nbInput;
		buttonValidReconciliation.name = "buttonValidReconciliation_" + nbInput;
		//buttonValidReconciliation.value = "Valid reconciliation";
		buttonValidReconciliation.type = "button";
		var activeReconcile = new Boolean(true);
		buttonValidReconciliation.setAttribute("onclick", "setReconciliation(" + activeReconcile + "," + nbInput + ")");
        buttonValidReconciliation.setAttribute('class',"btn btn-default waves-effect waves-light font-small-button");

		var divButtonValidReconciliation = document.createElement('div');
		divButtonValidReconciliation.id = "divButtonValidReconciliation_" + nbInput;
		divButtonValidReconciliation.setAttribute('class', "col-lg-6");
		divButtonValidReconciliation.appendChild(buttonValidReconciliation);
       
		divButtonValidReconciliation.style.display ="none";

		divSubmitReconcile.appendChild(divButtonValidReconciliation);
         $("#buttonValidReconciliation_" + nbInput).text("Valid reconciliation");

	}
}

function getTagsReconcileFromTable(nbInput){
	var tagsReconcile = [];

	for(var i = 0; i < 5 ; i++ ){
		var dropdownReconcile = document.getElementById("dropdownReconcile_" + nbInput + "_" + i);
		tagsReconcile.push(dropdownReconcile.value);
	}
	return tagsReconcile;
}

function getColumChecked(nbInput){
	var dropdownReconcile = document.getElementById("dropdownReconcile_" + nbInput + "_0");
	var columnCheck = dropdownReconcile.value;

	return columnCheck;

}

function startReconciliation(nbInput){
	var divReconciliationCheck = document.getElementById("divReconciliationCheck_" + nbInput);
	divReconciliationCheck.style.display = "none";

	var divButtonStartReconciliation = document.getElementById("divButtonStartReconciliation_" + nbInput);
	divButtonStartReconciliation.style.display = "none";

	var divButtonValidReconciliation = document.getElementById("divButtonValidReconciliation_" + nbInput);
	divButtonValidReconciliation.style.display = "block";

	var divTableReconcile = document.getElementById("divTableReconcile_" + nbInput);
	var tableReconcile = document.getElementById("tableReconcile_" + nbInput);
	var tableReconcileWrapper = document.getElementById("tableReconcile_" + nbInput + "_wrapper");
	
	
	if(tableReconcile == null && tableReconcileWrapper == null){
		console.log("tableReconcile and wrapper are null");
		//var tableReconcile = document.createElement("div");
		var tableReconcile = document.createElement("table");
		tableReconcile.id = "tableReconcile_" + nbInput;
		tableReconcile.name = "tableReconcile_" + nbInput;
		tableReconcile.border = "0";
		tableReconcile.setAttribute("class", "table-mapping table-marges");
		
		var reconcile = $("#reconcileActive_" + nbInput);
		if(reconcile.length != 1){
			var reconcileNew = document.createElement("input");
			reconcileNew.id = "reconcileActive_" + nbInput;
			reconcileNew.name = "reconcileActive_" + nbInput;
			reconcileNew.value = "false";
			reconcileNew.type = "hidden";
			divTableReconcile.appendChild(reconcileNew);
		}
		
		
		divTableReconcile.appendChild(tableReconcile);
		divTableReconcile.style.display = "inline-block";
	}
	else if(tableReconcileWrapper != null){
		console.log("already table reconcile exist and must be deleted");
		//var reconcileOld = document.getElementById("reconcileActive_" + nbInput);
		divTableReconcile.removeChild(tableReconcileWrapper);
		//divTableReconcile.removeChild(reconcileOld);
		var tableReconcile = document.createElement("table");
		tableReconcile.id = "tableReconcile_" + nbInput;
		tableReconcile.name = "tableReconcile_" + nbInput;
		tableReconcile.border = "0";
		tableReconcile.setAttribute("class", "table-mapping table-marges compact");

		divTableReconcile.appendChild(tableReconcile);
		divTableReconcile.style.display = "inline-block";
	}
	var tagsReconcile = this.getTagsReconcileFromTable(nbInput);
	var columnCheck = this.getColumChecked(nbInput);
	var inputObj = this.getInput(nbInput);
	inputObj.tagsReconcile = tagsReconcile;
    var inputElement = this.getInput(nbInput);
    console.log(inputElement);
    console.log(inputList);
	var rows = this.getRows(nbInput);
	var cols = this.getCols(nbInput);
	var i = cols.length - 1;
	var col = cols[i];
	if(col["title"] != "Show all or mask all"){
		col["title"] = "Show all or mask all";
		cols.push(col);
	}

    var table = $(tableReconcile).DataTable({
		"data": rows,
		"columns": cols,
		"bAutoWidth" : true,
		"sScrollX" : '50%',
        "bFilter" : false, 
        "bInfo" : false,
        "ordering": false,
        "info":     false,
        "dom": '<"top"i>rt<"bottom"flp><"clear">',
        "bLengthChange" : false,
		"columnDefs": [
		               {
		            	   targets: -1,
		            	   className: 'details-control',
		            	   orderable: false,
		            	   data: null,
		            	   defaultContent: '',
		            	   cursor: 'pointer'
		               }
		               ]
	});

	this.adjustJSON(nbInput);
	$(window).bind('resize', function () {
		$(tableReconcile).fnAdjustColumnSizing();
	} );

	// Add event listener for opening and closing details
	$('#tableReconcile_' + nbInput + ' tbody').on('click', 'td.details-control', function () {
		var tr = $(this).closest('tr');
		var td = $(this).closest('td');
		var row = table.row(tr);
		rowIndex = row.index();

		var id = "tableInput_" + nbInput + "_row_" + rowIndex;
		var child = row.child();
		
		if(child){
			var tableDetailsControl = document.getElementById(id);
			// This row is already open - close it
			if($('#' + id).css('display') == 'none'){ 
			   $('#' + id).css("display", "block"); 
			   tr.addClass('shown');
			} else { 
			   $('#' + id).css("display", "none");
			   tr.removeClass('shown');
			}
		}
		else {

			// Open this row
			console.log("create child");
			addTableForReconcile(nbInput, columnCheck, row);
			tr.addClass('shown');
		}
		

	} );
}

function createDataJSON(headers, lines){
	var rows = [];
	for(var i = 0 ; i < lines.length ; i++){
		var line = lines[i];
		var row = [];

		if(i != 0 && line != ""){
			for(var j = 0 ; j < headers.length ; j++){
				var value = line.split(",");
				row.push(value[j]);
			}

			rows.push(row);
		}

	}
	return rows;
}

function createTitleJSON(headers){
	var cols = [];
	for(var i = 0 ; i < headers.length ; i++){
		var header = headers[i];
		var col = new Object();
		col['title'] = header;

		cols.push(col);
	}
	return cols;
}

function adjustJSON(nbInput){
	var tableReconcile = $("#tableReconcile_" + nbInput).dataTable();
	var tagsReconcile = this.getReconcileTags(nbInput);
	var tagsAll = this.getTags(nbInput);
	for(var i = 0 ; i < tagsAll.length; i++){
		var tag = tagsAll[i];
		if(jQuery.inArray(tag, tagsReconcile) != -1){
			tableReconcile.fnSetColumnVis(i, true);
		}
		else{
			tableReconcile.fnSetColumnVis(i, false);
		}

	}		
}

function createTableReconciliationService(resultsReconcile, nbInput, row){
	var indexRow = row.index();
	var subTableReconcile = '<table id=tableInput_' + nbInput + '_row_' + indexRow + ' cellpadding="5" cellspacing="0" border="0" style="display:block; padding-left:50px;" >';
	var size = 0;
	for(i in resultsReconcile){
		size ++;
	}
	if(size > 0){
		subTableReconcile +=
			'<tr>'+
			//'<td style="font-weight : bold">check</td>'+
			'<td style="font-weight : bold">name</td>' +
			'<td style="font-weight : bold">score</td>' +
			'</tr>';
		for(i in resultsReconcile){
			var result = resultsReconcile[i];
			var name = result[0];
			var score = result[1];
			var nameRadio = "radio_" + nbInput + "_" + indexRow;
			var idRadio = "radio_" + nbInput + "_" + indexRow + "_" + i;
			subTableReconcile += 
				'<tr>'+
					'<td><input type="radio" name="' + 'group' + indexRow + '" value="' + name + '" id="' + idRadio + '"/>' +
					'<label for="' + idRadio + '">' + name +'</label></td>' +
					'<td>' + score +'</td>' +
				'</tr>';
		}

	}
	else{
		subTableReconcile += '<tr><td>No match found</td></tr>';
	}
	subTableReconcile += '</table>';
	return subTableReconcile;
}



function addTableForReconcile(nbInput, columnCheck, row){
	var rows = this.getRows(nbInput);
	var cols = this.getCols(nbInput);
	var idColumnCheck = "";
	var indexRow = row.index(); 
	for(i in cols){
		var col = cols[i];
		if(col['title'] == columnCheck){
			idColumnCheck = i;
		}

	}
	var urlTaxo = $('#urlAPItaxo_' + nbInput).val();
	//http://data1.kew.org/reconciliation/reconcile/IpniName
	//http://refine.taxonomics.org/gbifchecklists/reconcile
	var scientificName = rows[indexRow][idColumnCheck];
	callRestService(urlTaxo, scientificName, nbInput, row);

}

function callRestService(urlAPI, scientificName, nbInput, row){
	//xhr.open('GET', 'http://data1.kew.org/reconciliation/reconcile/IpniName?query={"query":"Lamiaceae+Congea+chinensis+Moldenke"}');
	var url = urlAPI + '?query={"query":"' + scientificName.replace(" ", "+") + '"}';
	var htmlResult = "";
	$.ajax({
		url : url,
		type: 'GET',
		dataType: 'jsonp',
		data : {symbol : "AAPL"},
		success: function(data) {
			var result = {};
			for (var i = 0; i < data.result.length; i++) {
				result[i] = [data.result[i].name, data.result[i].score];
			}
			htmlResult = createTableReconciliationService(result, nbInput, row);
			row.child(htmlResult).show();
		},
		error: function(xhr, sts, err) {
			console.log('Erreur !!');
		}
	});

	return htmlResult;
}


function cancelReconciliation(nbInput){
	var divReconciliationCheck = document.getElementById("divReconciliationCheck_" + nbInput);
	divReconciliationCheck.style.display ="none";

	var divSubmitReconcile = document.getElementById("divSubmitReconcile_" + nbInput);
	divSubmitReconcile.style.display = "none";

	var divButtonStartReconciliation = document.getElementById("divButtonStartReconciliation_" + nbInput);
	divButtonStartReconciliation.style.display = "block";

	var divButtonValidReconciliation = document.getElementById("divButtonValidReconciliation_" + nbInput);
	divButtonValidReconciliation.style.display = "none";

	var divTableReconcile = document.getElementById("divTableReconcile_" + nbInput);
	divTableReconcile.style.display = "none";

	var tableReconcile = document.getElementById("tableReconcile_" + nbInput + "_wrapper");
	if(tableReconcile != null){
		divTableReconcile.removeChild(tableReconcile);
	}

	setReconciliation(false, nbInput);

}

function setReconciliation(reconcileActive, nbInput){

	var divTableReconcile = document.getElementById("divTableReconcile_" + nbInput);
	var divMessageReconcileCancelled  = document.getElementById("divMessageReconcileCancelled_" + nbInput);
	var divMessageReconcileSaved = document.getElementById("divMessageReconcileSaved_" + nbInput);
	var divButtonStartReconciliation = document.getElementById("divButtonStartReconciliation_" + nbInput);
	var divButtonCancelReconciliation = document.getElementById("divButtonCancelReconciliation_" + nbInput);
	var divButtonValidReconciliation = document.getElementById("divButtonValidReconciliation_" + nbInput);
	var divReconciliationCheck = document.getElementById("divReconciliationCheck_" + nbInput);
	var divSubmitReconcile = document.getElementById("divSubmitReconcile_" + nbInput);

	//var reconcileActive = document.getElementById("reconcileActive_" + nbInput);

	if(reconcileActive){
		divSubmitReconcile.style.display = "block";
		divTableReconcile.style.display = "none";
		divMessageReconcileCancelled.style.display = "none";
		divMessageReconcileSaved.style.display = "block";
		divButtonStartReconciliation.style.display = "none";
		divButtonCancelReconciliation.style.display = "none";
		divButtonValidReconciliation.style.display = "none";
		divReconciliationCheck.style.display = "none";

		var idReconcileActive = "#reconcileActive_" + nbInput;
	    var reconcileActiveJquery = $(idReconcileActive),val = reconcileActiveJquery.val();
	    reconcileActiveJquery.val(val === "true" ? "false" : "true");
		//console.log("reconcileActive value :" );
		//console.log(reconcileActiveJquery.val());

	}
	else{
		divSubmitReconcile.style.display = "block";
		divTableReconcile.style.display = "none";
		divMessageReconcileCancelled.style.display = "block";
		divMessageReconcileSaved.style.display = "none";
		divButtonStartReconciliation.style.display = "none";
		divButtonCancelReconciliation.style.display = "none";
		divButtonValidReconciliation.style.display = "none";
		divReconciliationCheck.style.display = "none";

		reconcileActive.value = "false";
	}
}

