package cn.edu.jit.ybq.loadrunnertest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 客户端申请任务
 *
 * @author yaoboqian
 */

public class Client {
    Socket socket;
    int clientNumber;
    BufferedReader reader;
    PrintWriter writer;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream());
        this.clientNumber = Integer.parseInt(reader.readLine());
    }

    public int applyWork() {
        int status;
        try {
            this.writer.println("Apply");
            writer.flush();
            status = Integer.parseInt(reader.readLine());
            if (String.valueOf(status).equals("")) {
                System.err.println("Server has no work to do");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Error : cannot apply the network");
            throw new RuntimeException(e);
        }
        return status;
    }

    /**
     * @return 处理
     * @author ybq
     */
    public int execute(int status) throws IOException {
        System.out.println("好的已经处理成功祝您省会愉快");
        return status;
    }

    /**
     * 传递结果到服务器
     * @param workNumber
     * @return
     */

    public boolean sendStatus(int workNumber) {
        boolean trueOrFalse;
        try {
            this.writer.println("finish");
            writer.flush();
            trueOrFalse = Boolean.parseBoolean(this.reader.readLine());
            if (!trueOrFalse) {
                System.err.println("Error : could not launch the network");
                System.exit(1);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
