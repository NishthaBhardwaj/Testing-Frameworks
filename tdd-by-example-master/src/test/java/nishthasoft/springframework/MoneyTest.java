package nishthasoft.springframework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MoneyTest {
    @Test
    void testMutliplication() {
        Dollar five = new Dollar(2);
        Dollar product = five.times(5);
        assertEquals(10,product.amount);
        Dollar product1 = five.times(6);
        assertEquals(12,product1.amount);

    }




    @Test
    public void testEquality() {
        assertEquals(new Dollar(5),new Dollar(5));
        assertNotEquals(new Dollar(5),new Dollar(8));
    }
}
