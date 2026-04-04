package com.example.trabalho_1_db.models;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String employedId;
    private String fullName;
    private String department;
    private String jobTitle;

    public User(String username, String password, String employedId,
                String fullName, String department, String jobTitle) {
        this.username = username;
        this.password = password;
        this.employedId = employedId;
        this.fullName = fullName;
        this.department = department;
        this.jobTitle = jobTitle;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmployedId() {
        return employedId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDepartment() {
        return department;
    }

    public String getJobTitle() {
        return jobTitle;
    }
}
