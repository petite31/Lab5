package Server;

import java.net.ServerSocket;

public class MailServer {
        public static void main(String[] args) {
            FileManage.init();
            try (ServerSocket server = new ServerSocket(3101)) {
                System.out.println("Server is running on port 3101...");
                while (true) {
                    new ClientHandler(server.accept()).start();
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
}
