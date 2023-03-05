import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Test {
    public static void  main(String args[]) throws FileNotFoundException {

        File file = new File("C:\\Users\\Anca\\OneDrive\\Desktop\\materiale\\anul 2 sem 1\\POO\\tema\\tema\\src\\test01.txt");
        Scanner sc = new Scanner(file);
        ArrayList<Integer> check = new ArrayList<>();
        Catalog catalog  = Catalog.getInstance();
        int courses_no;
        HashMap<Teacher, ArrayList<ScoreVisitor.Tuple<Student, String, Double>>> totalexamScores = new HashMap<>();
        HashMap<Assistant, ArrayList<ScoreVisitor.Tuple<Student, String, Double>>> totalpartialScores = new HashMap<>();
        while (sc.hasNextLine()) {
            //primul element din fisier e nr de cursuri
            courses_no = Integer.valueOf(sc.nextLine());
            System.out.println(courses_no);
            while(check.size()!=courses_no) {
                check.add(1);
                StringTokenizer values = new StringTokenizer(sc.nextLine(), " ");
                String course_type = values.nextToken();
                String course_name = values.nextToken();
                String teacher_firstName = values.nextToken();
                String teacher_lastName = values.nextToken();
                int no_credits = Integer.valueOf(values.nextToken());
                Course course;
                //int ok = 0;
                int okk = 0;
                User teacher = null; //UserFactory.initializeUser("Teacher", teacher_firstName, teacher_lastName);
                for(Course cr : catalog.getCourses())
                    if(cr.getProfessor().getLastName().compareTo(teacher_lastName) == 0 && cr.getProfessor().getFirstName().compareTo(teacher_firstName) == 0) {
                        teacher =  cr.getProfessor();
                        okk =1;
                    }
                if(okk == 0)
                    teacher = UserFactory.initializeUser("Teacher", teacher_firstName, teacher_lastName);
                okk= 0;
                if (course_type.equals("PartialCourse"))
                    course = (PartialCourse) new PartialCourse.PartialCourseBuilder(course_name).setProfessor((Teacher) teacher).setNo_credits(no_credits).setStrategy(new BestTotalScore()).build();
                else
                    course = (FullCourse) new FullCourse.FullCourseBuilder(course_name).setProfessor((Teacher) teacher).setNo_credits(no_credits).setStrategy(new BestTotalScore()).build();
                ((Teacher) teacher).addCourse(course);
                int no_groups = Integer.valueOf(sc.nextLine());
                for(int i = 1; i <= no_groups; i++){
                    StringTokenizer groups_details = new StringTokenizer(sc.nextLine()," ");
                    String group_name = groups_details.nextToken();
                    String assistant_firstName = groups_details.nextToken();
                    String assistant_lastName = groups_details.nextToken();
                    User assistant = null;
                    for(Course cr : catalog.getCourses())
                        for(Assistant as : cr.getAssistants())
                            if(as.getLastName().compareTo(assistant_lastName) == 0 && as.getFirstName().compareTo(assistant_firstName) == 0)
                                assistant = as;
                    if(assistant == null)
                        assistant = UserFactory.initializeUser("Assistant", assistant_firstName, assistant_lastName);
                    ((Assistant) assistant).addCourse(course);
                    Group grupa;
                    if (i % 2 == 1) {
                        //cream grupa ca sa o adaugam la final cu addGroup(Group grupa)
                        grupa = new Group(group_name, (Assistant) assistant, new Comparator<Student>() {
                        @Override
                        public int compare(Student o1, Student o2) {
                            if (o1.getLastName().compareTo(o2.getLastName()) == 0)
                                return o1.getFirstName().compareTo(o2.getFirstName());
                            return o1.getLastName().compareTo(o2.getLastName());
                        }
                      });
                        course.addGroup(grupa);
                    }
                    else {
                        //adaugam grupa si o initializam totodata cu addgroup(String ID, Assistant assistant)
                        course.addGroup(group_name, (Assistant) assistant);
                        }
                    //adaugam asistentul in arraylist-ul de asistenti din clasa Course
                    course.addAssistant(group_name, (Assistant) assistant);
                    int no_students = Integer.valueOf(groups_details.nextToken());
                    for(int j = 1; j <= no_students; j++) {
                        String student_firstName = groups_details.nextToken();
                        String student_lastName = groups_details.nextToken();
                        User student = UserFactory.initializeUser("Student", student_firstName, student_lastName);
                        String type_of_parent = groups_details.nextToken();
                        String parent_firstName = groups_details.nextToken();
                        String parent_lastName = groups_details.nextToken();
                        User parent = UserFactory.initializeUser("Parent", parent_firstName, parent_lastName);
                        ((Parent) parent).addCourses(course);
                        if(type_of_parent.compareTo("mother") == 0)
                            ((Student) student).setMother((Parent) parent);
                        else
                            ((Student) student).setFather((Parent) parent);
                        int ok = 0;
                        for(Course cr : catalog.getCourses())
                            for(Student st : cr.getAllStudents())
                                if(st.getFirstName().compareTo(student_firstName) == 0 && st.getLastName().compareTo(student_lastName) == 0) {
                                    course.addStudent(group_name, (Student) st);
                                    ((Student) st).addCourse(course);
                                    ok = 1;
                                    if(st.getMother() != null)
                                        ((Student) st).getMother().addCourses(course);
                                    else
                                        ((Student) st).getFather().addCourses(course);
                                }
                           if( ok == 0) {
                               course.addStudent(group_name, (Student) student);
                               ((Student) student).addCourse(course);
                           }

                    }
                    //sorteaza studentii din grupele care au fost initializate cu comparator
                    if (course.getGroups().get(group_name).comp != null)
                        Collections.sort(course.getGroups().get(group_name), course.getGroups().get(group_name).comp);
                }
                int no_grades = Integer.valueOf(sc.nextLine());
                for(int i = 1; i <= no_grades; i++) {
                    StringTokenizer grade_details = new StringTokenizer(sc.nextLine(), " ");
                    String student_firstName = grade_details.nextToken();
                    String student_lastName = grade_details.nextToken();
                    Double partialScore = Double.valueOf(grade_details.nextToken());
                    Double examScore = Double.valueOf(grade_details.nextToken());
                    //User student =  UserFactory.initializeUser("Student", student_firstName, student_lastName);
                    for(Student st : course.getAllStudents()) {
                        if(st.getFirstName().compareTo(student_firstName) == 0 && st.getLastName().compareTo(student_lastName) == 0)
                            course.addGrade(new Grade(course_name, (Student) st, partialScore, examScore));
                    }
                    //System.out.println(course.getGrade((Student) student));
                }
                //getAllStudents()
                System.out.println("Studentii de la cursul de " + course.getName() + " sunt \n");
                System.out.println(course.getAllStudents());

                System.out.println("Grupele sunt: ");
                for(Group gr : course.getGroups().values())
                    gr.printGroup();

                //getAllStudentGrades
                System.out.println(course.getAllStudentsGrades());

                //getGraduatedStudents()
                System.out.println("Studentii trecuti sunt " + course.getGraduatedStudents());
                catalog.addCourse(course);
                //addCourse
                for(Grade gr: course.getGrades()) {
                    if(gr.getstudent().getMother() != null) {
                        catalog.addObserver((Parent) gr.getstudent().getMother());
                        catalog.notifyObservers(gr);
                    }
                    else {
                        catalog.addObserver((Parent) gr.getstudent().getFather());
                        catalog.notifyObservers(gr);
                    }
                }
                //Strategy
                System.out.println("Cel mai bun student la coursul " + course.getName() + " este " + course.getBestStudent());
                //System.out.println(course.toString());
                //Visitor
                int no_notes = Integer.valueOf(sc.nextLine());
                HashMap<Teacher, ArrayList<ScoreVisitor.Tuple<Student, String, Double>>> examScores = new HashMap<>();
                ArrayList<ScoreVisitor.Tuple<Student, String, Double>> array1 = new ArrayList<>();
                HashMap<Assistant, ArrayList<ScoreVisitor.Tuple<Student, String, Double>>> partialScores = new HashMap<>();
                ArrayList<ScoreVisitor.Tuple<Student, String, Double>> array2 = new ArrayList<>();
                ArrayList<Assistant> assistants_notes = new ArrayList<>();
                for(int i = 1 ; i <= no_notes; i++) {
                    StringTokenizer note_details = new StringTokenizer(sc.nextLine(), " ");
                    String student_firstName = note_details.nextToken();
                    String student_lastName = note_details.nextToken();
                    String type_of_score = note_details.nextToken();
                    Double score = Double.valueOf(note_details.nextToken());
                    for(Student st : course.getAllStudents())
                        if(st.getFirstName().compareTo(student_firstName) == 0 && st.getLastName().compareTo(student_lastName) == 0)
                         if(type_of_score.compareTo("examScore") == 0) {
                            array1.add(new ScoreVisitor.Tuple(st, course.getName(), score));
                            examScores.put((Teacher) teacher, array1);
                            int ok = 0;
                            //cautam sa vedem daca mai exista acel profesor in hashmap
                            for(Teacher tc : totalexamScores.keySet())
                                if(tc.equals(teacher)) {
                                    ok = 1;
                                    totalexamScores.get(tc).add(new ScoreVisitor.Tuple(st, course.getName(), score));
                                    break;
                                }
                            if(ok == 0)
                                totalexamScores.put((Teacher) teacher, array1);

                        }
                        else {
                            array2.add(new ScoreVisitor.Tuple(st,course.getName(), score));
                            for(Group group : course.getGroups().values())
                                if(group.contains(st)) {
                                    partialScores.put((Assistant) group.getAssistant(), array2);
                                    assistants_notes.add(group.getAssistant());
                                    int ok = 0;
                                    for(Assistant as : totalpartialScores.keySet())
                                        if(as.equals(group.getAssistant())) {
                                            ok = 1;
                                            totalpartialScores.get(as).add(new ScoreVisitor.Tuple(st,course.getName(), score));
                                        }
                                    if (ok == 0) {
                                        totalpartialScores.put((Assistant) group.getAssistant(),new ArrayList<ScoreVisitor.Tuple<Student, String, Double>>());
                                        totalpartialScores.get((Assistant) group.getAssistant()).add(new ScoreVisitor.Tuple(st,course.getName(), score));

                                    }

                                }
                         }
                }
                //Memento backup
                course.makeBackup();
                int no_modified_notes = Integer.valueOf(sc.nextLine());
                System.out.println(no_modified_notes);
                ArrayList<Grade> grades = new ArrayList<>();
                for(int i = 1 ; i <= no_modified_notes; i++) {
                    StringTokenizer note_details = new StringTokenizer(sc.nextLine(), " ");
                    String student_firstName = note_details.nextToken();
                    String student_lastName = note_details.nextToken();
                    Double partialScore = Double.valueOf(note_details.nextToken());
                    Double examScore = Double.valueOf(note_details.nextToken());
                    for(Grade gr : course.getGrades())
                        if(gr.getstudent().getFirstName().compareTo(student_firstName) == 0 && gr.getstudent().getLastName().compareTo(student_lastName) == 0) {
                            gr.setexamScore(examScore);
                            gr.setPartialScore(partialScore);
                            catalog.notifyObservers(gr);
                        }
                }
                System.out.println("Memento check");
                System.out.println(course.getAllStudentsGrades());
                course.undo();
                System.out.println("notele dupa backup");
                System.out.println(course.getAllStudentsGrades());
                System.out.println("Lista asistentilor este " + course.getAssistants().toString());
            }

        }
        //Student
        int index = 0;
        DefaultListModel<Student> students = new DefaultListModel<Student>();
        for(Course cr : catalog.getCourses())
            for(Student st : cr.getAllStudents())
                if(!students.contains(st)) {
                    students.add(index, st);
                    index++;
                }
        new StudentPage(students, catalog.getCourses());

        //TeacherPage
        index = 0;
        DefaultListModel<Teacher> teachers = new DefaultListModel<Teacher>();
        for(Course cr: catalog.getCourses())
            if(!teachers.contains(cr.getProfessor()))
            {
                teachers.add(index, cr.getProfessor());
                index++;
            }
        new TeacherPage(teachers, catalog.getCourses(), totalexamScores, totalpartialScores);

        //AssistantPage
        index = 0;
        DefaultListModel<Assistant> assistants = new DefaultListModel<Assistant>();
        for(Course cr : catalog.getCourses())
            for(Assistant as : cr.getAssistants())
                if(!assistants.contains(as))
                {
                    assistants.add(index, as);
                    index++;
                }
        new AssistantPage(assistants, catalog.getCourses(), totalexamScores, totalpartialScores);

        //ParentPage
        index = 0;
        DefaultListModel<Parent> parents = new DefaultListModel<Parent>();
        for(Course cr : catalog.getCourses())
            for(Student st : cr.getAllStudents())
                if(st.getMother()!=null) {
                    if(!parents.contains(st.getMother())) {
                        parents.add(index, st.getMother());
                        index++;
                    }
                }
                else
                {
                    if(!parents.contains(st.getFather())) {
                        parents.add(index, st.getFather());
                        index++;
                    }
                }

        new ParentPage(parents, catalog.getCourses());
    }
}

