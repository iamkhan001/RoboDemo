package com.csjbot.blackgaga.feature.story;

/**
 * Created by jingwc on 2017/10/16.
 */

public class StoryPresenter implements StoryContract.Presenter {

    StoryContract.View view;

    @Override
    public StoryContract.View getView() {
        return view;
    }

    @Override
    public void initView(StoryContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if(view != null){
            view = null;
        }
    }
}
