package innout.controller;

import java.io.IOException;
import java.util.Optional;

import innout.model.Admin;
import innout.model.User;
import innout.service.UserService;
import innout.service.UserStorageService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableCell;

public class ManageUserController {

    @FXML private TableView<User> userTableView;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> passwordColumn;
    @FXML private TableColumn<User, Void> deleteColumn;

    @FXML private Button kembaliButton;

    private UserService userService;
    private ObservableList<User> userList;

    public ManageUserController() {
        userService = new UserService();
    }

    @FXML
    private void initialize() {
        userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        emailColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        passwordColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPassword()));

        deleteColumn.setCellFactory(column -> {
            return new TableCell<User, Void>() {
                private final Button deleteButton = new Button("Delete");
                private final Button toggleTypeButton = new Button();

                private final HBox pane = new HBox(5);

                {
                    deleteButton.getStyleClass().add("delete-button");
                    toggleTypeButton.getStyleClass().add("toggle-type-button");

                    pane.setAlignment(Pos.CENTER);
                    pane.getChildren().addAll(deleteButton, toggleTypeButton);

                    deleteButton.setOnAction(e -> {
                        User userToDelete = getTableRow().getItem();
                        if (userToDelete != null) {
                            handleDeleteUser(userToDelete);
                        }
                    });

                    toggleTypeButton.setOnAction(e -> {
                        User userToToggle = getTableRow().getItem();
                        if (userToToggle != null) {
                            toggleUserType(userToToggle);
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        User user = getTableRow().getItem();
                        if (user != null) {
                            if (user instanceof Admin) {
                                toggleTypeButton.setText("Set to User");
                            } else {
                                toggleTypeButton.setText("Set to Admin");
                            }
                            setGraphic(pane);
                        } else {
                            setGraphic(null);
                        }
                    }
                }
            };
        });

        loadUserList();
    }


    private void loadUserList() {
        userList = FXCollections.observableArrayList(userService.getAllUsers());
        userTableView.setItems(userList);
    }

    private void handleDeleteUser(User user) {
        if (user != null) {
            boolean isDeleted = userService.deleteUser(user);
            if (isDeleted) {
                userList.remove(user);
                showAlert("Success", "User berhasil dihapus.");
            } else {
                showAlert("Error", "Gagal menghapus user.");
            }
        }
    }

    private void toggleUserType(User userToToggle) {
        if (userToToggle == null) {
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi Perubahan Tipe User");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Yakin untuk menukar tipe user " + userToToggle.getEmail() + "?");

        String cssPath = getClass().getResource("/style.css") != null ?
                         getClass().getResource("/style.css").toExternalForm() : null;
        if (cssPath != null) {
            confirmationAlert.getDialogPane().getStylesheets().add(cssPath);
            confirmationAlert.getDialogPane().getStyleClass().add("dialog-pane");
        }

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            User currentUser = UserService.getCurrentUser();

            if (currentUser != null && currentUser.getEmail().equals(userToToggle.getEmail()) && (userToToggle instanceof Admin)) {
                showAlert("Peringatan", "Anda tidak bisa mengubah tipe akun Anda sendiri di sini.");
                return;
            }

            UserStorageService.toggleUserType(userToToggle.getEmail());
            loadUserList();
        } else {
            System.out.println("Perubahan tipe user dibatalkan untuk " + userToToggle.getEmail());
        }
    }


    @FXML
    private void handleAddNewUserAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_user.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New User");
            dialogStage.setScene(new Scene(root, 600, 400));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(userTableView.getScene().getWindow());
            dialogStage.showAndWait();
            loadUserList();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open the registration form.");
        }
    }

    @FXML
    private void handleKembaliButtonAction(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) kembaliButton.getScene().getWindow();
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Tidak dapat kembali ke Dashboard.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");

        Label contentLabel = (Label) alert.getDialogPane().lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.getStyleClass().add("label");
        }

        Button okButton = (Button) alert.getDialogPane().lookupButton(javafx.scene.control.ButtonType.OK);
        if (okButton != null) {
            okButton.getStyleClass().add("button");
        }

        alert.showAndWait();
    }
}
