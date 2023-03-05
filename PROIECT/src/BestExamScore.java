import java.util.TreeSet;

public class BestExamScore implements Strategy{
    @Override
    public Student getBestStudent(TreeSet<Grade> grades) {
        Grade st = grades.first();
        for(Grade grade: grades)
            if(grade.getExamScore() > st.getExamScore()) {
                st = grade;
            }
        return st.getstudent();
    }
}
