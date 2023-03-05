import java.util.*;
public class ScoreVisitor implements Visitor {
    static class Tuple<Student, String, Double> {

        private Student tuple_student;
        private String tuple_name_course;
        private Double tuple_grade;

        public Tuple(Student tuple_student, String tuple_name_course, Double tuple_grade) {
            this.tuple_grade = tuple_grade;
            this.tuple_student = tuple_student;
            this.tuple_name_course = tuple_name_course;
        }

        public Student getTuple_student() {
            return this.tuple_student;
        }

        public String getTuple_name_course() {
            return this.tuple_name_course;
        }

        public Double getTuple_grade() {
            return this.tuple_grade;
        }

        public java.lang.String toString()
        {
            return this.getTuple_name_course() + " " + this.getTuple_grade().toString();
        }


    }

    public HashMap<Teacher, ArrayList<Tuple<Student, String, Double>>> examScores;
    public HashMap<Assistant, ArrayList<Tuple<Student, String, Double>>> partialScores;

    public ScoreVisitor(HashMap<Teacher, ArrayList<Tuple<Student, String, Double>>> examScores, HashMap<Assistant, ArrayList<Tuple<Student, String, Double>>> partialScores) {
        this.examScores = examScores;
        this.partialScores = partialScores;
    }

    public void visit(Assistant assistant) {
        ArrayList<Tuple<Student, String, Double>> triplet = partialScores.get(assistant);
        for (Tuple<Student, String, Double> t : triplet) {
            //facem un arraylist pe care il folosim ca sa verificam daca exista studentul cu nota respectiva
            ArrayList<Integer> check = new ArrayList<>();
            Course c = Catalog.getInstance().getCourse((String) t.getTuple_name_course());
            for (Grade gr : c.grades)
                if (gr.getstudent().equals(t.getTuple_student())) {
                    check.add(1);
                    gr.setPartialScore(t.getTuple_grade());
                    Catalog.getInstance().notifyObservers(gr);
                }
            if(check.size() == 0) {
                Grade new_grade = new Grade(c.getName(), (Student) t.getTuple_student());
                new_grade.setPartialScore(t.getTuple_grade());
                c.grades.add(new_grade);
                Catalog.getInstance().notifyObservers(new_grade);
            }
        }

    }

    public void visit(Teacher teacher) {
        ArrayList<Tuple<Student, String, Double>> triplet = examScores.get(teacher);
        for (Tuple<Student, String, Double> t : triplet) {
            //facem un arraylist pe care il folosim ca sa verificam daca exista studentul cu nota respectiva
            ArrayList<Integer> check = new ArrayList<>();
            Course c = Catalog.getInstance().getCourse((String) t.getTuple_name_course());
            for (Grade gr : c.grades)
                if (gr.getstudent() == t.getTuple_student()) {
                    check.add(1);
                    gr.setexamScore(t.getTuple_grade());
                    Catalog.getInstance().notifyObservers(gr);
                }
            if(check.size() == 0) {
                Grade new_grade = new Grade(c.getName(), (Student) t.getTuple_student());
                new_grade.setexamScore(t.getTuple_grade());
                c.grades.add(new_grade);
                Catalog.getInstance().notifyObservers(new_grade);
            }
        }

    }
}

