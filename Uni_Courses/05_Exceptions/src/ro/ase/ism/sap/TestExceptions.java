package ro.ase.ism.sap;

import java.util.List;

public class TestExceptions {
	
	public static class DivisionByZeroException extends Exception{
		
	}
	
	
	//managed exception context
	public static float divide(float a, float b) throws DivisionByZeroException {
		if(b == 0)
			throw new DivisionByZeroException();
		else
		{
			return a/b;
		}
	}
	
	public static void doSomething(int x) {
		
		System.out.println("Let's do something");
		int value = 10;
		
		try {
			
			float result  = divide(value, x);
			value += result;
			if(x == 5) {
				List<Integer> values = null;
				values.add(10);
			}
			System.out.println("Ops are done !");
			
		} catch (DivisionByZeroException e) {
			value = 0;
		}
		catch(Exception ex) {
			System.out.println("Houston we have a problem: " + ex.getMessage());
		}
		finally {
			System.out.println("Hello from doSomething");
		}
		
		System.out.println("End result = " + value);
	}

	public static void main(String[] args) {
		
		int[] values = {10,20,30};
		float sum = 0;
		int n = 0;
		for(int value : values) {
			sum += value;
		}
		sum = sum / n;
		
//		int value  = 10;
//		int divider = 0;
//		int result = value / divider;
		
		System.out.println("Result  = " + sum);
		
		//check the managed exception context
		int vb1 = 10;
		int vb2 = 0;
		
		try {
			float result = divide(vb1, vb2);
		} catch (DivisionByZeroException e) {
			System.out.println("Sorry !");
		}
		
		System.out.println("End of example");
		
		doSomething(2);			//-> no exceptions
		//doSomething(0);		//-> DivisionByZero
		//doSomething(5);		//-> generic Catch
		
	}

}
