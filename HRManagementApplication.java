package com.example.assignment2gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;

public class HRManagementApplication extends Application {
    static ArrayList<Employee> employeeList = new ArrayList<>();

    @Override
    public void start(Stage stage) throws IOException {
        // Load employee data from file
        load();

        FXMLLoader fxmlLoader = new FXMLLoader(HRManagementApplication.class.getResource("HRManagementDashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("HR Management System");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            // Save employee data to file when application is closed
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }

    // Save the current state of the employeeList to a file
    public static void save() throws IOException {
        FileOutputStream fos = new FileOutputStream("employee.data");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(employeeList);
        oos.flush();
        oos.close();
    }

    // Load the previously saved employeeList from a file
    public static void load() {
        try {
            FileInputStream fis = new FileInputStream("employee.data");
            ObjectInputStream ois = new ObjectInputStream(fis);
            employeeList = (ArrayList<Employee>) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
