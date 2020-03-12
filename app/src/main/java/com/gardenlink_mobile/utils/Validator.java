package com.gardenlink_mobile.utils;

import java.util.regex.Pattern;

public class Validator {
    public final static String MAIL_REGEX_MESSAGE = "Format email";
    public final static String NAME_REGEX_MESSAGE = "De 3 à 25 lettres et -";
    public final static String PHONE_REGEX_MESSAGE = "0 suivi de 9 chiffres";
    public final static String POST_TITLE_REGEX_MESSAGE = "De 5 à 255 lettres et espace";
    public final static String INTEGER_REGEX_SIMPLE_MESSAGE = "Valeur entre 0 et 9999";
    public final static String PRICE_REGEX_DECIMAL_MESSAGE = "Max 4 chiffres avant , ou . et max 2 chiffres décimaux";
    public final static String INTEGER_REGEX_MAX_MESSAGE  = "Valeur max 10000";
    public final static String DURATION_REGEX_SIMPLE_MESSAGE = "De 1 à 36 mois";
    public final static String DURATION_REGEX_MAX_MESSAGE = "De 1 à 36 mois";
    public final static String STREET_NAME_REGEX_MESSAGE = "De 6 à 255 lettres";
    public final static String POST_CODE_REGEX_MESSAGE = "5 chiffres";
    public final static String CITY_REGEX_MESSAGE = "De 1 à 255 lettres";
    public final static String DESCRIPTION_MESSAGE ="Max 25000 caractères";
    public final static String STREET_NUMBER_MESSAGE = "Valeur comprise entre 1 et 9999";

    private final static String MAIL_REGEX= "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    private final static String NAME_REGEX="[A-Za-z-]{3,25}";

    private final static String PHONE_REGEX="0[0-9]{9}";

    private final static String POST_TITLE_REGEX="[A-Za-z \\s]{5,255}";

    private final static String INTEGER_REGEX_SIMPLE ="[0-9]{1,4}";

    private final static String PRICE_REGEX_DECIMAL = INTEGER_REGEX_SIMPLE +"[.,][0-9]{1,2}";

    private final static String INTEGER_REGEX_MAX = "10000";

    private final static String DURATION_REGEX_SIMPLE = "[1-2]{0,1}[0-9]";

    private final static String DURATION_REGEX_MAX = "3[0-6]";

    private final static String STREET_NAME_REGEX="[A-Za-z \\s]{6,255}";

    private final static String POST_CODE_REGEX ="[0-9]{5}";

    private final static String CITY_REGEX = "[A-Za-z0-9 \\s -]{1,255}";





    private static final Pattern mMailPattern = Pattern.compile(MAIL_REGEX);

    private static final Pattern mNamePattern= Pattern.compile(NAME_REGEX);

    private static final Pattern mPhoneRegex= Pattern.compile(PHONE_REGEX);

    private static final Pattern mPostTitleRegex= Pattern.compile(POST_TITLE_REGEX);

    private static final Pattern mIntegerPatternSimple = Pattern.compile(INTEGER_REGEX_SIMPLE);

    private static final Pattern mPricePatternDecimal = Pattern.compile(PRICE_REGEX_DECIMAL);

    private static final Pattern mDurationPatternSimple = Pattern.compile(DURATION_REGEX_SIMPLE);

    private static final Pattern mDurationPatternMax = Pattern.compile(DURATION_REGEX_MAX);

    private static final Pattern mStreetNamePattern = Pattern.compile(STREET_NAME_REGEX);

    private static final Pattern mPostCodePattern = Pattern.compile(POST_CODE_REGEX);

    private static final Pattern mCityPattern = Pattern.compile(CITY_REGEX);


    /**
     *
     * @param pInput the email to test as a String
     * @return true if the input is conform, false otherwise
     */
    public static boolean emailValidator(final String pInput)
    {
        if(pInput.length()  > 254 || pInput.length()<6)
        {
            return false;
        }
        return mMailPattern.matcher(pInput).matches();
    }

    /**
     *
     * @param pInput the firstname or lastname to test as a String
     * @return true if the input is conform, false otherwise
     */
    public static boolean nameValidator(final String pInput)
    {
        return mNamePattern.matcher(pInput).matches();
    }

    /**
     *
     * @param pInput the phone number to test as a String
     * @return true if the input is conform, false otherwise
     */
    public static boolean phoneValidator(final String pInput)
    {
        return mPhoneRegex.matcher(pInput).matches();
    }

    /**
     *
     * @param pInput the password to test as a String
     * @return true if the password is conform, false otherwise
     */
    public static boolean passwordValidator(final String pInput)
    {
        if(pInput.length()<5 || pInput.length()>30)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     *
     * @param pInput the title of a post to test as a String
     * @return true if the title is conform, false otherwise
     */
    public static boolean postTitleValidator(final String pInput)
    {

        return mPostTitleRegex.matcher(pInput).matches();
    }

    /**
     *
     * @param pInput The price to test as a String
     * @return true if the price is conform, false otherwise
     */
    public static boolean priceValidator(final String pInput)
    {
       if(mIntegerPatternSimple.matcher(pInput).matches() ||mPricePatternDecimal.matcher(pInput).matches() || INTEGER_REGEX_MAX.equals(pInput))
       {
           return true;
       }
       else{
           return false;
       }
    }
    /**
     *
     * @param pInput The surface value to test as a String
     * @return true if the value is conform, false otherwise
     */
    public static boolean surfaceValidator(final String pInput)
    {
        if(mIntegerPatternSimple.matcher(pInput).matches() || INTEGER_REGEX_MAX.equals(pInput))
        {
            return true;
        }

        else{
            return false;
        }

    }

    /**
     *
     * @param pInput The duration value to test as a String
     * @return true if the value is conform, false otherwise
     */
    public static boolean durationValidator(final String pInput)
    {

        return mDurationPatternSimple.matcher(pInput).matches() || mDurationPatternMax.matcher(pInput).matches();
    }

    /**
     *
     * @param pInput The street number to test as a String
     * @return true if the value is conform, false otherwise
     */
    public static boolean streetNumberValidator(final String pInput)
    {
        if(!"0".equals(pInput))
        {
            return mIntegerPatternSimple.matcher(pInput).matches();
        }
        else
        {
            return false;
        }
    }

    /**
     *
     * @param pInput The street name to test as a String
     * @return true if the value is conform, false otherwise
     */
    public static boolean streetNameValidator(final String pInput)
    {
        return mStreetNamePattern.matcher(pInput).matches();
    }

    /**
     *
     * @param pInput The post code to test as a String
     * @return true if the value is conform, false otherwise
     */
    public static boolean postCodeValidator(final String pInput)
    {
        return mPostCodePattern.matcher(pInput).matches();
    }

    /**
     *
     * @param pInput The city name to test as a String
     * @return true if the value is conform, false otherwise
     */
    public static boolean cityValidator(final String pInput)
    {
        return mCityPattern.matcher(pInput).matches();
    }

    /**
     *
     * @param pInput The description to test as a String
     * @return true if the value is conform, false otherwise
     */
    public static boolean descriptionValidator(final String pInput)
    {
        if(pInput.length()>25000)
        {
            return false;
        }
        else return true;
    }

    public static boolean commentsValidators(final String pInput)
    {
        if(pInput.length()>4048)
        {
            return false;
        }
        else{
            return true;
        }
    }


}
