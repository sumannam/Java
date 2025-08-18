import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdvancedCalculatorTest {
    AdvancedCalculator advancedCalc;

    @BeforeEach
    void setUp() {
        advancedCalc = new AdvancedCalculator();
        advancedCalc.setNumbers(9, 3);
    }

    @Test
    void testMultiply() {
        assertEquals(27, advancedCalc.multiply());
    }

    @Test
    void testDivide() {
        assertEquals(3, advancedCalc.divide());
    }
}