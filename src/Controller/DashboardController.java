package Controller;

import Client.NetworkService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private Label lblUser;
    @FXML private ListView<String> lvEmails;
    @FXML private TextField txtTo;
    @FXML private TextField txtSubject;
    @FXML private TextArea txtContent;

    private String currentUser;

    @FXML
    public void initialize() {
        lvEmails.setOnMouseClicked((MouseEvent event) -> {
            // Kiểm tra nếu người dùng click đúp (2 lần)
            if (event.getClickCount() == 2) {
                String selectedFile = lvEmails.getSelectionModel().getSelectedItem();
                if (selectedFile != null) {
                    readEmailContent(selectedFile);
                }
            }
        });
    }

    // 2. Nhận dữ liệu từ màn hình Đăng nhập truyền sang
    public void initData(String username, String fileList) {
        this.currentUser = username;
        lblUser.setText("Hộp thư của: " + username);

        if (fileList != null && !fileList.isEmpty()) {
            lvEmails.getItems().addAll(fileList.split(","));
        }
    }

    // 3. Gửi Email
    @FXML
    void handleSend(ActionEvent event) {
        String to = txtTo.getText().trim();
        String sub = txtSubject.getText().trim();
        String content = txtContent.getText().trim();

        if (to.isEmpty() || content.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập người nhận và nội dung!");
            return;
        }

        String encodedContent = content.replace("\n", "<br>");

        String req = "SEND|" + to + "|" + sub + "|" + encodedContent;
        String res = NetworkService.sendRequest(req);

        if (res != null) {
            showAlert("Thông báo", res.split("\\|")[1]);
        }

        // Gửi xong thì xóa trắng form
        txtTo.clear();
        txtSubject.clear();
        txtContent.clear();
    }

    // 4. Làm Mới Hộp Thư
    @FXML
    void handleRefresh(ActionEvent event) {
        String res = NetworkService.sendRequest("LOGIN|" + currentUser);
        if (res != null && res.startsWith("SUCCESS")) {
            lvEmails.getItems().clear(); // Xóa danh sách cũ
            String[] parts = res.split("\\|");

            // Nếu có file thì mới cắt chuỗi và đưa vào list
            if (parts.length > 1 && !parts[1].isEmpty()) {
                lvEmails.getItems().addAll(parts[1].split(","));
            }
        }
    }

    // 5. Logic gọi Server để Đọc nội dung file
    private void readEmailContent(String filename) {
        String res = NetworkService.sendRequest("READ|" + currentUser + "|" + filename);

        if (res != null && res.startsWith("SUCCESS")) {
            String content = res.substring(8).replace("<br>", "\n");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Đọc Email");
            alert.setHeaderText("Tiêu đề: " + filename.replace(".txt", ""));

            TextArea area = new TextArea(content);
            area.setWrapText(true);
            area.setEditable(false);
            alert.getDialogPane().setContent(area);
            alert.showAndWait();

        } else {
            showAlert("Lỗi", "Không thể tải nội dung thư từ Server!");
        }
    }

    // 6. Đăng Xuất
    @FXML
    void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}