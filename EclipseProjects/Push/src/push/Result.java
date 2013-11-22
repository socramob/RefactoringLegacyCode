package push;

public class Result {
    public double averageBalance = 0d;
    public int overallBalance = 0;

    public Result(int overallBalance) {
        this.overallBalance = overallBalance;
    }

    void addProportionToAverageBalance(int ultimo, int dayOfPreviousTransaction, int day) {
        addToAverageBalance(calculateProportionalBalance(dayOfPreviousTransaction, overallBalance, day, ultimo));
    }

    static double calculateProportionalBalance(int dayOfPreviousTransaction, int balance, int day, int daysInMonth)
	{
		int daysBetweenTransactions = day - dayOfPreviousTransaction;
		double proportion = (double) daysBetweenTransactions / daysInMonth;
		return (balance * proportion);
	}

    public void addToAverageBalance(double summand) {
        averageBalance += summand;
    }

    public void addToOverallBalance(int amount) {
        overallBalance += amount;
    }
}
