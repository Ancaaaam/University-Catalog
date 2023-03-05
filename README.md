# Detailed classes implementation

### Catalog Class
a class with a list of Course objects

This class contains the following methods:
- public void addCourse(Course course)  // add a course in Catalog
- public void removeCourse(Course course) //remove a course in Catalog

For the implementation of this class the **Singleton Design Pattern** was used.

### User, Student, Parent, Assistant, Teacher classes 

_User class_ is inherited by the other classes and contains a constructor and toString() method. The implementation is based on **Factory design pattern** for an efficient initialization(UserFactory class, which has a getUser method that returns an User object).
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
This **abstract class**  contains numerous fields(a Teacher, a collection of assitants, an ordered collection of Grade, a Group hashmap) and various methods that realizes 




