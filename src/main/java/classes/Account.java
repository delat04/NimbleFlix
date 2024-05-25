package classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
public class Account {
    private int id;
    private String username;


    private String password;
    private String type;
    private String fullName;
    private String profilePic;
    private String email;
    private Date dateOfBirth;
    private String aboutMe;

    public Account(int id, String username,String password, String type, String fullName, String profilePic, String email,
                   Date dateOfBirth, String aboutMe) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.type = type;
        this.fullName = fullName;
        this.profilePic = profilePic;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.aboutMe = aboutMe;
    }

    public static Account setSelfFromResult(ResultSet rs){
        try {
            return new Account(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("type"),
                    rs.getString("full_name"),
                    rs.getString("profile_pic"),
                    rs.getString("email"),
                    rs.getDate("date_of_birth"),
                    rs.getString("about_me"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void toSqlStatement(PreparedStatement statement) throws SQLException {
        //statement.setInt(1, this.getId());
        statement.setString(1, this.getUsername());
        statement.setString(2, this.getPassword());
        statement.setString(3, this.getType());
        statement.setString(4, this.getFullName());
        statement.setString(5, this.getProfilePic());
        statement.setString(6, this.getEmail());
        statement.setDate(7, this.getDateOfBirth());
        statement.setString(8, this.getAboutMe());
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getEmail() {
        return email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    //(int id, String username, String type, String fullName, String profilePic, String email,
    //                   Date dateOfBirth, String aboutM
    public String toString(){
        return "{"+id +","+ username+","+password+","+ type+","+ fullName+","+ profilePic +","+email +","+
                dateOfBirth +","+aboutMe +"}";
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
}

