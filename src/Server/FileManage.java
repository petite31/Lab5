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
        if (userDir.exists()) return "FAIL|Username already exists!";

        userDir.mkdirs();
        saveEmail(user, "Welcome", "Thank you for using this service. We hope you feel comfortable.");
        return "SUCCESS|Registration successful!";
    }

    public static String login(String user) {
        File userDir = new File(DATA_PATH + user);
        if (!userDir.exists()) return "FAIL|Account does not exist!";

        String[] files = userDir.list();
        return "SUCCESS|" + (files != null ? String.join(",", files) : "");
    }

    public static String saveEmail(String toUser, String subject, String content) {
        File userDir = new File(DATA_PATH + toUser);
        if (!userDir.exists()) return "FAIL|Recipient does not exist!";

        try (PrintWriter out = new PrintWriter(new File(userDir, subject + ".txt"))) {
            out.print(content.replace("<br>", "\n"));
            return "SUCCESS|Email sent successfully!";
        } catch (IOException e) {
            e.printStackTrace();
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
