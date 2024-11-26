import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args){
        try(Socket ss = new Socket("localhost",6969)){
            BufferedReader ui = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream out = new DataOutputStream(ss.getOutputStream());
            new Thread(new MsgReceiver(ss)).start();
            String msg;
            do{
                msg = ui.readLine();
                out.writeBytes(msg + "\n");
                out.flush();
            }while(!msg.equalsIgnoreCase("FINE"));

            System.out.println("Disconnessione in corso...");
            ui.close();
            out.close();
            System.out.println("Disconnessione effettuata");

        }catch(IOException e){
            System.out.println("Errore di connessione");
        }
    }
}
