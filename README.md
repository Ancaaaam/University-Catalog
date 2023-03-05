# Detailed classes implementation

### Catalog Class
a class with a list of Course objects

This class contains the following methods:
- public void addCourse(Course course)  // add a course in Catalog
- public void removeCourse(Course course) //remove a course in Catalog

For the implementation of this class the **Singleton Design Pattern** was used.

### User, Student, Parent, Assistant, Teacher classes 

_User class_ is inherited by other classes and contains a constructor and toString() method. The implementation is based on **Factory design pattern** for an efficient initialization(UserFactory class, which has a getUser method that returns an User object).
_Student class_ contains two private Parent objects( for both parents of a student) and contains the following methods:
- public void setMother(Parent mother)
- public void setFather(Parent father)

Further facilities of these classes will be explained later in this README.

### Grade class
It implements **Comparable and Cloneable interfaces** and contains _public Double getTotal()_ method that calculates the final score.

### Group class
This class models by **inheritance principle** an ordered collection of Student objects.
Each group will have associated an object of type Assistant and an string ID.
Two groups are equal if they have the same ID.
The implementation of the class follows the **encapsulation principle**.
This class has the following constructors:
- public Group(String ID, Assistant assistant, Comparator<Student> comp) { }
- public Group(String ID, Assistant assistant) { }
  
The comparator of this class alphabetizes the students by last name and if two last names are the same, the alphabetization is realized by the first name. 

### Course class
This **abstract class**  contains numerous fields(a Teacher, an Assistant collection, an ordered Grade collection, a Group hashmap) and various methods that realize the functionality of this class as a real-world application( public void addAssistant(), publuc void addGroup(), public void addStudent() , public HashMap<Student, Grade> gettAllStudentGrades() etc).
The implementation is based on **Builder desing pattern** .

### Observer desing pattern

The application allows parents to subscribe to the Catalog in order to receive notifications when their child is graded by a teacher or assistant. _Parent class_ implements Observer interface and it contains an ArrayList of Notification objects in which a new element is added when a child receive a new grade.  _Catalog class_ implements Subject interface. 

### Strategy design pattern
Each teacher selects a different approach in order to decide the best student of a semester. There are 3 possible approaches and for each one a class is created:
- **BestPartialScore**: this strategy selects the student with the greatest score throughout the semester
- **BestExamScore**: this strategy selects the student with the greatest score in the final exam
- **BestTotalScore**: this strategy selects the student with the greatest total score

A new method _public Student getBestStudent()_ is added in Course class.

### Visitor design pattern
This design pattern allows an assistant to change the partial scores of students and teachers to modify the exam scores. The implementation relies on two interfaces: Element and Visitor(implemented by ScoreVisitor).
The ScoreVisitor class has two hashmaps in which students' grades for exams and coursework are stored: 
• The **examScores hashmap** has a Teacher key and a Tuple list value
(Student, CourseName – as String, the grade the student was given for the indicated course – as Double).
• The **partialScores dictionary** with a similar meaning, but for grades assigned by assistants.
Also, in the methods of the ScoreVisitor class,  **notifyObservers method** is called , which sends notifications from the Catalog class.
The **Tuple class** is a private inner class of the ScoreVisitor class that is implemented generic.

### Memento design pattern
This desing pattern makes grades backup. In Grade class is implemented _public Object clone()_ method. In Course class, is created the Snapshot private inner class that stores grades and contains a Grade TreeSet, a setter and a getter.  The application has also a private field of type Snapshot in Course class the following two methods are designed:
- public void makeBackup() 
- public void undo() 

When a Backup is required, the SnapShot's setter is called and a course's grades are stored in SnapShot's TreeSet. In order to make an undo, the application uses the SnapShot's getter and replaces a courses's grades with the backup's grades.

### GUI
The GUI is implemented using Swing elements such as ScrollPanel, buttons, frames, background image and it can communicate with the terminal and input. For example,  Teacher Page and Assistant Page has each a ScrollPane in which all teachers/assistants appear. If the user selects one teacher/assistant, the courses taught by that teacher/assistant appear in the "Courses" textarea and a Panel with the respective courses and a button also appear. If the user selects a course and presses the "Show" button, all the grades of the teacher/assistant are validated (notifications are sent to parents) and the required grades appear as validated at the selected subject in the output console.