class StudentPage {
    JList listStudents;
    JFrame f;
    JLabel info;
    JLabel l1;
    JTextArea t1;
    DefaultListModel<Student> students;

    JList listCourses;

   ArrayList<Course> courses;
    public StudentPage(DefaultListModel<Student> students, ArrayList<Course> courses ) {
        f = new JFrame("Student Page");
        this.students = new DefaultListModel<>();
        this.students = students;
        listStudents = new JList(students);
        this.courses = new ArrayList<>();
        this.courses = courses;
        //listCourses = new JList(courses);
        JPanel info  = new JPanel();
        //JPanel info_cursuri = new JPanel();
        l1 = new JLabel("Cursurile frecventate");
        t1 = new JTextArea(1,30);
        f.setPreferredSize(new Dimension(1000,1000));
        f.setContentPane(new JLabel(new ImageIcon("C:\\Users\\Anca\\OneDrive\\Desktop\\materiale\\anul 2 sem 1\\POO\\tema\\tema\\src\\poli.png")));
        f.setLayout(new FlowLayout());

        JScrollPane scroll = new JScrollPane(listStudents);
        f.add(l1);
        f.add(t1);
       // f.add(info);
        f.add(scroll);
        listStudents.addListSelectionListener(new Listener());

        //f.setSize(500,500);
        f.setVisible(true);
        f.pack();

    }

