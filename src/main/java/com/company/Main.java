package com.company;

import java.sql.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static  String db_url ="jdbc:postgresql://localhost:5432/university";
    public static String  db_user ="postgres";
    public static String db_pass ="freeman1997";
    private static void FindHeadOfDepartment(String str){

        try {
            Connection con = DriverManager.getConnection(db_url,
                    db_user, db_pass);

            PreparedStatement stmt = con.prepareStatement("SELECT departments.name, departments.head_id, lectors.name FROM lectors, departments WHERE departments.head_id = lectors.id AND departments.name = ?");
            stmt.setString(1, str);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("Head of " + str + " department is " + rs.getString(3));
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void ShowDepartmentStatistic(String str){
        List<String> degreeList = new ArrayList<String>();
        try {
            Connection con = DriverManager.getConnection(db_url,
                    db_user, db_pass);
            PreparedStatement stmt = con.prepareStatement("SELECT departments.name, departments.id, lectors.id, lectors.degree, lectors_departments.department_id, lectors_departments.lector_id " +
                    "FROM lectors, departments, lectors_departments " +
                    "WHERE departments.id = lectors_departments.department_id " +
                    "AND lectors.id = lectors_departments.lector_id " +
                    "AND departments.name = ?");
            stmt.setString(1, str);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                degreeList.add(rs.getString(4));
            }

            int countAssistant = 0;
            int countAssociateProfessor = 0;
            int countProfessor = 0;

            for (String s : degreeList) {

             switch (s){
                 case "assistant":
                     countAssistant++;
                     break;
                 case "associated professor":
                     countAssociateProfessor++;
                     break;
                 case "professor":
                     countProfessor++;
                     break;
             }
            }
            System.out.println("assistans - " + countAssistant);
            System.out.println("associated professors - " + countAssociateProfessor);
            System.out.println("professors - " + countProfessor);

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void AverageSalary(String str){
        try {
            Connection con = DriverManager.getConnection(db_url,
                    db_user, db_pass);

            PreparedStatement stmt = con.prepareStatement("SELECT departments.name, departments.id, lectors.id, lectors.salary, lectors_departments.department_id, lectors_departments.lector_id " +
                    "FROM lectors, departments, lectors_departments " +
                    "WHERE departments.id = lectors_departments.department_id " +
                    "AND departments.name = ? " +
                    "AND lectors.id = lectors_departments.lector_id");
            stmt.setString(1, str);
            ResultSet rs = stmt.executeQuery();

            int sum = 0;
            int counter = 0;
            while (rs.next()) {
                sum += rs.getInt(4);
                counter++;
            }
            System.out.println("The avarage salary of " + str + " department is "+ sum/counter);

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void CountEmployee(String str){
        try {
            Connection con = DriverManager.getConnection(db_url,
                    db_user, db_pass);

            PreparedStatement stmt = con.prepareStatement("SELECT departments.name, departments.id, lectors_departments.department_id " +
                    "FROM lectors_departments, departments " +
                    "WHERE departments.id = lectors_departments.department_id " +
                    "AND departments.name = ?");
            stmt.setString(1, str);
            ResultSet rs = stmt.executeQuery();

            int count = 0;
            while (rs.next()) {
            count++;
            }
            System.out.println("There are " + count +" employees");

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void SearchByTemplate(String str){
        try {
            Connection con = DriverManager.getConnection(db_url,
                    db_user, db_pass);

            PreparedStatement stmt = con.prepareStatement("SELECT lectors.name FROM lectors WHERE lectors.name LIKE '%"  + str + "%'");
//            stmt.setString(1, str);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString(1));
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args){

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner inAction = new Scanner(System.in);
        Scanner in = new Scanner(System.in);
        Integer action;
        String str;

        do {
            System.out.println("1 - Who is head of department");
            System.out.println("2 - Show department statistic");
            System.out.println("3 - Show the average salary for department");
            System.out.println("4 - Show count of employee for department");
            System.out.println("5 - Global search by template");
            System.out.println("6 - Exit");

            System.out.println("Enter the number of action: ");
            action = inAction.nextInt();

            switch (action) {
                case 1:
                    System.out.println("Enter name of department:");
                    str = in.nextLine();
                    FindHeadOfDepartment(str);
                    break;
                case 2:
                    System.out.println("Enter name of department:");
                    str = in.nextLine();
                    ShowDepartmentStatistic(str);
                    break;
                case 3:
                    System.out.println("Enter name of department:");
                    str = in.nextLine();
                    AverageSalary(str);
                    break;
                case 4:
                    System.out.println("Enter name of department:");
                    str = in.nextLine();
                    CountEmployee(str);
                    break;
                case 5:
                    System.out.println("Enter the template: ");
                    str = in.nextLine();
                    SearchByTemplate(str);
                    break;
                case 6:
                    return;
            }
        } while (action != 6);

        in.close();
        inAction.close();

        }
    }



