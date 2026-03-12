package Server;

import java.io.*;
import java.nio.file.*;

public class FileManage {
    private static final String DATA_PATH = "MailData/";

    public static void init() {
        new File(DATA_PATH).mkdirs();
    }

    public static String register(String user) {
        File userDir = new File(DATA_PATH + user);
        if (userDir.exists()) return "FAIL|Tài khoản đã tồn tại!";

        userDir.mkdirs();
        saveEmail(user, "Welcome", "Thank you for using this service. We hope you feel comfortable.");
        return "SUCCESS|Đăng ký thành công!";
    }

    public static String login(String user) {
        File userDir = new File(DATA_PATH + user);
        if (!userDir.exists()) return "FAIL|Tài khoản không tồn tại!";

        String[] files = userDir.list();
        return "SUCCESS|" + (files != null ? String.join(",", files) : "");
    }

    public static void saveEmail(String toUser, String subject, String content) {
        try (PrintWriter out = new PrintWriter(DATA_PATH + toUser + "/" + subject + ".txt")) {
            out.print(content);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static String readEmail(String user, String filename) {
        File file = new File(DATA_PATH + user + "/" + filename);
        if (!file.exists()) return "FAIL|Thư không tồn tại!";

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("<br>"); // Đóng gói để truyền qua mạng
            }
            return "SUCCESS|" + content.toString();
        } catch (IOException e) {
            return "FAIL|Lỗi khi đọc file!";
        }
    }

}
