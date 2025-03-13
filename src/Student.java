import java.util.HashMap;
import java.util.Map;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private String major;
    private Map<Course, String> enrolledCourses; // Course -> Grade

    public Student(String studentId, String name, String email, String major) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.major = major;
        this.enrolledCourses = new HashMap<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMajor() {
        return major;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMajor(String Major) {
        this.major = Major;
    }

    public void enrollCourse(Course course) {
        enrolledCourses.put(course, "N/A"); // Default grade is "N/A"
    }

    public void assignGrade(Course course, String grade) {
        if (enrolledCourses.containsKey(course)) {
            enrolledCourses.put(course, grade);
        }
    }

    public Map<Course, String> getEnrolledCourses() {
        return enrolledCourses;
    }

    @Override
    public String toString() {
        return "ID: " + studentId + ", Name: " + name + ", Email: " + email + ", Major: " + major;
    }
}