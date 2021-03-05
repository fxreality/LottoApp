package com.lapi.lottopick.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.lapi.lottopick.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FragmentCheck extends Fragment {

    EditText numberForCheck1;
    EditText numberForCheck2;
    EditText numberForCheck3;
    EditText numberForCheck4;
    EditText numberForCheck5;

    TextView answerOne;

    public FragmentCheck() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment FragmentCheck.
     */
    public static FragmentCheck newInstance() {
        FragmentCheck fragment = new FragmentCheck();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View checkView = inflater.inflate(R.layout.fragment_check, container, false);
        attacheCheckButton(checkView);
        setNumbersForChat(checkView);
        setNumbersRangeRestriction();
        return checkView;
    }

    private void attacheCheckButton(final View checkView) {
        Button checkButton = checkView.findViewById(R.id.checkButton);
        answerOne = checkView.findViewById(R.id.responseCheckingText);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> mapListOfNumbers = getNumbersStringMap();
                if (validateCombination(mapListOfNumbers)) {
                    checkNumbers(view, mapListOfNumbers);
                } else {
                    answerOne.setText("Combination is not valid!");
                }
            }
        });
    }

    private boolean validateCombination(Map mapListOfNumbers) {
        HashSet<Integer> hashSet = new HashSet<>();
        for (Object value : mapListOfNumbers.values()) {
            if (!value.toString().isEmpty())
            hashSet.add(Integer.valueOf(value.toString()));
        }
        return hashSet.size()==5;
    }

    private void checkNumbers(final View view, Map mapListOfNumbers) {

        final String[] history = {""};
        AndroidNetworking.get("http://10.0.2.2:8080/checkCombination")
            .addQueryParameter(mapListOfNumbers)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    history[0] = response.getString("history");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                answerOne.setText("This combination already had " + history[0] + " hits");
            }
            @Override
            public void onError(ANError error) {
                Snackbar.make(view, error.getErrorDetail(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private Map<String, String> getNumbersStringMap() {
        Map<String, String> mapListOfNumbers = new HashMap<String, String>();
        mapListOfNumbers.put("number1", numberForCheck1.getText().toString());
        mapListOfNumbers.put("number2", numberForCheck2.getText().toString());
        mapListOfNumbers.put("number3", numberForCheck3.getText().toString());
        mapListOfNumbers.put("number4", numberForCheck4.getText().toString());
        mapListOfNumbers.put("number5", numberForCheck5.getText().toString());
        return mapListOfNumbers;
    }

    private void setNumbersForChat(View checkView) {
        numberForCheck1 = checkView.findViewById(R.id.number1Check);
        numberForCheck2 = checkView.findViewById(R.id.number2Check);
        numberForCheck3 = checkView.findViewById(R.id.number3Check);
        numberForCheck4 = checkView.findViewById(R.id.number4Check);
        numberForCheck5 = checkView.findViewById(R.id.number5Check);
    }

    private void setNumbersRangeRestriction() {
        numberForCheck1.setFilters(new InputFilter[]{new InputFilterMinMax(1, 50)});
        numberForCheck2.setFilters(new InputFilter[]{new InputFilterMinMax(1, 50)});
        numberForCheck3.setFilters(new InputFilter[]{new InputFilterMinMax(1, 50)});
        numberForCheck4.setFilters(new InputFilter[]{new InputFilterMinMax(1, 50)});
        numberForCheck5.setFilters(new InputFilter[]{new InputFilterMinMax(1, 50)});
    }

    public class InputFilterMinMax implements InputFilter {
        private int minimumValue;
        private int maximumValue;

        public InputFilterMinMax(int minimumValue, int maximumValue) {
            this.minimumValue = minimumValue;
            this.maximumValue = maximumValue;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.subSequence(0, dstart).toString() + source + dest.subSequence(dend, dest.length()));
                if (isInRange(minimumValue, maximumValue, input))
                    return null;
            }
            catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }

    }
}