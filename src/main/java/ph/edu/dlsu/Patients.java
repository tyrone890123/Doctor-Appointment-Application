package ph.edu.dlsu;

import java.io.*;

public class Patients extends AllFunctions {

    public int covidchecker(String a) throws IOException {
        File list = new File("database/SymptomsOfPUI.txt");
        BufferedReader reader = new BufferedReader(new FileReader(list));

        String program="";
        String line=reader.readLine();
        while (line != null)
        {
            program = program + line + System.lineSeparator();
            line = reader.readLine();
        }
        String[] holder=program.split("\n");
        int returner=0;
        for(int i=0;i<holder.length;i++){
            if(a.contains(holder[i].trim())){
                returner++;
            }
        }
        return returner;
    }

    public static void deleteitem(File sched,File sched2,String time) throws IOException {


        BufferedReader reader = new BufferedReader(new FileReader(sched));
        BufferedReader reader2 = new BufferedReader(new FileReader(sched2));

        String program="";
        String line=reader.readLine();
        int size=1;
        while (line != null)
        {
            program = program + line + System.lineSeparator();
            line = reader.readLine();
            size++;
        }
        String[] holder=new String[size];
        holder=program.split("\n");

        String program2="";
        String line2=reader2.readLine();
        while (line2 != null)
        {
            program2= program2 + line2 + System.lineSeparator();
            line2 = reader2.readLine();
        }
        String[] holder2=new String[size];
        holder2=program2.split("\n");

        String holder4=time.trim()+" Taken";
        String timeinsched="";
        String timeinsched2="";

        int check=0;
        for(int i=0;i<holder.length;i++){
            if(holder[i].trim().equals(holder4)){
                check++;
                timeinsched=timeinsched+holder[i];
                timeinsched2=timeinsched2+holder2[i];
            }
        }
        String[] holder3=timeinsched.split(" ");

        if(check==1){
            String replaced=program.replace(timeinsched,holder3[0]);
            String replaced2=program2.replace(timeinsched2,holder3[0]);
            FileWriter writer = new FileWriter(sched);
            writer.write(replaced);
            writer.close();

            FileWriter writer2 = new FileWriter(sched2);
            writer2.write(replaced2);
            writer2.close();
        }else {
            System.out.println("INVALID TIME");
        }

    }

    public static void userinput(File sched,File sched2,String name,String symptoms,String time) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(sched));
        BufferedReader reader2 = new BufferedReader(new FileReader(sched2));

        String program="";
        String line=reader.readLine();
        while (line != null)
        {
            program = program + line + System.lineSeparator();
            line = reader.readLine();
        }
        String[] holder;
        holder=program.split("\n");

        String program2="";
        String line2=reader2.readLine();
        while (line2 != null)
        {
            program2= program2 + line2 + System.lineSeparator();
            line2 = reader2.readLine();
        }


        int check=0;
        for(int i=0;i<holder.length;i++){
            if(holder[i].trim().equals(time)){
                check++;
            }
        }
        if(check==1){
            String replaced=program.replace(time,time+" Taken");
            String replaced2=program2.replace(time,time+" "+name+","+symptoms);
            FileWriter writer = new FileWriter(sched);
            writer.write(replaced);
            writer.close();

            FileWriter writer2 = new FileWriter(sched2);
            writer2.write(replaced2);
            writer2.close();
        }

    }
}
