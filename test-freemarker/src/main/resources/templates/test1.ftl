<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hello world</title>
</head>
<body>
 hello ${name}!

 <table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>价格</td>
        <td>生日</td>
    </tr>
    <#if studentList??>
    <#list studentList as student>
        <tr>
            <td>${student_index}</td>
            <td <#if student.name == '小明' > style='background:red'  </#if>>${student.name}</td>
            <td>${student.age}</td>
            <td>${student.price}</td>
            <td>${student.birthday?string("yyyy年MM月dd日")}</td>
        </tr>
     </#list>
     </#if>
 </table>

 学生的数量:${studentList?size}
<br>

姓名:${(stuMap['stu1'].name)!''}<br>
年龄:${(stuMap['stu1'].age)!''}

<#list stuMap?keys as key>
   姓名:${stuMap[key].name}<br>
   年龄:${stuMap[key].age}
</#list>
</body>
</html>