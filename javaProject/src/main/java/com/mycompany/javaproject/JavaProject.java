/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.javaproject;

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Course {
    String courseName;
    String courseCode;
    ArrayList<String> enrolledStudents = new ArrayList<>();

    public Course(String name, String code) {
        this.courseName = name;
        this.courseCode = code;
    }

    public void enrollStudent(String studentName) {
        enrolledStudents.add(studentName);
    }

    public String getDay() {
        return null;
    }

    public String toString() {
        return courseName + " (" + courseCode + ")";
    }
}

class LabCourse extends Course {
    private String requirements;
    private int labDuration;

    public LabCourse(String name, String code, int duration, String requirements) {
        super(name, code);
        this.labDuration = duration;
        this.requirements = requirements;
    }

    public void addRequirements(String req) {
        this.requirements = req;
    }

    public String toString() {
        return super.toString() + " [Lab, Duration: " + labDuration + "h, Req: " + requirements + "]";
    }
}

class OnlineCourse extends Course {
    private String courseTime;
    private String day;

    public OnlineCourse(String name, String code, String courseTime, String day) {
        super(name, code);
        this.courseTime = courseTime;
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setScheduleDate(String day) {
        this.day = day;
    }

    public String toString() {
        return super.toString() + " [Online, Time: " + courseTime + ", Day: " + day + "]";
    }
}

class InPersonCourse extends Course {
    private String room;
    private String schedule;

    public InPersonCourse(String name, String code, String room, String schedule) {
        super(name, code);
        this.room = room;
        this.schedule = schedule;
    }

    public String getDay() {
        return schedule;
    }

    public String toString() {
        return super.toString() + " [Regular, Room: " + room + ", Schedule: " + schedule + "]";
    }
}

class Person {
    private String name;
    private int id;

    public Person(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (String.valueOf(id).length() == 6) {
            this.id = id;
        } else {
            System.out.println("Id must be 6 digits");
        }
    }

    public String toString() {
        return ("The name is: " + name + "  The Id is: " + id);
    }
}

class Student extends Person {
    private String major;
    private ArrayList<Double> grades;

    public Student(String name, int id, String major) {
        super(name, id);
        this.major = major;
        this.grades = new ArrayList<>();
    }

    public void addGrade(double grade) {
        grades.add(grade);
    }

    public double getGPA() {
        if (grades.isEmpty()) return 0.0;
        double sum = 0;
        for (double grade : grades) {
            sum += grade;
        }
        return sum / grades.size();
    }

    public void enrollInCourse(String courseCode, ArrayList<Course> availableCourses) {
        for (Course c : availableCourses) {
            if (c.courseCode.equals(courseCode)) {
                c.enrollStudent(getName());
                System.out.println(getName() + " enrolled in " + c.courseName);
                return;
            }
        }
        System.out.println("Course " + courseCode + " not found. Enrollment failed.");
    }

    public void saveStudentInfo() {
        try (FileWriter writer = new FileWriter("StudentInfo.txt", true)) {
            writer.write("Name: " + getName() + ", ID: " + getId() + ", GPA: " + String.format("%.2f", getGPA()) + "\n");
            System.out.println("Student info saved to StudentInfo.txt");
        } catch (IOException e) {
            System.out.println("Error saving student info: " + e.getMessage());
        }
    }

    public String toString() {
        return super.toString() + " Major: " + major + " GPA: " + getGPA();
    }
}

class Professor extends Person {
    private String department;
    private ArrayList<String> courses;

    public Professor(String name, int id, String department) {
        super(name, id);
        this.department = department;
        this.courses = new ArrayList<>();
    }

    public void assignCourse(String course) {
        courses.add(course);
    }

    public void addCourseToSystem(Course course, ArrayList<Course> courseList) {
        courseList.add(course);
        assignCourse(course.courseCode);
        System.out.println("Course added: " + course);
    }

