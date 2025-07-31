
# 개요

- 디버깅은 프로그램의 흐름을 한 줄씩 따라가면서 **오류를 찾거나 로직을 확인**할 수 있는 도구

# 방법
## ✅ 1. **중단점(Breakpoint)** 설정

- 코드 왼쪽 **줄 번호 영역을 클릭** (=> 중단점)

- 중단점을 걸면 **그 줄에서 프로그램이 멈춤**.


## ✅ 2. **디버그 실행**

- `Shift + F9` 또는 상단 메뉴에서 ▶ 옆에 벌레 🐞모양 아이콘 클릭!
    
- 또는 `오른쪽 클릭 → Debug 'Main'` 선택
    
- 프로그램이 중단점에서 멈춤!

## ✅ 3. **디버그 창 확인 (하단 Debug 탭)**

|패널|설명|
|---|---|
|**Variables**|현재 줄까지의 변수 값들을 보여줌|
|**Watches**|내가 직접 감시하고 싶은 변수 추가 가능|
|**Call Stack**|어떤 함수들이 호출되었는지 순서대로 보여줌|

## ✅ 4. **중단점 해제 또는 삭제**

- 빨간 점 클릭하면 제거
    
- 또는 `Ctrl + Shift + F8` 누르면 **모든 중단점 목록**이 뜨고 삭제 가능



```java
public class DebugExample {
    public static void main(String[] args) {
        int a = 10;
        int b = 5;
        int sum = a + b; // 여기에 중단점 걸기
        int product = a * b;
        System.out.println("합: " + sum);
        System.out.println("곱: " + product);
    }
}
```



---

## 단축키

|단축키|이름|설명|
|---|---|---|
|`F8`|Step Over|현재 줄 실행 → 다음 줄로 넘어감 (함수는 건너뜀)|
|`F7`|Step Into|함수 안으로 들어감 (내부 구현 확인 가능)|
|`Shift + F8`|Step Out|현재 함수 빠져나오기|
|`Alt + F9`|Run to Cursor|커서 위치까지 실행|
|`Ctrl + F8`|Toggle Breakpoint|중단점 추가/삭제|
|`Resume (F9)`|계속 실행|다음 중단점까지 실행|