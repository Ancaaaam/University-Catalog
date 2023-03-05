public class Notification {
    private Grade grade;
    public Notification(Grade grade) {
        this.grade = grade;
    }

    public String toString() {
        return "Notificare: Elevul " + grade.getstudent().toString() + " a primit nota " + grade.getTotal() + " la materia" + grade.getcourse();
    }

    public Grade getGrade() {
        return grade;
    }
}
