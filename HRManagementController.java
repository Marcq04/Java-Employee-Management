package com.example.assignment2gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

public class HRManagementController {


    @FXML
    private ListView<Employee> employeeListView;


    @FXML
    private TextField nameField;


    @FXML
    private TextField salaryField;


    @FXML
    private TextField hoursWorkedField;


    @FXML
    private TextField overtimeField;


    @FXML
    private TextField bonusField;


    @FXML
    private TextArea reportDisplayArea;

    @FXML
    private ComboBox<Department> departmentComboBox; // ComboBox for selecting departments

    private ObservableList<Department> departmentObservableList = FXCollections.observableArrayList();
    private Map<Integer, PayrollData> payrollDataMap = new HashMap<>();
    private static  String DATA_FILE = "employees.dat";

    @FXML
    public void initialize() {
        loadData();
        setupDepartments(); // initialize departments
        updateDepartmentComboBox();
        employeeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameField.setText(newValue.getEmployeeName());
                salaryField.setText(String.valueOf(newValue.getEmployeeSalary()));
                PayrollData payrollData = payrollDataMap.get(newValue.getEmployeeID());
                if (payrollData != null) {
                    hoursWorkedField.setText(String.valueOf(payrollData.hoursWorked));
                    overtimeField.setText(String.valueOf(payrollData.overtimeHours));
                    bonusField.setText(String.valueOf(payrollData.bonus));
                    departmentComboBox.setItems(departmentObservableList);
                }
            }
        });
        updateEmployeeListView();
    }

    // Call this method when you need to display departments in the ComboBox
    private void updateDepartmentComboBox() {
        departmentComboBox.setItems(departmentObservableList);
        departmentComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Department department) {
                return department == null ? null : department.getName();
            }

            @Override
            public Department fromString(String string) {
                return departmentObservableList.stream()
                        .filter(dept -> dept.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }
    // Method to handle adding a new employee to the system
    @FXML
    public void addEmployee() {
        // Trim to remove any leading or trailing whitespace from the name and salary inputs.
        String name = nameField.getText().trim();
        String salaryStr = salaryField.getText().trim();

        // Check if the name field is empty and alert the user if so
        if (name.isEmpty()) {
            showAlert("Validation Error", "Employee name cannot be empty.");
            return;
        }

        // Attempt to parse the salary as a double and add the new employee to the list
        try {
            double salary = Double.parseDouble(salaryStr);
            // Ensure salary is not negative alert the user if this  fails
            if (salary < 0) {
                showAlert("Validation Error", "Salary cannot be negative.");
                return;
            }

            // Create a new Employee object and add it to the employee list
            Employee newEmployee = new Employee(name, salary);
            HRManagementApplication.employeeList.add(newEmployee);
            // Update the ListView UI component and save the updated list to file
            updateEmployeeListView();
            saveData();
        } catch (NumberFormatException e) {
            // Alert the user if the salary input is not a valid numeric value
            showAlert("Input Error", "Invalid salary input. Please enter a numeric value.");
        }
    }

    // Method to delete the selected employee from the system
    @FXML
    private void deleteEmployee() {
        // Get the currently selected employee from the ListView
        Employee selected = employeeListView.getSelectionModel().getSelectedItem();
        // If an employee is selected remove them from the list and update the UI and save the data
        if (selected != null) {
            HRManagementApplication.employeeList.remove(selected);
            updateEmployeeListView();
            saveData();
        } else {
            // If no employee is selected alert the user
            showAlert("Error", "No employee selected. Please select an employee to delete.");
        }
    }

    // Method to update the details of an existing employee
    @FXML
    private void updateEmployee() {
        // Get the currently selected employee from the ListView
        Employee selected = employeeListView.getSelectionModel().getSelectedItem();
        // If an employee is selected proceed with updating their details
        if (selected != null) {
            String name = nameField.getText().trim();
            String salaryStr = salaryField.getText().trim();

            // Check for empty name input and alert the user if necessary
            if (name.isEmpty()) {
                showAlert("Validation Error", "Employee name cannot be empty.");
                return;
            }

            // Attempt to parse the updated salary and apply the changes to the employee object
            try {
                double salary = Double.parseDouble(salaryStr);
                // Ensure the salary is not negative also alert the user if this validation fails
                if (salary < 0) {
                    showAlert("Validation Error", "Salary cannot be negative.");
                    return;
                }

                // Update the employee's name and salary then refresh the ListView and save the data
                selected.setEmployeeName(name);
                selected.setEmployeeSalary(salary);
                updateEmployeeListView();
                saveData();
            } catch (NumberFormatException e) {
                // Alert the user if the salary input is not a valid numeric value.
                showAlert("Input Error", "Invalid salary input. Please enter a numeric value.");
            }
        } else {
            // If no employee is selected, alert the user to select one for updating.
            showAlert("Error", "No employee selected. Please select an employee to update.");
        }
    }

    private void setupDepartments() {
        // departments created (4 of them)
        departmentObservableList.addAll(
                new Department("Human Resources", 50000),
                new Department("Finance", 75000),
                new Department("IT", 60000),
                new Department("Sales", 30000)
        );

        // Select the first department by default
        departmentComboBox.setItems(departmentObservableList);
    }

    // Assign Employee to specific department
    @FXML
    private void assignToDepartment() {
        Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();
        Department selectedDepartment = departmentComboBox.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null && selectedDepartment != null) {
            selectedDepartment.addEmployee(selectedEmployee);
            updateReportArea();
        }
    }

    // To Remove from department
    @FXML
    private void removeFromDepartment() {
        Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();
        Department selectedDepartment = departmentComboBox.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null && selectedDepartment != null) {
            selectedDepartment.removeEmployee(selectedEmployee);
            updateReportArea();
        }
    }

    // To view departments
    @FXML
    private void viewDepartments() {
        StringBuilder report = new StringBuilder();
        for (Department department : departmentObservableList) {
            report.append("Department: ").append(department.getName()).append("\n");
            for (Employee employee : department.getEmployees()) {
                report.append("\t").append(employee.toString()).append("\n");
            }
            report.append("\n"); // newline for spacing between departments
        }
        reportDisplayArea.setText(report.toString());
    }

    @FXML
    private void generateDepartmentReport() {
        Department selectedDepartment = departmentComboBox.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            String report = selectedDepartment.generatePayrollReport();
            reportDisplayArea.setText(report);
        } else {
            showAlert("Error", "No department selected. Please select a department to generate the report.");
        }
    }


    // Updates the report text area with department information
    private void updateReportArea() {
        Department selectedDepartment = departmentComboBox.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            reportDisplayArea.setText(selectedDepartment.generateReport());
        }
    }

    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(new ArrayList<>(HRManagementApplication.employeeList));
            out.writeObject(new ArrayList<>(departmentObservableList)); // Convert ObservableList to ArrayList for serialization
        } catch (IOException e) {
            showAlert("Save Error", "Could not save data: " + e.getMessage());
        }
    }


    private void loadData() {
        File dataFile = new File(DATA_FILE);
        if (!dataFile.exists() || dataFile.length() == 0) {
            showAlert("Load Warning", "Data file is empty or does not exist. Starting with an empty dataset.");
            return; // Exit the method as there is nothing to load
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile))) {
            List<Employee> loadedEmployeeList = (List<Employee>) in.readObject();
            if (loadedEmployeeList != null) {
                HRManagementApplication.employeeList.clear();
                HRManagementApplication.employeeList.addAll(loadedEmployeeList);
            }

            // Attempt to load department data if available
            if (in.available() > 0) { // Check if more data is available in the stream
                List<Department> loadedDepartmentList = (List<Department>) in.readObject();
                if (loadedDepartmentList != null) {
                    departmentObservableList.clear();
                    departmentObservableList.addAll(loadedDepartmentList); // Convert List to ObservableList
                    updateDepartmentComboBox();
                }
            }
        } catch (FileNotFoundException e) {
            showAlert("Load Error", "Data file not found: " + e.getMessage());
        } catch (EOFException e) {
            showAlert("Load Error", "Data file is incomplete or corrupted: " + e.getMessage());
        } catch (IOException e) {
            showAlert("Load Error", "Could not read data file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            showAlert("Load Error", "Class not found during deserialization: " + e.getMessage());
        }
    }

    // Displays an alert dialog with a custom title and message.
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Updates the ListView UI component to display all employees.
    private void updateEmployeeListView() {
        employeeListView.getItems().clear();
        employeeListView.getItems().addAll(HRManagementApplication.employeeList); // Add all employees from the application's list.
    }

    // Generates and displays a payroll report in a text area.
    @FXML
    private void generateAndDisplayReport() {
        StringBuilder report = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.00");

        // Append the headers for the report columns.
        report.append("ID\tName\tHours\tOvertime\tBonus\tGross\tTax\tNet\n");
        // go through each employee to generate their payroll report line.
        for (Employee employee : HRManagementApplication.employeeList) {
            PayrollData payrollData = payrollDataMap.get(employee.getEmployeeID()); // Retrieve payroll data for each employee.
            if (payrollData != null) { // Check if payroll data exists
                // Append every of payroll
                report.append(employee.getEmployeeID()).append("\t")
                        .append(employee.getEmployeeName()).append("\t")
                        .append(df.format(payrollData.hoursWorked)).append("\t")
                        .append(df.format(payrollData.overtimeHours)).append("\t")
                        .append(df.format(payrollData.bonus)).append("\t")
                        .append(df.format(payrollData.grossPay)).append("\t")
                        .append(df.format(payrollData.tax)).append("\t")
                        .append(df.format(payrollData.netPay)).append("\n");
            }
        }

        reportDisplayArea.setText(report.toString()); // Display the full report in the report area
    }


    // uses double on each payrolls section
    private static class PayrollData implements Serializable {
        double hoursWorked;
        double overtimeHours;
        double bonus;
        double grossPay;
        double tax;
        double netPay;
    }


    // Method to calculate payroll with input validation
    @FXML
    private void calculatePayroll() {
        Employee selected = employeeListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                double hoursWorked = parseAndValidateInput(hoursWorkedField.getText(), "Hours Worked");
                double overtimeHours = parseAndValidateInput(overtimeField.getText(), "Overtime Hours");
                double bonus = parseAndValidateInput(bonusField.getText(), "Bonus");


                PayrollData payrollData = new PayrollData();
                payrollData.hoursWorked = hoursWorked;
                payrollData.overtimeHours = overtimeHours;
                payrollData.bonus = bonus;
                payrollData.grossPay = calculatePayrollAmount(hoursWorked, overtimeHours, bonus);
                payrollData.tax = payrollData.grossPay * 0.20; // Sample tax rate
                payrollData.netPay = payrollData.grossPay - payrollData.tax;


                payrollDataMap.put(selected.getEmployeeID(), payrollData);


                showAlert("Payroll Calculated", "The payroll amount for " + selected.getEmployeeName() + " is: " + payrollData.netPay);
            } catch (IllegalArgumentException e) {
                showAlert("Input Error", e.getMessage());
            }
        } else {
            showAlert("Error", "No employee selected. Please select an employee to calculate the payroll.");
        }
    }


    // Method to parse and validate input fields for payroll calculation
    private double parseAndValidateInput(String input, String fieldName) throws IllegalArgumentException {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }


        try {
            double value = Double.parseDouble(input.trim());
            if (value < 0) {
                throw new IllegalArgumentException(fieldName + " cannot be negative.");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input for " + fieldName + ". Please enter a numeric value.");
        }
    }


    // Method to calculate the payroll amount
    private double calculatePayrollAmount(double hoursWorked, double overtimeHours, double bonus) {
        // Assume hourly rate is static for this example; in a real scenario, it would come from the employee or another source.
        double hourlyRate = 20.0; // Replace with actual logic to get hourly rate
        double overtimeRate = hourlyRate * 1.5; // Example overtime rate
        double basicPay = hoursWorked * hourlyRate;
        double overtimePay = overtimeHours * overtimeRate;
        double grossPay = basicPay + overtimePay + bonus;


        //tax (i am using .20)
        double tax = grossPay * 0.20;
        double netPay = grossPay - tax;


        return netPay; // Returning net pay after tax for this example
    }
}


