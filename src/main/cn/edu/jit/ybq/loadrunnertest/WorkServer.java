package cn.edu.jit.ybq.loadrunnertest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WorkServer {
    /**
     * queue
     *
     * @author ybq
     */
    static class WorkQueue {
        private final int[] workFlag;
        int nowNumber;

        WorkQueue(int totalNumber) {
            this.workFlag = new int[totalNumber];
            for (int i = 0; i < totalNumber; i++) {
                this.workFlag[i] = 0;
            }
            this.nowNumber = 1;
        }

        //接受客户端
        int giveOutWork() {
            int k = this.nowNumber;
            this.workFlag[this.nowNumber] = 1;

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.nowNumber++;
            return k;
        }

        boolean finishWork(int workNumber) {
            if (this.workFlag[workNumber] == 1) {
                this.workFlag[workNumber] = 2;
                return true;
            } else {
                System.err.println();
            }
            return false;
        }
    }


    /**
     * 客户端链接对话线程
     */
    static class AcceptClientThread extends Thread {
        private final Socket socket;
        private final int clientNumber;
        private final WorkQueue queue;

        public AcceptClientThread(Socket socket, int clientNumber, WorkQueue queue) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            this.queue = queue;
        }

        int giveOutWork() {
            try {
                sleep(100);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return queue.giveOutWork();
        }

        boolean finishWork(int workNumber) {
            return queue.finishWork(workNumber);
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                pw.println(this.clientNumber);
                pw.flush();
                String step = br.readLine();
                while (!step.equals("Apply")) {
                    sleep((int) (Math.random() * 100));
                    step = br.readLine();
                }
                int worknumber = this.giveOutWork();
                pw.println(worknumber);
                pw.flush();
                step = br.readLine();
                while (!step.equals("finish")) {
                    sleep(100);
                    step = br.readLine();
                }
                boolean result = this.finishWork(worknumber);
                pw.println(result);
                pw.flush();
                if (result) {
                    System.out.println("Work" + worknumber + "done" + clientNumber);
                }
                pw.close();
                br.close();
                socket.close();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static void main(String[] args) {
        ServerSocket serverSocket;
        WorkQueue queue = new WorkQueue(200000);
        try {
            serverSocket = new ServerSocket(8000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            int clientNumber = 0;
            while (true) {
                Socket socket = new Socket();
                socket = serverSocket.accept();
                clientNumber++;
                new AcceptClientThread(socket, clientNumber, queue).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
