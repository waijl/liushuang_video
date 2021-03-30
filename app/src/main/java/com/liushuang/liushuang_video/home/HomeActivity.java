package com.liushuang.liushuang_video.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.liushuang.liushuang_video.AppManager;
import com.liushuang.liushuang_video.FragmentManagerWrapper;
import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.login.HeaderActivity;
import com.liushuang.liushuang_video.login.OnLoginListener;
import com.liushuang.liushuang_video.login.OnTouXiangSelectedListener;
import com.liushuang.liushuang_video.search.SearchActivity;
import com.liushuang.liushuang_video.base.BaseActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * APP首页
 * 实现了ToolBar与侧滑框DrawerLayout的绑定（单击打开或关闭）
 * 同时在首页Activity中主体实现运用了Fragment,即各个页面的切换仅仅是Fragment的改变
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "HomeActivity";
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private MenuItem mPreItem;
    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    private LinearLayout mMenuHome;
    private LinearLayout mMenuNovel;
    private LinearLayout mMenuMe;
    private HomeFragment mHomeFragment = new HomeFragment();
    private NovelFragment mNovelFragment = new NovelFragment();
    private MeFragment mMeFragment = new MeFragment();
    private CircleImageView mCircleImageView;
    private TextView mMyname;
    private LinearLayout mPreMenuItem;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        setSupportActionBar();
        setActionBarIcon(R.drawable.ic_drawer_home);
        setTitle("首页");

        mDrawerLayout = bindViewId(R.id.drawer_layout);
        mNavigationView = bindViewId(R.id.navigation_view);

        mMenuHome = bindViewId(R.id.menu_main);
        mMenuNovel = bindViewId(R.id.menu_novel);
        mMenuMe = bindViewId(R.id.menu_me);
//        mMenuHome.setPressed(true);
        mMenuHome.setSelected(true);
        mPreMenuItem = mMenuHome;
//        mCircleImageView = bindViewId(R.id.profile_image);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar,R.string.drawer_open,R.string.drawer_close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        View headerView = mNavigationView.inflateHeaderView(R.layout.navigation_header);
        mCircleImageView = headerView.findViewById(R.id.profile_image);
        mMyname = headerView.findViewById(R.id.id_tv_myname);
        AppManager.getLoginUtils().setOnLoginListener(new OnLoginListener() {
            @Override
            public void onLogin(String username) {
                mMyname.setText(username);
                mCircleImageView.setImageResource(R.drawable.waiwai);
            }
        });
        AppManager.getLoginUtils().setOnTouXiangSelectedListener(new OnTouXiangSelectedListener() {
            @Override
            public void onTouXiangSelected(int imgId) {
                mCircleImageView.setImageResource(imgId);
            }
        });

        mPreItem = mNavigationView.getMenu().getItem(0);
        mPreItem.setChecked(true);

        initFragment();
        handleNavigationView();
        mMenuHome.setOnClickListener(this);
        mMenuNovel.setOnClickListener(this);
        mMenuMe.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        mCurrentFragment = FragmentManagerWrapper.getInstance().createFragment(HomeFragment.class);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction
                .add(R.id.fl_main_content, mCurrentFragment);
                /*.add(R.id.fl_main_content, mNovelFragment)
                .hide(mNovelFragment)
                .add(R.id.fl_main_content, mMeFragment)
                .hide(mMeFragment);*/
        fragmentTransaction.commit();
    }

    private void handleNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (mPreItem != null){
                    mPreItem.setChecked(false);
                }
                switch (item.getItemId()){
                    case R.id.navigation_item_video:
                        switchFragment(HomeFragment.class);
                        mToolBar.setTitle(R.string.home_title);
                        break;
                    case R.id.navigation_item_blog:
                        switchFragment(BlogFragment.class);
                        mToolBar.setTitle(R.string.blog_title);
                        break;
                    case R.id.navigation_item_about:
                        switchFragment(AboutFragment.class);
                        mToolBar.setTitle(R.string.about_title);
                        break;
                    default:
                        break;
                }
                mPreItem = item;
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                item.setChecked(true);
                return false;
            }
        });
    }

    private void switchFragment(Class<?> clazz){
        Fragment fragment = FragmentManagerWrapper.getInstance().createFragment(clazz);
        if (fragment.isAdded()){
            mFragmentManager.beginTransaction().hide(mCurrentFragment).show(fragment).commitAllowingStateLoss();
        }else {
            mFragmentManager.beginTransaction().hide(mCurrentFragment).add(R.id.fl_main_content, fragment).commitAllowingStateLoss();
        }
        mCurrentFragment = fragment;
    }
    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        switch (v.getId()){
            case R.id.menu_main:
                /*mMenuHome.setPressed(true);
                mMenuNovel.setPressed(false);
                mMenuMe.setPressed(false);*/
                mPreMenuItem.setSelected(false);
                mPreMenuItem = mMenuHome;
                mMenuHome.setSelected(true);

                switchFragment(HomeFragment.class);
                /*mCurrentFragment = mHomeFragment;
                fragmentTransaction
                        .show(mHomeFragment)
                        .hide(mNovelFragment)
                        .hide(mMeFragment);
                fragmentTransaction.commit();*/
                mToolBar.setTitle(R.string.home_title);

                break;
            case R.id.menu_novel:
                /*mMenuHome.setPressed(false);
                mMenuNovel.setPressed(true);
                mMenuMe.setPressed(false);*/
                mPreMenuItem.setSelected(false);
                mPreMenuItem = mMenuNovel;
                mMenuNovel.setSelected(true);
                switchFragment(NovelFragment.class);
//                mCurrentFragment = mNovelFragment;
                /*fragmentTransaction
                        .hide(mHomeFragment)
                        .show(mNovelFragment)
                        .hide(mMeFragment);
                fragmentTransaction.commit();*/
                mToolBar.setTitle(R.string.menu_novel);
                break;
            case R.id.menu_me:
                /*mMenuHome.setPressed(false);
                mMenuNovel.setPressed(false);
                mMenuMe.setPressed(true);*/

                mPreMenuItem.setSelected(false);
                mPreMenuItem = mMenuMe;
                mMenuMe.setSelected(true);
                switchFragment(MeFragment.class);
//                mCurrentFragment = mMeFragment;
                /*fragmentTransaction
                        .hide(mHomeFragment)
                        .hide(mNovelFragment)
                        .show(mMeFragment);
                fragmentTransaction.commit();*/
                mToolBar.setTitle(R.string.menu_me);
                break;
            default:
                break;

        }

//        v.setPressed(true);
//        v.invalidate();
    }
}