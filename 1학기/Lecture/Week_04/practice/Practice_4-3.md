
# # (실습문제3) 형변환하는 프로그램 개발

- 아래 소스 코드를 통해 형변환을 실행한 후 정답 이미지와 그 이유를 각각 작성하시오.

```java
public class TypeCastingCalculator {
    public static void main(String[] args) {
        // 주어진 값들
        double doubleValue = 2.71828;
        
        // double에서 다른 타입으로의 형변환 결과 출력
        System.out.println("\n=== double 형변환 ===");
        System.out.println("1. double -> byte: " + (byte)doubleValue);
        System.out.println("2. double -> short: " + (short)doubleValue);
        System.out.println("3. double -> int: " + (int)doubleValue);
        System.out.println("4. double -> long: " + (long)doubleValue);
        System.out.println("5. double -> float: " + (float)doubleValue);
    }
}
```

# [실습문제 제출](../../../Notice/실습문제%20제출.md)

- 이미지 : IntelliJ에서 실행결과 캡쳐화면 제출
- 비고 : 위 1-5번의 결과에 대한 이유 작성