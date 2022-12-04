package cn.edu.jit.ybq.loadrunnertest;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class Test {
    public void ApplyProcess() throws IOException {
       Client client = new Client(new Socket("127.0.0.1",8080));
        try{
            int workNumber = client.applyWork();
            int result = client.execute(workNumber);
            boolean ensureResult=client.sendStatus(workNumber);
            if (!ensureResult){
                System.err.println("Error !! work check error");
                client.socket.close();
                System.exit(0);
            }
            else {
                System.out.println("Finish work no ."+ workNumber);
            }
        } catch (UnknownHostException e) {
            System.err.println("Unknown host : 127.0.0.1");
            client.socket.close();
            System.exit(1);
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to 127.0.0.1"+e);
            client.socket.close();
            System.exit(1);
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) throws IOException {
new Test().ApplyProcess();
    }
}
