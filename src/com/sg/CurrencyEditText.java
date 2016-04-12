package com.sg;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Normalizer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.EditText;

public class CurrencyEditText extends EditText {
	public static final String TAG = CurrencyEditText.class.getSimpleName();
	private onTextChangedListener textChangedListener;
	int colorPrimary;
	private String SYMBOL = "PKR ";
	final static String[] units = { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
			"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen",
			"Nineteen" };
	final static String[] tens = { "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty",
			"Ninety" };
	String current = "";

	public CurrencyEditText(Context context) {
		this(context, null);
	}

	public CurrencyEditText(Context context, AttributeSet set) {
		super(context, set);
		init(context);
	}

	private void init(Context context) {
		colorPrimary = context.getResources().getColor(R.color.primary);
		setInputType(InputType.TYPE_CLASS_NUMBER);
		setHint(getSymbol() + "0.00");
		// addTextChangedListener(new CurrencyWatcher());
		setfocusChangeListner();
		// SYMBOL =
		// Currency.getInstance(getResources().getConfiguration().locale).getSymbol();

	}

	public Double getAmount() {
		double amount = 0d;
		String text = getText().toString();
		if (TextUtils.isEmpty(text))
			return 0d;
		text = text.replaceAll("[^.\\d]", "");

		try {
			amount = Double.parseDouble(text);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return amount;
	}

	public String getAmountInWords() {
		return intoWords(getAmount().intValue());
	}

	@Override
	public Editable getText() {
		/*
		 * String text = super.getText().toString(); if
		 * (TextUtils.isEmpty(text)) return super.getText(); text =
		 * text.replaceAll("[^.\\d]", ""); return new
		 * SpannableStringBuilder(text);
		 */
		return super.getText();

	}

	private void setfocusChangeListner() {
		addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!s.toString().equals(current)) {
					removeTextChangedListener(this);

					String replace = String.format("[%s,.\\s]", getSymbol());
					String clean = s.toString().replaceAll(replace, "");

					double parsed;
					try {
						parsed = Double.parseDouble(clean);
					} catch (NumberFormatException e) {
						parsed = 0.00;
						e.printStackTrace();
					}
					DecimalFormat format = new DecimalFormat("#,##0");
					String formatted = format.format((parsed), new StringBuffer(getSymbol()), new FieldPosition(0))
							.toString();
					current = formatted;
					setText(highlight(getSymbol(), formatted));
					setSelection(getText().toString().length());
					addTextChangedListener(this);
					if (textChangedListener != null)
						textChangedListener.onTextChanged(getText());
				}
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private CharSequence highlight(String search, String origin) {
		String normalized = Normalizer.normalize(origin, Normalizer.Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

		int start = normalized.indexOf(search);
		if (start < 0) {
			return origin;
		} else {
			Spannable highlight = new SpannableString(origin);
			while (start >= 0) {
				int spanStart = Math.min(start, origin.length());
				int spanEnd = Math.min(start + search.length(), origin.length());

				highlight.setSpan(new ForegroundColorSpan(colorPrimary), spanStart, spanEnd,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

				start = normalized.indexOf(search, spanEnd);
			}
			return highlight;
		}
	}

	private static String intoWords(Integer i) {
		if (i < 20)
			return units[i];
		if (i < 100)
			return tens[i / 10] + ((i % 10 > 0) ? " " + intoWords(i % 10) : "");
		if (i < 1000)
			return units[i / 100] + " Hundred" + ((i % 100 > 0) ? " and " + intoWords(i % 100) : "");
		if (i < 1000000)
			return intoWords(i / 1000) + " Thousand " + ((i % 1000 > 0) ? " " + intoWords(i % 1000) : "");
		return intoWords(i / 1000000) + " Million " + ((i % 1000000 > 0) ? " " + intoWords(i % 1000000) : "");
	}

	public onTextChangedListener getTextChangedListener() {
		return textChangedListener;
	}

	public void setTextChangedListener(onTextChangedListener textChangedListener) {
		this.textChangedListener = textChangedListener;
	}

	public String getSymbol() {
		return SYMBOL;
	}

	public void setSymbol(String Symbol) {
		SYMBOL = Symbol + "\u200a";
	}

	interface onTextChangedListener {

		void onTextChanged(CharSequence s);
	}
}
