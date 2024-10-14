import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView adminImage;

    @FXML
    private Button logOutButton;

    @FXML
    private Label adminNameText;

    @FXML
    private Button bookButton;

    @FXML
    private Button userButton;

    @FXML
    private AnchorPane userPane;

    @FXML
    private Button userSearchButton;

    @FXML
    private TextField userSearchBar;

    @FXML
    private TableView<user> userTable;

    @FXML
    private TableColumn<user, Integer> userIDColumn;

    @FXML
    private TableColumn<user, String> userNameColumn;

    @FXML
    private TableColumn<user, String> userEmailColumn;

    @FXML
    private TextField userIDText;

    @FXML
    private TextField userNameText;

    @FXML
    private TextField userEmailText;

    @FXML
    private Button userDeleteButton;

    @FXML
    private Button userAddButton;

    @FXML
    private Button userUpdateBtton;

    @FXML
    private ComboBox<String> userSelectRole2;

    @FXML
    private ComboBox<String> userSelectRole1;

    @FXML
    private AnchorPane bookPane;

    @FXML
    private Button bookSearchButton;

    @FXML
    private TextField bookSearchBar;

    @FXML
    private TableView<?> bookTable;

    @FXML
    private TableColumn<?, ?> isbnColumn;

    @FXML
    private TableColumn<?, ?> titleColumn;

    @FXML
    private TableColumn<?, ?> authorColumn;

    @FXML
    private TableColumn<?, ?> publishDateColumn;

    @FXML
    private TextField isbnText;

    @FXML
    private TextField titleText;

    @FXML
    private TextField authorText;

    @FXML
    private Button bookDeleteButton;

    @FXML
    private Button bookAddButton;

    @FXML
    private Button bookUpdateBotton;

    @FXML
    private TextField publishDateText;

    @FXML
    private TableColumn<?, ?> userPasswordColumn;

    @FXML
    private MenuItem userSelectionUser1;

    @FXML
    private MenuItem userSelectionStaff1;

    @FXML
    private MenuItem userSelectionUser2;

    @FXML
    private MenuItem userSelectionStaff2;

    private Connection connection;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;

    public ObservableList<user> userListData() {
        ObservableList<user> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users";

        connection = DatabaseConnection.getConnection();

        try{
            prepare = connection.prepareStatement(sql);
            result = prepare.executeQuery();
            user user1;

            while(result.next()) {
                user1 = new user(result.getString("User ID")
                        , result.getString("User name")
                        , result.getString("Email")
                        , result.getString("Password"));
                listData.add(user1);
            }

        }catch (Exception e){e.printStackTrace();}
        return listData;
    }

    public void DisplayAdminName() {
        adminNameText.setText(user.getName());
    }

    private ObservableList<user> userList;
    public void UserShowListData(){
        userList = userListData();

        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        userTable.setItems(userList);
    }

    public void userSelect() {
        user user1 = userTable.getSelectionModel().getSelectedItem();
        int num = userTable.getSelectionModel().getSelectedIndex();

        if((num - 1) < -1){return;}

        userIDColumn.setText(String.valueOf(user1.getID()));
        userNameColumn.setText(user.getName());
        userEmailColumn.setText(user1.getEmail());
        userPasswordColumn.setText(user1.getPassword());
    }

    public void userAdd() {

        String sql = "INSERT INTO user "
                + "(userID,userName,email,password) "
                + "VALUES(?,?,?,?)";

        connection = DatabaseConnection.getConnection();

        try{
            Alert alert;
            if(userIDText.getText().isEmpty() || userNameText.getText().isEmpty() || userEmailText.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error meassage");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blank fields");
                alert.showAndWait();
            }

            else {
                String check = "SELECT userID FROM users WHERE userID = '"
                        + userIDText.getText() + "'";
                statement = connection.createStatement();
                result = statement.executeQuery(check);

                if(result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error meassage");
                    alert.setHeaderText(null);
                    alert.setContentText("User ID already exist");
                    alert.showAndWait();
                }

                prepare = connection.prepareStatement(sql);
                prepare.setString(1, userIDText.getText());
                prepare.setString(2, userNameText.getText());
                prepare.setString(3, userEmailText.getText());

                String uri = getData.path;
                uri = uri.replace("\\", "\\\\");

                prepare.setString(4, uri);
                prepare.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information meassage");
                alert.setHeaderText(null);
                alert.setContentText("Successfully added");
                alert.showAndWait();

                UserShowListData();
            }

        }catch(Exception e){e.printStackTrace();}
    }

    public void userUpdateUser() {
        String sql = "UPDATE users SET ID = '"
                + userIDText.getText() + "' , name = '"
                + userNameText.getText() + "' , email = '"
                + userPasswordColumn.getText() + "'";

        connection = DatabaseConnection.getConnection();

        try {
            Alert alert;
            if(userIDText.getText().isEmpty() || userNameText.getText().isEmpty() || userEmailText.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error meassage");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blank fields");
                alert.showAndWait();
            }
            else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation meassage");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure want to UPDATE user information");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)) {
                    statement = connection.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information meassage");
                    alert.setHeaderText(null);
                    alert.setContentText("Sucessfully updated");
                    alert.showAndWait();


                }
            }
        } catch (Exception e) {throw new RuntimeException(e);}
    }

    public void userDelete() {
        String sql = "DELETE FROM users WHERE ID = '"
                + userIDText.getText() + "'";
        connection = DatabaseConnection.getConnection();

        try {
            Alert alert;
            if(userIDText.getText().isEmpty() || userNameText.getText().isEmpty() || userEmailText.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error meassage");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blank fields");
                alert.showAndWait();
            }
            else {

            }

        } catch (Exception e) {throw new RuntimeException(e);}
    }

    private String[] listRole = {"Staff", "User"};
    public void userRoleList() {
        List<String> listR = new ArrayList<>();

        for(String data : listRole) {
            listR.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listR);
        userSelectRole2.setItems(listData);
        userSelectRole1.setItems(listData);
    }

    public void SwitchForm(ActionEvent event) throws IOException {
        if(event.getSource() == bookButton) {
            bookPane.setVisible(true);
            userPane.setVisible(false);
        }

        if(event.getSource() == userButton) {
            bookPane.setVisible(false);
            userPane.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        UserShowListData();
        userRoleList();
    }
}
