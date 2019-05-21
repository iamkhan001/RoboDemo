package com.csjbot.blackgaga.model.http.notice;

import com.csjbot.blackgaga.model.http.base.BaseImpl;
import com.csjbot.blackgaga.model.http.bean.NoticeBean;

import io.reactivex.Observer;

/**
 * Created by jingwc on 2018/3/1.
 */

public class NoticeServiceImpl extends BaseImpl implements INoticeService {

    @Override
    public NoticeService getRetrofit() {
        return getRetrofit(NoticeService.class);
    }

    @Override
    public void getGlobalAnnouncement(String sn, Observer<NoticeBean> observer) {
        scheduler(getRetrofit().getGlobalAnnouncement(sn)).subscribe(observer);
    }
}
