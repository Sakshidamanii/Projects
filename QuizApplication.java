import java.util.*;

class Question {
    String questionText;
    List<String> options;
    int correctAnswerIndex;

    public Question(String questionText, List<String> options, int correctAnswerIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public boolean isCorrect(int userAnswer) {
        return userAnswer == correctAnswerIndex;
    }
}

public class QuizApplication {
    static List<Question> questions = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadQuestions();
        int score = 0;

        System.out.println("Welcome to the Java Quiz!");
        for (int i = 0; i < questions.size(); i++) {
            System.out.println("\nQuestion " + (i + 1) + ":");
            Question q = questions.get(i);
            System.out.println(q.questionText);
            for (int j = 0; j < q.options.size(); j++) {
                System.out.println((j + 1) + ". " + q.options.get(j));
            }

            System.out.print("Your answer (1-4): ");
            int userAnswer = scanner.nextInt() - 1;

            if (q.isCorrect(userAnswer)) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Wrong! The correct answer was: " + (q.correctAnswerIndex + 1));
            }
        }

        System.out.println("\nQuiz Over! Your Score: " + score + "/" + questions.size());
    }

    static void loadQuestions() {
        questions.add(new Question("What is the size of an int in Java?", Arrays.asList("16 bits", "32 bits", "64 bits", "8 bits"), 1));
        questions.add(new Question("Which of the following is not a Java feature?", Arrays.asList("Object-oriented", "Use of pointers", "Portable", "Dynamic"), 1));
        questions.add(new Question("Which keyword is used to inherit a class in Java?", Arrays.asList("this", "super", "extends", "implements"), 2));
        questions.add(new Question("Which of these is a valid keyword in Java?", Arrays.asList("interface", "string", "unsigned", "goto"), 0));

        // Additional Questions (Level 0 to Pro)
        questions.add(new Question("What is the default value of a boolean in Java?", Arrays.asList("true", "false", "null", "0"), 1));
        questions.add(new Question("Which method is the entry point of a Java program?", Arrays.asList("main()", "start()", "init()", "run()"), 0));
        questions.add(new Question("Java is _________ typed language.", Arrays.asList("dynamically", "loosely", "statically", "none"), 2));
        questions.add(new Question("Which of these cannot be used for a variable name in Java?", Arrays.asList("identifier", "keyword", "literal", "constant"), 1));
        questions.add(new Question("What does JVM stand for?", Arrays.asList("Java Virtual Machine", "Java Variable Machine", "Java Verified Mode", "None of the above"), 0));
        questions.add(new Question("Which of the following is not an access modifier in Java?", Arrays.asList("public", "protected", "private", "package"), 3));
        questions.add(new Question("Which operator is used by Java for comparing values?", Arrays.asList("=", "==", "!=", "<>"), 1));
        questions.add(new Question("Which package contains the Random class?", Arrays.asList("java.util", "java.lang", "java.io", "java.net"), 0));
        questions.add(new Question("What is the extension of Java bytecode files?", Arrays.asList(".java", ".class", ".jar", ".exe"), 1));
        questions.add(new Question("Which of these is not a primitive data type in Java?", Arrays.asList("int", "float", "boolean", "String"), 3));
        questions.add(new Question("What is the parent class of all classes in Java?", Arrays.asList("Object", "Class", "Main", "Super"), 0));
        questions.add(new Question("Which keyword is used to create an instance of a class?", Arrays.asList("this", "super", "new", "instance"), 2));
        questions.add(new Question("Which exception is thrown when a null object is accessed?", Arrays.asList("NullPointerException", "ArrayIndexOutOfBoundsException", "ClassNotFoundException", "ArithmeticException"), 0));
        questions.add(new Question("Which loop is guaranteed to execute at least once?", Arrays.asList("for", "while", "do-while", "foreach"), 2));
        questions.add(new Question("Which keyword is used to define a constant in Java?", Arrays.asList("constant", "final", "static", "const"), 1));
        questions.add(new Question("Which collection class allows duplicate elements?", Arrays.asList("Set", "List", "Map", "Queue"), 1));
        questions.add(new Question("Which method can be used to find the length of a string?", Arrays.asList("length()", "size()", "getLength()", "count()"), 0));
        questions.add(new Question("Java supports multiple inheritance through ________.", Arrays.asList("classes", "interfaces", "abstract classes", "objects"), 1));
        questions.add(new Question("Which data type is used to create a variable that should store text?", Arrays.asList("String", "int", "char", "boolean"), 0));
        questions.add(new Question("Which statement is true about Java?", Arrays.asList("Java is a platform-dependent language", "Java is a platform-independent language", "Java is a scripting language", "Java is not object-oriented"), 1));
    }
}
