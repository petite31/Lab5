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
                    String toUser = data[1];
                    String subject = data.length > 2 ? data[2] : "No_Subject";
                    String content = data.length > 3 ? data[3] : "";

                    // Log Server
                    System.out.println("\n[SERVER LOG] Received an email sending request:");
                    System.out.println(" ↳ Recipient account identified: " + toUser);
                    System.out.println(" ↳ Email subject: " + subject);

                    // Save
                    String result = FileManage.saveEmail(toUser, subject, content);

                    // Console
                    if (result.startsWith("SUCCESS")) {
                        System.out.println(" ↳ Successfully created file in directory: MailData/" + toUser);
                    } else {
                        System.out.println(" ↳ FAILED: " + result.split("\\|")[1]);
                    }
                    break;

                case "READ":
                    out.println(FileManage.readEmail(data[1], data[2]));
                    break;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
