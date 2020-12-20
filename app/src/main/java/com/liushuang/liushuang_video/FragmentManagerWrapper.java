package com.liushuang.liushuang_video;

import androidx.fragment.app.Fragment;

import java.util.HashMap;

public class FragmentManagerWrapper {

    private volatile static FragmentManagerWrapper mInstance = null;

    public static FragmentManagerWrapper getInstance(){
        if (mInstance == null){
            synchronized (FragmentManagerWrapper.class){
                if (mInstance == null){
                    mInstance = new FragmentManagerWrapper();
                }
            }
        }
        return mInstance;
    }

    private HashMap<String, Fragment> mHashMap = new HashMap<>();

    public Fragment createFragment(Class<?> clazz){
        return createFragment(clazz, true);
    }

    private Fragment createFragment(Class<?> clazz, boolean isObtain) {
        Fragment fragment = null;
        String className = clazz.getName();
        if (mHashMap.containsKey(className)){
            fragment = mHashMap.get(className);
        }else{
            try {
                fragment = (Fragment) Class.forName(className).newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (isObtain){
            mHashMap.put(className, fragment);
        }
        return fragment;
    }

}
