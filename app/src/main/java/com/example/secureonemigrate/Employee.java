package com.example.secureonemigrate;

import android.os.Parcel;
import android.os.Parcelable;

public class Employee implements Parcelable {
    String name;
    String designation;
    String empID;
    String phoneNum;
    String photoLink, qrLink;
    String site;

    public Employee() {
    }

    public Employee(String name, String designation, String empID, String phoneNum, String photoLink, String qrLink, String site) {
        this.name = name;
        this.designation = designation;
        this.empID = empID;
        this.phoneNum = phoneNum;
        this.photoLink = photoLink;
        this.qrLink = qrLink;
        this.site = site;
    }

    protected Employee(Parcel in) {
        name = in.readString();
        designation = in.readString();
        empID = in.readString();
        phoneNum = in.readString();
        photoLink = in.readString();
        qrLink = in.readString();
        site = in.readString();
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

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public String getQrLink() {
        return qrLink;
    }

    public void setQrLink(String qrLink) {
        this.qrLink = qrLink;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
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
        parcel.writeString(photoLink);
        parcel.writeString(qrLink);
        parcel.writeString(site);
    }
}

