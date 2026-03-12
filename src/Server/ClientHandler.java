package Server;

import java.net.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) { this.socket = socket; }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String line = in.readLine();
            if (line == null) return;

            String[] data = line.split("\\|");
            String cmd = data[0];

            switch (cmd) {
                case "REGISTER":
                    out.println(FileManage.register(data[1]));
                    break;
                case "LOGIN":
                    out.println(FileManage.login(data[1]));
                    break;
                case "SEND":
                    FileManage.saveEmail(data[1], data[2], data[3]);
                    out.println("SUCCESS|Đã gửi mail!");
                    break;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
