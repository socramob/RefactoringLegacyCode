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
		int finalBalanceOfPreviousMonth = 0;

		for (BalancesOfMonth balancesOfMonth : balancesOfMonthList)
		{
            Result result = getResult(finalBalanceOfPreviousMonth, balancesOfMonth);

			balancesOfMonth.setBalance(result.overallBalance);
			balancesOfMonth.setAverageBalance((int) result.averageBalance);

            finalBalanceOfPreviousMonth = result.overallBalance;
		}
	}

    private Result getResult(int overallBalance, BalancesOfMonth balancesOfMonth) {
        Result result = new Result(overallBalance);

        int ultimo = balancesOfMonth.getDate().getDayOfMonth();

        int dayOfPreviousTransaction = 1;
        List<Transaction> transactionsOfMonth = transactionsOfMonth(balancesOfMonth.getDate());
        for (Transaction transaction : transactionsOfMonth)
        {
            int day = transaction.getDate().getDayOfMonth();
            result.addProportionToAverageBalance(ultimo, dayOfPreviousTransaction, day);
            result.addToOverallBalance(transaction.getAmount());
            dayOfPreviousTransaction = day;
        }
        result.addProportionToAverageBalance(ultimo, dayOfPreviousTransaction, ultimo + 1);
        return result;
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
