package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestStrictBankAccount {

    private final static int INITIAL_AMOUNT = 100;

    // Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;

    /**
     * Prepare the tests.
     */
    @BeforeEach
    public void setUp() {
        this.mRossi = new AccountHolder("Mario", "Rossi", 1);
        this.bankAccount = new StrictBankAccount(mRossi, INITIAL_AMOUNT);
    }

    /**
     * Test the initial state of the StrictBankAccount.
     */
    @Test
    public void testInitialization() {
        assertEquals(INITIAL_AMOUNT, bankAccount.getBalance());
        assertEquals(mRossi, bankAccount.getAccountHolder());
        assertEquals(0, bankAccount.getTransactionsCount());
    }

    /**
     * Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
     */
    @Test
    public void testManagementFees() {
        bankAccount.deposit(mRossi.getUserID(), 100);
        double expected = bankAccount.getBalance() - (StrictBankAccount.MANAGEMENT_FEE + bankAccount.getTransactionsCount() * StrictBankAccount.TRANSACTION_FEE);
        bankAccount.chargeManagementFees(mRossi.getUserID());
        assertEquals(expected, bankAccount.getBalance());
        assertEquals(0, bankAccount.getTransactionsCount());
    }

    /**
     * Test that withdrawing a negative amount causes a failure.
     */
    @Test
    public void testNegativeWithdraw() {
        try {
            bankAccount.withdraw(mRossi.getUserID(), -1);
            fail("Withdrawing a negative amount didn't cause a failure");
        } catch(IllegalArgumentException e){
            assertEquals("Cannot withdraw a negative amount", e.getMessage());
        }
    }

    /**
     * Test that withdrawing more money than it is in the account is not allowed.
     */
    @Test
    public void testWithdrawingTooMuch() {
        try {
            bankAccount.withdraw(mRossi.getUserID(), bankAccount.getBalance()+1);
            fail("Withdrawing more money than it is in the account was allowed");
        } catch(IllegalArgumentException e){
            assertEquals("Insufficient balance", e.getMessage());
        }
    }
}
