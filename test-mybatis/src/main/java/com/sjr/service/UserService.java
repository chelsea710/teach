package com.sjr.service;

import com.sjr.dao.UserMapper;
import com.sjr.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> selAll() {
        return userMapper.selAll();
    }

    /**
     * 测试批量新增
     * @return 是否成功
     */
    @Transactional
    public String saveAll() {
        List<User> users = new ArrayList<>();
        for(int i =1;i<=50;i++){
            User userSingle = new User();
            userSingle.setUserName("测试"+i);
            userSingle.setPassWord("测试"+i);
            userSingle.setRealName("测试"+i);
            users.add(userSingle);
        }

        //一次插入的条数，也就是分批的list大小
        int pointsDataLimit = 500;
        int listSize=users.size();
        int maxSize=listSize - 1;
        List<User> newList = new ArrayList<User>();//新建一个载体list
        for (int i = 0; i < listSize; i++) {
            //分批次处理
            newList.add(users.get(i));//循环将数据填入载体list
            if (pointsDataLimit == newList.size() || i == maxSize) {  //载体list达到要求,进行批量操作
                //调用批量插入
                userMapper.insertByBatch(newList);
                System.out.println(newList);
                newList.clear();//每次批量操作后,情况载体list,等待下次的数据填入
            }
        }
        return "success";
    }


    public String updateByBatch() {
        List<User> users = userMapper.selUserByUserName("测试%");
        for (User user : users) {
            user.setPassWord("654321");
        }
        //一次插入的条数，也就是分批的list大小
        int pointsDataLimit = 500;
        int listSize=users.size();
        int maxSize=listSize - 1;
        List<User> newList = new ArrayList<User>();//新建一个载体list
        for (int i = 0; i < listSize; i++) {
            //分批次处理
            newList.add(users.get(i));//循环将数据填入载体list
            if (pointsDataLimit == newList.size() || i == maxSize) {  //载体list达到要求,进行批量操作
                //调用批量插入
                userMapper.updateByBatch(newList);
                newList.clear();//每次批量操作后,情况载体list,等待下次的数据填入
            }
        }
        return "success";
    }

    public List<User> testInnerJoinUser() {
        return userMapper.testInnerJoinUser();
    }


    public List<User> testAssociaion() {
        return userMapper.testInnerJoinUser();
    }
}