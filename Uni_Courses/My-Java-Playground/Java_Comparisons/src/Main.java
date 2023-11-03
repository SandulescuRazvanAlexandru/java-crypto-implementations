public class Main {

    class Foo{
        Integer code;
        String description;
        int[] values;
        public Foo(int code, String description, int no){
            System.out.println("=");
            this.code = code;
            this.description = description;
            for(int i = 0;i<no;i++)
                values[i] = i+1;
        }
    }

    public static void main(String[] args) {
        String name1 = "John";
        String name2 ="John";

        if(name1 == name2)
            System.out.println("Strings are equal");
        else
            System.out.println("Strings are NOT equal");

        String name3 = new String("John");
        if(name1 == name3)
            System.out.println("Strings are equal");
        else
            System.out.println("Strings are NOT equal");

        Integer i1 = 10;
        Integer i2 = 10;

        if(i1 == i2)
            System.out.println("Integers are equal");
        else
            System.out.println("Integers are NOT equal");

        Integer i3 = 300;
        Integer i4 = 300;
        if(i3 == i4)
            System.out.println("Integers are equal");
        else
            System.out.println("Integers are NOT equal");
    }



}
