package push;

import common.Transaction;

import java.util.List;

public class MonthlyFigure {
    private double averageBalance;
    private int balance;

    static double calculateProportionalBalance(int dayOfLatestBalance, int balance, int day, int daysInMonth)
	{
		int countingDays = day - dayOfLatestBalance;
		if (countingDays == 0)
		{
			return 0;
		}
		double rate = (double) countingDays / daysInMonth;
		return (balance * rate);
	}

    public static MonthlyFigure createDummy() {
        return new MonthlyFigure(0);
    }

    public int getBalance() {
        return balance;
    }

    public void addBalance(int bal){
        this.balance = this.balance + bal;
    }

    public void addAverageBalance(double bal){
        this.averageBalance = this.averageBalance + bal;
    }

    public double getAverageBalance() {
        return averageBalance;
    }

    MonthlyFigure(int balance) {
        this.balance = balance;
        this.averageBalance = 0;
    }

    public MonthlyFigure getMonthlyFigureOfNextMonth(int ultimo, final List<Transaction> transactionsOfMonth) {
        MonthlyFigure monthlyFigure = new MonthlyFigure(getBalance());

        int dayOfLatestBalance = 1;
        for (Transaction transaction : transactionsOfMonth)
        {
            int day = transaction.getDate().getDayOfMonth();
            monthlyFigure.addAverageBalance(calculateProportionalBalance(dayOfLatestBalance, monthlyFigure.getBalance(), day, ultimo));
            monthlyFigure.addBalance(transaction.getAmount());


            dayOfLatestBalance = day;
        }

        monthlyFigure.addAverageBalance(calculateProportionalBalance(dayOfLatestBalance, monthlyFigure.getBalance(), ultimo + 1, ultimo));
        return monthlyFigure;
    }
}
