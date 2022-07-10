package jdbc.preparedStatement;

public class User {
    private int userId;
    private String fullName;
    private String userType;
    private java.sql.Date addedTime;

    public User() {
        super();
    }

    public User(int userId, String fullName, String userType, java.sql.Date addedTime) {
        this.userId = userId;
        this.fullName = fullName;
        this.userType = userType;
        this.addedTime = addedTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", userType='" + userType + '\'' +
                ", addedTime=" + addedTime +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public java.sql.Date getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(java.sql.Date addedTime) {
        this.addedTime = addedTime;
    }
}
