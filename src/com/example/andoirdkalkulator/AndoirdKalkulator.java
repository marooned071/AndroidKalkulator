package com.example.andoirdkalkulator;



import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

class CalcException extends Exception{
	CalcException(String msg){
		super(msg);
	}
}
public class AndoirdKalkulator extends Activity {

	private TextView textView_large;
	private TextView textView_medium;
	private TextView textView_small;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_andoird_kalkulator);
		
		textView_large = (TextView) findViewById(R.id.textView_large);
		textView_medium = (TextView) findViewById(R.id.textView_medium);
		textView_small = (TextView) findViewById(R.id.textView_small);

		
		Listener_Buttons buttonListener = new Listener_Buttons(this);

		findViewById(R.id.button_main_enter).setOnClickListener(buttonListener);
		findViewById(R.id.button_main_allClear).setOnClickListener(buttonListener);
		findViewById(R.id.button_main_clearExisting).setOnClickListener(buttonListener);
		
		findViewById(R.id.button_operation_divide).setOnClickListener(buttonListener);
		findViewById(R.id.button_operation_multiply).setOnClickListener(buttonListener);
		findViewById(R.id.button_operation_plus).setOnClickListener(buttonListener);
		findViewById(R.id.button_operation_minus).setOnClickListener(buttonListener);
		
		findViewById(R.id.button_operation_mod).setOnClickListener(buttonListener);
		findViewById(R.id.button_operation_sqrt).setOnClickListener(buttonListener);
		findViewById(R.id.button_operation_pow).setOnClickListener(buttonListener);
		findViewById(R.id.button_operation_1x).setOnClickListener(buttonListener);
		
		findViewById(R.id.button_operation_sin).setOnClickListener(buttonListener);
		findViewById(R.id.button_operation_cos).setOnClickListener(buttonListener);
		findViewById(R.id.button_operation_tg).setOnClickListener(buttonListener);
		findViewById(R.id.button_operation_ctg).setOnClickListener(buttonListener);
		
		findViewById(R.id.button_operation_ln).setOnClickListener(buttonListener);
		findViewById(R.id.button_operation_log).setOnClickListener(buttonListener);
		findViewById(R.id.button_operation_gcd).setOnClickListener(buttonListener);
		findViewById(R.id.button_operation_factorial).setOnClickListener(buttonListener);
		
		findViewById(R.id.button_const_pi).setOnClickListener(buttonListener);
		findViewById(R.id.button_const_e).setOnClickListener(buttonListener);
		findViewById(R.id.button_const_phi).setOnClickListener(buttonListener);
		
		findViewById(R.id.button_other_dot).setOnClickListener(buttonListener);
		findViewById(R.id.button_other_setPlusMinus).setOnClickListener(buttonListener);
		
		
		
		findViewById(R.id.button_number_0).setOnClickListener(buttonListener);
		findViewById(R.id.button_number_1).setOnClickListener(buttonListener);
		findViewById(R.id.button_number_2).setOnClickListener(buttonListener);
		findViewById(R.id.button_number_3).setOnClickListener(buttonListener);
		findViewById(R.id.button_number_4).setOnClickListener(buttonListener);
		findViewById(R.id.button_number_5).setOnClickListener(buttonListener);
		findViewById(R.id.button_number_6).setOnClickListener(buttonListener);
		findViewById(R.id.button_number_7).setOnClickListener(buttonListener);
		findViewById(R.id.button_number_8).setOnClickListener(buttonListener);
		findViewById(R.id.button_number_9).setOnClickListener(buttonListener);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.andoird_kalkulator, menu);
		return true;
	}
	
	
	
	//OBSLUGA KALULATORA
	
	private final int NORMAL_MODE = 0;
	private final int ERROR_MODE = 1;
	private int displayMode;
	private boolean clearOnNextDigit;
	private double lastNumber=0.0;
	private String lastOperator="0";
	
	public void textViewUp(){
		textView_small.setText(textView_medium.getText());
		textView_medium.setText(textView_large.getText());
	}
	

	public void setDisplayString(String s){
		System.out.println("DISPLAY : "+s);
		textView_large.setText(s);
		
	}

	public String getDisplayString (){
		return (String) textView_large.getText();
	}

	public void onDigitAppend(int digit){
		if (clearOnNextDigit)
			setDisplayString("");

		String displayString = getDisplayString();
		
		if (displayString.indexOf("0") == 0){ //jesli zero na pierwszym miejscu (na wyswietlaczu)
			displayString = displayString.substring(1);
		}

		if ((!displayString.equals("0") || digit > 0)  && displayString.length() < R.integer.max_length_large){
			setDisplayString(displayString + digit);
		}	
		displayMode = NORMAL_MODE;
		clearOnNextDigit = false;
	}

	public void onDecimalPoint(){
		displayMode = NORMAL_MODE;

		if (clearOnNextDigit){
			setDisplayString("");
		}
		String displayString = getDisplayString();
	
		if(!displayString.contains(".")){ //jesli nie zawiera kropki
			setDisplayString(displayString+".");
		}
		
	}

	public void onPlusMinus(){
		String dispalyString = getDisplayString();
		
		if (dispalyString.length() > 0 && !dispalyString.equals("0")){
			if (dispalyString.indexOf("-") == 0)
				setDisplayString(dispalyString.substring(1));
			else
				setDisplayString("-" + dispalyString);
		}
	}

	public void onClearAll(){
		setDisplayString("0");
		textView_small.setText("");
		textView_medium.setText("");
		lastOperator = "0";
		lastNumber = 0;
		displayMode = NORMAL_MODE;
		clearOnNextDigit = true;
	}
	
	public void onClearExisting(){
		setDisplayString("0");
		clearOnNextDigit = true;
		displayMode = NORMAL_MODE;
	}

	public double getNumberInDisplay()	{
		String input = (String )textView_large.getText();
		double d=0.0;
		try{
			d=Double.parseDouble(input);
		}catch(NumberFormatException e){}
		return d;
	}
	
	
	public void onOperation(String op) {
		if (displayMode != ERROR_MODE){
			double numberInDisplay = getNumberInDisplay();
			
			if(op.equals("1/x")||op.equals("sqrt")||op.equals("sin")||op.equals("cos")||op.equals("tg")||op.equals("ctg")||op.equals("factorial")||op.equals("ln")){ //operatory unarne
				try{
					double result = getResult(op);
					displayResult(result);
					lastOperator = "0";
					clearOnNextDigit = true;
					return;
				}catch(CalcException e){
					onError(e.getMessage());
				}

			}

			else if (!lastOperator.equals("0")){
				try{
					double result = getResult(lastOperator);
					displayResult(result);
					lastNumber = result;
				}

				catch (CalcException e){
					onError(e.getMessage());
				}
			}
		
			else{
				lastNumber = numberInDisplay;
			}
			
			clearOnNextDigit = true;
			lastOperator = op;
		}
	}

	public void onEquals(){
		double result = 0;

		if (displayMode != ERROR_MODE){
			try	{
				result = getResult(lastOperator);
				displayResult(result);
			}
			
			catch (CalcException e)	{
				onError(e.getMessage());
			}

			lastOperator = "0";
		}
	}

	private double getResult(String operator) throws CalcException {
		double result = 0;
		double numberInDisplay = getNumberInDisplay();

		if (operator.equals("/")){
			if (numberInDisplay == 0)
				throw (new CalcException("Dziel. przez 0!"));

			result = lastNumber / numberInDisplay;
		}
			
		else if (operator.equals("*"))
			result = lastNumber * numberInDisplay;

		else if (operator.equals("-"))
			result = lastNumber - numberInDisplay;

		else if (operator.equals("+"))
			result = lastNumber + numberInDisplay;
		
		else if (operator.equals("mod")){
			result = lastNumber%numberInDisplay;
		}
		else if (operator.equals("pow")){
			result = Math.pow(lastNumber, numberInDisplay);
		}
		
		else if(operator.equals("1/x")){
			result = Math.pow(numberInDisplay, -1);
		}
		else if(operator.equals("sqrt")){
			result = Math.pow(numberInDisplay, 0.5);
		}
		else if(operator.equals("sin")){
			result = Math.sin(numberInDisplay);
		}
		else if(operator.equals("cos")){
			result = Math.cos(numberInDisplay);
		}
		else if(operator.equals("tg")){
			result = Math.tan(numberInDisplay);
		}
		else if(operator.equals("ctg")){
			result =  Math.pow(Math.tan(numberInDisplay), -1);
		}
		else if(operator.equals("ln")){
			result = Math.log(numberInDisplay);
		}
		else if(operator.equals("log")){
			result = Math.log(lastNumber)/Math.log(numberInDisplay);
		}
		else if(operator.equals("gcd")){
			if(isInteger(lastNumber)&&isInteger(numberInDisplay)){
				result = (double) GCD((int)lastNumber,(int)numberInDisplay);
			}
			else{
				throw (new CalcException("Nie calkowita!"));
			}

		}
		else if(operator.equals("factorial")){
			if(!isInteger(numberInDisplay)) 
				throw (new CalcException("Nie calkowita!"));
			
			result =  factorial((int)numberInDisplay);
			
		}
		
		return result;
	}
	
	private static int GCD(int a, int b){
	   if (b==0) return a;
	   return GCD(b,a%b);
	}
	
    private  static int factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
	
	private static boolean isInteger(double d) {
		  // Note that Double.NaN is not equal to anything, even itself.
		  return (d == Math.floor(d)) && !Double.isInfinite(d);
		}



	public void displayResult(double result){
		textViewUp();
		setDisplayString(Double.toString(result));
		
		lastNumber = result;
//		displayMode = RESULT_MODE;
		clearOnNextDigit = true;
		
	}

	public void onError(String errorMessage){
		setDisplayString(errorMessage);
		lastNumber = 0;
		displayMode = ERROR_MODE;
		clearOnNextDigit = true;
	}

	public void onConstans(String number){
		displayMode = NORMAL_MODE;
		double c=0.0;
		if(number.equals("pi")){
			c=Math.PI;
			
		}
		else if(number.equals("e")){
			c=Math.E;
		}
		else if(number.equals("phi")){
			c=1.618033988749894882;
		}

		setDisplayString(c+"");
		clearOnNextDigit = true;
		
	}
	
}

