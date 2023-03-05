import java.util.ArrayList;

public class Student extends User{
    private Parent mother;
    private Parent father;
    private String courses;
    public Student(String firstname, String lastname) {
        super(firstname, lastname);
        courses = " ";
    }

    public void addCourse(Course course){
       courses += " ";
       courses += course.getName();
    }

    public String getCourses() {
        return courses;

    }
    public void setMother(Parent mother) {
        this.mother = mother;
    }
    public void setFather(Parent father) {
        this.father = father;
    }
    public Parent getMother() {
        return this.mother;
    }

    public Parent getFather() {
        return this.father;
    }

    public boolean isMother(Observer observer) {
        if(observer == mother)
            return true;
        return false;
    }

    public boolean isFather(Observer observer) {
        if(observer == father)
            return true;
        return false;
    }

}
