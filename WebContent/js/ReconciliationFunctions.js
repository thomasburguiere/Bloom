var inputList = [];

function InputObject(nbInput, jsonObj, presentTags, reconcileTags){
	this.id = nbInput;
	this.json = jsonObj;
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
function taxonReconciliation(fileReader, counter, changeOrLoad) {
	var divAddLoad = document.getElementById("divAddLoad_" + counter);
	var reconcileButton = document.getElementById("reconcileButton_" + counter);
	var buttonReconcile = null;

	if(!reconcileButton){
		var divReconcile = document.getElementById("divReconcile_" + counter);
		buttonReconcile = document.createElement('input');
		buttonReconcile.type = "button";
		buttonReconcile.id = "reconcileButton_" + counter;
		buttonReconcile.name = "reconcileButton_" + counter;
		buttonReconcile.value = "Taxonomic validation";
		buttonReconcile.setAttribute("onclick" , "loadInputFile(" + counter + ",\"reconcile\")");
		divReconcile.appendChild(buttonReconcile);
		divAddLoad.appendChild(divReconcile);
		/*var testSelect = document.createElement('a');
		var divReconciliationCheck = document.getElementById("divReconciliationCheck_" + counter);
		testSelect.id = "testSelect";
		testSelect.setAttribute('href',"#");
		divReconciliationCheck.appendChild(testSelect);*/
	}
	else{
		var divReconciliationCheck = document.getElementById("divReconciliationCheck_" + counter);
		divReconciliationCheck.style.display =" block";

		var divSubmitReconcile = document.getElementById("divSubmitReconcile_" + counter);
		divSubmitReconcile.style.display = "block";

		var divTableReconcile = document.getElementById("divTableReconcile_" + counter);
		divTableReconcile.style.display = "none";

		var tableReconcile = document.getElementById("tableReconcile_" + counter);
		if(tableReconcile != null){
			divTableReconcile.removeChild(tableReconcile);
		}
		
	}

	buttonReconcile = document.getElementById("reconcileButton_" + counter);

	buttonReconcile.addEventListener('click', function() {
		var divReconciliationCheck = document.getElementById("divReconciliationCheck_" + counter);
		var urlTaxo = document.getElementById("urlAPItaxo_" + counter);
		if(urlTaxo == null){
			var urlTaxo = document.createElement("input");
			urlTaxo.id = "urlAPItaxo_" + counter;
			urlTaxo.type = "url";
			urlTaxo.setAttribute('class', "form-control col-lg-12");
			urlTaxo.placeholder = "http://data1.kew.org/reconciliation/reconcile/IpniName";
			divReconciliationCheck.appendChild(urlTaxo);
		}

		if(changeOrLoad == "change"){
			var fileInput = document.querySelector('#inp_' + counter);
			var reader = new FileReader();
			reader.addEventListener('load', function() {
				readInputFileReconcile(reader.result, counter);
				
			}, false);
			reader.readAsText(fileInput.files[0]);
		}
		else if(changeOrLoad == "reconcile"){
			var divButtonStartReconciliation = document.getElementById("divButtonStartReconciliation_" + counter);
			divButtonStartReconciliation.style.display = "block";

			var divButtonValidReconciliation = document.getElementById("divButtonValidReconciliation_" + counter);
			divButtonValidReconciliation.style.display = "none";
		}



		/*
		var testSelect = document.getElementById("testSelect");
		testSelect.setAttribute('data-type', "select");
		testSelect.setAttribute('data-pk', "1");
		testSelect.setAttribute('data-title', "Select correct taxonomy");
		testSelect.setAttribute('class', 'col-lg-12');

		//<a href="#" id="sex" data-type="select" data-pk="1" data-value="" data-title="Select sex"></a>
		//make testSelect editable		    
		$('#testSelect').editable({
			type: 'select',
			title: 'Select correct taxonomy',
			placement: 'right',
			value: 2,
			source: [
			         {value: 1, text: 'status 1'},
			         {value: 2, text: 'status 2'},
			         {value: 3, text: 'status 3'}
			         ]
		});

		divReconciliationCheck.appendChild(testSelect);
		 */
	}, false);

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
		tablePrepareReconcile.setAttribute("class", "tableMapping");
		for(var i = 0 ; i < columnPrepare.length; i++){
			var row = tablePrepareReconcile.insertRow(-1);
			row.id = "rowReconcile_"+i;
			row.name = "rowReconcile_"+i;
			var cellInput = row.insertCell(0);
			cellInput.innerHTML = columnPrepare[i];

			var dropdownReconcile = document.createElement("select");
			dropdownReconcile.name = "dropdownReconcile_" + nbInput + "_" + i;
			dropdownReconcile.id = "dropdownReconcile_" + nbInput + "_" + i;
			var cellReconcile = row.insertCell(1);
			for(var j = 0 ; j < presentTags.length; j++){
				var tag = presentTags[j];
				var optionTag = document.createElement('option');
				optionTag.value = tag;
				optionTag.text = tag;

				dropdownReconcile.appendChild(optionTag);
			}
			cellReconcile.appendChild(dropdownReconcile);
			tablePrepareReconcile.appendChild(row);
			tablePrepareReconcile.appendChild(cellInput);
			tablePrepareReconcile.appendChild(cellReconcile);
		}

		divReconciliationCheck.appendChild(tablePrepareReconcile);

		var buttonStartReconciliation = document.createElement('input');
		buttonStartReconciliation.id = "buttonStartReconciliation_" + nbInput;
		buttonStartReconciliation.name = "buttonStartReconciliation_" + nbInput;
		buttonStartReconciliation.value = "Start reconciliation";
		buttonStartReconciliation.type = "button";
		buttonStartReconciliation.setAttribute("onclick", "startReconciliation(" + nbInput + ")");

		var divButtonStartReconciliation = document.createElement('div');
		divButtonStartReconciliation.id = "divButtonStartReconciliation_" + nbInput;
		divButtonStartReconciliation.setAttribute('class', "col-lg-6");
		divButtonStartReconciliation.appendChild(buttonStartReconciliation);

		var buttonCancelReconciliation = document.createElement('input');
		buttonCancelReconciliation.id = "buttonCancelReconciliation_" + nbInput;
		buttonCancelReconciliation.name = "buttonCancelReconciliation_" + nbInput;
		buttonCancelReconciliation.value = "Cancel reconciliation";
		buttonCancelReconciliation.type = "button";
		buttonCancelReconciliation.setAttribute("onclick", "cancelReconciliation(" + nbInput + ")");

		var divButtonCancelReconciliation= document.createElement('div');
		divButtonCancelReconciliation.id = "divButtonCancelReconciliation_" + nbInput;
		divButtonCancelReconciliation.setAttribute('class', "col-lg-6");
		divButtonCancelReconciliation.appendChild(buttonCancelReconciliation);

		var buttonValidReconciliation = document.createElement('input');
		buttonValidReconciliation.id = "buttonValidReconciliation_" + nbInput;
		buttonValidReconciliation.name = "buttonValidReconciliation_" + nbInput;
		buttonValidReconciliation.value = "Valid reconciliation";
		buttonValidReconciliation.type = "button";
		buttonValidReconciliation.setAttribute("onclick", "validReconciliation(" + nbInput + ")");

		var divButtonValidReconciliation = document.createElement('div');
		divButtonValidReconciliation.id = "divButtonValidReconciliation_" + nbInput;
		divButtonValidReconciliation.setAttribute('class', "col-lg-6");
		divButtonValidReconciliation.appendChild(buttonValidReconciliation);

		divButtonValidReconciliation.style.display ="none";

		divSubmitReconcile.appendChild(divButtonStartReconciliation);
		divSubmitReconcile.appendChild(divButtonCancelReconciliation);
		divSubmitReconcile.appendChild(divButtonValidReconciliation);


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

	var tablePreparateReconcile = document.getElementById("tablePreparateReconcile_" + nbInput);
	
	if(tableReconcile == null){
		var tableReconcile = document.createElement("div");
		tableReconcile.id = "tableReconcile_" + nbInput;
		tableReconcile.name = "tableReconcile_" + nbInput;
		tableReconcile.border = "0";
		tableReconcile.setAttribute("class", "tableMapping");

		divTableReconcile.appendChild(tableReconcile);
		divTableReconcile.style.display = "inline-block";
	
	}

	var tagsReconcile = this.getTagsReconcileFromTable(nbInput);
	var columnCheck = this.getColumChecked(nbInput);
	var inputObj = this.getInput(nbInput);
	inputObj.tagsReconcile = tagsReconcile;
	var json = this.getJson(nbInput);
	
	this.adjustJSON(nbInput);
	this.adjustJSONforReconcile(nbInput, columnCheck);
	$(tableReconcile).WATable({
		data : this.getJson(nbInput)
	});
	
}

function adjustJSON(nbInput){
	var jsonInput = this.getJson(nbInput);
	var tagsReconcile = this.getReconcileTags(nbInput);
	var inputObj = this.getInput(nbInput);
	var tagsAll = this.getTags(nbInput);
	for(var i = 0 ; i < tagsAll.length; i++){
		var tag = tagsAll[i];
		if(jQuery.inArray(tag, tagsReconcile) != -1){
			jsonInput["cols"][tag].hidden = false;
		}
		else{
			jsonInput["cols"][tag].hidden = true;
		}
		
	}

	
	inputObj.json = jsonInput;
		
}

function callRestService(urlAPI, scientificName){
    //xhr.open('GET', 'http://data1.kew.org/reconciliation/reconcile/IpniName?query={"query":"Lamiaceae+Congea+chinensis+Moldenke"}');
    var url = urlAPI + '?query={"query":"' + scientificName.replace(" ", "+") + '"}';
  /*  $.ajax({
        url  : url,
        data : {symbol : "AAPL"},
        type : 'GET',
        dataType : 'jsonp'
      }).done(function(data) {
    	  //document.getElementById("divTableReconcile_0").innerHTML = JSON.stringify(data);
      });*/
    $.ajax({
    	url : url,
        type: 'GET',
        dataType: 'jsonp',
        data : {symbol : "AAPL"},
        success: function(data) {
        	for (var i = 0; i < data.length; i++) {
            	console.log(scientificName + "  " + data[i]);
        	}
        	
        },
        error: function(xhr, sts, err) {
            console.log('Erreur !!');
        }
    });
}


function adjustJSONforReconcile(nbInput, columnCheck){
	var jsonInput = this.getJson(nbInput);
	var urlTaxo = $('#urlAPItaxo_' + nbInput).val();
	//"http://data1.kew.org/reconciliation/reconcile/IpniName"
	//"http://refine.taxonomics.org/gbifchecklists/reconcile"
	for(var i = 0 ; i < 5 ; i++){
		var scientificName = jsonInput["rows"][i][columnCheck];		
		callRestService(urlTaxo, scientificName);
	}
}

/*
function callRestService(urlAPI, scientificName){
    var xhr = new XMLHttpRequest();
    //xhr.open('GET', 'http://data1.kew.org/reconciliation/reconcile/IpniName?query={"query":"Lamiaceae+Congea+chinensis+Moldenke"}');
    var request = urlAPI + '?query={"query":"' + scientificName + '"}';
    console.log(request);
    xhr.open('GET', request);
    xhr.addEventListener('readystatechange', function() {
        if (xhr.readyState === 4) {
            var response = JSON.parse(xhr.responseText);
           consolel.log(response);
        }
    }, false);
    xhr.send(null);
}
*/
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
	
	var tableReconcile = document.getElementById("tableReconcile_" + nbInput);
	if(tableReconcile != null){
		divTableReconcile.removeChild(tableReconcile);
	}
	
}



