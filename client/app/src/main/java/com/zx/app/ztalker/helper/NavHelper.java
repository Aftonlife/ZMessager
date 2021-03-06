package com.zx.app.ztalker.helper;

import android.content.Context;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * author Afton
 * date 2020/2/10
 * 解决对Fragment的调度与重用问题，
 * 达到最优的Fragment切换
 */
public class NavHelper<T> {

    /*所有的Tab集合*/
    private final SparseArray<Tab<T>> tabs = new SparseArray<>();

    /*用于初始化的参数*/
    private final Context context;
    private final FragmentManager fragmentManager;
    private final int containerId;
    private final onTabChangeListener listener;

    /*当前选中的Tab*/
    private Tab<T> currentTab;

    public NavHelper(Context context, FragmentManager fragmentManager, int containerId, onTabChangeListener listener) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.listener = listener;
    }

    /**
     * 添加Tab menuId为唯一标识
     * @param menuId Tab对应的菜单Id
     * @param newTab tab
     */
    public NavHelper<T> add(int menuId, Tab<T> newTab) {
        tabs.put(menuId, newTab);
        return this;
    }

    /**
     * 获取当前Tab
     *
     * @return 当前Tab
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    /**
     * Menu被点击
     *
     * @param itemId 菜单Id
     * @return 是否能处理这个Tab
     */
    public boolean performClickMenu(int itemId) {
        Tab<T> tab = tabs.get(itemId);
        if (null != tab) {
            doSelectTab(tab);
            return true;
        }
        return false;
    }

    /**
     * 选中Tab事件
     *
     * @param tab tab
     */
    private void doSelectTab(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (null != currentTab) {
            oldTab = currentTab;
            /*再次点击*/
            if (tab == oldTab) {
                notifyTabReselect(oldTab);
                return;
            }
        }
        currentTab = tab;
        doTabChange(currentTab, oldTab);
    }

    /**
     * 切换Fragment主要逻辑
     * Fragment：add:添加；remove:直接移除不缓存；replace：先移除再添加（remove+add）；hide/show：隐藏显示不移除；
     * attach/detach：布局中移除，但是存储到缓存列表中，不会被测量，但可以重用
     * fragmentManager.getFragmentFactory().instantiate 通过Fragment的类名新建Framgent
     *
     * @param newTab 新的
     * @param oldTab 旧的
     */
    private void doTabChange(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (null != oldTab) {
            /*从布局移除，缓存保留*/
            ft.detach(oldTab.fragment);
        }
        if (null != newTab) {
            if (null == newTab.fragment) {
                /*第一次新建,通过fragment的类名新建*/
                newTab.fragment = fragmentManager.getFragmentFactory().instantiate(context.getClassLoader(), newTab.cls.getName());
                ft.add(containerId, newTab.fragment, newTab.cls.getName());
            } else {
                /*从缓存提取，加入布局*/
                ft.attach(newTab.fragment);
            }
        }
        ft.commit();
        notifyTabSelect(newTab, oldTab);
    }

    /**
     * 回调我们的监听器
     * @param newTab 新的
     * @param oldTab 旧的
     */
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {
        if (null != listener) {
            listener.onTabChange(newTab, oldTab);
        }
    }

    private void notifyTabReselect(Tab<T> tab) {
        // TODO 二次点击Tab所做的操作
    }

    /**
     * 我们的所有的Tab基础属性
     *
     * @param <T> 范型的额外参数
     */
    public static class Tab<T> {
        public Tab(Class<?> cls, T extra) {
            this.cls = cls;
            this.extra = extra;
        }

        // Fragment对应的Class信息
        public Class<?> cls;
        // 额外的字段，用户自己设定需要使用
        public T extra;
        Fragment fragment;
    }

    /**
     * Tab切换回调接口
     */
    public interface onTabChangeListener<T> {
        void onTabChange(Tab<T> newTab, Tab<T> oldTab);
    }
}
