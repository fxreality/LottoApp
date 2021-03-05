package com.lapi.lottopick.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.lapi.lottopick.R;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentGenerate extends Fragment {

    View generateView;

    private String number1;
    private String number2;
    private String number3;
    private String number4;
    private String number5;
    private String star1;
    private String star2;

    public FragmentGenerate() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment FragmentGenerate.
     */
    public static FragmentGenerate newInstance() {
        FragmentGenerate fragment = new FragmentGenerate();
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
        generateView = inflater.inflate(R.layout.fragment_generate, container, false);
        attacheGenerateButton(generateView);
        return generateView;
    }

    private void attacheGenerateButton(final View generateView) {
        Button generateButton = generateView.findViewById(R.id.generateButton);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNumbersFromApi(view);
            }
        });
    }

    private void getNumbersFromApi(final View view) {

        AndroidNetworking.get("http://10.0.2.2:8080/getCombination")
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                setNumbers(response);
            }
            @Override
            public void onError(ANError error) {
                Snackbar.make(view, error.getErrorDetail(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setNumbers(JSONObject response) {
        try {
            number1 = response.getString("number1");
            number2 = response.getString("number2");
            number3 = response.getString("number3");
            number4 = response.getString("number4");
            number5 = response.getString("number5");
            star1 = response.getString("star1");
            star2 = response.getString("star2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fillNumbersAndStars();
    }

    private void fillNumbersAndStars() {

        TextView gn1tv = generateView.findViewById(R.id.generateNumber1Text);
        TextView gn2tv = generateView.findViewById(R.id.generateNumber2Text);
        TextView gn3tv = generateView.findViewById(R.id.generateNumber3Text);
        TextView gn4tv = generateView.findViewById(R.id.generateNumber4Text);
        TextView gn5tv = generateView.findViewById(R.id.generateNumber5Text);
        TextView gs1tv = generateView.findViewById(R.id.generateStar1Text);
        TextView gs2tv = generateView.findViewById(R.id.generateStar2Text);
        gn1tv.setText(number1);
        gn2tv.setText(number2);
        gn3tv.setText(number3);
        gn4tv.setText(number4);
        gn5tv.setText(number5);
        gs1tv.setText(star1);
        gs2tv.setText(star2);
    }
}