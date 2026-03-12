package Controller;

import Client.NetworkService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private Label lblUser;
    @FXML private ListView<String> lvEmails;
    @FXML private TextField txtTo;
    @FXML private TextField txtSubject;
    @FXML private TextArea txtContent;

    private String currentUser;

    // Hàm này dùng để nhận dữ liệu từ màn hình Login truyền sang
    public void initData(String username, String fileList) {
        this.currentUser = username;
        lblUser.setText("Hộp thư của: " + username);

        if (fileList != null && !fileList.isEmpty()) {
            lvEmails.getItems().addAll(fileList.split(","));
        }
    }

    @FXML
    void handleSend(ActionEvent event) {
        String to = txtTo.getText().trim();
        String sub = txtSubject.getText().trim();
        String content = txtContent.getText().trim();

        if (to.isEmpty() || content.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập người nhận và nội dung!");
            return;
        }

        String req = "SEND|" + to + "|" + sub + "|" + content;
        String res = NetworkService.sendRequest(req);
        showAlert("Thông báo", res.split("\\|")[1]);

        // Gửi xong thì xóa trắng form cho đẹp
        txtTo.clear();
        txtSubject.clear();
        txtContent.clear();
    }

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