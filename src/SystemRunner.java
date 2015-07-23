import java.io.BufferedReader;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jsybrand on 7/10/15.
 */
public class SystemRunner extends Thread {
    private String command[], logfile;
    AtomicBoolean isComplete = new AtomicBoolean(false);
    SystemRunner(String cmd[],String lFile)
    {
        command = cmd;
        logfile = lFile;
    }

    @Override
    public void run(){
        try {

            String cmd="";
            for(String s : command)cmd+=s;

            System.out.println(cmd);

            Process proc = Runtime.getRuntime().exec(command);
            Scanner scan = new Scanner(proc.getErrorStream());

            proc.waitFor();


            if(proc.exitValue()!=0) {
                String err = "";
                while(scan.hasNextLine())err += scan.nextLine() + "\n";
                CustomLog.log(err,logfile);
                throw new Exception(command[0] + " returned " + proc.exitValue() + " " + command[command.length - 1]);
            }

        }catch(Exception e){
            try {
                System.err.println(e);
                CustomLog.log(e.getClass().toString() + ":" + e.getMessage(), logfile);
            }catch(Exception ignore) {}
        }
        finally{
            isComplete.set(true);
        }
    }
}
