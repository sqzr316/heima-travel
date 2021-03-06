package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;
import com.sun.tools.corba.se.idl.constExpr.GreaterEqual;

public class UserServiceImpl implements UserService {

    private UserDao userDao= new UserDaoImpl();

    /**
     * 注册用户
     * @param user
     */
    @Override
    public boolean regist(User user) {
        //1.根据用户名查询用户对象
        User u = userDao.fingByUsername(user.getUsername());
        //判断u是否为null
        if (u != null) {
            // 存在
            return false;
        }
        //2.将用户信息存入
        //2.1设置激活码
        user.setCode(UuidUtil.getUuid());
        //2.2设置激活状态
        user.setStatus("N");
        userDao.save(user);
        //3.激活邮件正文
        String content = "<a href='http://localhost/travel/user/active?code=" + user.getCode() + "'>点击激活【黑马旅游网】</a>";
        MailUtils.sendMail(user.getEmail(), content, "激活邮件");
        return true;
    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //1.根据激活码查询用户对象
        User user = userDao.findByCode(code);
        if (user != null) {
            //2.调用dao的修改激活状态的方法
            userDao.updateStatus(user);
            return true;
        }
        return false;
    }

    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
