package com.meowing.loud.arms.base;

import androidx.annotation.NonNull;

import com.meowing.loud.arms.di.component.AppComponent;

/**
 * ================================================
 * 框架要求框架中的每个 {@link android.app.Application} 都需要实现此类, 以满足规范
 *
 * @see
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki">请配合官方 Wiki 文档学习本框架</a>
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki/UpdateLog">更新日志, 升级必看!</a>
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki/Issues">常见 Issues, 踩坑必看!</a>
 * @see <a href="https://github.com/JessYanCoding/ArmsComponent/wiki">MVPArms 官方组件化方案 ArmsComponent, 进阶指南!</a>
 * Created by JessYan on 25/04/2017 14:54
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface AppUtil {
    @NonNull
    AppComponent getAppComponent();
}