/**
 * Listener przyciskow 
 *
 */
class Listener_Buttons  implements OnClickListener {

	private AndoirdKalkulator ak;
	Listener_Buttons(AndoirdKalkulator ak){
		this.ak=ak;
	}
	
	@Override
	public void onClick(View view) {
		 switch (view.getId()) {
		 
		 //MAIN
		    case R.id.button_main_clearExisting:{
		    	ak.onClearExisting();
		    	break;
		    }
		    case R.id.button_main_enter:{
		    	ak.onEquals();
		    	break;
		    }
		    case R.id.button_main_allClear:{
		    	ak.onClearAll();
		    	break;
		    }
		   //OPERATIOn
		    case R.id.button_operation_divide:{
		    	ak.onOperation("/");
		    	break;
		    }
		    case R.id.button_operation_multiply:{
		    	ak.onOperation("*");
		    	break;
		    }
		    case R.id.button_operation_plus:{
		    	ak.onOperation("+");
		    	break;
		    }
		    case R.id.button_operation_minus:{
		    	ak.onOperation("-");
		    	break;
		    }
		    case R.id.button_operation_mod:{
		    	ak.onOperation("mod");
		    	break;
		    }
		    case R.id.button_operation_sqrt:{
		    	ak.onOperation("sqrt");
		    	break;
		    }
		    case R.id.button_operation_pow:{
		    	ak.onOperation("pow");
		    	break;
		    }
		    case R.id.button_operation_1x:{
		    	ak.onOperation("1/x");
		    	break;
		    }
		    case R.id.button_operation_sin:{
		    	ak.onOperation("sin");
		    	break;
		    }
		    case R.id.button_operation_cos:{
		    	ak.onOperation("cos");
		    	break;
		    }
		    case R.id.button_operation_tg:{
		    	ak.onOperation("tg");
		    	break;
		    }
		    case R.id.button_operation_ctg:{
		    	ak.onOperation("ctg");
		    	break;
		    }
		    case R.id.button_operation_ln:{
		    	ak.onOperation("ln");
		    	break;
		    }
		    case R.id.button_operation_log:{
		    	ak.onOperation("log");
		    	break;
		    }
		    case R.id.button_operation_gcd:{
		    	ak.onOperation("gcd");
		    	break;
		    }
		    case R.id.button_operation_factorial:{
		    	ak.onOperation("factorial");
		    	break;
		    }
		    //CONST
		    
		    case R.id.button_const_pi:{
		    	ak.onConstans("pi");
		    	break;
		    }
		    
		    case R.id.button_const_e:{
		    	ak.onConstans("e");
		    	break;
		    }
		    case R.id.button_const_phi:{
		    	ak.onConstans("phi");
		    	break;
		    }
		    

		 
		 //LICZBY
		    case R.id.button_number_0:{
		    	ak.onDigitAppend(0);
		    	break;
		    }
		    case R.id.button_number_1:{
		    	ak.onDigitAppend(1);
		    	break;
		    }
		    case R.id.button_number_2:{
		    	ak.onDigitAppend(2);
		    	break;
		    }
		    case R.id.button_number_3:{
		    	ak.onDigitAppend(3);
		    	break;
		    }
		    case R.id.button_number_4:{
		    	ak.onDigitAppend(4);
		    	break;
		    }
		    case R.id.button_number_5:{
		    	ak.onDigitAppend(5);
		    	break;
		    }
		    case R.id.button_number_6:{
		    	ak.onDigitAppend(6);
		    	break;
		    }
		    case R.id.button_number_7:{
		    	ak.onDigitAppend(7);
		    	break;
		    }
		    case R.id.button_number_8:{
		    	ak.onDigitAppend(8);
		    	break;
		    }
		    case R.id.button_number_9:{
		    	ak.onDigitAppend(9);
		    	break;
		    }
		    
		    //OTHER
		    case R.id.button_other_dot:{
		    	ak.onDecimalPoint();
		    	break;
		    }
		    case R.id.button_other_setPlusMinus:{
		    	ak.onPlusMinus();
		    	break;
		    }
		 }
	}
	
}


