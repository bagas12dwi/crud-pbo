/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package cruddatasiswa;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author bagas12dwi
 */
public class FXMLDocumentController implements Initializable {
    
    /**
     *
     */
    @FXML
    public TextField txtNama;
    @FXML
    public TextField txtNis;
    @FXML
    public TextField txtKelas;
    @FXML
    public TextField txtNo_hp;
    public TableView <Siswa_model> tableSiswa;
    public TableColumn <Siswa_model,String>colNama;
    public TableColumn <Siswa_model,String>colNis;
    public TableColumn <Siswa_model,String>colKelas;
    
    private Button btnSubmit;
    private Button btnReset;
    private Button btnUpdate;
    private Button btnHapus;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnSubmit) {
            insertSiswa();
        } else if (event.getSource() == btnUpdate) {
            updateSiswa();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        showSiswa();
        getValueFromTbl();
        txtNis.setEditable(true);
    }    
    
    
    public Connection getConn(){
        Connection conn;
        
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_siswa", "root","");
            return conn;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    public ObservableList<Siswa_model> getSiswa(){
        ObservableList<Siswa_model> siswaList = FXCollections.observableArrayList();
        Connection conn = getConn();
        String query = "SELECT * FROM siswa";
        Statement stm;
        ResultSet rs;
        
        try {
            stm = conn.createStatement();
            rs = stm.executeQuery(query);
            Siswa_model siswa;
            
            while(rs.next()){
                siswa = new Siswa_model(rs.getInt("id"), rs.getString("nis"), rs.getString("nama"), rs.getString("kelas"));
                siswaList.add(siswa);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return siswaList;
    }
    
    public void showSiswa(){
        ObservableList<Siswa_model> siswaList = getSiswa();
        colNama.setCellValueFactory(new PropertyValueFactory<Siswa_model, String>("nama"));
        colNis.setCellValueFactory(new PropertyValueFactory<Siswa_model, String>("nis"));
        colKelas.setCellValueFactory(new PropertyValueFactory<Siswa_model, String>("kelas"));
    
        tableSiswa.setItems(siswaList);
    }
    
    public void getValueFromTbl(){
        txtNis.setEditable(false);
        tableSiswa.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Siswa_model siswa = tableSiswa.getItems().get(tableSiswa.getSelectionModel().getSelectedIndex());
                txtNis.setText(siswa.getNis());
                txtNama.setText(siswa.getNama());
                txtKelas.setText(siswa.getKelas());
            }
        });
    }
    
    public void reset(){
        txtNis.setText("");
        txtNama.setText("");
        txtKelas.setText("");
        txtNis.setEditable(true);
    }
    
    public void insertSiswa(){
        String query = "INSERT INTO siswa (nis, nama, kelas) VALUES ('" + txtNis.getText() + "', '" + txtNama.getText() + "', '" + txtKelas.getText() + "')";
        executeQuery(query);
        showSiswa();
        reset();
    }
    
    public void updateSiswa(){
        String query = "UPDATE siswa SET " + 
                "nama = '" + txtNama.getText() + "', " + 
                "kelas = '" + txtKelas.getText() + "' "
                + "WHERE nis = '" + txtNis.getText() + "' ";
        executeQuery(query);
        showSiswa();
    }
    
    public void hapusSiswa() {
        String query = "DELETE FROM siswa WHERE nis = '" + txtNis.getText() + "' ";
        executeQuery(query);
        showSiswa();
        reset();
    }
    
    private void executeQuery(String query){
        Connection conn = getConn();
        Statement stm;
        try {
            stm = conn.createStatement();
            stm.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
