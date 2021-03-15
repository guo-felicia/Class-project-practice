import tester.Tester;

//interface of generic list
interface IList<T> {

  // check if the list passes
  public boolean ormap(IFunc<T, Boolean> f);

  // combine return value so far
  <U> U foldr(IFunc2<T, U, U> func, U base);
}

//an empty list of T
class MtList<T> implements IList<T> {

  // check if the list passes
  public boolean ormap(IFunc<T, Boolean> f) {
    return false;
  }

  // combine return value so far
  public <U> U foldr(IFunc2<T, U, U> func, U base) {
    return base;
  }
}

//an non-empty list of T
class ConsList<T> implements IList<T> {

  T first;
  IList<T> rest;

  //the constructor
  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * Template: 
   * fields: this.first -- T 
   * this.rest -- IList<T> 
   * methods:
   * this.foldr(IFunc2<T, U, U, U> -- <U> 
   * this.ormap(IFunc<T, U>) -- boolean
   * methods for parameters: 
   * this.func.apply(T, IList<T>) -- <U>
   * 
   */

  // check if the any of the element in the list passes the func
  public boolean ormap(IFunc<T, Boolean> f) {
    return f.apply(this.first) || this.rest.ormap(f);
  }

  // fold an element into a list of elements
  public <U> U foldr(IFunc2<T, U, U> func, U base) {
    return func.apply(this.first, this.rest.foldr(func, base));
  }
}

// Represents instructor of course
class Instructor {
  String name;
  IList<Course> courses;

  //the constructor to represent the initial Instructor with name
  Instructor(String name) {
    this.name = name;
    this.courses = new MtList<Course>();
  }

  //the constructor to represent the instructor and the courses (list)
  Instructor(String name, IList<Course> courses) {
    this.name = name;
    this.courses = courses;
  }
  
  /*
   * Template: 
   * fields: 
   * this.name -- String
   * this.courses -- IList<Course> 
   * methods:
   * this.sameProf(Instructor prof) -- boolean 
   * this.dejavu(Student s) -- boolean
   * this.addCourse(Course course) -- void
   * methods for parameters: 
   * this.name.equals(String name) -- boolean
   * student.dejevuhelp(this.name) -- int
   */

  // determin whether the two professors are same
  public boolean sameProf(Instructor prof) {
    return this.name.equals(prof.name);
  }

  //determines whether the given Student is in 
  //more than one of this Instructor’s Courses.
  public boolean dejavu(Student s) {
    return s.dejavuHelp(this.name) >= 2;
  }

  //update the course list by adding the new course into list
  public void addCourse(Course course) {
    this.courses = new ConsList<Course>(course, this.courses);
  }
  
  //determin whether the given name is same as this instructor's name
  public boolean samename(String name) {
    return this.name.equals(name);
  }
}

// Represent Course class
class Course {
  String name;
  Instructor prof;
  IList<Student> students;

  //the constructor to represent the course before any enrollment
  Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.prof.addCourse(this);
    this.students = new MtList<Student>();
  }

  //the constructor to represent the course with list of students
  Course(String name, Instructor prof, IList<Student> students) {
    this.name = name;
    this.prof = prof;
    this.prof.addCourse(this);
    this.students = students;
  }
  
  /*
   * Template: 
   * fields: 
   * this.name -- String
   * this.prof -- Instructor
   * this.students -- IList<Student> 
   * methods:
   * this.addStudent(Student student) -- void 
   */

  //method for updating the list of student of this course
  public void addStudent(Student student) {
    this.students = new ConsList<Student>(student, this.students);
  }
}

//represent student
class Student {
  String name;
  int id;
  IList<Course> courses;