    class Listener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            if(listStudents.isSelectionEmpty())
                return;
            if(!e.getValueIsAdjusting()) {
                t1.setText(students.getElementAt(listStudents.getSelectedIndex()).getCourses());
                DefaultListModel<String> coursesStudent = new DefaultListModel<>();
                int index = 0;
                for (Course cr : courses)
                    for (Student st : cr.getAllStudents())
                        if (st.equals(students.getElementAt(listStudents.getSelectedIndex()))) {
                            String answer = "";
                            answer += cr.getName() ;
                            coursesStudent.add(index, answer);
                            index++;
                        }
                JList listcoursesStudent = new JList(coursesStudent);
                JPanel info_courses = new JPanel();
                listcoursesStudent.setBounds(100, 100, 75, 75);
                //JScrollPane scrollCourses = new JScrollPane(listcoursesStudent);
                info_courses.add(listcoursesStudent);
                JButton b = new JButton("Show");
                b.setBounds(10, 100, 15, 15);
                JLabel labell = new JLabel();
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String data = "";
                        if (listcoursesStudent.getSelectedIndex() != -1) {
                            data = "<html>Cursul selectat: " + listcoursesStudent.getSelectedValue();
                            data += "<br/> Profesorul titular este: ";
                            for(Course cr : courses)
                                if(cr.getName().equals(listcoursesStudent.getSelectedValue())) {
                                    data += cr.getProfessor().toString() + "<br/> Lista asistentilor este: " + cr.getAssistants().toString();
                                    data += "<br/>";
                                    for(Student st : cr.getAllStudents())
                                      if(  st.equals(students.getElementAt(listStudents.getSelectedIndex()))) {
                                          for(Group gr : cr.getGroups().values())
                                              if(gr.contains(st))
                                                  data += "Asistentul studentului este:" + gr.getAssistant().toString() + "<br/>";
                                          data += cr.getGrade(st);
                                      }

                                }

                            labell.setText(data);
                        }
                        labell.setText(data);
                    }
                });
                info_courses.add(b);
                info_courses.add(labell);
                f.add(info_courses, BorderLayout.SOUTH);
            }

        }

    }
}

