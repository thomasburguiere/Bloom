function initialiseFinalPage(){
	var step1_involved = document.getElementById("step1_involved");
	if(step1_involved){
		this.initialiseStep1();
	}
	
	var step2_involved = document.getElementById("step2_involved");
	if(step2_involved){
		this.initialiseStep1();
	}
	
	var step3_involved = document.getElementById("step3_involved");
	if(step3_involved){
		this.initialiseStep1();
	}
	
	var step4_involved = document.getElementById("step4_involved");
	if(step4_involved){
		this.initialiseStep1();
	}
	
	var step5_involved = document.getElementById("step5_involved");
	if(step5_involved){
		this.initialiseStep1();
	}
	
	var step6_involved = document.getElementById("step6_involved");
	if(step6_involved){
		this.initialiseStep1();
	}
	
	var step7_involved = document.getElementById("step7_involved");
	if(step7_involved){
		this.initialiseStep1();
	}
	
	var step8_involved = document.getElementById("step8_involved");
	if(step8_involved){
		this.initialiseStep1();
	}
}

function initialiseStep1(){
	var step1_ok = document.getElementById("step1_ok");
	var divStep1 = document.getElementById("divStep1");
	divStep1.setAttribute('style', "margin-left: 40px");
	divStep1.setAttribute("style", "visibility: visible");
	//divStep1.appendChild(divStep1Results);
}