  //the constructor of the student before he/she 
  //enroll in any course
  Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new MtList<Course>();
  }

  //the constructor includes student's name, id and the 
  //list of course the student enroll in
  Student(String name, int id, IList<Course> courses) {
    this.name = name;
    this.id = id;
    this.courses = courses;
  }
  
  /*
   * Template: 
   * fields: 
   * this.name -- String
   * this.id -- int
   * this.courses -- IList<Course> 
   * methods:
   * this.enroll(Course c) -- void
   * this.classmates(Student s) -- boolean
   * this.dejavuHelp(String name) -- int
   * methods for the parameters:
   * this.courses.ormap(IFunc<T, Boolean> f) -- boolean
   * this.courses.foldr(IFunc2<T, U, U> func, U base) -- int
   */

  // enrolls a Student in the given Course.
  public void enroll(Course c) {
    // update the course list for students
    this.courses = new ConsList<Course>(c, this.courses);
    // update the students fields for the course
    c.addStudent(this);
  }

  // determines whether the given Student is in any of the same classes as this
  // Student.
  boolean classmates(Student s) {
    return this.courses.ormap(new SameCourse(s.courses));
  }

  // helper of dejavu method
  public int dejavuHelp(String name) {
    return this.courses.foldr(new Dejavu(name), 0);
  }
}

//Interface for one-argument function-objects with signature [A -> R]
interface IFunc<A, R> {
  // A -> R
  R apply(A arg);
}

//represent the class of SameCourse to check whether there is overlapping of two lists
class SameCourse implements IFunc<Course, Boolean> {
  IList<Course> courses;

  //the constructor
  SameCourse(IList<Course> courses) {
    this.courses = courses;
  }
  
  /*
   * Template: 
   * fields: 
   * this.courses -- IList<Course> 
   * methods:
   * this.apply(Course c) -- Boolean
   */

  public Boolean apply(Course t) {
    return this.courses.ormap(new CheckSame(t));
  }
}

// represent a class of CheckSame to check whether there is course same as the
// given course
class CheckSame implements IFunc<Course, Boolean> {
  Course c;

  //the constructor
  CheckSame(Course c) {
    this.c = c;
  }
  
  /*
   * Template: 
   * fields: 
   * this.course -- Course
   * methods:
   * this.apply(Course c) -- Boolean
   */

  public Boolean apply(Course course) {
    return course.name.equals(c.name) && course.prof.sameProf(c.prof)
        && course.students.ormap(new CheckID(c.students));
  }
}

//represent the class of SameID to check whether two students have same ID
class CheckID implements IFunc<Student, Boolean> {
  IList<Student> students;

  //the constructor
  CheckID(IList<Student> students) {
    this.students = students;
  }
  
  /*
   * Template: 
   * fields: 
   * this.students -- IList<Student>
   * methods:
   * this.apply(Student t) -- Boolean
   */

  public Boolean apply(Student t) {
    return this.students.ormap(new CheckIDSame(t));
  }
}

//represent a class of CheckIDSame to check whether these two student has same id number
class CheckIDSame implements IFunc<Student, Boolean> {
  Student s;

  //the constructor
  CheckIDSame(Student s) {
    this.s = s;
  }
  
  /*
   * Template: 
   * fields: 
   * this.student -- Student
   * methods:
   * this.apply(Student student) -- Boolean
   */

  public Boolean apply(Student student) {
    return student.id == s.id;
  }
}

//Interface for two-argument function-objects with signature [A1, A2 -> R]
interface IFunc2<A1, A2, R> {
  R apply(A1 arg1, A2 arg2);
}

//represent a Dejavu class to check how many courses are have same instructor
class Dejavu implements IFunc2<Course, Integer, Integer> {
  String name;

  //the constructor
  Dejavu(String name) {
    this.name = name;
  }
  
  /*
   * Template: 
   * fields: 
   * this.name -- String
   * methods:
   * this.apply(Course arg1, Integer arg2) -- Integer
   */


  public Integer apply(Course arg1, Integer arg2) {
    if (arg1.prof.samename(this.name)) {
      return arg2 + 1;
    }
    else {
      return arg2;
    }
  }
}

