package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import freemarker.template.utility.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RequestMapping("/freemarker")
@Controller
public class FreemarkerController {

    @Autowired
    RestTemplate restTemplate;


    @RequestMapping("/banner")
    public String banner(Map<String,Object> map){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        map.putAll(forEntity.getBody());
        return "index_banner";
    }

    @RequestMapping("/test1")
    public String freemarker(Map<String,Object> map){
        map.put("name","world");
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("小明",25,new Date(),15.36));
        studentList.add(new Student("张三",25,new Date(),15.40));
        map.put("studentList",studentList);
        map.put("stu1",new Student("小明",25,new Date(),15.36));
        map.put("stu2",new Student("张三",25,new Date(),15.40));
        Map<String,Student> stuMap = new LinkedHashMap<>();
        stuMap.put("stu1",new Student("小明",25,new Date(),15.36));
        stuMap.put("stu2",new Student("张三",25,new Date(),15.40));
        map.put("stuMap",stuMap);
        return "test1";
    }
}
