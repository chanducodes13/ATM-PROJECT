package com.codegnan.Operations;

import java.util.*;
import javax.smartcardio.CardException;

import com.codegnan.Exceptions.*;
import com.codegnan.cards.*;
import com.codegnan.interfaces.IATMService;

public class ATMOperations {

    // Initial ATM MACHINE BALANCE
    public static double ATM_MACHINE_BALANCE = 1000000.0;

    // List to track activities
    public static ArrayList<String> ACTIVITY = new ArrayList<>();

    // Database (cardNumber -> card object)
    public static HashMap<Long, IATMService> database = new HashMap<>();

    // Machine ON/OFF flag
    public static boolean MACHINE_ON = true;

    public static IATMService card;

    //Validate card number
    public static IATMService validateCard(long cardNumber) throws CardException {
        if (database.containsKey(cardNumber)) {
            return database.get(cardNumber);
        } else {
            ACTIVITY.add("Accessed by: " + cardNumber + " (Invalid Card)");
            throw new CardException("This is not a valid card!");
        }
    }

    //show ATM activities
    public static void checkATMActivities() {
        System.out.println("======== Activities Performed ========");
        for (String activity : ACTIVITY) {
            System.out.println(activity);
            System.out.println("-------------------------------------");
        }
    }

    // Reset user attempts (by operator)
    public static void resetUserAttempts(IATMService operatorCard, Scanner sc) {
        System.out.print("Enter user card number to reset attempts: ");
        long number = sc.nextLong();
        try {
            IATMService userCard = validateCard(number);
            userCard.resetPinChances();
            ACTIVITY.add("Accessed by Operator: " + operatorCard.getUserName() + " -> Reset user pin attempts");
            System.out.println("User attempts have been reset successfully!");
        } catch (CardException e) {
            System.out.println(e.getMessage());
        }
    }

    // Validate card credentials (PIN + user type)
    public static IATMService validateCredentials(long cardNumber, int pinNumber)
            throws CardException, InCorrectPinLimitreachedException, InvalidPinException {

        if (!database.containsKey(cardNumber)) {
            throw new CardException("This card is not valid!");
        }

        IATMService tempCard = database.get(cardNumber);

        if (tempCard.getChances() <= 0) {
            throw new InCorrectPinLimitreachedException("PIN attempt limit reached!");
        }

        if (tempCard.getPinNumber() != pinNumber) {
            tempCard.decreaseChances();
            throw new InvalidPinException("Invalid PIN! Remaining chances: " + tempCard.getChances());
        }

        return tempCard;
    }

    //Withdraw amount validation
    public static void withdrawAmount(double amount) throws InsufficientMachineBalanceException {
        if (amount > ATM_MACHINE_BALANCE) {
            throw new InsufficientMachineBalanceException("Insufficient cash in the ATM machine!");
        }
    }

    //Deposit amount validation
    public static void validateDepositAmount(double amount)
            throws InvalidAmountException, InsufficientMachineBalanceException {
        if (amount % 100 != 0) {
            throw new InvalidAmountException("Deposit must be in multiples of 100!");
        }
        if (ATM_MACHINE_BALANCE + amount > 2000000.0) {
            ACTIVITY.add("Unable to deposit - machine limit reached!");
            throw new InsufficientMachineBalanceException("ATM machine cash limit reached!");
        }
    }