    public String toString() {
        return super.toString() + " Department: " + department + " Courses: " + courses;
    }
}

public class JavaProject {
    private static ArrayList<Course> courseList = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JavaProject::createAndShowGUI);
        preloadData();
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("University Course Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));

        JButton listCoursesButton = new JButton("List All Courses");
        JButton enrollButton = new JButton("Enroll Student in Course");
        JButton addCourseButton = new JButton("Add New Course");
        JButton saveScheduleButton = new JButton("Save Schedule to File");
        JButton exitButton = new JButton("Exit");

        panel.add(listCoursesButton);
        panel.add(enrollButton);
        panel.add(addCourseButton);
        panel.add(saveScheduleButton);
        panel.add(exitButton);

        frame.add(panel);

        // Button actions
        listCoursesButton.addActionListener(e -> listCourses());
        enrollButton.addActionListener(e -> enrollStudent());
        addCourseButton.addActionListener(e -> addNewCourse());
        saveScheduleButton.addActionListener(e -> generateSchedule(courseList));
        exitButton.addActionListener(e -> System.exit(0));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void preloadData() {
        LabCourse lab = new LabCourse("Java Lab", "CS112", 3, "Laptop");
        courseList.add(lab);
        lab.enrollStudent("Lama");
        lab.enrollStudent("Zayneb");

        InPersonCourse regular = new InPersonCourse("Software Engineering", "SE301", "Room 204", "Wed");
        courseList.add(regular);
        regular.enrollStudent("Sara");

        OnlineCourse online = new OnlineCourse("Introduction to Cyber Security", "FC112", "11:00 AM", "Monday");
        courseList.add(online);
        online.enrollStudent("Zayneb");

        Professor p1 = new Professor("Dr. Khalid", 654321, "Computer Science");
        p1.addCourseToSystem(new OnlineCourse("Data Ethics", "CS300", "2:00 PM", "Tuesday"), courseList);
    }

    public static void listCourses() {
        StringBuilder sb = new StringBuilder();
        for (Course course : courseList) {
            sb.append(course).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Courses", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void enrollStudent() {
        String studentName = JOptionPane.showInputDialog("Enter student name:");
        String courseCode = JOptionPane.showInputDialog("Enter course code to enroll:");

        boolean found = false;
        for (Course c : courseList) {
            if (c.courseCode.equalsIgnoreCase(courseCode)) {
                c.enrollStudent(studentName);
                JOptionPane.showMessageDialog(null, studentName + " enrolled in " + c.courseName);
                found = true;
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void addNewCourse() {
        String name = JOptionPane.showInputDialog("Course Name:");
        String code = JOptionPane.showInputDialog("Course Code:");
        String type = JOptionPane.showInputDialog("Type (Lab/Online/InPerson):");

        Course newCourse = null;
        if (type.equalsIgnoreCase("Lab")) {
            String durationStr = JOptionPane.showInputDialog("Lab Duration (hours):");
            String requirements = JOptionPane.showInputDialog("Requirements:");
            newCourse = new LabCourse(name, code, Integer.parseInt(durationStr), requirements);
        } else if (type.equalsIgnoreCase("Online")) {
            String time = JOptionPane.showInputDialog("Course Time:");
            String day = JOptionPane.showInputDialog("Day:");
            newCourse = new OnlineCourse(name, code, time, day);
        } else if (type.equalsIgnoreCase("InPerson")) {
            String room = JOptionPane.showInputDialog("Room:");
            String schedule = JOptionPane.showInputDialog("Schedule:");
            newCourse = new InPersonCourse(name, code, room, schedule);
        }

        if (newCourse != null) {
            courseList.add(newCourse);
            JOptionPane.showMessageDialog(null, "Course added successfully!");
        }
    }

    public static void generateSchedule(ArrayList<Course> courseList) {
        Map<String, java.util.List<String>> schedule = new HashMap<>();

        for (Course course : courseList) {
            String day = course.getDay();
            if (day != null) {
                schedule.putIfAbsent(day, new ArrayList<>());
                schedule.get(day).add(course.toString());
            }
        }

        try (FileWriter writer = new FileWriter("CourseSchedule.txt")) {
            for (String day : schedule.keySet()) {
                writer.write(day + ":\n");
                for (String c : schedule.get(day)) {
                    writer.write("  - " + c + "\n");
                }
            }
            JOptionPane.showMessageDialog(null, "Schedule saved to CourseSchedule.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing schedule: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
