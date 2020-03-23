package com.sjr.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class User {
    private Integer id;
    private String userName;
    private String passWord;
    private String realName;
    private UserLinkeCourse userLinkeCourses;
    private List<Course> courses;
}
