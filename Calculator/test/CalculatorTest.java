import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    // 테스트 메서드들이 공유할 Calculator 객체
    Calculator calc;

    // 각 테스트 메서드가 실행되기 전에 호출되는 setup 메서드
    @BeforeEach
    void setup() {
        calc = new Calculator();
        calc.setNumbers(4, 2);
    }

    @Test
    void testAdd() {
        assertEquals(6, calc.add());
    }

    @Test
    void testSubtract() {
        assertEquals(2, calc.subtract());
    }

    @Test
    void testMultiply() {
        assertEquals(8, calc.multiply());
    }

    @Test
    void testDivide() {
        assertEquals(2, calc.divide());
    }
}