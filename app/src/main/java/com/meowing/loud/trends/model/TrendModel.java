package com.meowing.loud.trends.model;

import com.meowing.loud.arms.base.BaseModel;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.arms.integration.IRepositoryManager;
import com.meowing.loud.login.model.LoginModel;
import com.meowing.loud.trends.contract.TrendContract;

import java.util.HashMap;

import javax.inject.Inject;

@ActivityScope
public class TrendModel extends BaseModel implements TrendContract.Model {

    private HashMap<Integer, LoginModel.Listener> listenerHashMap;

    @Inject
    public TrendModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        this.listenerHashMap = new HashMap<>();
    }
}
