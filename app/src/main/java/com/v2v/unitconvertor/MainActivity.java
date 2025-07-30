package com.v2v.unitconvertor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerCategory, spinnerFrom, spinnerTo;
    EditText inputValue;
    TextView resultText;
    Button btnConvert;

    Map<String, String[]> unitsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        inputValue = findViewById(R.id.inputValue);
        resultText = findViewById(R.id.resultText);
        btnConvert = findViewById(R.id.btnConvert);

        unitsMap.put("Length", new String[]{"Meters", "Kilometers", "Miles"});
        unitsMap.put("Weight", new String[]{"Grams", "Kilograms", "Pounds"});
        unitsMap.put("Temperature", new String[]{"Celsius", "Fahrenheit", "Kelvin"});

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(unitsMap.keySet()));
        spinnerCategory.setAdapter(categoryAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String category = parent.getItemAtPosition(pos).toString();
                ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        unitsMap.get(category)
                );
                spinnerFrom.setAdapter(unitAdapter);
                spinnerTo.setAdapter(unitAdapter);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnConvert.setOnClickListener(v -> {
            try {
                double input = Double.parseDouble(inputValue.getText().toString());
                String category = spinnerCategory.getSelectedItem().toString();
                String fromUnit = spinnerFrom.getSelectedItem().toString();
                String toUnit = spinnerTo.getSelectedItem().toString();
                double result = convert(category, fromUnit, toUnit, input);
                resultText.setText("Result: " + result);
            } catch (Exception e) {
                resultText.setText("Error: " + e.getMessage());
            }
        });
    }

    private double convert(String category, String from, String to, double value) {
        if (category.equals("Length")) {
            return convertLength(from, to, value);
        } else if (category.equals("Weight")) {
            return convertWeight(from, to, value);
        } else if (category.equals("Temperature")) {
            return convertTemperature(from, to, value);
        }
        return 0;
    }

    private double convertLength(String from, String to, double value) {
        // Convert everything to meters first
        if (from.equals("Kilometers")) value *= 1000;
        else if (from.equals("Miles")) value *= 1609.34;

        // Convert from meters to target unit
        if (to.equals("Kilometers")) return value / 1000;
        else if (to.equals("Miles")) return value / 1609.34;
        return value;
    }

    private double convertWeight(String from, String to, double value) {
        // Convert to grams
        if (from.equals("Kilograms")) value *= 1000;
        else if (from.equals("Pounds")) value *= 453.592;

        // Convert to target
        if (to.equals("Kilograms")) return value / 1000;
        else if (to.equals("Pounds")) return value / 453.592;
        return value;
    }

    private double convertTemperature(String from, String to, double value) {
        double tempInCelsius;
        if (from.equals("Fahrenheit")) tempInCelsius = (value - 32) * 5 / 9;
        else if (from.equals("Kelvin")) tempInCelsius = value - 273.15;
        else tempInCelsius = value;

        if (to.equals("Fahrenheit")) return tempInCelsius * 9 / 5 + 32;
        else if (to.equals("Kelvin")) return tempInCelsius + 273.15;
        return tempInCelsius;
    }
}
