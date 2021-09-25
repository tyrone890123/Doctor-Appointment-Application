package ph.edu.dlsu;

import java.io.*;

public class Doctors extends AllFunctions {

    public static String[] getAccounts() throws IOException {
        File list = new File("database/doctor_list.txt");
        BufferedReader reader = new BufferedReader(new FileReader(list));
        String program = "";
        String line = reader.readLine();
        while(line != null) {
            program = program + line + System.lineSeparator();
            line = reader.readLine();
        }
        String[] accountsSep = program.split("\n");
        return accountsSep;
    }

    public static int login(String username, String pass, String path) throws IOException {
        File list = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(list));
        String program = "";
        String line = reader.readLine();
        while (line != null) {
            program = program + line + System.lineSeparator();
            line = reader.readLine();
        }
        String[] accountsSep = program.split("\n");
        int identifier = -1;
        for (int i = 0; i < accountsSep.length; i++) {
            String[] holder1 = accountsSep[i].split(" ");
            if((holder1[0].trim().equals(username) && (holder1[1].trim().equals(pass)))) {
                identifier = i;
                break;
            }
        }
        return identifier;
    }

    public static void scheduler(File sched,File sched2) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(sched));

        int interval=Integer.parseInt(reader.readLine());
        String[] timein = splitter(reader.readLine());
        String[] timeout = splitter(reader.readLine());

        String holder="";
        String replaced=timein[0]+":"+timein[1]+" Time in";

        int hours=Integer.parseInt(timein[0]);
        int mins=Integer.parseInt(timein[1]);

        String finaltime=timeout[0]+":"+timeout[1];
        while (!holder.equals(finaltime)){
            mins=mins==60?mins=0:mins+interval;
            hours=mins>=60?hours+1:hours;
            mins=mins>=60?mins=mins-60:mins;
            if(mins>Integer.parseInt(timeout[1])){
                if(hours>=Integer.parseInt(timeout[0])){
                    break;
                }else {
                    holder=Integer.toString(hours)+":"+(mins==0?"00":Integer.toString(mins));
                    replaced=replaced+"\n"+holder;
                }
            }else{
                holder=Integer.toString(hours)+":"+(mins==0?"00":Integer.toString(mins));
                replaced=replaced+"\n"+holder;
            }
        }

        replaced = replaced+" Time out";

        FileWriter writer = new FileWriter(sched);
        writer.write(replaced);
        writer.close();

        FileWriter writer2 = new FileWriter(sched2);
        writer2.write(replaced);
        writer2.close();
    }


}