function validReconciliation(nbInput){
	/*var divTableReconcile = document.getElementById("divTableReconcile_" + nbInput);
	//$('#')
	var fileTest = $.getJSON("json/test_Orchidaceae_Dactylorhiza_fuchsii.json", function(donnees) {
        alert(donnees.result[0].id);
	});*/
}

function readInputFileReconcile(contentFile, nbInput){
	var firstLine = contentFile.split('\n')[0].split(',');
	this.createReconciliationPreparation(firstLine, nbInput);

	var jsonObj = this.CSVtoJSON(contentFile);
	
	var input = new InputObject(nbInput, jsonObj, firstLine, []);
	
	inputList.push(input);//add an input to the list
}

function CSVtoJSON(csvFile){

	var lines = csvFile.split("\n");
	var headers = lines[0].split(",");
	var cols = this.createColsJSON(headers);
	var rows = this.createRowsJSON(headers, lines);

	var json = "{" + cols + "," + rows + "}";

	jsonObj = JSON.parse(json);

	return jsonObj;
}

function createColsJSON(headers){
	var cols = '"cols":{';
	for(var i = 0 ; i < headers.length ; i++){

		var header = headers[i];
		if(i != headers.length -1){
			cols += '"' + header + '": {"index": ' + (i + 1) + ',"hidden": true, "type": "string"},';
		}
		else{
			cols += '"' + header + '": {"index": ' + (i + 1) + ',"hidden": true, "type": "string"}}';

		}
	}
	return cols;
}

