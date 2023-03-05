public class Teacher extends User {

    public String courses;
    public Teacher(String firstname, String lastname) {
    super(firstname, lastname);
    courses = "";
    }

    public void addCourse(Course course) {
        courses += course.getName();
        courses += " ";
    }

    public String getCourses() {
        return courses;
    }
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