class ExampleRegistrars {
  Instructor prof1;
  Instructor prof2;
  Course c1;
  Course c2;
  Course c3;
  Course c4;
  Course c1D;
  Student s1;
  Student s2;
  Student s3;
  Student s4;
  Student s5;
  IList<Course> emptyCourse = new MtList<Course>();
  IList<Student> emptyStudent = new MtList<Student>();

  // EFFECT: Sets up the initial conditions for our tests, by re-initializing
  //prof1,prof2,c1,c2,c3,c4,c1D and all students
  void initTestConditions() {
    this.prof1 = new Instructor("Donald");
    this.prof2 = new Instructor("Mark");
    this.c1 = new Course("The Art of Computer Programming (volume 1)", prof1);
    this.c2 = new Course("The Art History of Renaissance", prof2);
    this.c3 = new Course("The Art History of Renaissance", prof1);
    this.c4 = new Course("The Music Psychology", prof2);
    this.c1D = new Course("The Art of Computer Programming (volume 1)", prof1);
    this.s1 = new Student("AStudent", 1);
    this.s2 = new Student("BStudent", 2);
    this.s3 = new Student("CStudent", 3);
    this.s4 = new Student("DStudent", 4);
    this.s5 = new Student("EStudent", 5);
  }
  
  //TEST OF THE HELPER METHOD
  
  // test for the method samename
  boolean testSamename(Tester t) {
    // 1. Set up the initial conditions
    this.initTestConditions();
    // 2. check the method
    return t.checkExpect(this.prof1.samename("Apple"), false)
        && t.checkExpect(this.prof1.samename(""), false)
        && t.checkExpect(this.prof1.samename("Donald"), true)
        && t.checkExpect(this.prof2.samename("Mark"), true)
        && t.checkExpect(this.prof2.samename("MArk"), false);
  }
  
  //test for sameProf
  boolean testSameProf(Tester t) {
    // 1. Set up the initial conditions
    this.initTestConditions();
    // 2. check the method
    return t.checkExpect(this.prof1.sameProf(this.prof1),true)
        && t.checkExpect(this.prof1.sameProf(this.prof2),false)
        && t.checkExpect(this.prof2.sameProf(new Instructor("MARK")),false)
        && t.checkExpect(this.prof2.sameProf(this.prof2),true);
  }
  
  // testing the method in CheckIDSame class
  boolean testCheckIDSame(Tester t) {
    // 1. Set up the initial conditions
    this.initTestConditions();
    // 2. check the method
    return t.checkExpect(new CheckIDSame(this.s1).apply(this.s1), true)
        && t.checkExpect(new CheckIDSame(this.s1).apply(new Student("AStudent", 6)), false)
        && t.checkExpect(new CheckIDSame(this.s3).apply(this.s4), false);   
  }
  
  // testing the method in CheckID class
  boolean testCheckID(Tester t) {
    // 1. Set up the initial conditions
    this.initTestConditions();
    // 2. check the method
    return t.checkExpect(new CheckID(this.emptyStudent).apply(this.s1), false)
        && t.checkExpect(new CheckID(
            new ConsList<Student>(this.s4, new ConsList<Student>(this.s1, this.emptyStudent)))
                .apply(new Student("AStudent", 6)),
            false)
        && t.checkExpect(
            new CheckID(new ConsList<Student>(this.s4, this.emptyStudent)).apply(this.s4), true);
  }  
  
  //testing the method in CheckSame class
  boolean testCheckSame(Tester t) {
    // 1. Set up the initial conditions
    this.initTestConditions();
    // 2. check the method
    return t.checkExpect(new CheckSame(this.c2).apply(this.c1), false)
        && t.checkExpect(new CheckSame(this.c1).apply(this.c1D), false)
        && t.checkExpect(new CheckSame(this.c3).apply(this.c3), true)
        && t.checkExpect(new CheckSame(this.c2).apply(this.c3), false);
 
  }

