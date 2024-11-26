import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private Socket cs;

    public ClientHandler(Socket cs) {
        this.cs = cs;
    }

    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            DataOutputStream out = new DataOutputStream(cs.getOutputStream());
            Server.css.add(out);
            String msg = "";
            boolean done = false;
            boolean finish = false;
            int a = 0;
            int b = 0;
            while((msg = in.readLine()) != null && !msg.equalsIgnoreCase("FINE")){
                System.out.println("Messaggio da " + cs + ": " + msg);
                for(int i = 0; i < Server.css.size() && !msg.equalsIgnoreCase("FINE") && (msg.equalsIgnoreCase("A") || msg.equalsIgnoreCase("B")) && (!done && !finish); i++){
                    if(i==0){
                        synchronized (Server.class) {
                            if(msg.equalsIgnoreCase("A")){
                                Server.a++;
                            } else {
                                Server.b++;
                            }
                            a=Server.a;
                            b=Server.b;
                        }
                    }
                    Server.css.get(i).writeBytes("A : " + a + " B: " + b + "\n");
                    out.flush();
                    if(i==Server.css.size()-1){
                        done = true;
                    }
                }
                finish = true;
            }
            in.close();
            Server.css.remove(out);
            out.close();
            System.out.println("Client " + cs + " si è disconnesso.");
            cs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
