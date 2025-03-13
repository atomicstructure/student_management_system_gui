import java.util.HashMap;
import java.util.Map;

public class Student {
    private String studentId;
    private String name;
    private Map<Course, String> enrolledCourses; // Course -> Grade

    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        this.enrolledCourses = new HashMap<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "ID: " + studentId + ", Name: " + name;
    }
}