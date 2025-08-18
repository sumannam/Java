class Calculator {
    // 멤버 변수 선언
    protected int num1;
    protected int num2;
    protected int result; // 연산 결과를 저장할 멤버 변수

    // Setter 메서드 (선택적으로 생성자를 사용할 수도 있습니다)
    public void setNumbers(int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    public int add() { // 멤버 변수를 사용하므로 인자를 제거
        result = num1 + num2;
        return result;
    }

    public int subtract() { // 멤버 변수를 사용하므로 인자를 제거
        result = num1 - num2;
        return result;
    }

    // multiply: add 메서드를 루프를 사용해서 구현 (부호 결정 로직 제거)
    public int multiply() { // 멤버 변수를 사용하므로 인자를 제거
        if (num1 == 0 || num2 == 0) {
            result = 0;
            return result;
        }

        int tempResult = 0; // 지역 변수
        // num2만큼 num1을 더합니다. (부호에 관계없이 그대로 더함)
        for (int i = 0; i < num2; i++) {
            // add 메서드는 더 이상 인자를 받지 않고 멤버 변수를 사용하므로,
            // 이 곳에서는 직접 덧셈을 수행하거나 add를 호출하기 위한 다른 접근이 필요합니다.
            // 여기서는 루프를 위해 직접 덧셈 로직을 사용하겠습니다.
            tempResult += num1;
        }
        result = tempResult; // 최종 결과를 멤버 변수에 저장
        return result;
    }

    // divide: subtract 메서드를 루프를 사용해서 구현 (부호 결정 로직 제거)
    public int divide() { // 멤버 변수를 사용하므로 인자를 제거
        if (num2 == 0) {
            System.out.println("0으로 나눌 수 없습니다.");
            result = Integer.MAX_VALUE; // 에러를 나타내는 값
            return result;
        }
        if (num1 == 0) {
            result = 0;
            return result;
        }

        int quotient = 0;
        int currentNum1 = num1; // 루프 내에서 num1의 값을 변경해야 하므로 지역 변수 사용
        // num1에서 num2를 반복적으로 뺍니다. (부호에 관계없이 그대로 뺌)
        while (currentNum1 >= num2) {
            // subtract 메서드는 더 이상 인자를 받지 않고 멤버 변수를 사용하므로,
            // 이 곳에서는 직접 뺄셈을 수행하거나 subtract를 호출하기 위한 다른 접근이 필요합니다.
            // 여기서는 루프를 위해 직접 뺄셈 로직을 사용하겠습니다.
            currentNum1 -= num2;
            quotient++;
        }
        result = quotient; // 최종 결과를 멤버 변수에 저장
        return result;
    }

    // 결과를 출력하는 메서드
    public void displayResult(String operator) {
        System.out.println(num1 + " " + operator + " " + num2 + " = " + result);
    }
}