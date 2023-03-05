import java.util.TreeSet;

public class BestPartialScore implements Strategy{
    @Override
    public Student getBestStudent(TreeSet<Grade> grades) {
        Grade st = grades.first();
        for(Grade grade: grades)
            if(grade.getPartialScore() > st.getPartialScore()) {
                st = grade;
            }
        return st.getstudent();
    }
}