function createRowsJSON(headers, lines){
	
	var rows = '"rows": [';
	for(var i = 0 ; i < lines.length ; i++){
		var line = lines[i];
		if(i != 0){
			for(var j = 0 ; j < headers.length ; j++){
				var header = headers[j];
				var value = line.split(",");
				
				if(j == 0){
					rows += '{"' + header + '": "' + value[j] + '",';
				}
				else if(j != headers.length - 1){
					rows += '"' + header + '": "' + value[j] + '",';
				}
				else if(i != lines.length - 1){
					rows += '"' + header + '": "' + value[j] + '"},';
				}
				else{
					rows += '"' + header + '": "' + value[j] + '"}';
				}

			}
		}
	}
	rows += "]";

	return rows;
}

//Generates some data.
function generateSampleData(limit, simpleMode) {
	//First define the columns
	var cols = {
			userId: {
				index: 1, //The order this column should appear in the table
				type: "number", //The type. Possible are string, number, bool, date(in milliseconds).
				friendly: "<span class='glyphicon glyphicon-user'></span>",  //Name that will be used in header. Can also be any html as shown here.
				format: "<a href='#' target='_blank'>{0}</a>",  //Used to format the data anything you want. Use {0} as placeholder for the actual data.
				unique: true,  //This is required if you want checkable rows, databinding or to use the rowClicked callback. Be certain the values are really unique or weird things will happen.
				sortOrder: "asc", //Data will initially be sorted by this column. Possible are "asc" or "desc"
				tooltip: "This column has an initial filter", //Show some additional info about column
				filter: "1..400" //Set initial filter.
			},
			name: {
				index: 2,
				type: "string",
				friendly: "Name",
				cls: "blue, anotherClass", //apply some css classes
				tooltip: "This column has a custom placeholder and css classes", //Show some additional info about column
				placeHolder: "abc123" //Overrides default placeholder and placeholder specified in data types(row 34).
			},
			age: {
				index: 3,
				type: "number",
				friendly: "Age",
				sorting: true, //dont allow sorting
				tooltip: "This column has filtering and sorting turned off", //Show some additional info about column
				filter: false //Removes filter field for this column
			},
			weight: {
				index: 4,
				type: "number",
				decimals: 2, //Force decimal precision
				friendly: "Weight",
				placeHolder: "50..90",
				format: "{0}kg",
				tooltip: "This column has no tooltip for the filter", //Show some additional info about column
				filterTooltip: false //Turn off tooltip for this column
			},
			height: {
				index: 5,
				type: "number",
				friendly: "Height",
				hidden: true //Hides the column. Useful if you want this value later on but not visible to user. It can be made visible if columnPicker is enabled.
			},
			important: {
				index: 6,
				type: "bool",
				friendly: "Important"
			},
			someDate: {
				index: 7,
				type: "date", //Don't forget dates are expressed in milliseconds.
				friendly: "SomeDate"
			},
			someDateDay: {
				index: 8,
				type: "date", //Don't forget dates are expressed in milliseconds
				dateFormat: "dddd", //Special date format. See all possible formats here http://arshaw.com/xdate/#Formatting.
				friendly: "Day"
			},
			remove: {
				index: 9,
				type: "string",
				friendly: "<span class='glyphicon glyphicon-remove'></span>",
				format: "<a href='#' title='Remove me'><span class='glyphicon glyphicon-remove'></span></a>",
				filter: false,
				sorting: false,
				hidden: simpleMode //For the sake of this demo, this column gets hidden for example1
			},
			check: {
				index: 10,
				type: "string",
				friendly: "<span class='glyphicon glyphicon-check'></span>",
				format: "<a href='#' title='Toggle checked'><span class='glyphicon glyphicon-check'></span></a>",
				filter: false,
				sorting: false,
				hidden: simpleMode //For the sake of this demo, this column gets hidden for example1
			}
	};
	/*
      Create the rows (This step is of course normally done by your web server). 
      What's worth mentioning is the special row properties. See some examples below.
      <column>Format allows you to override column format and have it formatted the way you want.
      <column>Cls allows you to add css classes on the cell(td) element.
      row-checkable allows you to prevent rows from being checkable.
      row-checked allows you to pre-check a row.
      row-cls allows you to add css classes to the row(tr) element.
	 */
	var rows = [];
	var i = 1;
	while(i <= limit)
	{
		//We leave some fields intentionally undefined, so you can see how sorting/filtering works with these.
		var row = {};
		row.userId           = i;
		row.name             = "magna"; //Random name
		row.age              = Math.floor(Math.random() * 50) + 20; //Random age
		row.ageFormat        = row.age + (row.age > 50 ? " (senior)" : " (junior)"); //Special formatting
		row.weight           = (Math.floor(Math.random() * 40) + 50) + (Math.floor(Math.random() * 100) / 100); //Random weight
		row.weightCls        = row.weight < 70 ? 'green' : row.weight < 80 && row.weight >= 70 ? 'yellow' : 'red'; //Add css class to cell
		row.height           = Math.floor(Math.random() * 50) + 150; //Random height
		row.important        = i % 5 == 0 ? 1 : 0; //Random important
		row.someDate         = i % 4 == 0 ? undefined : Date.now() + (i*Math.floor(Math.random()*(60 * 60 * 24 * 100))); //Random date
		row.someDateDay      = i % 4 == 0 ? "" : row.someDate; //Use same date as above, but its column has different formatting
		row["row-checkable"] = i % 4 != 0; //disable checking for every 4th row
		row["row-checked"]   = i % 3 == 0; //pre check every 3rd row
		row["row-cls"]       = i % 3 == 0 ? "gray, anotherClass" : ""; //apply some row css classes for every 3rd row

		row.secret           = { "secret" : "foobar" }; //Add something else that you want later(ex in rowClicked event)
		rows.push(row);
		i++;
	}

	//Create the returning object. Besides cols and rows, you can also pass any other object you would need later on.
	var data = {
			cols: cols,
			rows: rows,
			otherStuff: {
				thatIMight: 1,
				needLater: true
			}
	};
	return data;
}

