package classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class MediaObj{
    private int id;
    private String type;
    private int parentId;
    private String name;
    private String imageUrl;
    private String videoUrl;
    private int seasonNumber;
    private Date startDate;
    private String language;
    private String country;
    private String genre;
    private String description;

    public MediaObj(int id, String type, int parentId, String name, String imageUrl, String videoUrl,
                    int seasonNumber, Date startDate, String language, String country, String genre, String description) {
        this.id = id;
        this.type = type;
        this.parentId = parentId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.seasonNumber = seasonNumber;
        this.startDate = startDate;
        this.language = language;
        this.country = country;
        this.genre = genre;
        this.description = description;
    }

    public static MediaObj setSelfFromResult(ResultSet rs){
        try {
            return new MediaObj(
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getInt("parent_id"),
                    rs.getString("name"),
                    rs.getString("image_url"),
                    rs.getString("video_url"),
                    rs.getInt("season_number"),
                    rs.getDate("start_date"),
                    rs.getString("language"),
                    rs.getString("country"),
                    rs.getString("genre"),
                    rs.getString("description"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void toSqlStatement(PreparedStatement statement) throws SQLException {
        statement.setString(1, this.getType());
        statement.setInt(2, this.getParentId());
        statement.setString(3, this.getName());
        statement.setString(4, this.getImageUrl());
        statement.setString(5, this.getVideoUrl());
        statement.setInt(6, this.getSeasonNumber());
        statement.setDate(7, this.getStartDate());
        statement.setString(8, this.getLanguage());
        statement.setString(9, this.getCountry());
        statement.setString(10, this.getGenre());
        statement.setString(11, this.getDescription());
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }


    public void setType(String type) {
        this.type = type;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString(){
        return "{"+id +","+ type+","+ parentId+","+ name+","+ imageUrl +","+videoUrl +","+
        seasonNumber +","+startDate+","+ language+","+ country+","+genre +","+description+"}";
    }

}