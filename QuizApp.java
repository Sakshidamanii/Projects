import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

class Question {
    String question;
    List<String> options;
    int correctAnswer;

    public Question(String question, List<String> options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}

public class QuizApp extends JFrame implements ActionListener {
    private List<Question> questions = Arrays.asList(
            new Question("What is the size of an int in Java?", Arrays.asList("16 bits", "32 bits", "64 bits", "8 bits"), 1),
            new Question("Which of the following is not a Java feature?", Arrays.asList("Object-oriented", "Use of pointers", "Portable", "Dynamic"), 1),
            new Question("Which keyword is used to inherit a class in Java?", Arrays.asList("this", "super", "extends", "implements"), 2),
            new Question("Which of these is a valid keyword in Java?", Arrays.asList("interface", "string", "unsigned", "goto"), 0),
            new Question("What is the default value of a boolean in Java?", Arrays.asList("true", "false", "null", "0"), 1),
            new Question("Which method is the entry point of a Java program?", Arrays.asList("main()", "start()", "init()", "run()"), 0),
            new Question("Java is _________ typed language.", Arrays.asList("dynamically", "loosely", "statically", "none"), 2),
            new Question("Which of these cannot be used for a variable name in Java?", Arrays.asList("identifier", "keyword", "literal", "constant"), 1),
            new Question("What does JVM stand for?", Arrays.asList("Java Virtual Machine", "Java Variable Machine", "Java Verified Mode", "None of the above"), 0),
            new Question("Which of the following is not an access modifier in Java?", Arrays.asList("public", "protected", "private", "package"), 3),
            new Question("Which operator is used by Java for comparing values?", Arrays.asList("=", "==", "!=", "<>"), 1),
            new Question("Which package contains the Random class?", Arrays.asList("java.util", "java.lang", "java.io", "java.net"), 0),
            new Question("What is the extension of Java bytecode files?", Arrays.asList(".java", ".class", ".jar", ".exe"), 1),
            new Question("Which of these is not a primitive data type in Java?", Arrays.asList("int", "float", "boolean", "String"), 3),
            new Question("What is the parent class of all classes in Java?", Arrays.asList("Object", "Class", "Main", "Super"), 0),
            new Question("Which keyword is used to create an instance of a class?", Arrays.asList("this", "super", "new", "instance"), 2),
            new Question("Which exception is thrown when a null object is accessed?", Arrays.asList("NullPointerException", "ArrayIndexOutOfBoundsException", "ClassNotFoundException", "ArithmeticException"), 0),
            new Question("Which loop is guaranteed to execute at least once?", Arrays.asList("for", "while", "do-while", "foreach"), 2),
            new Question("Which keyword is used to define a constant in Java?", Arrays.asList("constant", "final", "static", "const"), 1),
            new Question("Which collection class allows duplicate elements?", Arrays.asList("Set", "List", "Map", "Queue"), 1),
            new Question("Which method can be used to find the length of a string?", Arrays.asList("length()", "size()", "getLength()", "count()"), 0),
            new Question("Java supports multiple inheritance through ________.", Arrays.asList("classes", "interfaces", "abstract classes", "objects"), 1),
            new Question("Which data type is used to create a variable that should store text?", Arrays.asList("String", "int", "char", "boolean"), 0),
            new Question("Which statement is true about Java?", Arrays.asList("Java is a platform-dependent language", "Java is a platform-independent language", "Java is a scripting language", "Java is not object-oriented"), 1)
    );

    private JLabel questionLabel;
    private JRadioButton[] options = new JRadioButton[4];
    private ButtonGroup group;
    private JButton nextButton;
    private int currentQuestion = 0, score = 0;

    public QuizApp() {
        setTitle("Java Quiz Application");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        questionLabel = new JLabel();
        add(questionLabel);

        group = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            add(options[i]);
        }

        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        add(nextButton);

        loadQuestion();
        setVisible(true);
    }

    private void loadQuestion() {
        if (currentQuestion < questions.size()) {
            Question q = questions.get(currentQuestion);
            questionLabel.setText(q.question);
            for (int i = 0; i < 4; i++) {
                options[i].setText(q.options.get(i));
                options[i].setSelected(false);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Quiz Over! Your score: " + score);
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selected = -1;
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                selected = i;
                break;
            }
        }

        if (selected == questions.get(currentQuestion).correctAnswer) {
            JOptionPane.showMessageDialog(this, "Correct! Well done.");
            score++;
        } else {
            JOptionPane.showMessageDialog(this, "Wrong! The correct answer is: " + questions.get(currentQuestion).options.get(questions.get(currentQuestion).correctAnswer));
        }

        currentQuestion++;
        loadQuestion();
    }

    public static void main(String[] args) {
        new QuizApp();
    }
}
