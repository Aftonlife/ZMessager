package com.zx.app.ztalker.fragments.assist;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zx.app.common.app.Application;
import com.zx.app.ztalker.R;

import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * author Afton
 * date 2020/2/13
 */
public class PermissionsFragment extends BottomSheetDialogFragment implements EasyPermissions.PermissionCallbacks {

    /*权限回调标识*/
    public static final int RC = 0x0100;

    public PermissionsFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(Objects.requireNonNull(getContext()));
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        FragmentActivity activity = getActivity();
        if (null != activity) {
            haveAll(activity, activity.getSupportFragmentManager());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_permissions, container, false);
        root.findViewById(R.id.btn_submit)
                .setOnClickListener(v -> {
                    /*申请权限*/
                    requestPerm();
                });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshState(getView());
    }

    /**
     * 刷新权限的选中状态
     *
     * @param root 根布局
     */
    private void refreshState(View root) {
        if (null == root) {
            return;
        }
        Context context = getContext();
        root.findViewById(R.id.iv_state_permission_network)
                .setVisibility(haveNetWorkPerm(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.iv_state_permission_read)
                .setVisibility(haveReadPerm(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.iv_state_permission_write)
                .setVisibility(haveWritePerm(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.iv_state_permission_record_audio)
                .setVisibility(haveRecordAudioPerm(context) ? View.VISIBLE : View.GONE);
    }

    /**
     * 网络权限
     *
     * @param context
     * @return
     */
    private static boolean haveNetWorkPerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 外部存储读取权限
     *
     * @param context
     * @return
     */
    private static boolean haveReadPerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 外部存储写入权限
     *
     * @param context
     * @return
     */
    private static boolean haveWritePerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 录音权限
     *
     * @param context
     * @return
     */
    private static boolean haveRecordAudioPerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO,
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 是否拥有全部权限
     *
     * @param context
     * @param manager
     * @return
     */
    public static boolean haveAll(Context context, FragmentManager manager) {
        /*是否具有所有权限*/
        boolean haveAll = haveNetWorkPerm(context)
                && haveReadPerm(context)
                && haveWritePerm(context)
                && haveRecordAudioPerm(context);
        if (!haveAll) {
            show(manager);
        }
        return haveAll;
    }

    /**
     * 显示
     *
     * @param manager
     */
    private static void show(FragmentManager manager) {
        /*通过tag防止重复显示导致弹出框叠加*/
        String tag = PermissionsFragment.class.getName();
        Fragment oldFragment = manager.findFragmentByTag(tag);
        if (null != oldFragment) {
            manager.beginTransaction()
                    .remove(oldFragment)
                    .commitAllowingStateLoss();
        }
        /*显示新的*/
        new PermissionsFragment()
                .show(manager, tag);
    }

    /**
     * 申请权限
     */
    @AfterPermissionGranted(RC)
    private void requestPerm() {
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
        };
        if (EasyPermissions.hasPermissions(Objects.requireNonNull(getContext()), perms)) {
            Application.showToast(R.string.label_permission_ok);
            // Fragment 中调用getView得到跟布局，前提是在onCreateView方法之后
            refreshState(getView());
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.title_assist_permissions), RC, perms);
        }
    }

    /**
     * 已授权
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    /**
     * 未授权
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // 如果权限有没有申请成功的权限存在，则弹出弹出框，用户点击后去到设置界面自己打开权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog
                    .Builder(this)
                    .build()
                    .show();
        }
    }
    /**
     * 权限申请的时候回调的方法，在这个方法中把对应的权限申请状态交给EasyPermissions框架
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 传递对应的参数，并且告知接收权限的处理者是我自己
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