class TeacherPage {
    JList listTeachers;
    JFrame f;
    JLabel info;
    JLabel l1;
    JTextArea t1;
    DefaultListModel<Teacher> teachers;

    JList listCourses;

    ArrayList<Course> courses;

    HashMap<Teacher, ArrayList<ScoreVisitor.Tuple<Student, String, Double>>> totalexamScores;

    HashMap<Assistant, ArrayList<ScoreVisitor.Tuple<Student, String, Double>>> totalpartialScores ;
    public TeacherPage(DefaultListModel<Teacher> teachers, ArrayList<Course> courses, HashMap<Teacher, ArrayList<ScoreVisitor.Tuple<Student, String, Double>>> totalexamScores,  HashMap<Assistant, ArrayList<ScoreVisitor.Tuple<Student, String, Double>>> totalpartialScores) {
        f = new JFrame("TeacherPage");
        this.teachers = new DefaultListModel<>();
        this.teachers = teachers;
        listTeachers = new JList(teachers);
        this.courses = new ArrayList<>();
        this.courses = courses;
        this.totalexamScores = totalexamScores;
        this.totalpartialScores = totalpartialScores;
        //listCourses = new JList(courses);
        JPanel info  = new JPanel();
        //JPanel info_cursuri = new JPanel();
        l1 = new JLabel("Cursuri");
        t1 = new JTextArea(1,30);
        f.setPreferredSize(new Dimension(1000,1000));
        f.setContentPane(new JLabel(new ImageIcon("C:\\Users\\Anca\\OneDrive\\Desktop\\materiale\\anul 2 sem 1\\POO\\tema\\tema\\src\\poli.png")));
        f.setLayout(new FlowLayout());

        JScrollPane scroll = new JScrollPane(listTeachers);
        f.add(l1);
        f.add(t1);
        // f.add(info);
        f.add(scroll);
        listTeachers.addListSelectionListener(new Listener());

        //f.setSize(500,500);
        f.setVisible(true);
        f.pack();

    }