  // testing the method in SameCourse class
  boolean testSameCourse(Tester t) {
    // 1. Set up the initial conditions
    this.initTestConditions();
    // 2. check the method
    return t.checkExpect(new SameCourse(
        this.emptyCourse)
            .apply(this.c1),
        false)
        && t.checkExpect(
            new SameCourse(new ConsList<Course>(this.c1, this.emptyCourse)).apply(this.c1D), false)
        && t.checkExpect(new SameCourse(new ConsList<Course>(this.c1, this.emptyCourse))
            .apply(this.c1), true)
        && t.checkExpect(new SameCourse(new ConsList<Course>(this.c1D,
            new ConsList<Course>(this.c2, new ConsList<Course>(this.c1, this.emptyCourse))))
                .apply(this.c1D),
            true)
        && t.checkExpect(new SameCourse(
            new ConsList<Course>(this.c2, new ConsList<Course>(this.c1, this.emptyCourse)))
                .apply(this.c4),
            false);
  }

  //testing the method in Dejavu class
  boolean testDejavuClass(Tester t) {
    // 1. Set up the initial conditions
    this.initTestConditions();
    // 2. check the method
    return t.checkExpect(new Dejavu("Mark").apply(this.c2, 0),1)
        && t.checkExpect(new Dejavu("Donald").apply(this.c4, 0),0)
        && t.checkExpect(new Dejavu("MARK").apply(this.c4, 2),2)
        && t.checkExpect(new Dejavu("Donald").apply(this.c3, 1),2)
        && t.checkExpect(new Dejavu("DONAALD").apply(this.c1, 0),0);
  }
  
  // test for the ormap function
  boolean testOrmap(Tester t) {
    // 1. Set up the initial conditions
    this.initTestConditions();
    // 2. check the method
    return t.checkExpect(this.emptyCourse.ormap(new CheckSame(this.c2)), false)
        && t.checkExpect(
            new ConsList<Course>(this.c1, this.emptyCourse).ormap(new CheckSame(this.c2)), false)
        && t.checkExpect(
            new ConsList<Course>(this.c2, this.emptyCourse).ormap(new CheckSame(this.c2)), true)
        && t.checkExpect(this.emptyStudent.ormap(new CheckIDSame(this.s1)), false)
        && t.checkExpect(
            new ConsList<Student>(this.s1, this.emptyStudent).ormap(new CheckIDSame(this.s1)), true)
        && t.checkExpect(
            new ConsList<Student>(this.s1, new ConsList<Student>(this.s5, this.emptyStudent))
                .ormap(new CheckIDSame(this.s5)),
            true);
  }
  
  // test for the foldr function
  boolean testFoldr(Tester t) {
    // 1. Set up the initial conditions
    this.initTestConditions();
    // 2. check the method
    return t.checkExpect(this.emptyCourse.foldr(new Dejavu("Mark"),
        0), 0) && t
            .checkExpect(new ConsList<Course>(this.c1, this.emptyCourse).foldr(new Dejavu("Mark"),
                0), 0)
        && t.checkExpect(
            new ConsList<Course>(this.c2, this.emptyCourse).foldr(new Dejavu("Mark"), 0), 1)
        && t.checkExpect(
            new ConsList<Course>(this.c2, this.emptyCourse).foldr(new Dejavu("Donald"), 3), 3)
        && t.checkExpect((new ConsList<Course>(this.c4,
            (new ConsList<Course>(this.c3,
                (new ConsList<Course>(this.c2, new ConsList<Course>(this.c1, this.emptyCourse)))))))
                    .foldr(new Dejavu("Donald"), 0),
            2);
  }
  
