package com.gardenlink_mobile.utils;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.gardenlink_mobile.R;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CriteriaFragment extends Fragment {

    //Constantes

    private static final Double MIN_PRICE=0d;
    private static final Double MAX_PRICE=10000d;
    private static final Integer MIN_DURATION=0;
    private static final Integer MAX_DURATION = 36;
    private static final Integer MIN_SIZE=0;
    private static final Integer MAX_SIZE=10000;


    //vue
    private View mView;

    //Sliders
    private Slider durationSlider;
    private Slider areaSlider;
    private Slider priceSlider;
    private Slider distanceSlider;

    //TextFields
    private TextInputEditText minDurationText;
    private TextInputEditText maxDurationText;

    private TextInputEditText minAreaText;
    private TextInputEditText maxAreaText;

    private TextInputEditText minPriceText;
    private TextInputEditText maxPriceText;

    private ViewGroup orientationButtons;
    private ViewGroup soilButtons;

    private RadioButton orientationActivated;
    private RadioButton soilActivated;

    private Button reinitButton;


    private final Slider.OnChangeListener changeListener =
            new Slider.OnChangeListener() {


                @Override
                public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

                    if (fromUser) {
                        List<Float> lValues = new ArrayList<Float>();
                        ;
                        for (Float lF : slider.getValues()) {
                            lValues.add(Math.round(lF) + 0f);
                        }
                        slider.setValues(lValues);

                        switch (slider.getId()) {
                            case R.id.durationSlider:
                                changeTextInputOnSliderChange(minDurationText, maxDurationText, slider);
                                break;

                            case R.id.areaSlider:
                                changeTextInputOnSliderChange(minAreaText, maxAreaText, slider);
                                break;

                            case R.id.priceSlider:
                                changeTextInputOnSliderChange(minPriceText, maxPriceText, slider);

                            default:
                                break;
                        }

                    }

                }
            };


    private void changeTextInputOnSliderChange(TextInputEditText min, TextInputEditText max, Slider slider) {
        min.setText(String.valueOf((Math.round(slider.getValues().get(0)))));
        max.setText(String.valueOf((Math.round(slider.getValues().get(1)))));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.criterias_fragment, parent, false);

        initSliders(view);
        initTextFields(view);
        initRadioButtons(view);
        initReinitButton(view);


        view.setBackgroundColor(getResources().getColor(R.color.colorWhiteTransparent));

        mView=view;
        return view;
    }

    private void initReinitButton(View view) {
        reinitButton = view.findViewById(R.id.resetCriteriaButton);
        reinitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reinitFields((View) view.getParent());
            }
        });
    }


    private void initSliders(View view) {
        durationSlider = view.findViewById(R.id.durationSlider);
        areaSlider = view.findViewById(R.id.areaSlider);
        priceSlider = view.findViewById(R.id.priceSlider);
        distanceSlider = view.findViewById(R.id.distanceSlider);

        //Init values
        durationSlider.setValues(0f, 36f);
        areaSlider.setValues(0f, 10000f);
        priceSlider.setValues(0f, 10000f);
        distanceSlider.setValue(0f);

        //add listeners
        durationSlider.addOnChangeListener(changeListener);
        areaSlider.addOnChangeListener(changeListener);
        priceSlider.addOnChangeListener(changeListener);
        distanceSlider.addOnChangeListener(changeListener);

    }

    private void initTextFields(View view) {
        minDurationText = view.findViewById(R.id.minTime);
        maxDurationText = view.findViewById(R.id.maxTime);

        minAreaText = view.findViewById(R.id.minArea);
        maxAreaText = view.findViewById(R.id.maxArea);

        minPriceText = view.findViewById(R.id.minPrice);
        maxPriceText = view.findViewById(R.id.maxPrice);


        TextWatcher minDurationWatcher = new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                onTextInputChange(minDurationText, durationSlider, true);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        TextWatcher maxDurationWatcher = new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                onTextInputChange(maxDurationText, durationSlider, false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        TextWatcher minAreaWatcher = new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                onTextInputChange(minAreaText, areaSlider, true);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };


        TextWatcher maxAreaWatcher = new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                onTextInputChange(maxAreaText, areaSlider, false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        TextWatcher minPriceWatcher = new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                onTextInputChange(minPriceText, priceSlider, true);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };


        TextWatcher maxPriceWatcher = new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                onTextInputChange(maxPriceText, priceSlider, false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };


        minDurationText.addTextChangedListener(minDurationWatcher);
        maxDurationText.addTextChangedListener(maxDurationWatcher);
        minAreaText.addTextChangedListener(minAreaWatcher);
        maxAreaText.addTextChangedListener(maxAreaWatcher);
        minPriceText.addTextChangedListener(minPriceWatcher);
        maxPriceText.addTextChangedListener(maxPriceWatcher);


    }

    private void onTextInputChange(TextInputEditText pInput, Slider pSlider, boolean isMin) {

        if (pInput.getText() != null && !pInput.getText().toString().isEmpty()) {

            List<Float> lValues = pSlider.getValues();
            ArrayList<Float> lNewValues = new ArrayList<Float>();
            String inputValue = pInput.getText().toString();
            Float changedValue;
            if (!inputValue.isEmpty()) {
                changedValue = Float.parseFloat(pInput.getText().toString());
            } else {

                changedValue = 0f;
            }
            Float intactValue;

            if (isMin) {
                intactValue = lValues.get(1);
                lNewValues.add(changedValue);
                lNewValues.add(intactValue);
            } else {
                intactValue = lValues.get(0);
                lNewValues.add(intactValue);
                lNewValues.add(changedValue);
            }


            pSlider.setValues(lNewValues);

        }

    }


    private void initRadioButtons(View view) {
        orientationButtons = (ViewGroup) view.findViewById(R.id.orientationButtons);
        soilButtons = (ViewGroup) view.findViewById(R.id.soilButtons);

        initListenerOnRadioButtons(orientationButtons);
        initListenerOnRadioButtons(soilButtons);


    }

    private void initListenerOnRadioButtons(ViewGroup radioGroup) {
        RadioButton test;

        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (((View) view.getParent()).getId() == R.id.orientationButtons) {
                    if (orientationActivated != null && view.getId() != orientationActivated.getId()) {
                        orientationActivated.setChecked(false);
                    }
                    orientationActivated = (RadioButton) view;

                } else if (((View) view.getParent()).getId() == R.id.soilButtons) {
                    if (soilActivated != null && view.getId() != soilActivated.getId()) {
                        soilActivated.setChecked(false);
                    }
                    soilActivated = (RadioButton) view;

                }

            }
        };

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setOnClickListener(clickListener);
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }


    public void reinitFields(View view) {
        minDurationText.setText("");
        maxDurationText.setText("");

        minAreaText.setText("");
        maxAreaText.setText("");

        minPriceText.setText("");
        maxPriceText.setText("");


        durationSlider.setValues(durationSlider.getValueFrom(), durationSlider.getValueTo());
        areaSlider.setValues(areaSlider.getValueFrom(), areaSlider.getValueTo());
        priceSlider.setValues(priceSlider.getValueFrom(), priceSlider.getValueTo());


        ((TextInputEditText) view.findViewById(R.id.streetNameField)).setText("");
        ((TextInputEditText) view.findViewById(R.id.streetNumberField)).setText("");
        ((TextInputEditText) view.findViewById(R.id.postNameField)).setText("");
        ((TextInputEditText) view.findViewById(R.id.cityField)).setText("");


        if (orientationActivated != null) {
            orientationActivated.setChecked(false);
            orientationActivated = null;
        }

        if (soilActivated != null) {
            soilActivated.setChecked(false);
            soilActivated = null;
        }

        ((RadioGroup) view.findViewById(R.id.waterProvidedButtons)).clearCheck();
        ((RadioGroup) view.findViewById(R.id.equipmentProvidedButtons)).clearCheck();
        ((RadioGroup) view.findViewById(R.id.directAccessButtons)).clearCheck();


    }


    public void setCityField(final String pCity)
    {
        ((TextInputEditText)mView.findViewById(R.id.cityField)).setText(pCity);
    }

    public void setPostCodeField(final String pPostCode)
    {
        ((TextInputEditText)mView.findViewById(R.id.postNameField)).setText(pPostCode);
    }

    public void setStreetNameField(final String pStreetName)
    {
        ((TextInputEditText)mView.findViewById(R.id.streetNameField)).setText(pStreetName);
    }

    public void setStreetNumberField(final String pStreetNumber)
    {
        ((TextInputEditText)mView.findViewById(R.id.streetNumberField)).setText(pStreetNumber);
    }

    public String getCity()
    {
        String lCity=((TextInputEditText)mView.findViewById(R.id.cityField)).getText().toString();
        return lCity.isEmpty()?null:lCity;
    }

    public Integer getPostalCode()
    {
        String lPostalCode=((TextInputEditText)mView.findViewById(R.id.postNameField)).getText().toString();
        return lPostalCode.isEmpty()?null:Integer.parseInt(lPostalCode);
    }

    public String getStreetName()
    {
        String lStreetName=((TextInputEditText)mView.findViewById(R.id.streetNameField)).getText().toString();
        return lStreetName.isEmpty()?null:lStreetName;
    }

    public Integer getStreetNumber()
    {
        String lStreetNumber = ((TextInputEditText)mView.findViewById(R.id.streetNumberField)).getText().toString();
      return lStreetNumber.isEmpty()?null:Integer.parseInt(lStreetNumber);
    }

    public Double getMinPrice()
    {   String lMinPrice = minPriceText.getText().toString();
        return lMinPrice.isEmpty()?MIN_PRICE:Double.parseDouble(lMinPrice);
    }

    public void setMinPrice(final String pPrice)
    {
        minPriceText.setText(pPrice);
    }

    public Double getMaxPrice()
    {
        String lMaxPrice = maxPriceText.getText().toString();
        return lMaxPrice.isEmpty()?MAX_PRICE:Double.parseDouble(lMaxPrice);
    }

    public void setMaxPrice(final String pPrice)
    {
        maxPriceText.setText(pPrice);
    }

    public Integer getMinDuration()
    {
        String lMinDuration = minDurationText.getText().toString();
        return lMinDuration.isEmpty()?MIN_DURATION:Integer.parseInt(lMinDuration);
    }

    public void setMinDuration(final String pDuration)
    {
        minDurationText.setText(pDuration);
    }

    public Integer getMaxDuration()
    {
        String lMaxDuration = maxDurationText.getText().toString();
        return lMaxDuration.isEmpty()?MAX_DURATION:Integer.parseInt(lMaxDuration);
    }

    public void setMaxDuration(final String pDuration)
    {
        maxDurationText.setText(pDuration);
    }

    public Integer getMinSize()
    {
        String lMinSize = minAreaText.getText().toString();
        return lMinSize.isEmpty()?MIN_SIZE:Integer.parseInt(lMinSize);
    }

    public void setMinSize(final String pSize)
    {
        minAreaText.setText(pSize);
    }

    public Integer getMaxSize()
    {
        String lMaxSize = maxAreaText.getText().toString();
        return lMaxSize.isEmpty()?MAX_SIZE:Integer.parseInt(lMaxSize);
    }

    public void setMaxSize(final String pSize)
    {
        maxAreaText.setText(pSize);
    }

    public String getSoilType()
    {
        if(soilActivated != null)
        {
            return soilActivated.getText().toString();
        }
        else
        {
            return null;
        }
    }

    public String getOrientation()
    {
        if(orientationActivated !=null)
        {
            return convertOrientation(orientationActivated.getText().toString());
        }
        else
        {
            return null;
        }
    }


    public Boolean getWaterProvided()
    {
        return translateButtonToBoolean(R.id.waterProvidedButtons);
    }

    public Boolean getEquipmentProvided()
    {
        return translateButtonToBoolean(R.id.equipmentProvidedButtons);
    }

    public Boolean getDirectAccess()
    {
        return  translateButtonToBoolean(R.id.directAccessButtons);
    }


    private Boolean translateButtonToBoolean(Integer id)
    {
        RadioButton lButton = findCheckedRadioButton(id);
        if(lButton != null)
        {
            if(("Oui").equals(lButton.getText().toString()))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else {
            return null;
        }
    }



    private RadioButton findCheckedRadioButton(Integer pRadioGroup)
    {
        RadioGroup lGroup = ((RadioGroup) mView.findViewById(pRadioGroup));

        RadioButton lButton = null;
        if(lGroup.getCheckedRadioButtonId() != -1) {
            lButton = mView.findViewById(lGroup.getCheckedRadioButtonId());
        }

        return lButton;
    }


    private String convertOrientation(String pOrientation)
    {
        String lFormattedOrientation;

        switch (pOrientation)
        {
            case "Nord":
                lFormattedOrientation = "North";
                break;
            case "Sud":
                lFormattedOrientation="South";
                break;
            case "Est":
                lFormattedOrientation="East";
                break;
            case "Ouest" :
                lFormattedOrientation = "West";
                break;
            case "Nord-Est":
                lFormattedOrientation = "NorthEast";
                break;
            case "Nord-Ouest":
                lFormattedOrientation="NorthWest";
                break;
            case "Sud-Ouest":
                lFormattedOrientation="SouthWest";
                break;
            case "Sud-Est":
                lFormattedOrientation="SouthEast";
                break;
            default:
                lFormattedOrientation=null;
                break;

        }

        return lFormattedOrientation;
    }




}
