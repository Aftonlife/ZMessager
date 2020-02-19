package net.zx.web.ztalker.push.factory;


import com.google.common.base.Strings;
import net.zx.web.ztalker.push.bean.db.User;
import net.zx.web.ztalker.push.utils.Hib;
import net.zx.web.ztalker.push.utils.TextUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.UUID;

/**
 * @author Administrator
 * @date 2020/2/14
 * @time 19:09
 * 数据操作，控制数据库
 */
public class UserFactory {

    /*通过token查询User， 只能自己使用，查询的信息是个人信息，非他人信息*/
    public static User findByToken(String token) {
        return Hib.query(session -> (User) session.createQuery("from User where token=:token")
                .setParameter("token", token)
                .uniqueResult());
    }

    /*通过phone查询User*/
    public static User findByPhone(String phone) {
        return Hib.query(session -> (User) session.createQuery("from User where phone=:phone")
                .setParameter("phone", phone)
                .uniqueResult());
    }

    /*通过name查询User*/
    public static User findByName(String name) {
        return Hib.query(session -> (User) session.createQuery("from User where name=:name")
                .setParameter("name", name)
                .uniqueResult());
    }

    /*通过id查询User*/
    public static User findById(String id) {
        // 通过Id查询，更方便
        return Hib.query(session -> (User) session.createQuery("from User where id=:id")
                .setParameter("id", id)
                .uniqueResult());
    }

    /*更新用户信息*/
    public static User update(User user) {
        return Hib.query(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }

    /**
     * 绑定pushId
     *
     * @param user
     * @param pushId
     * @return
     */
    public static User bindPushId(User user, String pushId) {
        if (Strings.isNullOrEmpty(pushId)) {
            return null;
        }
        /*查询绑定了该pushId的用户（除了自己）清空pushId 防止推送混乱*/
        Hib.queryOnly(session -> {
            List<User> list = (List<User>) session.createQuery("from User where pushId=:pushId and id!=:userId")
                    .setParameter("pushId", pushId)
                    .setParameter("userId", user.getId())
                    .list();

            for (User u : list) {
                u.setPushId(null);
                session.saveOrUpdate(u);
            }
        });
        /*如果已经绑定了该pushId就直接返回，否则更新pushId*/
        if (pushId.equalsIgnoreCase(user.getPushId())) {
            return user;
        } else {
            user.setPushId(pushId);
            return update(user);
        }
    }

    /**
     * 账号密码登录
     *
     * @param account
     * @param password
     * @return
     */
    public static User login(String account, String password) {
        final String accountStr = account.trim();
        /*密码按相同的方法加密*/
        final String passwordStr = encodePassword(password);
        /*查询是否存在用户*/
        User user = Hib.query(session ->
                (User) session.createQuery(" from User where phone=:phone and password=:password")
                        .setParameter("phone", accountStr)
                        .setParameter("password", passwordStr)
                        .uniqueResult());
        if (null != user) {
            user = login(user);
        }
        return user;
    }

    /**
     * 用户注册
     * 注册的操作写入数据库，并返回数据库中的User信息
     *
     * @param account
     * @param password
     * @param name
     */
    public static User register(String account, String password, String name) {
        account = account.trim();
        password = encodePassword(password);
        User user = createUser(account, password, name);
        if (null != user) {
            user = login(user);
        }
        return user;
    }

    /**
     * 注册部分的新建用户逻辑
     *
     * @param account
     * @param password
     * @param name
     * @return
     */
    public static User createUser(String account, String password, String name) {
        User user = new User();
        user.setPhone(account);
        user.setPassword(password);
        user.setName(name);
        return Hib.query(session -> {
            session.save(user);
            return user;
        });
    }

    /**
     * 把一个User进行登录操作
     * 本质上是对Token进行操作
     *
     * @param user
     * @return
     */
    private static User login(User user) {
        // 使用一个随机的UUID值充当Token
        String newToken = UUID.randomUUID().toString();
        newToken = TextUtil.encodeBase64(newToken);
        user.setToken(newToken);
        /*更新Token*/
        return update(user);
    }

    /**
     * 密码加密
     *
     * @param password
     * @return
     */
    private static String encodePassword(String password) {
        // 密码去除首位空格
        password = password.trim();
        // 进行MD5非对称加密，加盐会更安全，盐也需要存储
        password = TextUtil.getMD5(password);
        // 再进行一次对称的Base64加密，当然可以采取加盐的方案
        return TextUtil.encodeBase64(password);
    }
}
