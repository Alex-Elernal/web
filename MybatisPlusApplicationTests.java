package com.szj;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szj.dao.UserDao;
import com.szj.pojo.User;
import com.szj.pojo.query.UserQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class MybatisPlusApplicationTests {

    @Autowired
    private UserDao userDao;



    //TODO 新增
    @Test
    void testInsert(){
        User user = new User();
        user.setName("世界树");
        user.setPassword("666");
        user.setTel("114514");
        user.setAge(100);
        userDao.insert(user);
    }

    //TODO 删除
    @Test
    void testDeleteById(){
        userDao.deleteById(1594634225909358594L);
    }

    //TODO 修改
    @Test
    void testUpdateById(){
        User user = new User();
        user.setName("世界树");
        user.setPassword("666");
        user.setTel("114514");
        user.setAge(10000);
        user.setId(1594635070780555265L);
        userDao.updateById(user);
    }

    //TODO 按ID查询
    @Test
    void testSelectById(){
        User user = userDao.selectById(1);
        System.out.println(user);
    }

    //TODO 查询全部
    @Test
    void testSelectList() {
        List<User> userList = userDao.selectList(null);
        System.out.println(userList);
    }

    //TODO 分页查询
    @Test
    void testSelectByPage(){
        //配置分页拦截器
        IPage page = new Page(1,2);
        userDao.selectPage(page,null);
        System.out.println("当前页码值" + page.getCurrent());
        System.out.println("每页显示数" + page.getSize());
        System.out.println("一共多少页" + page.getPages());
        System.out.println("一共多少条数据" + page.getTotal());
        System.out.println("数据" + page.getRecords());
    }

    //TODO 条件查询
    @Test
    void testSelectByWrapper(){
        /*
        普通写法不需要加泛型，lambda表达式需要添加泛型
        */
        //方式一:按条件查询
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.lt("age",18);
        List list = userDao.selectList(wrapper);
        System.out.println(list);

        //方式二:Lambda表达式按条件查询
        QueryWrapper<User> wrapper1 = new QueryWrapper();
        wrapper1.lambda().lt(User::getAge,100);
        List<User> users = userDao.selectList(wrapper1);
        System.out.println(users);

        //方式三:Lambda表达式条件查询
        LambdaQueryWrapper<User> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.lt(User::getAge,200);
        wrapper2.gt(User::getAge,5);
        //多条件的情况下也可以选择链式编程
        //wrapper2.lt(User::getAge,200).gt(User::getAge,5);//And情况
        //wrapper2.lt(User::getAge,200).or().gt(User::getAge,5);//Or情况
        List<User> users1 = userDao.selectList(wrapper2);
        System.out.println(users1);

        //TODO 条件查询添加空值判定
        //模拟前端发送的条件
        UserQuery uq = new UserQuery();
        uq.setAge(20);
        uq.setAge2(50);


        LambdaQueryWrapper<User> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.lt(uq.getAge2() != null,User::getAge,uq.getAge2());
        wrapper3.gt(uq.getAge() != null,User::getAge,uq.getAge());
        List<User> users2 = userDao.selectList(wrapper3);
        System.out.println(users2);
    }

    //TODO 查询投影
    @Test
    void testSelectProjection(){
        //普通写法
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.select("name","age","tel");
        List list = userDao.selectList(wrapper);
        System.out.println(list);
        //lambda表达式写法
        LambdaQueryWrapper<User> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.select(User::getName,User::getAge,User::getTel);
        List<User> users = userDao.selectList(wrapper1);
        System.out.println(users);

        //存在聚合函数
        QueryWrapper<User> wrapper2 = new QueryWrapper<>();
        wrapper2.select("count(*) as count","max(age)");
        List<Map<String, Object>> maps = userDao.selectMaps(wrapper2);
        System.out.println(maps);

        //按照某个字段分组
        QueryWrapper<User> wrapper3 = new QueryWrapper<>();
        wrapper3.select("count(*) as count","tel");
        wrapper3.groupBy("tel");
        List<Map<String, Object>> maps1 = userDao.selectMaps(wrapper3);
        System.out.println(maps1);
    }


    //TODO 等匹配的条件查询
    @Test
    void testConditionEquals(){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName,"jerry").eq(User::getPassword,"jerry");
        User user = userDao.selectOne(wrapper);
        System.out.println(user);
    }

    //TODO 范围查询
    @Test
    void testScope(){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(User::getAge,10,30);//注意前面是小数后面是大数
        List<User> users = userDao.selectList(wrapper);
        System.out.println(users);
    }

    //TODO 模糊匹配
    @Test
    void testLike(){
        //likeRight,likeLeft模糊匹配时百分号的位置
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(User::getName,"J");
        List<User> users = userDao.selectList(wrapper);
        System.out.println(users);
    }

}
