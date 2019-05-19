package com.monetary.transfer.data;

import com.monetary.transfer.models.Account;

import java.math.BigDecimal;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Utility class for storing operations related to a bank account.
 */
final class AccountUtils {

    /**
     * Random numbers generator
     */
    private static final Random random = new Random();

    /**
     * Set of numerical characters
     */
    private static final char[] NUMBERS = "0123456789".toCharArray();

    /**
     * Prevents class instantiation.
     */
    private AccountUtils() {}

    /**
     * Creates the bank account with randomly selected account number and balance.
     *
     * @return new instance of {@link Account}
     */
    static Account getRandomAccount() {
        return Account
                .builder()
                .number(AccountUtils::getRandomAccountNumber)
                .balance(AccountUtils::randomAccountBalance)
                .build();
    }

    /**
     * Creates the random bank account number.
     *
     * @return the bank account number consisting of eight digits
     */
    private static String getRandomAccountNumber() {
        return IntStream
                .generate(() -> random.nextInt(NUMBERS.length))
                .limit(8)
                .mapToObj(index -> NUMBERS[index])
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    /**
     * Returns random value of accounts generated for test purposes.
     *
     * @param limit top limit
     * @return double value distributed between 0 and {@code limit} parameter
     * value.
     */
    private static BigDecimal randomAccountBalance(int limit) {
        double value =  random.nextInt(limit - 1) +
                        random.nextInt(100) * .01;
        return BigDecimal.valueOf(value);
    }

    /**
     * Returns random value of accounts generated for test purposes.
     *
     * @return double value distributed between 0 and 1 000 000
     */
    static BigDecimal randomAccountBalance(Supplier<Integer> limitSupplier) {
        return randomAccountBalance(limitSupplier.get());
    }

    /**
     * Returns random value of accounts generated for test purposes.
     *
     * @return double value distributed between 0 and 1 000 000
     */
    private static BigDecimal randomAccountBalance() {
        return randomAccountBalance(999_999);
    }
}