    class Listener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            if(listTeachers.isSelectionEmpty())
                return;
            if(!e.getValueIsAdjusting()) {
                t1.setText(teachers.getElementAt(listTeachers.getSelectedIndex()).getCourses());
                DefaultListModel<String> coursesTeacher = new DefaultListModel<>();
                int index = 0;
                for (Course cr : courses)
                        if (cr.getProfessor().equals(teachers.getElementAt(listTeachers.getSelectedIndex()))) {
                            String answer = "";
                            answer += cr.getName() ;
                            coursesTeacher.add(index, answer);
                            index++;
                        }
                JList listcoursesTeacher = new JList(coursesTeacher);
                JPanel info_courses = new JPanel();
                listcoursesTeacher.setBounds(100, 100, 75, 75);
                //JScrollPane scrollCourses = new JScrollPane(listcoursesStudent);
                info_courses.add(listcoursesTeacher);
                JButton b = new JButton("Show");
                b.setBounds(10, 100, 15, 15);
                JLabel labell = new JLabel();
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String data = "";
                        if (listcoursesTeacher.getSelectedIndex() != -1) {
                            data = "<html>Cursul selectat: " + listcoursesTeacher.getSelectedValue() + "<br/>";
                            ArrayList<ScoreVisitor.Tuple<Student, String, Double>> triplet = totalexamScores.get(teachers.getElementAt(listTeachers.getSelectedIndex()));
                            for (ScoreVisitor.Tuple<Student, String, Double> t : triplet)
                                if(t.getTuple_name_course().compareTo((String) listcoursesTeacher.getSelectedValue()) == 0)
                                {
                                data += "De validat: ";
                                data += t.getTuple_student().toString();
                                data += " i se pune nota ";
                                data += t.getTuple_grade();
                                data += "<br/>";
                            }
                            labell.setText(data);
                        }
                        labell.setText(data);
                        Visitor vis = new ScoreVisitor(totalexamScores, totalpartialScores);
                        int index  = 0;
                        vis.visit((Teacher) teachers.getElementAt(listTeachers.getSelectedIndex()));
                    }

                });
                info_courses.add(b);
                info_courses.add(labell);
                f.add(info_courses, BorderLayout.SOUTH);
            }

        }

    }

}

