package com.zx.app.factory.presenter.contact;

import com.zx.app.factory.model.db.User;
import com.zx.app.factory.presenter.BaseContract;
import com.zx.app.factory.presenter.BasePresenter;

/**
 * author Afton
 * date 2020/3/14
 */
public interface PersonalContract  {
    interface Presenter extends BaseContract.Presenter{
       /*获取用户信息*/
        User getUserPersonal();
    }

    interface View extends BaseContract.View<Presenter>{
        /*获取用户Id*/
        String getUserId();
        /*加载数据完成*/
        void onLoadDone(User user);
        /*是否发起聊天*/
        void allowSayHello(boolean isAllow);
        /*设置关注状态*/
        void setFollowStatus(boolean isFollow);
    }
}
