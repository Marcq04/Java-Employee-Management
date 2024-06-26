package com.example.assignment2gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Department implements Serializable {
    private String name;
    private double budget;
    private List<Employee> employees;

    public Department(String name, double budget) {
        this.name = name;
        this.budget = budget;
        this.employees = new ArrayList<>();
    }

    // Add an employee to the department
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    // Remove an employee from the department
    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }

    // Generate a report for the department
    public String generateReport() {
        StringBuilder report = new StringBuilder("Department: " + name + "\n");
        report.append("Budget: ").append(budget).append("\n");
        report.append("Employees:\n");
        for (Employee employee : employees) {
            report.append(employee).append("\n");
        }
        return report.toString();
    }

    public String generatePayrollReport() {
        StringBuilder report = new StringBuilder();
        double departmentTotalPayroll = 0;

        report.append("Department: ").append(this.getName()).append("\n");
        for (Employee employee : this.getEmployees()) {
            double employeePay = employee.getEmployeeSalary();

            departmentTotalPayroll += employeePay;
            report.append(employee.toString()).append(" - Pay: ").append(employeePay).append("\n");
        }

        report.append("Total Department Payroll: ").append(departmentTotalPayroll).append("\n");
        return report.toString();
    }


    // Getters and setters for name and budget
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBudget() {
        return budget;
    }

    // Getter method to retrieve the list of employees
    public List<Employee> getEmployees() {
        return new ArrayList<>(employees); // Return a copy of the list to prevent external modifications
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}
