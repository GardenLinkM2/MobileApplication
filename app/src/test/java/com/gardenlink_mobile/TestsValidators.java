package com.gardenlink_mobile;

import com.gardenlink_mobile.utils.Validator;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestsValidators{

    @Test
    public void testEmail()
    {
        assertTrue(Validator.emailValidator("denis.test@hotmail.fr"));
        assertTrue(Validator.emailValidator("bal@bla.com"));

        assertFalse(Validator.emailValidator("><script>alert(a);</script>@gmail.com"));
        assertTrue(Validator.emailValidator("''@bla.com"));

    }

    @Test
    public void testName()
    {
        assertTrue(Validator.nameValidator("Denis"));
        assertTrue(Validator.nameValidator("denis"));
        assertTrue(Validator.nameValidator("Jean-Denis"));

        assertFalse(Validator.nameValidator("123"));
        assertFalse(Validator.nameValidator("ddzpdpiazdipzkdikzdpkazdzadnazondanzduzanduznadazudidnidniuazndinazdinazidnaziudn"));
        assertFalse(Validator.nameValidator("<script>"));
        assertFalse(Validator.nameValidator("az"));
    }

    @Test
    public void testPhone()
    {

        assertTrue(Validator.phoneValidator("0673656184"));
        assertTrue(Validator.phoneValidator("0912326545"));

        assertFalse(Validator.phoneValidator("091232654"));
        assertFalse(Validator.phoneValidator("09123265478925"));
        assertFalse(Validator.phoneValidator("6678986545"));
        assertFalse(Validator.phoneValidator("azertyuiaz"));
        assertFalse(Validator.phoneValidator("<>/*-"));

    }

    @Test
    public void testPassword()
    {
        assertTrue(Validator.passwordValidator("pouglou"));

        assertFalse(Validator.passwordValidator("az"));
        assertFalse(Validator.passwordValidator("azzodjzoaijdojdizjdoijaziodjazodijaoidjoiazjdoiazjdoazjdoazjdoijzaidjzoajid"));

    }

    @Test
    public void testPostTitle()
    {
        assertTrue(Validator.postTitleValidator("Bel espace vert"));
        assertTrue(Validator.postTitleValidator("jardin"));

        assertFalse(Validator.postTitleValidator("aze"));
        assertFalse(Validator.postTitleValidator("<78922//*-*"));
        assertFalse(Validator.postTitleValidator("zeeazeazeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeezaeazeazfvvfveafefafaefaefaefefafaefeafaefaefe"));


    }

    @Test
    public void testPrice()
    {
        assertTrue(Validator.priceValidator("15.20"));
        assertTrue(Validator.priceValidator("12"));
        assertTrue(Validator.priceValidator("158.3"));
        assertTrue(Validator.priceValidator("10000"));

        assertFalse(Validator.priceValidator("1231232"));
        assertFalse(Validator.priceValidator("12.103"));
        assertFalse(Validator.priceValidator("104.8.9"));
        assertFalse(Validator.priceValidator("123azeeaz78"));




    }

    @Test
    public void testsSurface()
    {
       assertTrue(Validator.surfaceValidator("158"));
       assertTrue(Validator.surfaceValidator("10000"));

       assertFalse(Validator.surfaceValidator("11111111111"));
       assertFalse(Validator.surfaceValidator("aiejfizjf"));

    }


    @Test
    public void testDuration()
    {
        assertTrue(Validator.durationValidator("30"));
        assertTrue(Validator.durationValidator("2"));
        assertTrue(Validator.durationValidator("12"));
        assertTrue(Validator.durationValidator("32"));

        assertFalse(Validator.durationValidator("40"));
        assertFalse(Validator.durationValidator("39"));
        assertFalse(Validator.durationValidator("123"));


    }



    @Test
    public void testStreetNumber()
    {
        assertTrue(Validator.streetNumberValidator("159"));
        assertTrue(Validator.streetNumberValidator("1234"));
        assertTrue(Validator.streetNumberValidator("159"));
        assertTrue(Validator.streetNumberValidator("9999"));

        assertFalse(Validator.streetNumberValidator("10000"));
        assertFalse(Validator.streetNumberValidator("0"));
    }


    @Test
    public void testStreetName()
    {
        assertTrue(Validator.streetNameValidator("rue de la victoire"));
        assertTrue(Validator.streetNameValidator("alphabravo"));

        assertFalse(Validator.streetNameValidator("<script>alert(a)</script>"));
        assertFalse(Validator.streetNameValidator("aze"));
    }


    @Test
    public void testPostCode()
    {
        assertTrue(Validator.postCodeValidator("63290"));
        assertTrue(Validator.postCodeValidator("43000"));

        assertFalse(Validator.postCodeValidator("123"));
        assertFalse(Validator.postCodeValidator("azert"));
        assertFalse(Validator.postCodeValidator("123456"));
    }

    @Test
    public void testCity()
    {
        assertTrue(Validator.cityValidator("Clermont-Ferrand"));
        assertTrue(Validator.cityValidator("Lyon 2"));
        assertTrue(Validator.cityValidator("Issy-les-moulineaux CEDEX"));

        assertFalse(Validator.cityValidator(" <script> alert(a) </script>"));
        assertFalse(Validator.cityValidator(""));
        assertFalse(Validator.cityValidator(" <script> alert(a) </script>"));
        assertFalse(Validator.cityValidator("zeeazeazeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeezaeazeazfvvfveafefafaefaefaefefafaefeafaefaefe"));
    }




}
