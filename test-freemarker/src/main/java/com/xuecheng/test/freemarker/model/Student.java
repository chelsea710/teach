package com.xuecheng.test.freemarker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {
    private String name;
    private Integer age;
    private Date birthday;
    private Double price;
}
