package com.example.bmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etWeight, etHeight;
    private Button btnCalculate;
    private MaterialCardView cardResult;
    private TextView tvBmiValue, tvBmiCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        btnCalculate = findViewById(R.id.btnCalculate);
        cardResult = findViewById(R.id.cardResult);
        tvBmiValue = findViewById(R.id.tvBmiValue);
        tvBmiCategory = findViewById(R.id.tvBmiCategory);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });
    }

    private void calculateBMI() {
        String weightStr = etWeight.getText().toString();
        String heightStr = etHeight.getText().toString();

        if (TextUtils.isEmpty(weightStr)) {
            etWeight.setError("Please enter weight");
            return;
        }

        if (TextUtils.isEmpty(heightStr)) {
            etHeight.setError("Please enter height");
            return;
        }

        float weight, height;
        try {
            weight = Float.parseFloat(weightStr);
            height = Float.parseFloat(heightStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            return;
        }

        if (height <= 0) {
            etHeight.setError("Height must be greater than 0");
            return;
        }

        // BMI Formula: weight (kg) / height (m)^2
        float bmi = weight / (height * height);

        displayResult(bmi);
    }

    private void displayResult(float bmi) {
        String category;
        int colorResId;

        if (bmi < 18.5) {
            category = "Underweight";
            colorResId = R.color.bmi_underweight;
        } else if (bmi >= 18.5 && bmi < 25) {
            category = "Normal";
            colorResId = R.color.bmi_normal;
        } else if (bmi >= 25 && bmi < 30) {
            category = "Overweight";
            colorResId = R.color.bmi_overweight;
        } else {
            category = "Obese";
            colorResId = R.color.bmi_obese;
        }

        tvBmiValue.setText(String.format(Locale.getDefault(), "%.1f", bmi));
        tvBmiCategory.setText(category);
        tvBmiCategory.setTextColor(ContextCompat.getColor(this, colorResId));
        
        cardResult.setVisibility(View.VISIBLE);
    }
}
