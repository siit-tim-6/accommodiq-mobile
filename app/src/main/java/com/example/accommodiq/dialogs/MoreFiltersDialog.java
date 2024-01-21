package com.example.accommodiq.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.accommodiq.R;
import com.example.accommodiq.dtos.MoreFiltersDto;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MoreFiltersDialog extends BottomSheetDialog {
    EditText minPriceField;
    EditText maxPriceField;
    Spinner typeSpinner;
    CheckBox airConditioningCb;
    CheckBox freeWifiCb;
    CheckBox fitnessCenterCb;
    CheckBox freeParkingCb;
    CheckBox swimmingPoolCb;
    CheckBox privateBalconyCb;
    Button applyBtn;
    MoreFiltersDto moreFiltersDto;

    public MoreFiltersDialog(@NonNull Context context, View view, MoreFiltersDto moreFiltersDto) {
        super(context);
        this.setContentView(view);
        this.moreFiltersDto = moreFiltersDto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        minPriceField = this.findViewById(R.id.min_price_search);
        maxPriceField = this.findViewById(R.id.max_price_search);
        typeSpinner = this.findViewById(R.id.type_search);
        airConditioningCb = this.findViewById(R.id.air_conditioning_cb);
        freeWifiCb = this.findViewById(R.id.free_wifi_cb);
        fitnessCenterCb = this.findViewById(R.id.fitness_center_cb);
        freeParkingCb = this.findViewById(R.id.free_parking_cb);
        swimmingPoolCb = this.findViewById(R.id.swimming_pool_cb);
        privateBalconyCb = this.findViewById(R.id.private_balcony_cb);
        applyBtn = this.findViewById(R.id.apply_btn);

        if (moreFiltersDto.getMinPrice() != null)
            minPriceField.setText(moreFiltersDto.getMinPrice().toString());
        if (moreFiltersDto.getMaxPrice() != null)
            maxPriceField.setText(moreFiltersDto.getMaxPrice().toString());

        if (moreFiltersDto.getBenefits() != null) {
            if (moreFiltersDto.getBenefits().contains("Air Conditioning")) {
                airConditioningCb.setSelected(true);
                airConditioningCb.setChecked(true);
            }
            if (moreFiltersDto.getBenefits().contains("Free Parking")) {
                freeParkingCb.setChecked(true);
                freeParkingCb.setSelected(true);
            }
            if (moreFiltersDto.getBenefits().contains("Fitness Center")) {
                fitnessCenterCb.setChecked(true);
                fitnessCenterCb.setSelected(true);
            }
            if (moreFiltersDto.getBenefits().contains("Free Wi-Fi")) {
                freeWifiCb.setChecked(true);
                freeWifiCb.setSelected(true);
            }
            if (moreFiltersDto.getBenefits().contains("Swimming Pool")) {
                swimmingPoolCb.setChecked(true);
                swimmingPoolCb.setSelected(true);
            }
            if (moreFiltersDto.getBenefits().contains("Private Balcony")) {
                privateBalconyCb.setChecked(true);
                privateBalconyCb.setSelected(true);
            }
        }

        setApplyBtnOnClickListener();

        airConditioningCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            airConditioningCb.setSelected(isChecked);
        });

        freeWifiCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            freeWifiCb.setSelected(isChecked);
        });

        fitnessCenterCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            fitnessCenterCb.setSelected(isChecked);
        });

        freeParkingCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            freeParkingCb.setSelected(isChecked);
        });

        swimmingPoolCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            swimmingPoolCb.setSelected(isChecked);
        });

        privateBalconyCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            privateBalconyCb.setSelected(isChecked);
        });
    }

    private void setApplyBtnOnClickListener() {
        applyBtn.setOnClickListener(v -> {
            Integer minPriceValue = null;
            Integer maxPriceValue = null;

            if (!minPriceField.getText().toString().isEmpty() && !maxPriceField.getText().toString().isEmpty()) {
                try {
                    minPriceValue = Integer.parseInt(minPriceField.getText().toString());
                    maxPriceValue = Integer.parseInt(maxPriceField.getText().toString());
                } catch (Exception ignored) {
                    Toast.makeText(getContext(), "Invalid min or max price.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            moreFiltersDto.setMinPrice(minPriceValue);
            moreFiltersDto.setMaxPrice(maxPriceValue);
            moreFiltersDto.setSelectedType(typeSpinner.getSelectedItem().toString());
            Log.i("CB", "onCreate: " + getCommaSeparatedBenefits());
            if (!getCommaSeparatedBenefits().isEmpty())
                moreFiltersDto.setBenefits(getCommaSeparatedBenefits());
            dismiss();
        });
    }

    private String getCommaSeparatedBenefits() {
        ArrayList<String> selectedBenefits = new ArrayList<>();

        if (airConditioningCb.isSelected()) {
            selectedBenefits.add(airConditioningCb.getText().toString());
        }

        if (freeWifiCb.isSelected()) {
            selectedBenefits.add(freeWifiCb.getText().toString());
        }

        if (fitnessCenterCb.isSelected()) {
            selectedBenefits.add(fitnessCenterCb.getText().toString());
        }

        if (freeParkingCb.isSelected()) {
            selectedBenefits.add(freeParkingCb.getText().toString());
        }

        if (swimmingPoolCb.isSelected()) {
            selectedBenefits.add(swimmingPoolCb.getText().toString());
        }

        if (privateBalconyCb.isSelected()) {
            selectedBenefits.add(privateBalconyCb.getText().toString());
        }

        return String.join(",", selectedBenefits);
    }

}
