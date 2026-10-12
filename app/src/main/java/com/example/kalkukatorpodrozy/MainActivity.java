package com.example.kalkukatorpodrozy;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etDistance, etFuelConsumption, etPassengers;
    private RadioGroup rgFuelType;
    private RadioButton rbGasoline, rbDiesel, rbLpg;
    private Button btnMinus, btnPlus, btnCalculate;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja widoków
        etDistance = findViewById(R.id.etDistance);
        etFuelConsumption = findViewById(R.id.etFuelConsumption);
        etPassengers = findViewById(R.id.etPassengers);
        rgFuelType = findViewById(R.id.rgFuelType);
        rbGasoline = findViewById(R.id.rbGasoline);
        rbDiesel = findViewById(R.id.rbDiesel);
        rbLpg = findViewById(R.id.rbLpg);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvResult = findViewById(R.id.tvResult);

        // Obsługa przycisków + i - dla liczby pasażerów
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = getPassengersCount();
                if (count > 1) {
                    etPassengers.setText(String.valueOf(count - 1));
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = getPassengersCount();
                etPassengers.setText(String.valueOf(count + 1));
            }
        });

        // Obsługa głównego przycisku Oblicz
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCost();
            }
        });
    }

    private int getPassengersCount() {
        try {
            return Integer.parseInt(etPassengers.getText().toString().trim());
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private void calculateCost() {
        try {
            double distance = Double.parseDouble(etDistance.getText().toString().trim().replace(',', '.'));
            double consumption = Double.parseDouble(etFuelConsumption.getText().toString().trim().replace(',', '.'));
            int passengers = Integer.parseInt(etPassengers.getText().toString().trim());

            // Walidacja danych wejściowych (> 0)
            if (distance <= 0 || consumption <= 0 || passengers <= 0) {
                showError();
                return;
            }

            // Ustalenie ceny paliwa na podstawie wybranego RadioButton
            double fuelPrice = 6.50; // Domyślnie Benzyna
            int checkedId = rgFuelType.getCheckedRadioButtonId();

            if (checkedId == R.id.rbDiesel) {
                fuelPrice = 6.70;
            } else if (checkedId == R.id.rbLpg) {
                fuelPrice = 3.10;
            }

            // Obliczenia według wzorów
            double totalFuel = (distance * consumption) / 100.0;
            double totalCost = totalFuel * fuelPrice;
            double costPerPerson = totalCost / passengers;

            // Wyświetlenie poprawnego wyniku (zielony tekst)
            tvResult.setTextColor(Color.parseColor("#2E7D32")); // Zielony
            String resultText = String.format(Locale.GERMANY,
                    "Całkowity koszt: %.2f zł |\nNa osobę: %.2f zł", totalCost, costPerPerson);

            tvResult.setText(resultText);
            tvResult.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            showError();
        }
    }

    private void showError() {
        // Wyświetlenie błędu (czerwony tekst)
        tvResult.setTextColor(Color.RED);
        tvResult.setText("Niepoprawne dane wejściowe!");
        tvResult.setVisibility(View.VISIBLE);
    }
}