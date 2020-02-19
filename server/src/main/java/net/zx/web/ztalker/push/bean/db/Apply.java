package net.zx.web.ztalker.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.omg.CORBA.PRIVATE_MEMBER;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Administrator
 * @date 2020/2/14
 * @time 16:28
 */
@Entity
@Table(name = "TB_APPLY")
public class Apply {
    public static final int TYPE_ADD_USER = 1;//添加好友
    public static final int TYPE_ADD_GROUP = 2;//添加群

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    // 描述部分，对我们的申请信息做描述
    // eg: 我想加你为好友！！
    @Column(nullable = false)
    private String description;

    // 附件 可为空
    // 可以附带图片地址，或者其他
    @Column(columnDefinition = "TEXT")
    private String attach;

    // 当前申请的类型
    @Column(nullable = false)
    private int type;

    @Column(nullable = false)
    private String targetId;

    @JoinColumn(name = "applicantId")
    @ManyToOne
    private User applicant;
    @Column(updatable = false, insertable = false)
    private String applicantId;

    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();
}
