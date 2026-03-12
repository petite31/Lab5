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
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtUser;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    void handleLogin(ActionEvent event) {
        String user = txtUser.getText().trim();
        if (user.isEmpty()) return;

        String res = NetworkService.sendRequest("LOGIN|" + user);

        if (res != null && res.startsWith("SUCCESS")) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardView.fxml"));
                Parent root = loader.load();

                DashboardController dashboardCtrl = loader.getController();
                String files = res.split("\\|").length > 1 ? res.split("\\|")[1] : "";
                dashboardCtrl.initData(user, files);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("System Error", "Cannot load the Dashboard screen.");
            }

        } else {
            showAlert("Error", res != null ? res.split("\\|")[1] : "Cannot connect to the server!");
        }

    }

    // Hàm này chạy khi bấm nút Đăng ký
    @FXML
    void handleRegister(ActionEvent event) {
        String user = txtUser.getText().trim();
        System.out.println("Processing registration for: " + user);

        String res = NetworkService.sendRequest("REGISTER|" + user);

        if (res != null) {
            showAlert("Notification", res.split("\\|")[1]);
        } else {
            showAlert("Error", "Cannot connect to the server.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        DialogPane dialogPane = alert.getDialogPane();
        try {
            dialogPane.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());
            dialogPane.getStyleClass().add("modern-dialog");
            dialogPane.setGraphic(null);
        } catch (Exception e) {
            System.out.println("Cannot load CSS for Alert");
        }

        alert.showAndWait();
    }
}