package com.meowing.loud.arms.base;

import android.app.AppComponentFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

public abstract class BaseActivity<VB extends ViewBinding, P extends IPresenter> extends AppCompatActivity implements IActivity {
}
