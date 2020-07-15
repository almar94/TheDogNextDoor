package com.android.thedognextdoor.RegistrationP;

public class SignUpObj {

    String Email, Password, Uid;

    public SignUpObj() {
    }

    public SignUpObj(String email, String password, String uid) {
        Email = email;
        Password = password;
        Uid = uid;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

}
