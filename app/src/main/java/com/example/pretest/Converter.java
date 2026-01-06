package com.example.pretest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Converter extends AppCompatActivity {

    TextInputEditText etNilai;
    Spinner spOptionFrom;
    Spinner spOptionTo;
    TextView tvResult;
    String[] OptionTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.converter_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etNilai = findViewById(R.id.etNilaiAwal);
        spOptionFrom = findViewById(R.id.spOptionFROM);
        spOptionTo = findViewById(R.id.spOptionTO);
        tvResult = findViewById(R.id.tvResult);

        String[] OptionFrom = {"Satuan awal","°C (Celcius)", "°F (Fahrenheit)", "°K (Kelvin)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                OptionFrom
        );

        OptionTo = new String[]{"Satuan akhir", "°C (Celcius)", "°F (Fahrenheit)", "°K (Kelvin)"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                OptionTo
        );
        spOptionFrom.setAdapter(adapter);
        spOptionTo.setAdapter(adapter1);


        etNilai.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateResult();
            }
        });

        AdapterView.OnItemSelectedListener spinnerListener =
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        updateResult();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        tvResult.setVisibility(View.GONE);
                    }
                };

        spOptionFrom.setOnItemSelectedListener(spinnerListener);
        spOptionTo.setOnItemSelectedListener(spinnerListener);



    }
    private void updateResult() {
        tvResult.setVisibility(View.GONE);

        String input = etNilai.getText().toString().trim();
        if (input.isEmpty()) return;

        int fromIndex = spOptionFrom.getSelectedItemPosition();
        int toIndex = spOptionTo.getSelectedItemPosition();

        if (fromIndex == 0 || toIndex == 0) return;

        double nilai;
        try {
            nilai = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return;
        }

        double hasil = hitungKonversi(nilai, fromIndex - 1, toIndex - 1);

        tvResult.setText(String.format("%.2f", hasil)+ getUnitIndex(toIndex - 1));
        tvResult.setVisibility(View.VISIBLE);
    }

    private String getUnitIndex(int toIndex) {
        switch (toIndex){
            case 0:
                return "°C";
            case 1:
                return "°F";
            case 2:
                return "°K";
            default:
                return "°";
        }
    }


    private double hitungKonversi(double nilai, int fromIndex, int toIndex) {
        double celsius;

        switch (fromIndex) {
            case 0:
                celsius = nilai;
                break;
            case 1:
                celsius = (nilai - 32) * 5 / 9;
                break;
            case 2:
                celsius = nilai - 272.15;
                break;
            default:
                celsius = 0;
                break;
        }

        switch (toIndex) {
            case 0:
                return celsius;
            case 1:
                return (celsius * 9 / 5) + 32;
            case 2:
                return celsius + 272.15;
            default:
                celsius = 0;
                break;
        }
        return celsius;
    }

    public void back(View view) {
        Intent intent = new Intent(Converter.this, MainActivity.class);
        startActivity(intent);
    }


}