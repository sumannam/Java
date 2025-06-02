
- [Class and Object_1](../Week_12/Class%20and%20Object_1.pdf)
- [Class and Object_2](../Week_12/Class%20and%20Object_2.pdf)

# [로직트리](로직트리.md)


# 예시


## 절차1

```java
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // 1. 학생 정보 저장용 배열
        String[] student = new String[5]; // 0: id, 1: name, 2: status, 3: birthDate, 4: contact

        // 2. 수강 과목 리스트
        ArrayList<String> enrolledCourses = new ArrayList<>();

        // 3. 초기 정보 입력
        student[0] = "20230001";              // id
        student[1] = "홍길동";                 // name
        student[2] = "재학";                  // status
        student[3] = "2001-05-10";            // birthDate
        student[4] = "010-1234-5678";         // contact

        // 4. 수강 과목 추가
        enrolledCourses.add("자료구조");
        enrolledCourses.add("운영체제");
        enrolledCourses.add("컴퓨터 네트워크");

        // 5. 수강 과목 삭제 예시
        enrolledCourses.remove("운영체제");

        // 6. 학생 정보 출력
        System.out.println("ID: " + student[0]);
        System.out.println("이름: " + student[1]);
        System.out.println("상태: " + student[2]);
        System.out.println("생년월일: " + student[3]);
        System.out.println("연락처: " + student[4]);

        // 7. 수강 과목 출력 (for문 사용)
        System.out.println("수강 교과목 목록:");
        if (enrolledCourses.isEmpty()) {
            System.out.println("없음");
        } else {
            for (int i = 0; i < enrolledCourses.size(); i++) {
                System.out.println("- " + enrolledCourses.get(i));
            }
        }
    }
}
```

---

## 절차2

```java
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // 1. 학생 3명의 기본 정보 저장 배열
        String[][] students = new String[3][5]; // [학생번호][0:id, 1:name, 2:status, 3:birthDate, 4:contact]

        // 2. 각 학생의 수강 과목 목록
        ArrayList<String>[] courseLists = new ArrayList[3];
        for (int i = 0; i < 3; i++) {
            courseLists[i] = new ArrayList<>();
        }

        // 3. 학생 정보 초기화
        students[0][0] = "20230001"; students[0][1] = "홍길동"; students[0][2] = "재학"; students[0][3] = "2001-05-10"; students[0][4] = "010-1234-5678";
        students[1][0] = "20230002"; students[1][1] = "김영희"; students[1][2] = "휴학"; students[1][3] = "2000-03-22"; students[1][4] = "010-2345-6789";
        students[2][0] = "20230003"; students[2][1] = "이철수"; students[2][2] = "재학"; students[2][3] = "2002-11-15"; students[2][4] = "010-3456-7890";

        // 4. 수강 과목 입력
        courseLists[0].add("자료구조");
        courseLists[0].add("운영체제");

        courseLists[1].add("데이터베이스");

        courseLists[2].add("컴퓨터 네트워크");
        courseLists[2].add("자료구조");
        courseLists[2].add("알고리즘");

        // 5. 전체 출력
        for (int i = 0; i < 3; i++) {
            System.out.println("====== 학생 " + (i + 1) + " 정보 ======");
            System.out.println("ID: " + students[i][0]);
            System.out.println("이름: " + students[i][1]);
            System.out.println("상태: " + students[i][2]);
            System.out.println("생년월일: " + students[i][3]);
            System.out.println("연락처: " + students[i][4]);

            System.out.println("수강 교과목 목록:");
            if (courseLists[i].isEmpty()) {
                System.out.println("없음");
            } else {
                for (String course : courseLists[i]) {
                    System.out.println("- " + course);
                }
            }
            System.out.println();
        }
    }
}
```

- 문제점
	- 소스 코드가 반복되는 부분이 있다.

---

## 절차3

```java
import java.util.ArrayList;

public class Main {

    // 전역 변수 선언
    static String[][] students = new String[3][5];
    static ArrayList<String>[] courseLists = new ArrayList[3];

    public static void main(String[] args) {
        // courseLists 배열 초기화
        for (int i = 0; i < 3; i++) {
            courseLists[i] = new ArrayList<>();
        }

        // 👇 학생 정보 설정
        setStudent(0, "20230001", "홍길동", "재학", "2001-05-10", "010-1234-5678");
        setStudent(1, "20230002", "김영희", "휴학", "2000-03-22", "010-2345-6789");
        setStudent(2, "20230003", "이철수", "재학", "2002-11-15", "010-3456-7890");

        // 👇 과목 설정 (학생 인덱스와 과목명 전달)
        addCourse(0, "자료구조");
        addCourse(0, "운영체제");

        addCourse(1, "데이터베이스");

        addCourse(2, "컴퓨터 네트워크");
        addCourse(2, "자료구조");
        addCourse(2, "알고리즘");

        // 전체 출력
        printAllStudents();
    }

    // ✅ 학생 정보를 저장하는 메서드
    public static void setStudent(int index, String id, String name, String status, String birthDate, String contact) {
        students[index][0] = id;
        students[index][1] = name;
        students[index][2] = status;
        students[index][3] = birthDate;
        students[index][4] = contact;
    }

    // ✅ 수강 과목을 추가하는 메서드
    public static void addCourse(int studentIndex, String courseName) {
        courseLists[studentIndex].add(courseName);
    }

    // 전체 학생 출력
    public static void printAllStudents() {
        for (int i = 0; i < students.length; i++) {
            printStudentInfo(i);
            System.out.println();
        }
    }

    // 단일 학생 정보 출력
    public static void printStudentInfo(int index) {
        System.out.println("====== 학생 " + (index + 1) + " 정보 ======");
        System.out.println("ID: " + students[index][0]);
        System.out.println("이름: " + students[index][1]);
        System.out.println("상태: " + students[index][2]);
        System.out.println("생년월일: " + students[index][3]);
        System.out.println("연락처: " + students[index][4]);

        System.out.println("수강 교과목 목록:");
        if (courseLists[index].isEmpty()) {
            System.out.println("없음");
        } else {
            for (String course : courseLists[index]) {
                System.out.println("- " + course);
            }
        }
    }
}

```

