package net.zx.web.ztalker.push.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Administrator
 * @date 2020/2/14
 * @time 17:19
 */
public class Hib {
    private static final Logger LOGGER = Logger.getLogger(Hib.class.getName());
    // 全局SessionFactory
    private static SessionFactory sessionFactory;

    static {
        // 静态初始化sessionFactory
        init();
    }

    private static void init() {
        // 从hibernate.cfg.xml文件初始化
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            // build 一个sessionFactory
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            // 错误则打印输出，并销毁
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    /**
     * 获取全局的SessionFactory
     *
     * @return SessionFactory
     */
    public static SessionFactory sessionFactory() {
        return sessionFactory;
    }

    /**
     * 从SessionFactory中得到一个Session会话
     *
     * @return Session
     */
    public static Session session() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 关闭sessionFactory
     */
    public static void closeFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    /**
     * 启动
     */
    public static void setup() {
        LOGGER.log(Level.INFO, "Hibernate setup succeed!");
    }

    public interface QueryOnly {
        void query(Session session);
    }

    /**
     * 查询
     */
    public static void queryOnly(QueryOnly query) {
        // 重开一个Session
        Session session = sessionFactory().openSession();
        /*开启事务*/
        final Transaction transaction = session.beginTransaction();
        try {
            // 调用传递进来的接口，
            // 并调用接口的方法把Session传递进去
            query.query(session);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                /*回滚*/
                transaction.rollback();
            } catch (RuntimeException e1) {
                e1.printStackTrace();
            }

        } finally {
            // 无论成功失败，都需要关闭Session
            session.close();
        }
    }

    /**
     * 用户实际操作的接口
     * 返回T
     *
     * @param <T>
     */
    public interface Query<T> {
        T query(Session session);
    }

    // 简化Session操作的工具方法，
    // 具有一个返回值
    public static <T> T query(Query<T> query) {
        // 重开一个Session
        Session session = sessionFactory.openSession();
        /*开启事务*/
        final Transaction transaction = session.beginTransaction();
        T result = null;
        try {
            result = query.query(session);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                /*回滚*/
                transaction.rollback();
            } catch (RuntimeException e1) {
                e1.printStackTrace();
            }

        } finally {
            // 无论成功失败，都需要关闭Session
            session.close();
        }
        return result;
    }
}

