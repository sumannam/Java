
# 📚 도서관 좌석 예약 시스템

## 🧩 기능 요구사항

1. 👤 사용자에게 **좌석의 행(row) 수**와 **열(column) 수**를 입력받는다.
    
2. 🪑 전체 좌석을 2차원 배열로 표현한다. (`0 = 비어있음`, `1 = 예약됨`)
    
3. 🙋 사용자에게 좌석 번호를 입력받아 **예약 처리**한다.
    
4. ⛔ 이미 예약된 좌석은 예약할 수 없다.
    
5. 🖨️ 예약이 완료된 후 전체 좌석 배치를 출력한다.
    

---

## ⌨️ 입력 요구사항

1. ✅ 사용자에게 행 수, 열 수를 입력받는다.
    
2. 🎯 예약할 좌석의 **행 번호와 열 번호**를 입력받는다.
    
3. 🔁 좌석 예약은 여러 번 반복할 수 있으며, `"0 0"` 입력 시 종료한다.
    

---

## ⚙️ 처리 요구사항

1. 🧮 2차원 배열 `int[][] seats`를 사용한다.
    
2. 🔁 `while` 반복문으로 좌석을 계속 예약받는다.
    
3. ❗ 예약된 좌석은 다시 예약할 수 없으며, 경고 메시지를 출력한다.
    

---

## 📤 출력 요구사항

1. 🖨️ 모든 예약이 끝나면, 현재 좌석 상태를 **표 형식**으로 출력한다.
    
    - 예: `0 1 0 1` (0 = 비어있음, 1 = 예약됨)
        

---

## 💻 전체 예제 코드

```java
import java.util.Scanner;

public class LibrarySeatReservation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. 좌석 배열 크기 설정
        System.out.print("좌석의 행 수를 입력하세요: ");
        int rows = scanner.nextInt();

        System.out.print("좌석의 열 수를 입력하세요: ");
        int cols = scanner.nextInt();

        int[][] seats = new int[rows][cols]; // 0: 비어있음, 1: 예약됨

        // 2. 예약 루프
        while (true) {
            System.out.println("\n좌석 예약을 진행하세요. (종료하려면 0 0 입력)");

            System.out.print("예약할 좌석의 행 번호 입력 (1~" + rows + "): ");
            int r = scanner.nextInt();
            System.out.print("예약할 좌석의 열 번호 입력 (1~" + cols + "): ");
            int c = scanner.nextInt();

            // 종료 조건
            if (r == 0 && c == 0) {
                break;
            }

            // 유효성 검사
            if (r < 1 || r > rows || c < 1 || c > cols) {
                System.out.println("⚠️ 잘못된 좌석 번호입니다. 다시 입력하세요.");
                continue;
            }

            // 인덱스 변환
            int rowIndex = r - 1;
            int colIndex = c - 1;

            // 이미 예약된 좌석인지 확인
            if (seats[rowIndex][colIndex] == 1) {
                System.out.println("❌ 이미 예약된 좌석입니다!");
            } else {
                seats[rowIndex][colIndex] = 1;
                System.out.println("✅ 좌석 예약이 완료되었습니다.");
            }
        }

        // 3. 좌석 현황 출력
        System.out.println("\n--- 현재 좌석 배치도 (0:비어있음, 1:예약됨) ---");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(seats[i][j] + " ");
            }
            System.out.println();
        }
    }
}
```
