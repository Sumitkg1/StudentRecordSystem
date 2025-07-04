import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Student {
    static int idCounter = 101;
    String studentId;
    int roll;
    String name;
    int age;
    String grade;
    Student next;

    public Student(int roll, String name, int age, String grade) {
        this.studentId = "S" + idCounter++;
        this.roll = roll;
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.next = null;
    }
}

class StudentLinkedList {
    Student head = null;

    // Check if roll number already exists
    public boolean rollExists(int roll) {
        Student current = head;
        while (current != null) {
            if (current.roll == roll) return true;
            current = current.next;
        }
        return false;
    }

    // Add a student at the end with unique roll
    public boolean addAtEnd(int roll, String name, int age, String grade) {
        if (rollExists(roll)) return false;

        Student newNode = new Student(roll, name, age, grade);
        if (head == null) {
            head = newNode;
        } else {
            Student current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        return true;
    }

    // Display all student records
    public String displayAll() {
        StringBuilder sb = new StringBuilder();
        Student current = head;
        while (current != null) {
            sb.append("ID: ").append(current.studentId)
              .append(", Roll: ").append(current.roll)
              .append(", Name: ").append(current.name)
              .append(", Age: ").append(current.age)
              .append(", Grade: ").append(current.grade)
              .append("\n");
            current = current.next;
        }
        return sb.length() == 0 ? "No Records." : sb.toString();
    }

    // Update grade by roll number
    public boolean updateGrade(int roll, String newGrade) {
        Student current = head;
        while (current != null) {
            if (current.roll == roll) {
                current.grade = newGrade;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Delete by roll number
    public boolean deleteByRoll(int roll) {
        if (head == null) return false;
        if (head.roll == roll) {
            head = head.next;
            return true;
        }

        Student current = head;
        while (current.next != null && current.next.roll != roll) {
            current = current.next;
        }

        if (current.next == null) return false;
        current.next = current.next.next;
        return true;
    }

    // Search by roll number
    public String searchByRoll(int roll) {
        Student current = head;
        while (current != null) {
            if (current.roll == roll) {
                return "Found: ID: " + current.studentId +
                       ", Roll: " + current.roll +
                       ", Name: " + current.name +
                       ", Age: " + current.age +
                       ", Grade: " + current.grade;
            }
            current = current.next;
        }
        return "Student not found.";
    }
}

public class StudentGUI {
    public static void main(String[] args) {
        StudentLinkedList list = new StudentLinkedList();

        JFrame frame = new JFrame("Student Record Manager");
        frame.setSize(500, 500);
        frame.setLayout(new GridLayout(9, 2));

        JTextField rollField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField gradeField = new JTextField();
        JTextArea output = new JTextArea(10, 30);
        output.setEditable(false);

        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete");
        JButton searchBtn = new JButton("Search");
        JButton updateBtn = new JButton("Update Grade");
        JButton displayBtn = new JButton("Display All");

        frame.add(new JLabel("Roll No:"));
        frame.add(rollField);
        frame.add(new JLabel("Name:"));
        frame.add(nameField);
        frame.add(new JLabel("Age:"));
        frame.add(ageField);
        frame.add(new JLabel("Grade:"));
        frame.add(gradeField);

        frame.add(addBtn);
        frame.add(deleteBtn);
        frame.add(searchBtn);
        frame.add(updateBtn);
        frame.add(displayBtn);
        frame.add(new JScrollPane(output));

        // Add Student
        addBtn.addActionListener(e -> {
            try {
                if (rollField.getText().isEmpty() || nameField.getText().isEmpty() ||
                        ageField.getText().isEmpty() || gradeField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields must be filled!");
                    return;
                }

                int roll = Integer.parseInt(rollField.getText().trim());
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String grade = gradeField.getText().trim();

                boolean added = list.addAtEnd(roll, name, age, grade);
                if (added) {
                    output.setText("Student added successfully.\n");
                } else {
                    output.setText("Roll number already exists! Use a unique roll number.\n");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Roll and Age must be numbers!");
            }
        });

        // Delete Student
        deleteBtn.addActionListener(e -> {
            try {
                if (rollField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Enter Roll No to delete.");
                    return;
                }
                int roll = Integer.parseInt(rollField.getText().trim());
                boolean result = list.deleteByRoll(roll);
                output.setText(result ? "Student deleted.\n" : "Student not found.\n");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Roll Number.");
            }
        });

        // Search Student
        searchBtn.addActionListener(e -> {
            try {
                if (rollField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Enter Roll No to search.");
                    return;
                }
                int roll = Integer.parseInt(rollField.getText().trim());
                output.setText(list.searchByRoll(roll));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Roll Number.");
            }
        });

        // Update Grade
        updateBtn.addActionListener(e -> {
            try {
                if (rollField.getText().isEmpty() || gradeField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Enter Roll No and new Grade.");
                    return;
                }
                int roll = Integer.parseInt(rollField.getText().trim());
                String grade = gradeField.getText().trim();
                boolean updated = list.updateGrade(roll, grade);
                output.setText(updated ? "Grade updated.\n" : "Student not found.\n");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Roll Number.");
            }
        });

        // Display All
        displayBtn.addActionListener(e -> {
            output.setText(list.displayAll());
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}