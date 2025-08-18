//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {
    public static void main(String[] args) {
        Calculator basicCalc = new Calculator();

        basicCalc.setNumbers(10, 5);
        basicCalc.add();
        basicCalc.displayResult("+");

        basicCalc.subtract();
        basicCalc.displayResult("-");

        AdvancedCalculator advancedCalc = new AdvancedCalculator();

        advancedCalc.setNumbers(20, 7);
        advancedCalc.multiply();
        advancedCalc.displayResult("*");

        advancedCalc.setNumbers(9, 3);
        advancedCalc.divide();
        advancedCalc.displayResult("/");
    }
}