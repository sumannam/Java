
## 📦 1. 1차원 배열 (1차 배열)

### ✅ 정의

- **같은 자료형의 데이터 여러 개를 한 줄로 저장**하는 구조이다.
    
- **크기가 고정**되어 있다! 즉, 처음 만들 때 몇 칸인지 정해야 한다.
    

### ✅ 예시

```java
int[] scores = new int[5]; // 정수 5개를 저장할 수 있는 배열
```

또는

```java
int[] scores = {90, 80, 70, 60, 50};
```


### ✅ 특징

- 인덱스(index)를 통해 값을 저장하거나 불러올 수 있다.
    인덱스는 0부터 시작한다.

```java
System.out.println(scores[0]); // 90 출력
```

## ✅ 1차 배열 예제

```java
public class OneDimensionalArrayExample {
    public static void main(String[] args) {
        // 정수형 1차 배열 선언 및 초기화
        int[] scores = {90, 80, 70, 60, 50};

        // 배열 요소 출력
        System.out.println("1차 배열 요소 출력:");
        for (int i = 0; i < scores.length; i++) {
            System.out.println("scores[" + i + "] = " + scores[i]);
        }
    }
}

```


---

## 🧊 2. 2차원 배열 (2차 배열)

### ✅ 정의

- **배열 안에 배열이 들어있는 구조**, 즉 **행(row)**과 **열(column)**이 있는 표 형태이다.
    
- 수학에서의 행렬(matrix)과 비슷하다.
    

### ✅ 예시

```java
int[][] matrix = {   {1, 2, 3},   {4, 5, 6} };
```


> 이 배열은 2행 3열이야.

### ✅ 특징

- 인덱스 2개를 사용해서 값을 다뤄야 한다.
    
```java
System.out.println(matrix[0][1]); // 2 출력 
System.out.println(matrix[1][2]); // 6 출력
```

## ✅ 2차 배열 예제

```java
public class TwoDimensionalArrayExample {
    public static void main(String[] args) {
        // 2행 3열의 정수형 2차 배열 선언 및 초기화
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6}
        };

        // 2차 배열 요소 출력
        System.out.println("2차 배열 요소 출력:");
        for (int i = 0; i < matrix.length; i++) { // 행 반복
            for (int j = 0; j < matrix[i].length; j++) { // 열 반복
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println(); // 줄 바꿈
        }
    }
}
```


---

## 🧩 3. ArrayList (어레이리스트)

### ✅ 정의

- 자바에서 제공하는 **가변 크기 배열**이다.
    
- 배열과 다르게 **크기를 미리 정하지 않아도 된다.**
    
- 객체로 관리되며 **기본 자료형은 못 쓰고, 래퍼 클래스**를 사용해야 한다.(예: `int` → `Integer`)
    

### ✅ 예시

```java
import java.util.ArrayList;  
ArrayList<String> fruits = new ArrayList<>(); 
fruits.add("사과"); 
fruits.add("바나나"); 
fruits.add("포도");
```

### ✅ 특징

- 값을 추가할 때 `.add()`, 삭제할 때 `.remove()`를 사용해야 한다.
    
- 배열보다 유연하게 사용할 수 있다.
    
```java
System.out.println(fruits.get(1)); // 바나나 출력 
fruits.remove(0); // 사과 삭제
```

## ✅ ArrayList 예제


```java
import java.util.ArrayList;

public class ArrayListExample {
    public static void main(String[] args) {
        // 문자열을 저장하는 ArrayList 생성
        ArrayList<String> fruits = new ArrayList<>();

        // 값 추가
        fruits.add("사과");
        fruits.add("바나나");
        fruits.add("포도");

        // 리스트 출력
        System.out.println("ArrayList 요소 출력:");
        for (int i = 0; i < fruits.size(); i++) {
            System.out.println("fruits[" + i + "] = " + fruits.get(i));
        }

        // 요소 삭제
        fruits.remove(0); // 인덱스 0인 "사과" 삭제

        // 삭제 후 출력
        System.out.println("\n삭제 후 ArrayList:");
        for (String fruit : fruits) {
            System.out.println(fruit);
        }
    }
}
```


---

## 🔍 비교 요약표

|구분|1차 배열|2차 배열|ArrayList|
|---|---|---|---|
|구조|한 줄로 된 값들|행과 열이 있는 표|유동적인 크기의 리스트|
|크기|고정|고정|가변|
|선언 방식|`int[] arr`|`int[][] arr`|`ArrayList<T> arr`|
|사용 예시|`arr[0]`|`arr[1][2]`|`arr.get(0)`|
|자료형 제한|기본형 가능|기본형 가능|객체형(래퍼 클래스)만 가능|


---
# 응용

> [!NOTE] 응용 기술
> `ArrayList` 안에 **배열(Array)을 넣는 방법**

## ✅ 목표

> ArrayList에 배열을 넣고, 저장된 배열들을 순회하며 값 출력하기

---

## 🔸 예제 1: `ArrayList<int[]>` – 정수 배열 넣기

```java
import java.util.ArrayList;

public class ArrayListOfStringArrayExample {
    public static void main(String[] args) {
        // 문자열 배열을 담는 ArrayList 생성
        ArrayList<String[]> studentGroups = new ArrayList<>();

        // 각 그룹마다 3명의 학생
        String[] group1 = {"Alice", "Bob", "Charlie"};
        String[] group2 = {"David", "Eva", "Frank"};

        // 리스트에 배열 추가
        studentGroups.add(group1);
        studentGroups.add(group2);

        // 출력
        System.out.println("ArrayList<String[]> 안의 학생 그룹:");
        for (int i = 0; i < studentGroups.size(); i++) {
            System.out.println("그룹 " + (i + 1) + ":");
            for (String name : studentGroups.get(i)) {
                System.out.println(" - " + name);
            }
        }
    }
}

```

---
## 🔸 예제 2: `ArrayList<String[]>` – 문자열 배열 넣기

```java
import java.util.ArrayList;
import java.util.Arrays;

public class AdvancedArrayListExample {
    // 배열을 받아 ArrayList에 저장하는 메서드
    public static void addRow(ArrayList<int[]> list, int... values) {
        list.add(values);
    }

    public static void main(String[] args) {
        ArrayList<int[]> matrix = new ArrayList<>();

        // 가변 인자를 배열로 저장
        addRow(matrix, 10, 20, 30);
        addRow(matrix, 40, 50, 60);

        // 출력
        System.out.println("사용자 정의 메서드로 추가된 배열:");
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
}

```
