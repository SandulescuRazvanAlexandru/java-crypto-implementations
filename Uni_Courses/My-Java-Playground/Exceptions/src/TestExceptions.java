import java.util.List;

public class TestExceptions {

    public static class DivisonByZeroException extends Exception{

    }

    //decorating the method with the exception
    //managed exception context
    public static float divide(float a, float b) throws DivisonByZeroException {
        if (b == 0)
            throw new DivisonByZeroException();//generated the exception
        else
        {
            return a/b;
        }
    }

    //doSomething(2) -> result = 5; value = 15; Hello from doSomething; End result = 15
   //doSomething(0) -> divisionByZeroException; value = 0; finally; result = 0;
    //doSomething(5) -> result = 2; value = 12; try is crashed; the 2nd catch - Houston we have a problem; finally; End result = 12(even when try crashed, all the operations until then remains// if something crashes in the try, nothing after that instruction will execute)
    public static void doSomething(int x){
        System.out.println("Let's do something");
        int value = 10;

        try {
            float result =  divide(value, x);
            value += result;
            if(x == 5){
                List<Integer> values = null;
                values.add(10);
            }
            System.out.println("Ops are done!"); //will never be displayed if x == 5
        } catch (DivisonByZeroException e) {
          value = 0;
        }
        catch(Exception ex)
        {
            System.out.println("Houston we have a problem: " + ex.getMessage());
        }
        finally { //will always execute
            System.out.println("Hello from doSomething");
        }
        System.out.println("End result = " + value);
    }

    public static void main(String[] args)  {
        int[] values = {10, 20, 30};
        float sum = 0;
        int n = 0;
        for(int value: values)
        {
            sum += value;
        }
        sum = sum/n;

        //int value = 10;
        //Integer result = value / 0;

       System.out.println("Result = " + sum);

       //check the managed exception context
        int vb1 = 10;
        int vb2 = 0;

        //throw exception - terminates the function
        //try catch
        try {
            float result = divide(vb1, vb2);
        } catch (DivisonByZeroException e) {
            System.out.println("Sorry");
        }

        //with try catch the program will reach the end even though the exception is thrown
        System.out.println("End of example");

        doSomething(2);  //-> no exceptions
        //doSomething(0);  //-> DivisionByZero
        //doSomething(5); //-> generic catch
    }
}