  // test for addCourse
  boolean testAddCourse(Tester t) {
    // 1. Set up the initial conditions
    this.initTestConditions();
    // 2. Modify them
    this.prof1.addCourse(this.c1);
    this.prof1.addCourse(this.c3);
    this.prof2.addCourse(this.c2);

    // 3. Check that the expected changes have occurred
    return t.checkExpect(this.prof1.courses, new ConsList<Course>(this.c1, this.emptyCourse))
        && t.checkExpect(this.prof1.courses,
            new ConsList<Course>(this.c3, new ConsList<Course>(this.c1, this.emptyCourse)))
        && t.checkExpect(this.prof2.courses, new ConsList<Course>(this.c3, this.emptyCourse));
  }

  // test for addStudent
  boolean testAddSttudent(Tester t) {
    // 1. Set up the initial conditions
    this.initTestConditions();
    // 2. Modify them
    this.c1.addStudent(this.s1);
    this.c2.addStudent(this.s1);
    this.c3.addStudent(this.s3);
    this.c3.addStudent(this.s4);
    this.c3.addStudent(this.s5);
    this.c1D.addStudent(this.s2);

    // 3. Check that the expected changes have occurred
    return t.checkExpect(this.c1.students, new ConsList<Student>(this.s1, this.emptyStudent))
        && t.checkExpect(this.c2.students, new ConsList<Student>(this.s1, this.emptyStudent))
        && t.checkExpect(this.c3.students,
            new ConsList<Student>(this.s5,
                new ConsList<Student>(this.s4, new ConsList<Student>(this.s3, this.emptyStudent))))
        && t.checkExpect(this.c1D.students, new ConsList<Student>(this.s2, this.emptyStudent));
  }

  // test for enroll Method:
  boolean testEnroll(Tester t) {
    // 1. Set up the initial conditions
    this.initTestConditions();
    // 2. Modify them
    this.s1.enroll(this.c1);
    this.s2.enroll(this.c1);
    this.s1.enroll(this.c3);
    this.s3.enroll(this.c2);
    this.s4.enroll(this.c3);
    this.s5.enroll(this.c4);

    // 3. Check that the expected changes have occurred
    return t.checkExpect(this.s1.courses,
        new ConsList<Course>(this.c3, new ConsList<Course>(this.c1, this.emptyCourse)))
        && t.checkExpect(this.s2.courses, new ConsList<Course>(this.c1, this.emptyCourse))
        && t.checkExpect(this.c4.students, new ConsList<Student>(this.s5, this.emptyStudent))
        && t.checkExpect(this.c3.students,
            new ConsList<Student>(this.s4, new ConsList<Student>(this.s1, this.emptyStudent)))
        && t.checkExpect(this.c2.students, new ConsList<Student>(this.s3, this.emptyStudent))
        && t.checkExpect(this.c1.students,
            new ConsList<Student>(this.s2, new ConsList<Student>(this.s1, this.emptyStudent)));
  }

  // test for classmates Method:
  boolean testClassmates(Tester t) {
    // 1. Set up the initial conditions
    this.initTestConditions();
    // 2. Modify them
    this.s1.enroll(this.c1);
    this.s2.enroll(this.c1);
    this.s3.enroll(this.c2);
    this.s4.enroll(this.c3);
    this.s4.enroll(this.c1D);
    this.s5.enroll(this.c1);
    this.s5.enroll(this.c2);
    this.s5.enroll(this.c1D);
    // 3. Check that the expected changes have occurred
    return // check the enroll methods of student
    t.checkExpect(this.s1.courses, new ConsList<Course>(this.c1, this.emptyCourse))
        && t.checkExpect(this.s2.courses, new ConsList<Course>(this.c1, this.emptyCourse))
        && t.checkExpect(this.s3.courses, new ConsList<Course>(this.c2, this.emptyCourse))
        && t.checkExpect(this.s4.courses,
            new ConsList<Course>(this.c1D, new ConsList<Course>(this.c3, this.emptyCourse)))
        && t.checkExpect(this.s5.courses, new ConsList<Course>(this.c1D,
            new ConsList<Course>(this.c2, new ConsList<Course>(this.c1, this.emptyCourse))))
        // check whether the course students field update
        && t.checkExpect(this.c3.students, new ConsList<Student>(this.s4, this.emptyStudent))
        && t.checkExpect(this.c2.students,
            new ConsList<Student>(this.s5, new ConsList<Student>(this.s3, this.emptyStudent)))
        && t.checkExpect(this.c1.students,
            new ConsList<Student>(this.s5,
                new ConsList<Student>(this.s2, new ConsList<Student>(this.s1, this.emptyStudent))))
        && t.checkExpect(this.c1D.students,
            new ConsList<Student>(this.s5, new ConsList<Student>(this.s4, this.emptyStudent)))
        // check the classmates method
        && t.checkExpect(this.s1.classmates(s2), true)
        && t.checkExpect(this.s1.classmates(s5), true)
        && t.checkExpect(this.s2.classmates(s1), true)
        && t.checkExpect(this.s2.classmates(s3), false)
        && t.checkExpect(this.s3.classmates(s1), false)
        && t.checkExpect(this.s4.classmates(s3), false)
        && t.checkExpect(this.s4.classmates(s5), true)
        && t.checkExpect(this.s1.classmates(s4), true);
  }