class AssistantPage {
    JList listTeachers;
    JFrame f;
    JLabel info;
    JLabel l1;
    JTextArea t1;
    DefaultListModel<Assistant> teachers;

    JList listCourses;

    ArrayList<Course> courses;

    HashMap<Teacher, ArrayList<ScoreVisitor.Tuple<Student, String, Double>>> totalexamScores;

    HashMap<Assistant, ArrayList<ScoreVisitor.Tuple<Student, String, Double>>> totalpartialScores ;
    public AssistantPage(DefaultListModel<Assistant> teachers, ArrayList<Course> courses, HashMap<Teacher, ArrayList<ScoreVisitor.Tuple<Student, String, Double>>> totalexamScores,  HashMap<Assistant, ArrayList<ScoreVisitor.Tuple<Student, String, Double>>> totalpartialScores) {
        f = new JFrame("AssistantPage");
        this.teachers = new DefaultListModel<>();
        this.teachers = teachers;
        listTeachers = new JList(teachers);
        this.courses = new ArrayList<>();
        this.courses = courses;
        this.totalexamScores = totalexamScores;
        this.totalpartialScores = totalpartialScores;
        //listCourses = new JList(courses);
        JPanel info  = new JPanel();
        //JPanel info_cursuri = new JPanel();
        l1 = new JLabel("Cursuri");
        t1 = new JTextArea(1,30);
        f.setPreferredSize(new Dimension(1000,1000));
        f.setContentPane(new JLabel(new ImageIcon("C:\\Users\\Anca\\OneDrive\\Desktop\\materiale\\anul 2 sem 1\\POO\\tema\\tema\\src\\poli.png")));
        f.setLayout(new FlowLayout());

        JScrollPane scroll = new JScrollPane(listTeachers);
        f.add(l1);
        f.add(t1);
        // f.add(info);
        f.add(scroll);
        listTeachers.addListSelectionListener(new Listener());

        //f.setSize(500,500);
        f.setVisible(true);
        f.pack();

    }

    class Listener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            if(listTeachers.isSelectionEmpty())
                return;
            if(!e.getValueIsAdjusting()) {
                t1.setText(teachers.getElementAt(listTeachers.getSelectedIndex()).getCourses());
                DefaultListModel<String> coursesTeacher = new DefaultListModel<>();
                int index = 0;
                for (Course cr : courses)
                    for(Assistant as : cr.getAssistants())
                    if (as.equals(teachers.getElementAt(listTeachers.getSelectedIndex()))) {
                        String answer = "";
                        answer += cr.getName() ;
                        coursesTeacher.add(index, answer);
                        index++;
                    }
                JList listcoursesTeacher = new JList(coursesTeacher);
                JPanel info_courses = new JPanel();
                listcoursesTeacher.setBounds(100, 100, 75, 75);
                //JScrollPane scrollCourses = new JScrollPane(listcoursesStudent);
                info_courses.add(listcoursesTeacher);
                JButton b = new JButton("Show");
                b.setBounds(10, 100, 15, 15);
                JLabel labell = new JLabel();
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String data = "";
                        if (listcoursesTeacher.getSelectedIndex() != -1) {
                            data = "<html>Cursul selectat: " + listcoursesTeacher.getSelectedValue() + "<br/>";
                            ArrayList<ScoreVisitor.Tuple<Student, String, Double>> triplet =totalpartialScores.get(teachers.getElementAt(listTeachers.getSelectedIndex()));
                            for (ScoreVisitor.Tuple<Student, String, Double> t : triplet)
                                if(t.getTuple_name_course().compareTo((String) listcoursesTeacher.getSelectedValue()) == 0)
                                {
                                    data += "De validat: ";
                                    data += t.getTuple_student().toString();
                                    data += " i se pune nota ";
                                    data += t.getTuple_grade();
                                    data += "<br/>";
                                }
                            labell.setText(data);
                        }
                        labell.setText(data);
                        Visitor vis = new ScoreVisitor(totalexamScores, totalpartialScores);
                        int index  = 0;
                        vis.visit((Assistant) teachers.getElementAt(listTeachers.getSelectedIndex()));
                    }

                });
                info_courses.add(b);
                info_courses.add(labell);
                f.add(info_courses, BorderLayout.SOUTH);
            }

        }

    }

}

