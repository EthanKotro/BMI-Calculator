package com.example.bmi_calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.slider.Slider;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Slider sliderWeight, sliderHeight;
    private TextView tvWeightValue, tvHeightValue;
    private Button btnCalculate;
    private MaterialCardView cardResult;
    private TextView tvBmiValue, tvBmiCategory;
    private CircularProgressIndicator progressBmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        sliderWeight = findViewById(R.id.sliderWeight);
        sliderHeight = findViewById(R.id.sliderHeight);
        tvWeightValue = findViewById(R.id.tvWeightValue);
        tvHeightValue = findViewById(R.id.tvHeightValue);
        
        btnCalculate = findViewById(R.id.btnCalculate);
        
        cardResult = findViewById(R.id.cardResult);
        tvBmiValue = findViewById(R.id.tvBmiValue);
        tvBmiCategory = findViewById(R.id.tvBmiCategory);
        progressBmi = findViewById(R.id.progressBmi);

        setupSliders();

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });
    }

    private void setupSliders() {
        // Weight Slider Listener
        sliderWeight.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                tvWeightValue.setText(String.format(Locale.getDefault(), "%.0f kg", value));
            }
        });

        // Height Slider Listener
        sliderHeight.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                tvHeightValue.setText(String.format(Locale.getDefault(), "%.0f cm", value));
            }
        });
    }

    private void calculateBMI() {
        float weight = sliderWeight.getValue();
        float heightCm = sliderHeight.getValue();
        float heightM = heightCm / 100f; // Convert cm to m

        if (heightM <= 0) return;

        // BMI Formula: weight (kg) / height (m)^2
        float bmi = weight / (heightM * heightM);

        displayResult(bmi);
    }

    private void displayResult(float bmi) {
        String category;
        int colorResId;
        int progress;

        // Determine category and color
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

        // Map BMI to progress (0-100) for the gauge
        // Let's say max BMI we care about is 40 for the gauge visual
        // 0 BMI = 0 progress, 40 BMI = 100 progress
        progress = (int) ((bmi / 40f) * 100);
        if (progress > 100) progress = 100;

        // Update UI
        tvBmiValue.setText(String.format(Locale.getDefault(), "%.1f", bmi));
        tvBmiCategory.setText(category);
        
        int color = ContextCompat.getColor(this, colorResId);
        tvBmiCategory.setTextColor(color);
        progressBmi.setIndicatorColor(color);

        // Animate Progress
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBmi, "progress", 0, progress);
        animation.setDuration(1000); // 1 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        
        // Show Card
        if (cardResult.getVisibility() != View.VISIBLE) {
            cardResult.setAlpha(0f);
            cardResult.setVisibility(View.VISIBLE);
            cardResult.animate().alpha(1f).setDuration(500).start();
        }
    }
}
