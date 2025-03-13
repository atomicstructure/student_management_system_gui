import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class StudentManagementGUI extends JFrame {
    private DataStorage dataStorage;
    private JTextArea outputArea;
    private JComboBox<String> studentComboBox;
    private JComboBox<String> courseComboBox;
    private JTable studentDetailsTable;

    public StudentManagementGUI() {
        dataStorage = new DataStorage();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Management System");
        setSize(1200, 800); // Increased width to accommodate more columns
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
        JButton viewStudentDetailsButton = new JButton("View Student Details");
        JButton addCourseButton = new JButton("Add Course");
        JButton enrollStudentButton = new JButton("Enroll Student");
        JButton assignGradeButton = new JButton("Assign Grade");

        buttonPanel.add(addStudentButton);
        buttonPanel.add(updateStudentButton);
        buttonPanel.add(viewStudentDetailsButton);
        buttonPanel.add(addCourseButton);
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

        // Table for Student Details
        studentDetailsTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(studentDetailsTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Event Handlers
        addStudentButton.addActionListener(e -> addStudent());
        updateStudentButton.addActionListener(e -> updateStudent());
        viewStudentDetailsButton.addActionListener(e -> viewStudentDetails());
        addCourseButton.addActionListener(e -> addCourse());
        enrollStudentButton.addActionListener(e -> enrollStudent());
        assignGradeButton.addActionListener(e -> assignGrade());

        // Populate dropdowns
        refreshDropdowns();
    }

    private void addStudent() {
        String studentId = JOptionPane.showInputDialog(this, "Enter Student ID:");
        String name = JOptionPane.showInputDialog(this, "Enter Student Name:");
        String email = JOptionPane.showInputDialog(this, "Enter Student Email:");
        String major = JOptionPane.showInputDialog(this, "Enter Student Major:");
        if (studentId != null && name != null && email != null && major != null) {
            dataStorage.addStudent(new Student(studentId, name, email, major));
            outputArea.append("Student Added: " + studentId + " - " + name + " - " + email + " - " + major + "\n");
            refreshDropdowns();
        }
    }

    private void updateStudent() {
        String studentId = JOptionPane.showInputDialog(this, "Enter Student ID to Update:");
        if (studentId != null) {
            Student student = findStudentById(studentId);
            if (student != null) {
                String newName = JOptionPane.showInputDialog(this, "Enter New Name:");
                String newEmail = JOptionPane.showInputDialog(this, "Enter New Email:");
                String newMajor = JOptionPane.showInputDialog(this, "Enter New Major:");
                if (newName != null && newEmail != null && newMajor != null) {
                    student.setName(newName);
                    student.setEmail(newEmail);
                    student.setMajor(newMajor);
                    outputArea.append("Student Updated: " + studentId + " - " + newName + " - " + newEmail + " - " + newMajor + "\n");
                    refreshDropdowns();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewStudentDetails() {
        String selectedStudent = (String) studentComboBox.getSelectedItem();
        if (selectedStudent != null) {
            // Extract student ID from the selected item
            String studentId = selectedStudent.split(" - ")[0];
            Student student = findStudentById(studentId);
            if (student != null) {
                // Display student details in the table
                String[] columns = {"Student ID", "Student Name", "Email", "Major", "Course ID", "Course Name", "Grade"};
                Object[][] data = new Object[student.getEnrolledCourses().size()][7];
                int index = 0;
                for (Map.Entry<Course, String> entry : student.getEnrolledCourses().entrySet()) {
                    Course course = entry.getKey();
                    String grade = entry.getValue();
                    data[index][0] = student.getStudentId();
                    data[index][1] = student.getName();
                    data[index][2] = student.getEmail();
                    data[index][3] = student.getMajor();
                    data[index][4] = course.getCourseId();
                    data[index][5] = course.getCourseName();
                    data[index][6] = grade;
                    index++;
                }
                studentDetailsTable.setModel(new javax.swing.table.DefaultTableModel(data, columns));
                outputArea.append("Displaying details for Student: " + studentId + "\n");
            } else {
                JOptionPane.showMessageDialog(this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addCourse() {
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID:");
        String courseName = JOptionPane.showInputDialog(this, "Enter Course Name:");
        if (courseId != null && courseName != null) {
            dataStorage.addCourse(new Course(courseId, courseName));
            outputArea.append("Course Added: " + courseId + " - " + courseName + "\n");
            refreshDropdowns();
        }
    }

    private void enrollStudent() {
        String selectedStudent = (String) studentComboBox.getSelectedItem();
        String selectedCourse = (String) courseComboBox.getSelectedItem();
        if (selectedStudent != null && selectedCourse != null) {
            // Extract student ID and course ID from the selected items
            String studentId = selectedStudent.split(" - ")[0];
            String courseId = selectedCourse.split(" - ")[0];
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
        String selectedStudent = (String) studentComboBox.getSelectedItem();
        String selectedCourse = (String) courseComboBox.getSelectedItem();
        String grade = JOptionPane.showInputDialog(this, "Enter Grade:");
        if (selectedStudent != null && selectedCourse != null && grade != null) {
            // Extract student ID and course ID from the selected items
            String studentId = selectedStudent.split(" - ")[0];
            String courseId = selectedCourse.split(" - ")[0];
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
            // Add both student ID and name to the dropdown
            studentComboBox.addItem(student.getStudentId() + " - " + student.getName());
        }
        for (Course course : dataStorage.getCourses()) {
            // Add both course ID and name to the dropdown
            courseComboBox.addItem(course.getCourseId() + " - " + course.getCourseName());
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