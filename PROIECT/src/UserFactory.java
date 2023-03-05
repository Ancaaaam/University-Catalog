public class UserFactory {
    public static User initializeUser(String which, String firstname, String lastname) {
        if(which.equals("Student"))
            return new Student(firstname, lastname);
        if(which.equals("Parent"))
            return new Parent(firstname, lastname);
        if(which.equals("Assistant"))
            return new Assistant(firstname, lastname);
        if(which.equals("Teacher"))
            return new Teacher(firstname, lastname);
        return null;
    }
}
