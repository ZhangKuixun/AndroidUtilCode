package com.zhixun.kevin;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.widget.Toast;


import java.lang.reflect.Field;

/**
 * Created by ${张奎勋} on 2018/5/21.
 * tab／导航管理
 */

public class NavHelper<T> {
    private final SparseArray<Tab<T>> tabs = new SparseArray<>();

    private final Context mContext;
    private final int containerId;
    private final FragmentManager fragmentManager;
    private final OnTabChangeListener<T> mListener;

    private Tab<T> currentTab;

    public NavHelper(Context mContext, int containerId, FragmentManager fragmentManager, OnTabChangeListener<T> mListener) {
        this.mContext = mContext;
        this.containerId = containerId;
        this.fragmentManager = fragmentManager;
        this.mListener = mListener;
    }

    /**
     * 添加tab
     */
    public NavHelper<T> add(int menuId, Tab<T> tab) {
        tabs.put(menuId, tab);
        return this;
    }

    /**
     * 获取当前Tab
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    /**
     * 执行点击菜单的操作
     */
    public boolean performClickMenu(int menuId) {
        Tab<T> tab = tabs.get(menuId);
        if (tab != null) {
            doSelect(tab);
            return true;
        }
        return false;
    }

    /**
     * 进行tab的选择操作
     */
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            if (oldTab == tab) {
                //如果当前tab点击的tab，不做任何操作或者刷新
                notifyReselect(tab);
                return;
            }
        }
        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }

    /**
     * Tab切换方法
     */
    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (oldTab != null) {
            if (oldTab.fragment != null) {
                //从界面移除，但在Fragment的缓存中
                ft.detach(oldTab.fragment);
            }
        }

        if (newTab != null) {
            if (newTab.fragment == null) {
                //首次新建并缓存
                Fragment fragment = Fragment.instantiate(mContext, newTab.clx.getName(), null);
                newTab.fragment = fragment;
                ft.add(containerId, fragment, newTab.clx.getName());
            } else {
                ft.attach(newTab.fragment);
            }
        }
        ft.commit();
        notifySelect(newTab, oldTab);
    }

    /**
     * 选择通知回调
     */
    private void notifySelect(Tab<T> newTab, Tab<T> oldTab) {
        if (mListener != null) {
            mListener.onTabChange(newTab, oldTab);
        }

    }

    private void notifyReselect(Tab<T> tab) {
        //TODO 刷新
    }

    public static class Tab<T> {
        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        Class<?> clx;
        public T extra;
        //内部缓存对应的Fragment
        private Fragment fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("RestrictedApi")
    public void disableShiftMode(BottomNavigationView navigationView) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                itemView.setShiftingMode(false);
                itemView.setChecked(itemView.getItemData().isChecked());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Toast.makeText(mContext, "disableShiftMode", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 事件处理回调接口
     */
    public interface OnTabChangeListener<T> {
        void onTabChange(Tab<T> newTab, Tab<T> oldTab);
    }
}