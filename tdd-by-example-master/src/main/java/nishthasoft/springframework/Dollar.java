package nishthasoft.springframework;

import java.util.Objects;

public class Dollar {

    int amount;
    public Dollar(int amount) {
        this.amount = amount;
    }

    Dollar times(int multiply)
    {
        return new Dollar(amount * multiply);

    }

    @Override
    public boolean equals(Object o) {
        Dollar dollar = (Dollar) o;
        return amount == dollar.amount;
    }


}


