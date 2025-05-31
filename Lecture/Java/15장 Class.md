
- [Class and Object_1](../Week_12/Class%20and%20Object_1.pdf)
- [Class and Object_2](../Week_12/Class%20and%20Object_2.pdf)

# [로직트리](로직트리.md)


# 예시

## 순서1


```java
import java.util.ArrayList;

public class Student {
    private String id;
    private String name;
    private String status;      // 예: "휴학", "재학" 등
    private String birthDate;   // 생년월일 (예: 2001-05-10)
    private String contact;     // 연락처
    private ArrayList<String> enrolledCourses;  // 수강 교과목 목록

    // 생성자
    public Student(String id, String name, String status, String birthDate, String contact) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.birthDate = birthDate;
        this.contact = contact;
        this.enrolledCourses = new ArrayList<>();
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public void addCourse(String courseName) {
        enrolledCourses.add(courseName);
    }

    public void removeCourse(String courseName) {
        enrolledCourses.remove(courseName);
    }

    public ArrayList<String> getCourses() {
        return enrolledCourses;
    }

    // 전체 정보 출력 메소드
    public void printInfo() {
        System.out.println("ID: " + id);
        System.out.println("이름: " + name);
        System.out.println("상태: " + status);
        System.out.println("생년월일: " + birthDate);
        System.out.println("연락처: " + contact);
        System.out.println("수강 교과목 목록: " + (enrolledCourses.isEmpty() ? "없음" : String.join(", ", enrolledCourses)));
    }
}


```



## 순서2

```
Person (부모 클래스)
├── Professor
├── Student
└── Staff
```

## Person

```java
// 부모 클래스: Person
public class Person {
    private String id;
    private String name;
    private String status;      // 예: "휴학", "휴직", "재직", "재학"
    private String birthDate;   // 생년월일 (예: 2001-05-10)
    private String contact;     // 연락처

    // 생성자
    public Person(String id, String name, String status, String birthDate, String contact) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.birthDate = birthDate;
        this.contact = contact;
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    // 전체 정보 출력 메소드
    public void printInfo() {
        System.out.println("ID: " + id);
        System.out.println("이름: " + name);
        System.out.println("상태: " + status);
        System.out.println("생년월일: " + birthDate);
        System.out.println("연락처: " + contact);
    }
}

```

## Professor

```java
import java.util.ArrayList;

public class Professor extends Person {
    private ArrayList<String> lectures;  // 수강 강의 목록

    public Professor(String id, String name, String status, String birthDate, String contact) {
        super(id, name, status, birthDate, contact);
        this.lectures = new ArrayList<>();
    }

    public void addLecture(String lectureName) {
        lectures.add(lectureName);
    }

    public void removeLecture(String lectureName) {
        lectures.remove(lectureName);
    }

    public ArrayList<String> getLectures() {
        return lectures;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("담당 강의 목록: " + (lectures.isEmpty() ? "없음" : String.join(", ", lectures)));
    }
}
```

## Student


```java
import java.util.ArrayList;

public class Student extends Person {
    private ArrayList<String> enrolledCourses;  // 수강 교과목 목록

    public Student(String id, String name, String status, String birthDate, String contact) {
        super(id, name, status, birthDate, contact);
        this.enrolledCourses = new ArrayList<>();
    }

    public void addCourse(String courseName) {
        enrolledCourses.add(courseName);
    }

    public void removeCourse(String courseName) {
        enrolledCourses.remove(courseName);
    }

    public ArrayList<String> getCourses() {
        return enrolledCourses;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("수강 교과목 목록: " + (enrolledCourses.isEmpty() ? "없음" : String.join(", ", enrolledCourses)));
    }
}

```

## Staff


```java
public class Staff extends Person {
    private String department;  // 담당 부서

    public Staff(String id, String name, String status, String birthDate, String contact) {
        super(id, name, status, birthDate, contact);
    }

    public void setDepartment(String dept) {
        this.department = dept;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("담당 부서: " + (department == null ? "미지정" : department));
    }
}

```
