package com.zx.app.factory.presenter.contact;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.zx.app.common.widget.recycler.RecyclerAdapter;
import com.zx.app.factory.data.DataSource;
import com.zx.app.factory.data.helper.UserHelper;
import com.zx.app.factory.model.card.UserCard;
import com.zx.app.factory.model.db.AppDatabase;
import com.zx.app.factory.model.db.User;
import com.zx.app.factory.model.db.User_Table;
import com.zx.app.factory.persistent.Account;
import com.zx.app.factory.presenter.BasePresenter;
import com.zx.app.factory.utils.DiffUiDataCallback;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * author Afton
 * date 2020/3/11
 */
public class ContactPresenter extends BasePresenter<ContactContract.View>
        implements ContactContract.Presenter, DataSource.SucceedCallback<List<UserCard>> {
    public ContactPresenter(ContactContract.View view) {
        super(view);
    }


    @Override
    public void start() {
        super.start();
        /*加载数据库数据*/
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
                        getView().getRecyclerAdapter().replace(tResult);
                        getView().onAdapterDataChanged();
                    }
                })
                .execute();

        /*网络数据*/
        UserHelper.contact(this);
    }

    // 运行到这里的时候是子线程
    @Override
    public void onDataLoaded(final List<UserCard> cards) {
        // 无论怎么操作，数据变更，最终都会通知到这里来
        final ContactContract.View view = getView();
        if (null == view) {
            return;
        }

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                final List<User> users = new ArrayList<>();
                for (UserCard card : cards) {
                    users.add(card.build());
                }
                /*保持到数据库*/
                DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
                definition.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        FlowManager.getModelAdapter(User.class)
                                .saveAll(users);
                    }
                }).build().execute();

                RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
                List<User> old = adapter.getItems();

                /*数据对比*/
                DiffUtil.Callback callback = new DiffUiDataCallback<>(old, users);
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

                /*对比完成后进行数据赋值*/
                getView().getRecyclerAdapter().replace(users);

                /*局部刷新*/
                result.dispatchUpdatesTo(adapter);
                getView().onAdapterDataChanged();
            }
        });
    }

}
