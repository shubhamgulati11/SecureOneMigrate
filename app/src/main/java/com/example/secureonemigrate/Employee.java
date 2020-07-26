package com.example.secureonemigrate;

import android.os.Parcel;
import android.os.Parcelable;

public class Employee implements Parcelable {
    public Employee(String name, String designation, String empID, String phoneNum) {
        this.name = name;
        this.designation = designation;
        this.empID = empID;
        this.phoneNum = phoneNum;
    }

    public Employee() {
    }

    protected Employee(Parcel in) {
        name = in.readString();
        designation = in.readString();
        empID = in.readString();
        phoneNum = in.readString();
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    String name;
    String designation;
    String empID;
    String phoneNum;

    public String toString(){
        return "{name:"+name+",designation:"+designation+",empID:"+empID+",phoneNum:"+phoneNum+"}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(designation);
        parcel.writeString(empID);
        parcel.writeString(phoneNum);
    }
}

