import java.io.Serializable;
import java.util.Date;
import java.util.Scanner;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

 class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accountNumber;
    private String accountHolderName;
    private double balance;

    public Account(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

 class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accountNumber;
    private double amount;
    private Date date;
    private String type;

    public Transaction(String accountNumber, double amount, String type) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.date = new Date();
        this.type = type;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}

 class Bank {
    private Map<String, Account> accounts;
    private static final String FILE_NAME = "accounts.dat";

    public Bank() {
        accounts = new HashMap<>();
        loadAccounts();
    }

    public void createAccount(String accountNumber, String accountHolderName, double initialBalance) {
        Account account = new Account(accountNumber, accountHolderName, initialBalance);
        accounts.put(accountNumber, account);
        saveAccounts();
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void deposit(String accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        if (account != null) {
            account.deposit(amount);
            saveAccounts();
        }
    }

    public boolean withdraw(String accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        if (account != null && account.withdraw(amount)) {
            saveAccounts();
            return true;
        }
        return false;
    }

    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        if (withdraw(fromAccountNumber, amount)) {
            deposit(toAccountNumber, amount);
            return true;
        }
        return false;
    }

    private void saveAccounts() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAccounts() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (Map<String, Account>) in.readObject();
        } catch (FileNotFoundException e) {
            // File not found, creating a new one
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
public class BankSystem {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            System.out.println("Welcome to BankY");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter account number: ");
                    String accountNumber = scanner.nextLine();
                    System.out.print("Enter account holder name: ");
                    String accountHolderName = scanner.nextLine();
                    System.out.print("Enter initial balance: ");
                    double initialBalance = scanner.nextDouble();
                    scanner.nextLine(); // consume newline
                    bank.createAccount(accountNumber, accountHolderName, initialBalance);
                    break;

                case "2":
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextLine();
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine(); // consume newline
                    bank.deposit(accountNumber, depositAmount);
                    break;

                case "3":
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextLine();
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine(); // consume newline
                    if (!bank.withdraw(accountNumber, withdrawAmount)) {
                        System.out.println("Insufficient funds.");
                    }
                    break;

                case "4":
                    System.out.print("Enter source account number: ");
                    String fromAccountNumber = scanner.nextLine();
                    System.out.print("Enter destination account number: ");
                    String toAccountNumber = scanner.nextLine();
                    System.out.print("Enter amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    scanner.nextLine(); // consume newline
                    if (!bank.transfer(fromAccountNumber, toAccountNumber, transferAmount)) {
                        System.out.println("Transfer failed.");
                    }
                    break;

                case "5":
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        } while (!choice.equals("5"));

        scanner.close();
    }
}
