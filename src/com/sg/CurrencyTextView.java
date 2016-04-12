package com.sg;

import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author sumeet.kumar
 */
public final class CurrencyTextView extends RelativeLayout {
	char space = '\u200a';
	char thin_space = '\u2009';
	TextView symbolTextView, amountTextView, centsTextView;
	String symbol = "Rs.";
	Double amount;
	int cents;

	public CurrencyTextView(final Context context) {
		this(context, null);
	}

	public CurrencyTextView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(context);

	}

	@Override
	public boolean isInEditMode() {
		return true;
	}

	private void init(Context context) {
		inflate(context, R.layout.currency_textview, this);

		symbolTextView = (TextView) findViewById(R.id.symbol);
		amountTextView = (TextView) findViewById(R.id.amount);
		centsTextView = (TextView) findViewById(R.id.cents);
		symbolTextView.setText(getSymbol());
	}

	public void setText(Double amount) {

		amountTextView.setText(String.format("%,d", amount.intValue()));
		String fr = String.format(Locale.getDefault(), "%,.2f", amount);
		centsTextView.setText(fr.substring(fr.lastIndexOf('.'), fr.length()));
	}

	public String getSymbol() {
		return symbol;
	}

	public Double getAmount() {
		return amount;
	}

	public int getCents() {
		return cents;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setCents(int cents) {
		this.cents = cents;
	}
}