    // Operator Mode
    public static void operatorMode(IATMService card, Scanner sc) {
        boolean flag = true;
        while (flag) {
            System.out.println("\n=== Operator Mode: " + card.getUserName() + " ===");
            System.out.println("1. Switch off machine");
            System.out.println("2. Check ATM balance");
            System.out.println("3. Deposit cash in machine");
            System.out.println("4. Reset user pin attempts");
            System.out.println("5. View ATM activities");
            System.out.println("6. Exit operator mode");
            System.out.print("Enter choice: ");
            int option = sc.nextInt();

            switch (option) {
                case 1:
                    MACHINE_ON = false;
                    ACTIVITY.add("Operator " + card.getUserName() + " switched off the ATM.");
                    flag = false;
                    break;

                case 2:
                    System.out.println("ATM Machine Balance: ₹" + ATM_MACHINE_BALANCE);
                    ACTIVITY.add("Operator " + card.getUserName() + " checked ATM balance.");
                    break;

                case 3:
                    System.out.print("Enter amount to deposit: ");
                    double amount = sc.nextDouble();
                    try {
                        validateDepositAmount(amount);
                        ATM_MACHINE_BALANCE += amount;
                        ACTIVITY.add("Operator " + card.getUserName() + " deposited ₹" + amount);
                        System.out.println("Cash successfully added!");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    resetUserAttempts(card, sc);
                    break;

                case 5:
                    checkATMActivities();
                    break;

                case 6:
                    flag = false;
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    //Main ATM Operation
    public static void main(String[] args) {
        // Register cards
        database.put(2222222222222222L, new AxisDebitCard("Raju", 2222222222222222l, 50000.0, 2222));
        database.put(3333333333333333L, new HDFCDebitCard("Ravi", 3333333333333333l, 60000.0, 3333));
        database.put(4444444444444444L, new KOTAKDebitCard("Shiva", 4444444444444444l, 70000.0, 4444));
        database.put(5555555555555555L, new SBIDebitCard("Arjun", 5555555555555555l, 80000.0, 5555));
        database.put(1111111111111111L, new OpertorCard(1111111111111111l, 1111, "Operator1"));

        Scanner sc = new Scanner(System.in);

        while (MACHINE_ON) {
            try {
                System.out.print("\nEnter your card number: ");
                long cardNumber = sc.nextLong();
                System.out.print("Enter PIN: ");
                int pin = sc.nextInt();

                card = validateCredentials(cardNumber, pin);
                ACTIVITY.add("Access granted: " + card.getUserName());

                if (card.getUserType().equalsIgnoreCase("operator")) {
                    operatorMode(card, sc);
                    continue;
                }

                boolean session = true;
                while (session) {
                    System.out.println("\n=== User Menu: " + card.getUserName() + " ===");
                    System.out.println("1. Withdraw");
                    System.out.println("2. Deposit");
                    System.out.println("3. Check Balance");
                    System.out.println("4. Change PIN");
                    System.out.println("5. Mini Statement");
                    System.out.println("6. Exit");
                    System.out.print("Enter choice: ");
                    int option = sc.nextInt();

                    try {
                        switch (option) {
                            case 1:
                                System.out.print("Enter amount to withdraw: ");
                                double withdrawAmt = sc.nextDouble();
                                withdrawAmount(withdrawAmt);
                                card.withdrawAmount(withdrawAmt);
                                ATM_MACHINE_BALANCE -= withdrawAmt;
                                ACTIVITY.add(card.getUserName() + " withdrew ₹" + withdrawAmt);
                                break;

                            case 2:
                                System.out.print("Enter amount to deposit: ");
                                double depositAmt = sc.nextDouble();
                                validateDepositAmount(depositAmt);
                                ATM_MACHINE_BALANCE += depositAmt;
                                card.depositAmount(depositAmt);
                                ACTIVITY.add(card.getUserName() + " deposited ₹" + depositAmt);
                                break;

                            case 3:
                                System.out.println("Your Account Balance: ₹" + card.checkAccountBalance());
                                ACTIVITY.add(card.getUserName() + " checked account balance.");
                                break;

                            case 4:
                                System.out.print("Enter new PIN: ");
                                int newPin = sc.nextInt();
                                card.changePinNUmber(newPin);
                                ACTIVITY.add(card.getUserName() + " changed PIN.");
                                System.out.println("PIN changed successfully!");
                                break;

                            case 5:
                                card.generateMiniStatements();
                                ACTIVITY.add(card.getUserName() + " viewed mini statement.");
                                break;

                            case 6:
                                session = false;
                                break;

                            default:
                                System.out.println("Invalid option!");
                        }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    if (session) {
                        System.out.print("Do you want to continue (Y/N)? ");
                        String cont = sc.next();
                        if (cont.equalsIgnoreCase("N")) {
                            session = false;
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("\n======================================");
        System.out.println("===== Thank You for Using ATM! =======");
        System.out.println("======================================");
        sc.close();
    }
}
