
```java
import java.util.Scanner;

public class CoffeePriceCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("음료를 선택하세요: 1-아메리카노, 2-카페라떼, 3-카푸치노");
        int drinkType = scanner.nextInt();
        
        System.out.println("크기를 선택하세요: 1-Small, 2-Medium, 3-Large");
        int size = scanner.nextInt();
        
        System.out.println("옵션을 선택하세요: 1-기본, 2-샷추가, 3-시럽추가, 4-샷+시럽추가");
        int option = scanner.nextInt();
        
        int basePrice = 0;
        
        // 음료 종류에 따른 기본 가격 설정
        if (drinkType == 1) {
            basePrice = 3000; // 아메리카노
        } else if (drinkType == 2) {
            basePrice = 4000; // 카페라떼
        } else if (drinkType == 3) {
            basePrice = 4500; // 카푸치노
        } else {
            System.out.println("잘못된 음료 선택입니다.");
            return;
        }
        
        // 크기에 따른 가격 조정
        if (size == 2) {
            basePrice += 500; // Medium 사이즈는 500원 추가
        } else if (size == 3) {
            basePrice += 1000; // Large 사이즈는 1000원 추가
        }
        
        // 옵션에 따른 가격 조정
        if (option == 2) {
            basePrice += 500; // 샷 추가는 500원
        } else if (option == 3) {
            basePrice += 300; // 시럽 추가는 300원
        } else if (option == 4) {
            basePrice += 800; // 샷과 시럽 모두 추가는 800원
        }
        
        System.out.println("최종 가격은 " + basePrice + "원입니다.");
        scanner.close();
    }
}
```


# 