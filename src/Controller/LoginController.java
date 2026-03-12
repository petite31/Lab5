package Controller;

import Client.NetworkService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML // Thẻ này giúp nối biến txtUser với cái ô text bạn kéo thả trong Scene Builder
    private TextField txtUser;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    // Hàm này chạy khi bấm nút Đăng nhập
    @FXML
    void handleLogin(ActionEvent event) {
        String user = txtUser.getText().trim();
        if (user.isEmpty()) return;

        String res = NetworkService.sendRequest("LOGIN|" + user);

        if (res != null && res.startsWith("SUCCESS")) {
            try {
                // Tải màn hình Dashboard với đường dẫn đúng chuẩn cấu trúc của bạn
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardView.fxml"));
                Parent root = loader.load();

                // Truyền dữ liệu sang Dashboard
                DashboardController dashboardCtrl = loader.getController();
                String files = res.split("\\|").length > 1 ? res.split("\\|")[1] : "";
                dashboardCtrl.initData(user, files);

                // Chuyển cảnh
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Lỗi hệ thống", "Không thể tải màn hình Dashboard.");
            }

        } else {
            showAlert("Lỗi", res != null ? res.split("\\|")[1] : "Không thể kết nối Server!");
        }

    }

    // Hàm này chạy khi bấm nút Đăng ký
    @FXML
    void handleRegister(ActionEvent event) {
        String user = txtUser.getText().trim();
        System.out.println("Đang xử lý đăng ký cho: " + user);

        String res = NetworkService.sendRequest("REGISTER|" + user);

        if (res != null) {
            showAlert("Thông báo", res.split("\\|")[1]);
        } else {
            showAlert("Lỗi", "Không thể kết nối tới Server.");
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
