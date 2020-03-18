package com.sjr.model;

import lombok.Data;
import lombok.ToString;

/**
 * 学生连接课程对象
 */
@Data
@ToString
public class UserLinkeCourse {
    private Integer id;
    private Integer userId;
    private Integer courseId;
}
