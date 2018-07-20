package com.vedas.weightloss.Alert;

/**
 * Created by VEDAS on 6/1/2018.
 */

public class Constants {
    public enum personlInfoKeys {
        dateOfBirth ("dateOfBirth"),
        gender ("gender"),
        location ("location"),
        height ("height"),
        weight ("weight"),
        bmi ("bmi"),
        bmr ("bmr"),
        targetWeight ("targetWeight"),
        activityLavel ("activityLavel"),
        recommendedPlan ("recommendedPlan"),
        targetCalories ("targetCalories"),
        profilePhoto ("profilePhoto"),
        zipCode ("zipCode"),
        targetDays ("targetDays");

        private final String name;

        private personlInfoKeys(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    public enum foodKeys {
        breakfast ("breakfast"),
        lunch ("lunch"),
        snacks ("sancks"),
        dinner ("dinner");

        private final String name;

        private foodKeys(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }
    public enum RegisterTypes {
        Manual ("Manual"),
        Facebook ("Facebook"),
        Google ("Google");

        private final String name;

        private RegisterTypes(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }
}
