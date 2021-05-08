package com.example.Application.Models;

public class Users {
    private String name, password, studentnumber;

    public Users() {

    }

    public Users(String name, String password, String studentnumber) {
        this.name = name;
        this.password = password;
        this.studentnumber = studentnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentnumber() {
        return studentnumber;
    }

    public void setStudentnumber(String studentnumber) {
        this.studentnumber = studentnumber;
    }
}