  // test for dejavu Method:
  boolean testDejavu(Tester t) {
    // 1. Set up the initial conditions
    this.initTestConditions();
    // 2. Modify them
    this.s1.enroll(this.c1);
    this.s1.enroll(this.c3);
    this.s2.enroll(this.c2);
    this.s2.enroll(this.c4);
    this.s5.enroll(this.c1);
    this.s5.enroll(this.c2);
    this.s5.enroll(this.c3);
    this.s5.enroll(this.c4);
    // 3. Check that the expected changes have occurred
    return // check the enroll methods of student
    t.checkExpect(this.s1.courses,
        new ConsList<Course>(this.c3, new ConsList<Course>(this.c1, this.emptyCourse)))
        && t.checkExpect(this.s2.courses,
            new ConsList<Course>(this.c4, new ConsList<Course>(this.c2, this.emptyCourse)))
        && t.checkExpect(this.s5.courses,
            new ConsList<Course>(this.c4, new ConsList<Course>(this.c3,
                new ConsList<Course>(this.c2, new ConsList<Course>(this.c1, this.emptyCourse)))))
        // check whether the course students field update
        && t.checkExpect(this.c4.students,
            new ConsList<Student>(this.s5, new ConsList<Student>(this.s2, this.emptyStudent)))
        && t.checkExpect(this.c3.students,
            new ConsList<Student>(this.s5, new ConsList<Student>(this.s1, this.emptyStudent)))
        && t.checkExpect(this.c2.students,
            new ConsList<Student>(this.s5, new ConsList<Student>(this.s2, this.emptyStudent)))
        && t.checkExpect(this.c1.students,
            new ConsList<Student>(this.s5, new ConsList<Student>(this.s1, this.emptyStudent)))
        // check the dejavu method
        && t.checkExpect(this.prof1.dejavu(s1), true)
        && t.checkExpect(this.prof1.dejavu(s2), false) 
        && t.checkExpect(this.prof1.dejavu(s5), true)
        && t.checkExpect(this.prof2.dejavu(s2), true) 
        && t.checkExpect(this.prof2.dejavu(s3), false)
        && t.checkExpect(this.prof2.dejavu(s5), true)
        // test the dejavuhelp method
        && t.checkExpect(this.s1.dejavuHelp("Donald"), 2)
        && t.checkExpect(this.s2.dejavuHelp("Donald"), 0) 
        && t.checkExpect(s5.dejavuHelp("Donald"), 2)
        && t.checkExpect(this.s2.dejavuHelp("Mark"), 2) 
        && t.checkExpect(this.s3.dejavuHelp("Mark"), 0)
        && t.checkExpect(this.s5.dejavuHelp("Mark"), 2); 
  }
}