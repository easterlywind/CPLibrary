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
import java.util.ResourceBundle;

public class StaffController implements Initializable{
    @FXML
    private ImageView adminImage;

    @FXML
    private Button logOutButton;

    @FXML
    private Label staffNameText;

    @FXML
    private Button bookButton;

    @FXML
    private Button userButton;

    @FXML
    private Button logOutButton1;

    @FXML
    private AnchorPane userPane;

    @FXML
    private Button userSearchButton;

    @FXML
    private TextField userSearchBar;

    @FXML
    private TableView<user> userTable;

    @FXML
    private TableColumn<?, ?> userIDColumn;

    @FXML
    private TableColumn<?, ?> userNameColumn;

    @FXML
    private TableColumn<?, ?> userEmailColumn;

    @FXML
    private TableColumn<?, ?> userPasswordColumn;

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
    private TextField userIDText1;

    @FXML
    private Button userDeleteButton1;

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
        staffNameText.setText(user.getName());
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

    public void userUpdate() {
        String sql = "UPDATE users SET ID = '"
                + userIDText.getText() + "' , name = '"
                + userNameText.getText() + "' , email = '"
                + userPasswordColumn.getText() + "'";

        connection = DatabaseConnection.getConnection();
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


    }
}

