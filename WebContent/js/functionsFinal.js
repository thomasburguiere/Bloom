function initialiseFinalPage(){
	var step1_involved = document.getElementById("step1_involved").value;
	if(step1_involved == "true"){
		
		this.initialiseStep1();
	}

	var step3_involved = document.getElementById("step3_involved").value;
	if(step3_involved == "true"){
		this.initialiseStep3();
	}

	var step4_involved = document.getElementById("step4_involved").value;
	if(step4_involved == "true"){
		this.initialiseStep4();
	}

	var step5_involved = document.getElementById("step5_involved").value;
	if(step5_involved == "true"){
		this.initialiseStep5();
	}

	var step6_involved = document.getElementById("step6_involved").value;
	if(step6_involved == "true"){
		this.initialiseStep6();
	}

	var step7_involved = document.getElementById("step7_involved").value;
	if(step7_involved == "true"){
		this.initialiseStep7();
	}

	var step8_involved = document.getElementById("step8_involved").value;
	if(step8_involved == "true"){
		this.initialiseStep8();
	}

	var step9_involved = document.getElementById("step9_involved").value;
	if(step9_involved == "true"){
		this.initialiseStep9();
	}
}

function checkErrorOk(step_ok, p_ok){
	if(step_ok == "true"){
		console.log("true : " + step_ok);
		p_ok.setAttribute("style", "color:#089A4C");
	}
	else{
		console.log("false : " + step_ok);
		p_ok.setAttribute("style", "color:#FF0000");
	}
}

function initialiseStep1(){
	var divStep1 = document.getElementById("divStep1");
	var step1_ok = document.getElementById("step1_ok").value;
	var p_ok = document.getElementById("p_ok1");
	
	this.checkErrorOk(step1_ok, p_ok);
	
	divStep1.setAttribute('style', "margin-left: 40px; visibility: visible");
}

function initialiseStep3(){
	var divStep3 = document.getElementById("divStep3");
	var step3_ok = document.getElementById("step3_ok").value;
	var p_ok = document.getElementById("p_ok2");
	
	this.checkErrorOk(step3_ok, p_ok);
	
	divStep3.setAttribute('style', "margin-left: 40px; visibility: visible");
}

function initialiseStep4(){
	var divStep4 = document.getElementById("divStep4");
	var step4_ok = document.getElementById("step4_ok").value;
	var p_ok = document.getElementById("p_ok3");
	
	this.checkErrorOk(step4_ok, p_ok);
	
	divStep4.setAttribute('style', "margin-left: 40px; visibility: visible");
}

function initialiseStep5(){
	var divStep5 = document.getElementById("divStep5");
	var step5_ok = document.getElementById("step5_ok").value;
	var p_ok = document.getElementById("p_ok4");
	
	this.checkErrorOk(step5_ok, p_ok);
	
	divStep5.setAttribute('style', "margin-left: 40px; visibility: visible");
}

function initialiseStep6(){
	var divStep6 = document.getElementById("divStep6");
	var step6_ok = document.getElementById("step6_ok").value;
	var p_ok = document.getElementById("p_ok5");
	
	this.checkErrorOk(step6_ok, p_ok);
	
	divStep6.setAttribute('style', "margin-left: 40px; visibility: visible");
}

function initialiseStep7(){
	var divStep7 = document.getElementById("divStep7");
	var step7_ok = document.getElementById("step7_ok").value;
	var p_ok = document.getElementById("p_ok6");
	
	this.checkErrorOk(step7_ok, p_ok);
	
	divStep7.setAttribute('style', "margin-left: 40px; visibility: visible");
}

function initialiseStep8(){
	var divStep8 = document.getElementById("divStep8");
	var step8_ok = document.getElementById("step8_ok").value;
	var p_ok = document.getElementById("p_ok7");
	
	this.checkErrorOk(step8_ok, p_ok);
	
	divStep8.setAttribute('style', "margin-left: 40px; visibility: visible");
}

function initialiseStep9(){
	var divStep9 = document.getElementById("divStep9");
	var step9_ok = document.getElementById("step9_ok").value;
	var p_ok = document.getElementById("p_ok8");
	console.log(step9_ok);
	this.checkErrorOk(step9_ok, p_ok);
	
	divStep9.setAttribute('style', "margin-left: 40px; visibility: visible");
}