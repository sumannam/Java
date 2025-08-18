class AdvancedCalculator extends Calculator {

    // 곱셈 메서드 (부모 클래스의 multiply와는 다른 직접 구현)
    // 부모 클래스의 num1, num2 멤버 변수를 사용하고 result에 결과를 저장합니다.
    @Override // 부모 클래스의 메서드를 오버라이딩함을 명시
    public int multiply() {
        if (num1 == 0 || num2 == 0) {
            result = 0;
            //operator = "*";
            return result;
        }

        // 직접 곱셈 연산 수행
        result = num1 * num2;
        //operator = "*";
        return result;
    }

    // 나눗셈 메서드 (부모 클래스의 divide와는 다른 직접 구현)
    // 부모 클래스의 num1, num2 멤버 변수를 사용하고 result에 결과를 저장합니다.
    @Override // 부모 클래스의 메서드를 오버라이딩함을 명시
    public int divide() {
        if (num2 == 0) {
            System.out.println("0으로 나눌 수 없습니다.");
            result = Integer.MAX_VALUE; // 에러를 나타내는 값
            //operator = "/";
            return result;
        }
        if (num1 == 0) {
            result = 0;
            //operator = "/";
            return result;
        }

        // 직접 나눗셈 연산 수행 (정수 나눗셈)
        result = num1 / num2;
        //operator = "/";
        return result;
    }
}