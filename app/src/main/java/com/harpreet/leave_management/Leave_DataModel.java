package com.harpreet.leave_management;

import java.io.StringReader;

public class Leave_DataModel {
    public String e_name;
    public String e_designation;
    public String empid;
    public String email;
    public String Leave_type;
    public String From;
    public String To;
    public String Reason;
    public String status;
    public String l_id;

    Leave_DataModel(String e_name, String e_designation, String empid, String Leave_type, String From, String To, String Reason, String status, String email) {
        this.e_name = e_name;
        this.e_designation = e_designation;
        this.empid = empid;
        this.Leave_type = Leave_type;
        this.From = From;
        this.To = To;
        this.Reason = Reason;
        this.status = status;
        this.email = email;
    }

    Leave_DataModel() {
    }
}
