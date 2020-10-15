package com.example.ecommerceapp.Model;

public class Courier {
    private String name,nic,phonenumber,email,colombosuburb,password,image;

    private Courier(){

    }

    public Courier(String name, String nic, String phonenumber, String email, String colombosuburb, String password, String image) {
        this.name = name;
        this.nic = nic;
        this.phonenumber = phonenumber;
        this.email = email;
        this.colombosuburb = colombosuburb;
        this.password = password;
        this.image = image;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getColombosuburb() {
        return colombosuburb;
    }

    public void setColombosuburb(String colombosuburb) {
        this.colombosuburb = colombosuburb;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