class ParentPage {
    JList listStudents;
    JFrame f;
    JLabel info;
    JLabel l1;
    JTextArea t1;
    DefaultListModel<Parent> students;

    JList listCourses;

    ArrayList<Course> courses;
    public ParentPage(DefaultListModel<Parent> students, ArrayList<Course> courses ) {
        f = new JFrame("Parent Page");
        this.students = new DefaultListModel<>();
        this.students = students;
        listStudents = new JList(students);
        this.courses = new ArrayList<>();
        this.courses = courses;
        //listCourses = new JList(courses);
        JPanel info  = new JPanel();
        //JPanel info_cursuri = new JPanel();
        l1 = new JLabel("Cursurile frecventate de copil");
        t1 = new JTextArea(1,30);
        f.setPreferredSize(new Dimension(1000,1000));
        f.setContentPane(new JLabel(new ImageIcon("C:\\Users\\Anca\\OneDrive\\Desktop\\materiale\\anul 2 sem 1\\POO\\tema\\tema\\src\\poli.png")));
        f.setLayout(new FlowLayout());

        JScrollPane scroll = new JScrollPane(listStudents);
        f.add(l1);
        f.add(t1);
        // f.add(info);
        f.add(scroll);
        listStudents.addListSelectionListener(new Listener());

        //f.setSize(500,500);
        f.setVisible(true);
        f.pack();

    }

    class Listener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            if(listStudents.isSelectionEmpty())
                return;
            if(!e.getValueIsAdjusting()) {
                t1.setText(students.getElementAt(listStudents.getSelectedIndex()).getCourses());
                DefaultListModel<String> coursesStudent = new DefaultListModel<>();
                int index = 0;
                for (Course cr : courses)
                    for (Student st : cr.getAllStudents())
                        if(st.getMother() != null) {
                            if (st.getMother().equals(students.getElementAt(listStudents.getSelectedIndex()))) {
                                String answer = "";
                                answer += cr.getName();
                                coursesStudent.add(index, answer);
                                index++;
                            }
                        }
                        else {
                            if (st.getFather().equals(students.getElementAt(listStudents.getSelectedIndex()))) {
                                String answer = "";
                                answer += cr.getName();
                                coursesStudent.add(index, answer);
                                index++;
                            }
                        }
                JList listcoursesStudent = new JList(coursesStudent);
                JPanel info_courses = new JPanel();
                listcoursesStudent.setBounds(100, 100, 75, 75);
                //JScrollPane scrollCourses = new JScrollPane(listcoursesStudent);
                info_courses.add(listcoursesStudent);
                JButton b = new JButton("Show");
                b.setBounds(10, 100, 15, 15);
                JLabel labell = new JLabel();
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String data = "";
                        if (listcoursesStudent.getSelectedIndex() != -1) {
                            data = "<html>Cursul selectat: " + listcoursesStudent.getSelectedValue() + "<br/>";
                            for(Course cr : courses)
                                if(cr.getName().equals(listcoursesStudent.getSelectedValue())) {
                                    for(Notification nt : students.getElementAt(listStudents.getSelectedIndex()).getNotifications())
                                        if(nt.getGrade().getcourse().compareTo(cr.getName()) == 0) {
                                            data += nt.toString();
                                            data += "<br/>";
                                        }
                                }
                            labell.setText(data);
                        }
                        labell.setText(data);
                    }
                });
                info_courses.add(b);
                info_courses.add(labell);
                f.add(info_courses, BorderLayout.SOUTH);
            }

        }

    }
}