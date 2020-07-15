package com.android.thedognextdoor.ProfileP;

public class MyProfileDog {

    private String myName;
    private String myAge;
    private String myGender;
    private String myDescription;
    private String dogName;
    private String dogGender;
    private String dogDescription;

    public MyProfileDog() {
    }

    public MyProfileDog(String myName, String myAge, String myGender,
                        String myDescription, String dogName, String dogGender, String dogDescription) {
        this.myName = myName;
        this.myAge = myAge;
        this.myGender = myGender;
        this.myDescription = myDescription;
        this.dogName = dogName;
        this.dogGender = dogGender;
        this.dogDescription = dogDescription;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyAge() {
        return myAge;
    }

    public void setMyAge(String myAge) {
        this.myAge = myAge;
    }

    public String getMyGender() {
        return myGender;
    }

    public void setMyGender(String myGender) {
        this.myGender = myGender;
    }

    public String getMyDescription() {
        return myDescription;
    }

    public void setMyDescription(String myDescription) {
        this.myDescription = myDescription;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }


    public String getDogGender() {
        return dogGender;
    }

    public void setDogGender(String dogGender) {
        this.dogGender = dogGender;
    }

    public String getDogDescription() {
        return dogDescription;
    }

    public void setDogDescription(String dogDescription) {
        this.dogDescription = dogDescription;
    }
}
