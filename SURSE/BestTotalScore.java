import java.util.TreeSet;

public class BestTotalScore implements Strategy{
    @Override
    public Student getBestStudent(TreeSet<Grade> grades) {
        Grade st = grades.first();
        for(Grade grade: grades)
            if(grade.getTotal() > st.getTotal()) {
                st = grade;
            }
        return st.getstudent();
    }
}
