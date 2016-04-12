package com.sg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SampleActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final CurrencyEditText editText = (CurrencyEditText) findViewById(R.id.currencyEditText);
		editText.setSymbol("PKR");
		editText.setTextChangedListener(new CurrencyEditText.onTextChangedListener() {

			@Override
			public void onTextChanged(CharSequence s) {
				((TextView) findViewById(R.id.textView)).setText("" + editText.getAmount());
				((TextView) findViewById(R.id.textView2)).setText(editText.getAmountInWords()+" rupees");
			}
		});
		
	}

}
