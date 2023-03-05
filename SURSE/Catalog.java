import java.util.*;

public class Catalog implements Subject{
    private ArrayList<Course> courses;

    public ArrayList<Observer> observers;
    private static Catalog obj = null;
    private Catalog() {
        courses = new ArrayList<Course>();
        observers = new ArrayList<Observer>();
    }
    public static Catalog getInstance() {
        if(obj == null)
            obj = new Catalog();
        return obj;
    }
    public void addCourse(Course course) {
        courses.add(course);
    }
    public void removeCourse(Course course) {
        if(courses.contains(course))
            courses.remove(course);
    }
    public ArrayList<Observer> getObservers() { return this.observers;}
    public void addObserver(Observer observer) {
        if(!observers.contains(observer))
            observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Grade grade) {
        for (Observer obs: observers)
        {
            if((grade.getstudent().isMother(obs) || grade.getstudent().isFather(obs))) {
                Grade new_grade = new Grade(grade.getcourse(), grade.getstudent(), grade.getPartialScore(), grade.getExamScore());
                obs.update(new Notification(new_grade));
            }

        }

    }
    /*folosesc functia asta ca sa verific ca mi se introduc bine date in catalog*/
    public String toString() {
        return "catalogul contine cursuri: " + courses;
    }

    public Course getCourse(String namee) {
        for (Course c: courses)
            if(c.getName().equals(namee))
                return c;
        return null;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }
}
