import java.io.*;
import java.util.*;

class Student {
    private String id;
    private String name;
    private int age;

    public Student(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + age;
    }

    public static Student fromString(String data) {
        String[] parts = data.split(",");
        return new Student(parts[0], parts[1], Integer.parseInt(parts[2]));
    }
}

public class StudentManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Student> students = new ArrayList<>();
    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        loadStudentsFromFile();

        while (true) {
            System.out.println("\n========== Student Management System ==========");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Delete Student");
            System.out.println("5. Sort Students");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewStudents();
                case 3 -> searchStudent();
                case 4 -> deleteStudent();
                case 5 -> sortStudents();
                case 6 -> {
                    saveStudentsToFile();
                    System.out.println("📌 Exiting Student Management System. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("❌ Invalid choice! Please select a valid option.");
            }
        }
    }

    // ✅ Load students from file
    private static void loadStudentsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                students.add(Student.fromString(line));
            }
            System.out.println("📂 Data loaded from file.");
        } catch (IOException e) {
            System.out.println("❌ Error loading students from file.");
        }
    }

    // ✅ Save students to file
    private static void saveStudentsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student student : students) {
                bw.write(student.toString());
                bw.newLine();
            }
            System.out.println("💾 Data saved to file.");
        } catch (IOException e) {
            System.out.println("❌ Error saving students to file.");
        }
    }

    // ✅ Add student with validation
    private static void addStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();

        if (students.stream().anyMatch(s -> s.getId().equals(id))) {
            System.out.println("❌ ID already exists! Please enter a unique ID.");
            return;
        }

        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine().trim();

        if (!name.matches("[a-zA-Z ]+")) {
            System.out.println("❌ Invalid name! Only letters and spaces are allowed.");
            return;
        }

        System.out.print("Enter Student Age: ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine());
            if (age < 5 || age > 100) {
                System.out.println("❌ Invalid age! Age must be between 5 and 100.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Please enter a valid number for age.");
            return;
        }

        students.add(new Student(id, name, age));
        saveStudentsToFile();
        System.out.println("✅ Student added successfully!");
    }

    // ✅ View students
    private static void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("📌 No students found.");
            return;
        }
        System.out.println("\n===== Student List =====");
        for (Student student : students) {
            System.out.println("ID: " + student.getId() + " | Name: " + student.getName() + " | Age: " + student.getAge());
        }
    }

    // ✅ Search for a student
    private static void searchStudent() {
        System.out.print("Enter Student ID to search: ");
        String id = scanner.nextLine().trim();

        for (Student student : students) {
            if (student.getId().equals(id)) {
                System.out.println("🔍 Student Found: " + student);
                return;
            }
        }
        System.out.println("❌ Student with ID " + id + " not found.");
    }

    // ✅ Delete a student
    private static void deleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        String id = scanner.nextLine().trim();

        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            Student student = iterator.next();
            if (student.getId().equals(id)) {
                iterator.remove();
                saveStudentsToFile();
                System.out.println("✅ Student with ID " + id + " deleted.");
                return;
            }
        }
        System.out.println("❌ Student with ID " + id + " not found.");
    }

    // ✅ Sort students
    private static void sortStudents() {
        if (students.isEmpty()) {
            System.out.println("📌 No students to sort.");
            return;
        }

        System.out.println("\nSort by:");
        System.out.println("1. Name");
        System.out.println("2. ID");
        System.out.print("Enter choice: ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Enter 1 or 2.");
            return;
        }

        switch (choice) {
            case 1 -> students.sort(Comparator.comparing(Student::getName));
            case 2 -> students.sort(Comparator.comparing(Student::getId));
            default -> {
                System.out.println("❌ Invalid choice!");
                return;
            }
        }
        saveStudentsToFile();
        System.out.println("✅ Students sorted successfully!");
        viewStudents();
    }
}
