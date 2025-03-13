import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class StudentManagementGUI extends JFrame {
    private DataStorage dataStorage;
    private JTextArea outputArea;
    private JComboBox<String> studentComboBox;
    private JComboBox<String> courseComboBox;

    public StudentManagementGUI() {
        dataStorage = new DataStorage();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Output Area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addStudentButton = new JButton("Add Student");
        JButton updateStudentButton = new JButton("Update Student");
        JButton viewStudentsButton = new JButton("View Students");
        JButton enrollStudentButton = new JButton("Enroll Student");
        JButton assignGradeButton = new JButton("Assign Grade");

        buttonPanel.add(addStudentButton);
        buttonPanel.add(updateStudentButton);
        buttonPanel.add(viewStudentsButton);
        buttonPanel.add(enrollStudentButton);
        buttonPanel.add(assignGradeButton);

        add(buttonPanel, BorderLayout.NORTH);

        // Dropdowns for Students and Courses
        studentComboBox = new JComboBox<>();
        courseComboBox = new JComboBox<>();
        JPanel dropdownPanel = new JPanel();
        dropdownPanel.add(new JLabel("Select Student:"));
        dropdownPanel.add(studentComboBox);
        dropdownPanel.add(new JLabel("Select Course:"));
        dropdownPanel.add(courseComboBox);
        add(dropdownPanel, BorderLayout.SOUTH);

        // Event Handlers
        addStudentButton.addActionListener(e -> addStudent());
        updateStudentButton.addActionListener(e -> updateStudent());
        viewStudentsButton.addActionListener(e -> viewStudents());
        enrollStudentButton.addActionListener(e -> enrollStudent());
        assignGradeButton.addActionListener(e -> assignGrade());

        // Populate dropdowns
        refreshDropdowns();
    }

    private void addStudent() {
        String studentId = JOptionPane.showInputDialog(this, "Enter Student ID:");
        String name = JOptionPane.showInputDialog(this, "Enter Student Name:");
        if (studentId != null && name != null) {
            dataStorage.addStudent(new Student(studentId, name));
            outputArea.append("Student Added: " + studentId + " - " + name + "\n");
            refreshDropdowns();
        }
    }

    private void updateStudent() {
        String studentId = JOptionPane.showInputDialog(this, "Enter Student ID to Update:");
        if (studentId != null) {
            Student student = findStudentById(studentId);
            if (student != null) {
                String newName = JOptionPane.showInputDialog(this, "Enter New Name:");
                if (newName != null) {
                    student.setName(newName);
                    outputArea.append("Student Updated: " + studentId + " - " + newName + "\n");
                    refreshDropdowns();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewStudents() {
        outputArea.setText(""); // Clear previous output
        for (Student student : dataStorage.getStudents()) {
            outputArea.append(student.toString() + "\n");
            for (Map.Entry<Course, String> entry : student.getEnrolledCourses().entrySet()) {
                outputArea.append("  Enrolled in: " + entry.getKey() + ", Grade: " + entry.getValue() + "\n");
            }
        }
    }

    private void enrollStudent() {
        String studentId = (String) studentComboBox.getSelectedItem();
        String courseId = (String) courseComboBox.getSelectedItem();
        if (studentId != null && courseId != null) {
            Student student = findStudentById(studentId);
            Course course = findCourseById(courseId);
            if (student != null && course != null) {
                student.enrollCourse(course);
                outputArea.append("Student " + studentId + " enrolled in " + courseId + "\n");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Student ID or Course ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void assignGrade() {
        String studentId = (String) studentComboBox.getSelectedItem();
        String courseId = (String) courseComboBox.getSelectedItem();
        String grade = JOptionPane.showInputDialog(this, "Enter Grade:");
        if (studentId != null && courseId != null && grade != null) {
            Student student = findStudentById(studentId);
            Course course = findCourseById(courseId);
            if (student != null && course != null) {
                student.assignGrade(course, grade);
                outputArea.append("Grade " + grade + " assigned to " + studentId + " for " + courseId + "\n");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Student ID or Course ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshDropdowns() {
        studentComboBox.removeAllItems();
        courseComboBox.removeAllItems();
        for (Student student : dataStorage.getStudents()) {
            studentComboBox.addItem(student.getStudentId());
        }
        for (Course course : dataStorage.getCourses()) {
            courseComboBox.addItem(course.getCourseId());
        }
    }

    private Student findStudentById(String studentId) {
        for (Student student : dataStorage.getStudents()) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }

    private Course findCourseById(String courseId) {
        for (Course course : dataStorage.getCourses()) {
            if (course.getCourseId().equals(courseId)) {
                return course;
            }
        }
        return null;
    }
}