package com.example.assignment2gui;


import java.io.Serializable;
// Name: Marcus Quitiquit, Jacques Vidjanagni, Kevin Bhangu
// ID: 101448926, 100989148, 101418717
// This class represents an Employee and contains information such as ID, name, and salary.
public class Employee implements Serializable {
    // Static variable to generate unique IDs for each employee.
    static private int idGenerator;


    // Instance variables to store employee-specific information.
    private int employeeID;
    private String employeeName;
    private double employeeSalary;


    // Setter method to set the ID generator. This is a static method as it operates on the class level.
    public static void setIdGenerator(int idGenerator) {
        Employee.idGenerator = idGenerator;
    }


    // Constructor to create an Employee object with a given name and salary.
    public Employee(String employeeName, double employeeSalary) {
        // Automatically assigns a unique ID to the employee using the static ID generator.
        this.employeeID = ++idGenerator;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
    }


    // Getter method to retrieve the employee ID.
    public int getEmployeeID() {
        return employeeID;
    }


    // Setter method to manually set the employee ID.
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }


    // Getter method to retrieve the employee name.
    public String getEmployeeName() {
        return employeeName;
    }


    // Setter method to set the employee name.
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }


    // Getter method to retrieve the employee salary.
    public double getEmployeeSalary() {
        return employeeSalary;
    }


    // Setter method to set the employee salary.
    public void setEmployeeSalary(double employeeSalary) {
        this.employeeSalary = employeeSalary;
    }


    // Overridden toString() method to provide a meaningful string representation of the Employee object.
    @Override
    public String toString() {
        return "Employee{" +
                "employeeID=" + employeeID +
                ", employeeName='" + employeeName + '\'' +
                ", employeeSalary=" + employeeSalary +
                '}';
    }
}
