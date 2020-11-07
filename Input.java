import java.util.Scanner;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

class Input{

    public static Scanner sc = new Scanner(System.in);
    public static char readChar(){
        return sc.next().charAt(0);
    }
    public static int readInt(){    
        return sc.nextInt();
    }
    public static double readDouble(){
        return sc.nextDouble();
    }
    public static String readString(){
        return sc.nextLine();
    }
    public static String readFile(String fn){
        String file = "";
        try{
            file = new String(Files.readAllBytes(Paths.get(fn)), StandardCharsets.UTF_8);
        }catch(Exception e){

        }
        return  file;
    }
    /*
    public static ArrayList<Row> readCSV(String fn){
        ArrayList<Row> data =  new ArrayList<>();

        String file = readFile(fn);
        String[] line = file.split("\n");
        String header = line[0];
        for(int pos = 1; pos < line.length; pos++){
            data.add(new Row(header,line[pos]));
        }
        return data;
    }*/

}