- 문제점
	- 캡슐화(그룹핑)
	- 정보은닉

---
## 절차4

### Student

```java
import java.util.ArrayList;

class Student {
    private String id;
    private String name;
    private String status;
    private String birthDate;
    private String contact;
    private ArrayList<String> courses;

    public Student(String id, String name, String status, String birthDate, String contact) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.birthDate = birthDate;
        this.contact = contact;
        this.courses = new ArrayList<>();
    }

    public void addCourse(String course) {
        courses.add(course);
    }

    public void printInfo() {
        System.out.println("ID: " + id);
        System.out.println("이름: " + name);
        System.out.println("상태: " + status);
        System.out.println("생년월일: " + birthDate);
        System.out.println("연락처: " + contact);

        System.out.println("수강 교과목 목록:");
        if (courses.isEmpty()) {
            System.out.println("없음");
        } else {
            for (String course : courses) {
                System.out.println("- " + course);
            }
        }
    }
}
```

### Main

```java
public class Main {
    public static void main(String[] args) {
        // 학생 객체 3명 생성
        Student[] students = new Student[3];

        students[0] = new Student("20230001", "홍길동", "재학", "2001-05-10", "010-1234-5678");
        students[1] = new Student("20230002", "김영희", "휴학", "2000-03-22", "010-2345-6789");
        students[2] = new Student("20230003", "이철수", "재학", "2002-11-15", "010-3456-7890");

        // 수강 과목 등록
        students[0].addCourse("자료구조");
        students[0].addCourse("운영체제");

        students[1].addCourse("데이터베이스");

        students[2].addCourse("컴퓨터 네트워크");
        students[2].addCourse("자료구조");
        students[2].addCourse("알고리즘");

        // 전체 정보 출력
        for (int i = 0; i < students.length; i++) {
            System.out.println("====== 학생 " + (i + 1) + " 정보 ======");
            students[i].printInfo();
            System.out.println();
        }
    }
}
```

- 교수와 교직원 클래스가 포함될 경우,
	- Student 클래스의 재사용성을 높여야 한다.


---

## 절차5

- 학교 인적 정보 관리를 위한 프로그램 개발

### 클래스 구조

```
Person (부모 클래스)
├── Professor
├── Student
└── Staff
```

### 소스 코드

### Person

```java
public class Person {
    protected String id;
    protected String name;
    protected String status;
    protected String birthDate;
    protected String contact;

    public Person(String id, String name, String status, String birthDate, String contact) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.birthDate = birthDate;
        this.contact = contact;
    }

    public void printInfo() {
        System.out.println("ID: " + id);
        System.out.println("이름: " + name);
        System.out.println("상태: " + status);
        System.out.println("생년월일: " + birthDate);
        System.out.println("연락처: " + contact);
    }
}
```

### Student

```java
import java.util.ArrayList;

public class Student extends Person {
    private ArrayList<String> courses;

    public Student(String id, String name, String status, String birthDate, String contact) {
        super(id, name, status, birthDate, contact);
        this.courses = new ArrayList<>();
    }

    public void addCourse(String course) {
        courses.add(course);
    }

    public ArrayList<String> getCourses() {
        return courses;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("수강 교과목 목록:");
        if (courses.isEmpty()) {
            System.out.println("없음");
        } else {
            for (String course : courses) {
                System.out.println("- " + course);
            }
        }
    }
}
```

### Perfessor

```java
import java.util.ArrayList;

public class Professor extends Person {
    private ArrayList<String> lectures;

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
        System.out.println("담당 강의 목록:");
        if (lectures.isEmpty()) {
            System.out.println("없음");
        } else {
            for (String lecture : lectures) {
                System.out.println("- " + lecture);
            }
        }
    }
}
```

### Staff

```java
public class Staff extends Person {
    private String department;

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

### Main

```java
public class Main {
    public static void main(String[] args) {
        // 학생 1명
        Student student = new Student("20230001", "홍길동", "재학", "2001-05-10", "010-1234-5678");
        student.addCourse("자료구조");
        student.addCourse("운영체제");

        // 교수 1명
        Professor prof = new Professor("P001", "최교수", "재직", "1975-04-03", "010-9999-8888");
        prof.addLecture("컴파일러");
        prof.addLecture("알고리즘");

        // 직원 1명
        Staff staff = new Staff("S001", "박행정", "재직", "1985-07-12", "010-7777-6666");
        staff.setDepartment("학사관리팀");

        // 출력
        System.out.println("=== 학생 정보 ===");
        student.printInfo();

        System.out.println("\n=== 교수 정보 ===");
        prof.printInfo();

        System.out.println("\n=== 직원 정보 ===");
        staff.printInfo();
    }
}
```
