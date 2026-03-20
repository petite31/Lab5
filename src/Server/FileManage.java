package Server;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileManage {
    private static final String DATA_PATH = "MailData/";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static void init() {
        new File(DATA_PATH).mkdirs();
    }

    //1 & 2: Đăng ký
    public static String register(String user, String pass) {
        File userDir = new File(DATA_PATH + user);
        if (userDir.exists()) return "FAIL|Account already exists!";

        userDir.mkdirs();

        try (PrintWriter out = new PrintWriter(new File(userDir, "info.txt"))) {
            out.println("Username: " + user);
            out.println("Password: " + pass);
            out.println("Created At: " + LocalDateTime.now().format(FORMATTER));

        } catch (IOException e) { e.printStackTrace(); }

        try (PrintWriter out = new PrintWriter(new File(userDir, "Welcome.txt"))) {
            out.println("Time: " + LocalDateTime.now().format(FORMATTER));
            out.println("From: System Admin");
            out.println("To: " + user);
            out.println("------------------------------------------------");
            out.print("Thank you for using this service. we hope that you will feel comfortable.......");
        } catch (IOException e) { e.printStackTrace(); }

        return "SUCCESS|Registration successful!";
    }

    // 1: Đăng nhập
    public static String login(String user, String pass) {
        File userDir = new File(DATA_PATH + user);
        if (!userDir.exists()) return "FAIL|Account does not exist!";

        File infoFile = new File(userDir, "info.txt");
        if (!infoFile.exists()) return "FAIL|Account info missing!";

        try (BufferedReader reader = new BufferedReader(new FileReader(infoFile))) {
            String userLine = reader.readLine(); // Đọc dòng 1 (chứa Username)
            String passLine = reader.readLine(); // Đọc dòng 2 (chứa Password)

            if (userLine == null || passLine == null) {
                return "FAIL|Invalid account info file!";
            }

            String savedUser = userLine.replace("Username: ", "").trim();
            String savedPass = passLine.replace("Password: ", "").trim();


            if (!user.equals(savedUser) || !pass.equals(savedPass)) {
                return "FAIL|Incorrect username or password!";
            }
        } catch (IOException e) {
            return "FAIL|Error reading account info!";
        }

        return getMailList(userDir);
    }


    public static String getMailList(File userDir) {
        String[] files = userDir.list((dir, name) -> !name.equals("info.txt"));
        return "SUCCESS|" + (files != null && files.length > 0 ? String.join(",", files) : "");
    }

    // 3: Lưu trữ thư
    public static String saveEmail(String fromUser, String toUser, String subject, String content) {
        File userDir = new File(DATA_PATH + toUser);
        if (!userDir.exists()) return "FAIL|Recipient does not exist!";

        try (PrintWriter out = new PrintWriter(new File(userDir, subject + ".txt"))) {
            // Ghi Metadata vào ngay đầu file
            out.println("Time: " + LocalDateTime.now().format(FORMATTER));
            out.println("From: " + fromUser);
            out.println("To: " + toUser);
            out.println("------------------------------------------------");

            out.print(content.replace("<br>", "\n"));
            return "SUCCESS|Email sent successfully!";
        } catch (IOException e) {
            return "FAIL|Server error while saving email!";
        }
    }

    public static String readEmail(String user, String filename) {
        File file = new File(DATA_PATH + user + "/" + filename);
        if (!file.exists()) return "FAIL|Email not found!";

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("<br>");
            }
            return "SUCCESS|" + content.toString();
        } catch (IOException e) {
            return "FAIL|Error reading file!";
        }
    }
}