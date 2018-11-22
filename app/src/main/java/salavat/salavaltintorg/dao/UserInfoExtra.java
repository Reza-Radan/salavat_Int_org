package salavat.salavaltintorg.dao;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by masoomeh on 12/14/17.
 */
@Entity
public class UserInfoExtra {

    @PrimaryKey
    int userId;
    String occupation, birthday,introducer, education, country, city, national_id ,salavat_num;
    boolean group_head;

    public UserInfoExtra(int userId, String occupation, String birthday, String introducer, String education, String country, String city, String national_id, String salavat_num, boolean group_head) {
        this.userId = userId;
        this.occupation = occupation;
        this.birthday = birthday;
        this.introducer = introducer;
        this.education = education;
        this.country = country;
        this.city = city;
        this.national_id = national_id;
        this.salavat_num = salavat_num;
        this.group_head = group_head;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIntroducer() {
        return introducer;
    }

    public void setIntroducer(String introducer) {
        this.introducer = introducer;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public String getSalavat_num() {
        return salavat_num;
    }

    public void setSalavat_num(String salavat_num) {
        this.salavat_num = salavat_num;
    }

    public boolean isGroup_head() {
        return group_head;
    }

    public void setGroup_head(boolean group_head) {
        this.group_head = group_head;
    }
}
