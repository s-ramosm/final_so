package logica;

public class IntAHex {
	
	
	//Convierte decial a hex
	public String toHex(int decimal){
		//System.out.println("Dec:" + decimal);
		int res;
		String hex="";
		char hexchars[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};  
	    while(decimal>0) {
	    	res = decimal%16;
	    	hex = hexchars[res]+hex;
	    	decimal = decimal/16;
	    }

	    //Para el ejercicio trabajamos con Hexadecimales de 6 valores
	    //Asi que si el valor devuelto por ejempo HEX de 0 = 0 se llena de ceros
	    while(hex.length() < 6) {
	    	hex = "0" + hex;
	    }
	    
	    return hex; 
	}    

}
