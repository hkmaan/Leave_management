package com.harpreet.leave_management;

public class Emp_data {
    public String designation;
    public String email;
    public String password;
    public String emp_id;
    public String emp_name;
    public String gender;
    public  String DOB;
    public  String Phoneno;
    public  String Address;
    public String bloodgrp;
    public  String image;

    public  Emp_data(String designation , String email ,String password ,String emp_id ,String emp_name,String gender,String DOB,String Phoneno, String Address,String bloodgrp)
    {
        this.designation=designation;
        this.email=email;
        this.password=password;
        this.emp_id=emp_id;
        this.emp_name=emp_name;
        this.gender=gender;
        this.DOB=DOB;
        this.Phoneno=Phoneno;
        this.Address=Address;
        this.bloodgrp=bloodgrp;




    }
    public Emp_data(){

    }

}
