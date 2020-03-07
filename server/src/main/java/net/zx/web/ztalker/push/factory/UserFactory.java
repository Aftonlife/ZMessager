package net.zx.web.ztalker.push.factory;

import com.google.common.base.Strings;
import net.zx.web.ztalker.push.bean.db.User;
import net.zx.web.ztalker.push.bean.db.UserFollow;
import net.zx.web.ztalker.push.utils.Hib;
import net.zx.web.ztalker.push.utils.TextUtil;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
     * @param user   用户
     * @param pushId 设备Id
     * @return User
     */
    public static User bindPushId(User user, String pushId) {
        if (Strings.isNullOrEmpty(pushId)) {
            return null;
        }
        /*查询绑定了该pushId的用户（除了自己）清空pushId 防止推送混乱*/
        Hib.queryOnly(session -> {
            @SuppressWarnings("unchecked")
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
     * @param account  手机号
     * @param password 密码
     * @return 用户
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
     * @param account  手机号
     * @param password 密码
     * @param name     用户名
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
     * @param account  手机号
     * @param password 密码
     * @param name     用户名
     * @return 用户
     */
    private static User createUser(String account, String password, String name) {
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
     * @param user 用户信息
     * @return 登录后的信息，token
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
     * @param password 原文
     * @return 密码
     */
    private static String encodePassword(String password) {
        // 密码去除首位空格
        password = password.trim();
        // 进行MD5非对称加密，加盐会更安全，盐也需要存储
        password = TextUtil.getMD5(password);
        // 再进行一次对称的Base64加密，当然可以采取加盐的方案
        return TextUtil.encodeBase64(password);
    }

    /**
     * 获取我的联系人
     *
     * @param self 自己
     * @return 我的联系人列表
     */
    public static List<User> contacts(User self) {
        return Hib.query(session -> {
            /*重新加载一次用户信息到self中，和当前的session绑定*/
            session.load(self, self.getId());
            /*获取我关注的人*/
            Set<UserFollow> flows = self.getFollowing();
            /*转换*/
            return flows.stream()
                    .map(UserFollow::getTarget)
                    .collect(Collectors.toList());
        });
    }


    /**
     * 关注人的操作（类似添加好友）
     *
     * @param origin 发起者
     * @param target 被关注的人
     * @param alias  别名
     * @return 被关注人的信息
     */
    public static User follow(User origin, User target, String alias) {
        UserFollow userFollow = getUserFollow(origin, target);
        if (null != userFollow) {
            /*已关注（已添加为好友）直接返回*/
            return userFollow.getTarget();
        }
        return Hib.query(session -> {
            /*想要操作懒加载数据，需要重新load一次*/
            session.load(origin, origin.getId());
            session.load(target, target.getId());

            /*我关注的人也会关注我，加两条数据（类似我添加别人好友，别人同意了我也就是他的好友，避免两个人都要添加）*/
            UserFollow originFollow = new UserFollow();
            originFollow.setOrigin(origin);
            originFollow.setTarget(target);
            /*默认发起者可以添加备注名，接收者不可以*/
            originFollow.setAlias(alias);

            /*发起者是他，我是被关注的人的记录*/
            UserFollow targetFollow = new UserFollow();
            targetFollow.setOrigin(target);
            targetFollow.setTarget(origin);

            /*保持到数据库*/
            session.save(originFollow);
            session.save(targetFollow);

            return target;
        });
    }

    /**
     * 查询两人是否已关注（类似是否添加了好友）
     *
     * @param origin 发起者
     * @param target 被关注的人
     * @return 中间类UserFollow
     */
    public static UserFollow getUserFollow(User origin, User target) {
        return Hib.query(session -> (UserFollow) session
                .createQuery("from UserFollow where originId=:originId and targetId=:targetId")
                .setParameter("originId", origin.getId())
                .setParameter("targetId", target.getId())
                .setMaxResults(1)
                .uniqueResult()
        );
    }

    /**
     * 根据用户名搜索
     *
     * @param name 查询的name，允许为空
     * @return 查询到的用户集合，如果name为空，则返回最近的用户
     */
    @SuppressWarnings("unchecked")
    public static List<User> search(String name) {
        if (Strings.isNullOrEmpty(name)) {
            /*防止name==null*/
            name = "";
        }
        /*模糊匹配*/
        final String searchName = "%" + name + "%";

        return Hib.query(session -> {
            // 查询的条件：name忽略大小写，并且使用like（模糊）查询；
            // 头像和描述必须完善才能查询到
            return (List<User>) session.createQuery(
                    "from User where lower(name) like :name and portrait is not null and description is not null")
                    .setParameter("name", searchName)
                    .setMaxResults(20)//最多20条
                    .list();

        });
    }
}
