import java.util;
import java.util.logging;


// ============= Entity Classes =============

class Student {
    private final String studentId;
    private final Map<String, List<String>> submittedAssignments;

    public Student(String studentId) {
        this.studentId = studentId;
        this.submittedAssignments = new HashMap<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public void submitAssignment(String className, String assignmentDetail) {
        submittedAssignments
            .computeIfAbsent(className, k -> new ArrayList<>())
            .add(assignmentDetail);
    }

    public List<String> getSubmissions(String className) {
        return submittedAssignments.getOrDefault(className, new ArrayList<>());
    }
}

class Classroom {
    private final String name;
    private final Map<String, Student> students;
    private final List<String> assignments;

    public Classroom(String name) {
        this.name = name;
        this.students = new HashMap<>();
        this.assignments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean addStudent(Student student) {
        if (students.containsKey(student.getStudentId())) return false;
        students.put(student.getStudentId(), student);
        return true;
    }

    public boolean hasStudent(String studentId) {
        return students.containsKey(studentId);
    }

    public Student getStudent(String studentId) {
        return students.get(studentId);
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public void addAssignment(String assignment) {
        assignments.add(assignment);
    }

    public List<String> getAssignments() {
        return assignments;
    }
}

// ============= Manager Class =============

class VirtualClassroomManager {
    private final Map<String, Classroom> classrooms;
    private final Map<String, Student> allStudents;
    private static final Logger logger = Logger.getLogger(VirtualClassroomManager.class.getName());

    public VirtualClassroomManager() {
        this.classrooms = new HashMap<>();
        this.allStudents = new HashMap<>();
        setupLogger();
    }

    private void setupLogger() {
        try {
            LogManager.getLogManager().reset();
            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.INFO);
            logger.addHandler(handler);
            logger.setLevel(Level.INFO);
        } catch (SecurityException e) {
            System.out.println("Could not configure logger.");
        }
    }

    public void addClassroom(String name) {
        if (name == null || name.trim().isEmpty()) {
            logger.warning("Classroom name cannot be empty.");
            return;
        }

        if (classrooms.containsKey(name)) {
            System.out.println("Classroom already exists.");
            return;
        }

        classrooms.put(name, new Classroom(name));
        logger.info("Classroom " + name + " has been created.");
        System.out.println("Classroom " + name + " has been created.");
    }

    public void addStudent(String studentId, String className) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null) {
            System.out.println("Classroom does not exist.");
            return;
        }

        Student student = allStudents.computeIfAbsent(studentId, id -> new Student(id));
        if (classroom.addStudent(student)) {
            logger.info("Student " + studentId + " has been enrolled in " + className + ".");
            System.out.println("Student " + studentId + " has been enrolled in " + className + ".");
        } else {
            System.out.println("Student already enrolled in this classroom.");
        }
    }

    public void scheduleAssignment(String className, String assignmentDetail) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null) {
            System.out.println("Classroom does not exist.");
            return;
        }

        classroom.addAssignment(assignmentDetail);
        logger.info("Assignment for " + className + " has been scheduled.");
        System.out.println("Assignment for " + className + " has been scheduled.");
    }

    public void submitAssignment(String studentId, String className, String assignmentDetail) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null || !classroom.hasStudent(studentId)) {
            System.out.println("Student not enrolled in this classroom.");
            return;
        }

        Student student = classroom.getStudent(studentId);
        student.submitAssignment(className, assignmentDetail);
        logger.info("Assignment submitted by Student " + studentId + " in " + className + ".");
        System.out.println("Assignment submitted by Student " + studentId + " in " + className + ".");
    }

    public void listClassrooms() {
        if (classrooms.isEmpty()) {
            System.out.println("No classrooms available.");
            return;
        }

        System.out.println("Available Classrooms:");
        for (String name : classrooms.keySet()) {
            System.out.println("- " + name);
        }
    }

    public void listStudents(String className) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null) {
            System.out.println("Classroom does not exist.");
            return;
        }

        Collection<Student> students = classroom.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students enrolled in " + className + ".");
        } else {
            System.out.println("Students in " + className + ":");
            for (Student s : students) {
                System.out.println("- " + s.getStudentId());
            }
        }
    }
}







import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VirtualClassroomManager manager = new VirtualClassroomManager();

        System.out.println("=== Welcome to Virtual Classroom Manager ===");
        System.out.println("Type 'exit' to quit.");
        System.out.println("Available Commands:");
        System.out.println(
                " add_classroom <class_name>\n" +
                " add_student <student_id> <class_name>\n" +
                " schedule_assignment <class_name> <assignment_details>\n" +
                " submit_assignment <student_id> <class_name> <assignment_details>\n" +
                " list_classrooms\n" +
                " list_students <class_name>");

        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;

            String[] parts = input.split(" ", 3); // Allow space in assignment message
            if (parts.length == 0) continue;

            try {
                switch (parts[0].toLowerCase()) {
                    case "add_classroom":
                        if (parts.length < 2) System.out.println("Usage: add_classroom <class_name>");
                        else manager.addClassroom(parts[1]);
                        break;

                    case "add_student":
                        if (parts.length < 3) System.out.println("Usage: add_student <student_id> <class_name>");
                        else manager.addStudent(parts[1], parts[2]);
                        break;

                    case "schedule_assignment":
                        if (parts.length < 3) System.out.println("Usage: schedule_assignment <class_name> <details>");
                        else manager.scheduleAssignment(parts[1], parts[2]);
                        break;

                    case "submit_assignment":
                        String[] subParts = input.split(" ", 4);
                        if (subParts.length < 4) System.out.println("Usage: submit_assignment <student_id> <class_name> <details>");
                        else manager.submitAssignment(subParts[1], subParts[2], subParts[3]);
                        break;

                    case "list_classrooms":
                        manager.listClassrooms();
                        break;

                    case "list_students":
                        if (parts.length < 2) System.out.println("Usage: list_students <class_name>");
                        else manager.listStudents(parts[1]);
                        break;

                    default:
                        System.out.println("Unknown command. Try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("Exiting Virtual Classroom Manager. Goodbye!");
    }
}