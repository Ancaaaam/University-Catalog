public class Grade implements Comparable, Cloneable{
    private Double partialScore, examScore;
    private Student student;
    private String course;

    public Grade(String course, Student student,Double partialScore, Double examScore ) {
        this.partialScore = partialScore;
        this.examScore = examScore;
        this.course = course;
        this.student = student;
    }

    public Grade(String course, Student student) {
        this.partialScore = 0.0;
        this.examScore = 0.0;
        this.course = course;
        this.student = student;
    }

    public Grade(String course) {
        this.course = course;
        this.partialScore = 0.0;
        this.examScore = 0.0;
        this.student = null;
    }
    public void setPartialScore(Double Score) {
        this.partialScore = Score;
    }

    public void setexamScore(Double Score) {
        this.examScore = Score;
    }

    public void setstudent(Student student) {
        this.student  = student;
    }

    public void setcourse(String course) {
        this.course = course;
    }

    public Double getPartialScore() {
       return partialScore;
    }

    public Double getExamScore() {
        return examScore;
    }

    public Student getstudent() {
       return student;
    }

    public String getcourse() {
        return course;
    }

    public Double getTotal() {
        return partialScore + examScore;
    }
    public int compareTo(Object o) {
        Grade obj = (Grade) o;
        if(this.getTotal() > obj.getTotal())
            return 1;
        else
            if(this.getTotal() == obj.getTotal())
                return 0;
            else
                return -1;
    }
    public Object clone()throws CloneNotSupportedException {
        return (Grade)super.clone();
    }

    public String toString() {
        return student.toString() + " are nota partiala " + partialScore + " si de la examen " + examScore + " si in total " + this.getTotal() + "\n";
    }
}
