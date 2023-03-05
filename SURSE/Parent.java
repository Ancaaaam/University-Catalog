import java.util.ArrayList;

public class Parent extends User implements Observer{
     ArrayList<Notification> notifications;

    private String courses;
    public Parent(String firstname, String lastname) {
        super(firstname, lastname);
        notifications = new ArrayList<>();
        courses = "";
    }

    public void update(Notification notification)  {
        notifications.add(notification);
        System.out.println(notification.toString());
    }

    public void addCourses(Course course) {
        courses += course.getName();
        courses += " ";
    }

    public String getCourses() {
        return courses;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }
}
