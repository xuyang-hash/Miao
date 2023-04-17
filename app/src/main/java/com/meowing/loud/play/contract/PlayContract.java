package com.meowing.loud.play.contract;

import com.meowing.loud.arms.base.IModel;
import com.meowing.loud.arms.base.IView;

public interface PlayContract {
    interface View extends IView {

    }

    interface Model extends IModel {

        interface Listener {
            void onSuccess(Object obj);

            void onFailed(int errorId);
        }
    }
}
