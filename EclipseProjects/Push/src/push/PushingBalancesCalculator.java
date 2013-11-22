package push;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import common.BalancesOfMonth;
import common.BalancesOfMonthCalculator;
import common.Transaction;

public class PushingBalancesCalculator implements BalancesOfMonthCalculator
{

	private final List<Transaction> transactions;

	public PushingBalancesCalculator(List<Transaction> transactions)
	{
		super();
		this.transactions = transactions;
	}

	@Override
	public void fillData(List<BalancesOfMonth> balancesOfMonthList)
	{
		int overallBalance = 0;

		for (BalancesOfMonth balancesOfMonth : balancesOfMonthList)
		{
            Result result = new Result();
            result.balance = overallBalance;

			int ultimo = balancesOfMonth.getDate().getDayOfMonth();

			int dayOfPreviousTransaction = 1;
			List<Transaction> transactionsOfMonth = transactionsOfMonth(balancesOfMonth.getDate());
            for (Transaction transaction : transactionsOfMonth)
			{
				int day = transaction.getDate().getDayOfMonth();
				result.averageBalance += calculateProportionalBalance(dayOfPreviousTransaction, result.balance, day, ultimo);
                result.balance += transaction.getAmount();
				dayOfPreviousTransaction = day;
			}

            result.averageBalance += calculateProportionalBalance(dayOfPreviousTransaction, result.balance, ultimo + 1, ultimo);

			balancesOfMonth.setBalance(result.balance);
			balancesOfMonth.setAverageBalance((int) result.averageBalance);

            overallBalance = result.balance;
		}
	}

	private double calculateProportionalBalance(int dayOfPreviousTransaction, int balance, int day, int daysInMonth)
	{
		int daysBetweenTransactions = day - dayOfPreviousTransaction;
		double proportion = (double) daysBetweenTransactions / daysInMonth;
		return (balance * proportion);
	}

	private List<Transaction> transactionsOfMonth(LocalDate date)
	{
		List<Transaction> results = new ArrayList<Transaction>();
		for (Transaction transaction : transactions)
		{
			LocalDate dateOfTransaction = transaction.getDate();
			if (areSameMonthAndYear(date, dateOfTransaction))
			{
				results.add(transaction);
			}
		}
		return results;
	}

	private boolean areSameMonthAndYear(LocalDate date, LocalDate dateOfTransaction)
	{
		return dateOfTransaction.getMonthOfYear() == date.getMonthOfYear() && dateOfTransaction.getYear() == date.getYear();
	}

}
