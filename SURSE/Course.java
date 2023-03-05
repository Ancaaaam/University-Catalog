import com.sun.source.tree.Tree;

import javax.lang.model.type.ArrayType;
import java.util.*;

public abstract class Course {
    public String name;
    public Teacher professor;
    public ArrayList<Assistant> assistants;
    public TreeSet<Grade> grades;
    public HashMap<String, Group> groups;
    public int no_credits;

    public Strategy strategy;

    public Course(CourseBuilder builder) {
        this.name = builder.name;
        this.professor = builder.professor;
        this.assistants = builder.assistants;
        this.grades  = builder.grades;
        this.groups = builder.groups;
        this.no_credits = builder.no_credits;
        this.strategy = builder.strategy;
    }

    public String toString() {
        return "Cursul " + name + " cu profesorul " + professor + ", asistentii " + assistants +  "\n notele " + grades.toString() +  ", grupele " + groups;
    }
    public String getName() {
        return name;
    }
    public Teacher getProfessor() {
        return professor;
    }

    public ArrayList<Assistant> getAssistants() {
        return assistants;
    }

    public TreeSet<Grade> getGrades() {
        return grades;
    }

    public HashMap<String, Group> getGroups() {
        return groups;
    }

    public int getNo_credits() {
        return no_credits;
    }
    static abstract class CourseBuilder {
        private String name;
        private Teacher professor;
        private ArrayList<Assistant> assistants;
        private TreeSet<Grade> grades;
        private HashMap<String, Group> groups;
        private int no_credits;

        private Strategy strategy;

        public CourseBuilder(String name) {
            this.name = name;
            assistants = new ArrayList<>();
            grades = new TreeSet<>();
            groups = new HashMap<>();
            }

        public CourseBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public CourseBuilder setProfessor(Teacher professor) {
            this.professor = professor;
            return this;
        }

        public CourseBuilder setAssistant(Assistant assistant) {
            this.assistants.add(assistant);
            return this;
        }

        public CourseBuilder setGrade(Grade grade) {
            this.grades.add(grade);
            return this;
        }

        public CourseBuilder setGroup(Group group) {
            this.groups.put(group.ID, group);
            return this;
        }

        public CourseBuilder setNo_credits(int no_credits) {
            this.no_credits = no_credits;
            return this;
        }

        public CourseBuilder setStrategy(Strategy strategy){
            this.strategy = strategy;
            return this;

        }
        public abstract Course build();
            //return new Course(this);
    }

    public void addAssistant(String ID, Assistant assitant) {
        if(!this.assistants.contains(assitant))
            this.assistants.add(assitant);
        Group g_modified = groups.get(ID);
        g_modified.assistant = assitant;
        groups.put(ID, g_modified);
    }

    public void addStudent(String ID, Student student) {
        Group g_modified = groups.get(ID);
        g_modified.add(student);
        groups.put(ID, g_modified);
    }

    public void addGroup(Group group) {
        groups.put(group.ID, group);
    }

    public void addGroup(String ID, Assistant assistant) {
        Group new_group = new Group(ID, assistant);
        groups.put(ID, new_group);
    }

    public void addGroup(String ID, Assistant assitant, Comparator<Student> comp) {
        Group new_group = new Group(ID, assitant, comp);
        groups.put(ID, new_group);
    }

    public Grade getGrade(Student student) {
        for(Grade gr: grades)
            if(student.equals(gr.getstudent()))
                return gr;
        return null;
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    public void removeGrade(Grade grade) {
        grades.remove(grade);
        System.out.println("exista acum"+ grades.size());
    }

   /* public void setGrade(Grade grade) {
        int i;
        for(Grade gr: grades)
            if(gr.getstudent().equals(grade.getstudent())) {
                gr.setPartialScore(grade.getPartialScore());
                gr.setexamScore(gra);
            }

    }*/

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> list_of_students = new ArrayList<>();
        for(Group g: groups.values())
            for(Student s: g)
                    list_of_students.add(s);
        return list_of_students;
    }

    public HashMap<Student, Grade> getAllStudentsGrades() {
        HashMap<Student, Grade> students_with_grades = new HashMap<>();
        for(Grade gr: grades)
            students_with_grades.put(gr.getstudent(), gr);
        return students_with_grades;
    }

    public Student getBestStudent() {
        return this.strategy.getBestStudent(grades);
    }

    public abstract  ArrayList<Student> getGraduatedStudents();

     class Snapshot {
        public TreeSet<Grade> current_grades;
        public Snapshot() {
            current_grades = new TreeSet<>();
        }

        public void setCurrent_grades(TreeSet<Grade> current_grades) {

            for(Grade gr : current_grades) {
                //this.current_grades.add(gr);
                Grade new_grade = new Grade(name, gr.getstudent(), gr.getPartialScore(), gr.getExamScore());
                this.current_grades.add(new_grade);
            }
        }

        public TreeSet<Grade> getCurrent_grades() {
            return this.current_grades;
        }
    }

     public Snapshot backup_grades;

    public void makeBackup() {
        backup_grades = new Snapshot();
        backup_grades.setCurrent_grades(grades);
    }

    public void undo() {
        grades.clear();
        for (Grade gr : backup_grades.getCurrent_grades()) {
            grades.add(gr);

        }
    }
    public TreeSet<Grade> getSnapshot() {
        return backup_grades.getCurrent_grades();
    }
}

class PartialCourse extends Course {
    public PartialCourse(PartialCourseBuilder builder) {
        super(builder);
    }

    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> list_of_students = new ArrayList<>();
        for(Grade g: grades)
            if(g.getTotal() >= 5)
                list_of_students.add(g.getstudent());
        return list_of_students;
    }
    static class PartialCourseBuilder extends Course.CourseBuilder {
        public PartialCourseBuilder(String name) {
            super(name);
        }

        public Course build() {
            return new PartialCourse(this);
        }
    }
}

class FullCourse extends Course {
    public FullCourse(FullCourseBuilder builder) {
        super(builder);
    }

    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> list_of_students = new ArrayList<>();
        for(Grade g: grades)
            if(g.getPartialScore() >= 3 && g.getExamScore() >= 2)
                list_of_students.add(g.getstudent());
        return list_of_students;
    }
    static class FullCourseBuilder extends Course.CourseBuilder {
       public FullCourseBuilder(String name) {
            super(name);
        }

        public Course build() {
            return new FullCourse(this);
        }
    }
}




