package com.vedas.weightloss.Models;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "User")
public class User {

    @DatabaseField(id = true, columnName = "email")
    private String email;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "password")
    private String password;

    @DatabaseField(columnName = "latitude")
    private String latitude;

    @DatabaseField(columnName = "longitude")
    private String longitude;

    @DatabaseField(columnName = "registerTime")
    private String registerTime;

    @DatabaseField(columnName = "registerType")
    private String registerType;

    @DatabaseField(columnName = "region")
    private String region;

    @DatabaseField(columnName = "isVerified")
    private Boolean isVerified;

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

    @ForeignCollectionField
    private ForeignCollection<PersonalInfoModel> personalInformationdata;


    @ForeignCollectionField
    private ForeignCollection<DayFoodObject> dayFooddata;

    @ForeignCollectionField
    private ForeignCollection<UnitsObject> unitsdata;

    public ForeignCollection<UnitsObject> getUnitsdata() {
        return unitsdata;
    }

    public ForeignCollection<RemainderObject> getRemainderobject() {
        return remainderobject;
    }

    @ForeignCollectionField
    private ForeignCollection<RemainderObject> remainderobject;

    public User() {
    }

    public ForeignCollection<DayFoodObject> getDayFooddata() {
        return dayFooddata;
    }

    public ForeignCollection<PersonalInfoModel> getPersonalInformationdata() {
        return personalInformationdata;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
