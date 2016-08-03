package com.jackiez.questionhouse.ui.activity.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jackiez.questionhouse.R;
import com.jackiez.questionhouse.listener.OnBackPressListener;
import com.jackiez.questionhouse.listener.OnFinishListener;
import com.jackiez.questionhouse.ui.fragment.base.BaseFragment;
import com.jackiez.questionhouse.ui.widget.LoadAndRetryViewManager;
import com.jackiez.questionhouse.utils.InputMethodUtil;
import com.jackiez.questionhouse.utils.log.AppDebugConfig;

/**
 * @author micle
 * @email zsigui@foxmail.com
 * @date 2015/12/13
 */
public abstract class BaseAppCompatActivity extends BaseAppCompatActivityLog implements View.OnClickListener,
        FragmentManager.OnBackStackChangedListener, OnFinishListener, OnBackPressListener {

    protected Toolbar mToolbar;
    private TextView tvTitle;

    protected boolean mNeedWorkCallback = false;
    // 封装加载和等待等页面的管理器对象
    protected LoadAndRetryViewManager mViewManager;
    protected Handler mHandler = new Handler(Looper.myLooper());

    // 保存当前栈顶对象
    protected Fragment mCurTopFragment;
    // fragment处理onActivityResult
    private Fragment mFragmentForResult;
    private int mFragmentRequestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int statusColor = getStatusBarColor();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(statusColor);
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        initView();
        mToolbar = getViewById(R.id.toolbar);
        if (mToolbar != null) {
            final View backIcon = mToolbar.findViewById(R.id.iv_bar_back);
            if (backIcon != null) {
                backIcon.setOnClickListener(this);
            }
            initMenu(mToolbar);
        }
        processLogic();
        mNeedWorkCallback = true;
    }

    /**
     * 重写此类设置状态栏颜色
     *
     * @return
     */
    protected int getStatusBarColor() {
        return getResources().getColor(R.color.co_status_bar_bg);
    }

    /**
     * this will be called before {@code super.onCreate()} when you override {@code onCreate()} method <br />
     * Note: all views initial work have better implemented here
     */
    protected abstract void initView();

    protected abstract void processLogic();

    protected void initMenu(@NonNull Toolbar toolbar) {
    }

    @SuppressWarnings("unchecked")
    protected <V extends View> V getViewById(@IdRes int id) {
        if (mViewManager == null) {
            View child = findViewById(id);
            return (child != null ? (V) child : null);
        } else {
            return getViewById(mViewManager.getContentView(), id);
        }
    }

    @SuppressWarnings("unchecked")
    protected <V extends View> V getViewById(View v, @IdRes int id) {
        View child = v.findViewById(id);
        return (child != null ? (V) child : null);
    }

    public void setBarTitle(@StringRes int res) {
        setBarTitle(getResources().getString(res));
    }

    public void setBarTitle(String title) {
        if (mToolbar != null) {
            if (tvTitle == null) {
                tvTitle = getViewById(mToolbar, R.id.tv_bar_title);
            }
            if (tvTitle != null) {
                tvTitle.setText(title);
            }
        }
    }

    protected void initViewManger(@LayoutRes int layoutResID) {
        mViewManager = LoadAndRetryViewManager.generate(this, layoutResID);
        setContentView(mViewManager.getContainer());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_bar_back) {
            onBackPressed();
        }
    }

    public void replaceFragWithTitle(@IdRes int id, BaseFragment newFrag, String title) {
        newFrag.setTitleName(title);
        replaceFrag(id, newFrag);
        setBarTitle(title);
    }

    public void replaceFragWithTitle(@IdRes int id, BaseFragment newFrag, String tag, String title) {
        newFrag.setTitleName(title);
        replaceFrag(id, newFrag, tag, true);
        setBarTitle(title);
    }

    public void replaceFragWithTitle(@IdRes int id, BaseFragment newFrag, String title,
                                     boolean isAddToBackStack) {
        newFrag.setTitleName(title);
        replaceFrag(id, newFrag, isAddToBackStack);
        setBarTitle(title);
    }

    public void replaceFrag(@IdRes int id, Fragment newFrag) {
        replaceFrag(id, newFrag, newFrag.getClass().getSimpleName(), true);
    }

    public void replaceFrag(@IdRes int id, Fragment newFrag, boolean isAddToBackStack) {
        replaceFrag(id, newFrag, newFrag.getClass().getSimpleName(), isAddToBackStack);
    }

    public void replaceFrag(@IdRes int id, Fragment newFrag, String tag, boolean isAddToBackStack) {
        if (isFinishing()) {
            // Activity处于Finished中
            return;
        }
        // 查找特定tag的Fragment
        Fragment f = getSupportFragmentManager().findFragmentByTag(tag);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (f != null) {
            // 查找的tag已经存在，直接显示
            ft.show(f);
        } else {
            // 查找的tag不存在
            if (newFrag == null) {
                // 需要新添加的Fragment为null，不处理
                return;
            }
            if (newFrag.isAdded()) {
                // 需要新添加的Fragment已经添加，直接显示
                ft.show(newFrag);
            } else {
                // 没有添加，调用replace方法
                ft.replace(id, newFrag, tag);
            }
        }
        if (isAddToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 重新<code>attach</code>添加Fragment到视图资源ID处，会先<code>detach</code>该ID处已拥有的视图，
     * 然后根据tag查找该Fragment是否已经存在且被add了，是则直接<code>attach</code>，否则执行<code>add<code/>，
     * 最后执行<code>show</code>显示视图
     *
     * @param id      进行添加的资源ID名，会先判断是否已存在该ID下的Fragment，存在则先<code>Detach</code>
     * @param newFrag 需要<code>attach</code>的新Fragment名
     * @param tag     当Fragment此前未被<code>add<code/>，需要先进行添加设置的Tag
     */
    public void reattachFrag(@IdRes int id, Fragment newFrag, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f = getSupportFragmentManager().findFragmentById(id);
        if (f != null && f == newFrag) {
            // 已经存在
            if (f.isDetached()) {
                // 不存在添加状态，先添加
                ft.attach(f);
            }
        } else {
            // 没有存在
            if (f != null && !f.isDetached()) {
                ft.detach(f);
            }
            Fragment self_f = getSupportFragmentManager().findFragmentByTag(tag);
            if (self_f == null) {
                // tag 找不到时，添加
                ft.add(id, newFrag, tag);
            } else {
                // tag 找到时，继续判断
                if (self_f != newFrag) {
                    // 并非是要添加的，移除，然后添加新的
                    ft.detach(self_f);
                    ft.add(id, newFrag, tag);
                } else if (self_f.isDetached()) {
                    // 是要添加的，但是不存在添加状态，附着
                    ft.attach(self_f);
                }
            }
        }
        ft.show(newFrag);
        ft.commitAllowingStateLoss();
    }

    /**
     * 隐藏当前栈顶Fragment
     */
//	public void reshowFrag(@IdRes int id, Fragment newFrag, String newTag, String... oldTags) {
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		Fragment f;
//		for (String oldTag : oldTags) {
//			f = getSupportFragmentManager().findFragmentByTag(oldTag);
//			if (f != null && !f.isHidden()) {
//				ft.hide(f);
//				f.setUserVisibleHint(false);
//			}
//		}
//		f = getSupportFragmentManager().findFragmentByTag(newTag);
//		if (f != null) {
//			ft.show(f);
//			f.setUserVisibleHint(true);
//		} else {
//            ft.add(id, newFrag, newTag);
//            newFrag.setUserVisibleHint(true);
//		}
//		ft.commit();
//	}

    /**
     * 执行Fragment出栈操作，栈中有Fragment时返回true，否则返回false
     */
    public boolean popFrag() {
        if (isFinishing()) {
            return false;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            if (isMainThread()) {
                if (getTopFragment() != null) {
                    mCurTopFragment.onPause();
                }
                return getSupportFragmentManager().popBackStackImmediate();
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (getTopFragment() != null) {
                            mCurTopFragment.onPause();
                        }
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                });
            }
            return true;
        }
        return false;
    }

    /**
     * 根据tag判断fragment是否处于栈顶
     */
    public boolean isTopFragment(String tag) {
        return getTopFragment() != null && tag.equals(getTopFragment().getTag());
    }

    /**
     * 获取当前栈顶Fragment
     */
    public Fragment getTopFragment() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            mCurTopFragment = getSupportFragmentManager()
                    .findFragmentByTag(getSupportFragmentManager().getBackStackEntryAt(count - 1).getName());
        }
        return mCurTopFragment;
    }

    /**
     * 执行fragment出栈 或者 activity终结操作
     */
    public boolean onBack() {
        InputMethodUtil.hideSoftInput(this);
        if (getTopFragment() != null && getTopFragment() instanceof OnBackPressListener
                && ((OnBackPressListener) getTopFragment()).onBack()) {
            // back事件被处理
            return false;
        }
        if (!popFrag() && !isFinishing()) {
            mNeedWorkCallback = false;
            doBeforeFinish();
            finish();
        } else {
            if (getTopFragment() instanceof BaseFragment) {
                setBarTitle(((BaseFragment) getTopFragment()).getTitleName());
            }
        }
        return true;
    }

    protected void doBeforeFinish() {
        AppDebugConfig.d(AppDebugConfig.TAG_DEBUG_INFO, "doBeforeFinish called");
    }

    /**
     * 判断是否处于主线程
     */
    private boolean isMainThread() {
        return Thread.currentThread() == getMainLooper().getThread();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void release() {
        mNeedWorkCallback = false;
        mToolbar = null;
        // 封装加载和等待等页面的管理器对象
        mViewManager = null;
        mCurTopFragment = null;
        // fragment处理onActivityResult
        mFragmentForResult = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mFragmentRequestCode && mFragmentForResult != null) {
            // 交由 Fragment 处理
            mFragmentForResult.onActivityResult(requestCode, resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackStackChanged() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            mCurTopFragment = getSupportFragmentManager()
                    .findFragmentByTag(getSupportFragmentManager().getBackStackEntryAt(count - 1).getName());
        }
    }
}
