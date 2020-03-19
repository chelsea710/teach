package com.xuecheng.test.freemarker;

import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FreemarkerTest {


    @Test
    public void testGenerateHtml() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());

        String path = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(path+"/templates/"));

        Template template = configuration.getTemplate("test1.ftl");

        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, this.getMap());

        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:/test.html"));
        IOUtils.copy(inputStream,fileOutputStream);
        inputStream.close();
        fileOutputStream.close();
    }


    public Map<String,Object> getMap(){
        Map<String,Object> map= new HashMap<>();
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
        return map;
    }


    @Test
    public void testGenerateStringHtml()throws Exception{
        Configuration configuration = new Configuration(Configuration.getVersion());

        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("templateString",this.getHtmlString());

        configuration.setTemplateLoader(stringTemplateLoader);

        Template template = configuration.getTemplate("templateString", "utf-8");

        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, this.getMap());

        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:/test.html"));
        IOUtils.copy(inputStream,fileOutputStream);
        inputStream.close();
        fileOutputStream.close();
    }


    public String getHtmlString(){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Hello world</title>\n" +
                "</head>\n" +
                "<body>\n" +
                " hello ${name}!\n" +
                "\n" +
                " <table>\n" +
                "    <tr>\n" +
                "        <td>序号</td>\n" +
                "        <td>姓名</td>\n" +
                "        <td>年龄</td>\n" +
                "        <td>价格</td>\n" +
                "        <td>生日</td>\n" +
                "    </tr>\n" +
                "    <#if studentList??>\n" +
                "    <#list studentList as student>\n" +
                "        <tr>\n" +
                "            <td>${student_index}</td>\n" +
                "            <td <#if student.name == '小明' > style='background:red'  </#if>>${student.name}</td>\n" +
                "            <td>${student.age}</td>\n" +
                "            <td>${student.price}</td>\n" +
                "            <td>${student.birthday?string(\"yyyy年MM月dd日\")}</td>\n" +
                "        </tr>\n" +
                "     </#list>\n" +
                "     </#if>\n" +
                " </table>\n" +
                "\n" +
                " 学生的数量:${studentList?size}\n" +
                "<br>\n" +
                "\n" +
                "姓名:${(stuMap['stu1'].name)!''}<br>\n" +
                "年龄:${(stuMap['stu1'].age)!''}\n" +
                "\n" +
                "<#list stuMap?keys as key>\n" +
                "   姓名:${stuMap[key].name}<br>\n" +
                "   年龄:${stuMap[key].age}\n" +
                "</#list>\n" +
                "</body>\n" +
                "</html>";
    }
}
