import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class levelString {
    File file;
    ArrayList<String> stringArr;

    public levelString(File f)
    {
        stringArr=new ArrayList<String>();
        this.file=f;
        Scanner fileReader;
        try{
            fileReader=new Scanner(file);

            //goes through code
            int i;
            while (fileReader.hasNextLine())
            {
                String nextLine=fileReader.nextLine();
                stringArr.add(nextLine);
            }
        }
        catch(Exception e)
        {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }

    public ArrayList<String> getStringArr() {
        return stringArr;
    }
}
