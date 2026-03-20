package Server;

import java.net.*;
import java.io.*;

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
                    out.println(FileManage.register(data[1], data[2]));
                    break;

                case "LOGIN":
                    out.println(FileManage.login(data[1], data[2]));
                    break;

                case "REFRESH":
                    out.println(FileManage.getMailList(new File("MailData/" + data[1])));
                    break;

                case "READ":
                    out.println(FileManage.readEmail(data[1], data[2]));
                    break;

                case "SEND":
                    String fromUser = data[1];
                    String toUser = data[2];
                    String subject = data.length > 3 ? data[3] : "No_Subject";
                    String content = data.length > 4 ? data[4] : "";


                    String currentTime = java.time.LocalDateTime.now().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                    );


                    System.out.println("\n[SERVER LOG] Ghi nhận email mới được gửi:");
                    System.out.println(" ↳ Time: " + currentTime);
                    System.out.println(" ↳ From: " + fromUser);
                    System.out.println(" ↳ To: " + toUser);
                    System.out.println(" ↳ Subject: " + subject);
                    System.out.println(" ↳ Content: \n------------------------------------------------");
                    System.out.println(content.replace("<br>", "\n"));
                    System.out.println("------------------------------------------------");


                    String result = FileManage.saveEmail(fromUser, toUser, subject, content);

                    if (result.startsWith("SUCCESS")) {
                        System.out.println(" ↳ SUCCESS: Đã lưu thành công vào MailData/" + toUser + "/" + subject + ".txt");
                    } else {
                        System.out.println(" ↳ FAILED: Lỗi khi lưu file!");
                    }

                    out.println(result);
                    break;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}