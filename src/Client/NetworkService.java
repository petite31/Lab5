package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkService {
    public static String sendRequest(String request) {
        try (Socket s = new Socket("localhost", 3101);
             PrintWriter out = new PrintWriter(s.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
            out.println(request);
            return in.readLine();
        } catch (IOException e) { return "FAIL|Lỗi kết nối Server!"; }
    }
}
