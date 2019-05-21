package com.csjbot.blackgaga.feature.navigation.contract;

import com.csjbot.blackgaga.cart.pactivity.evaluate.AnswerResponse;
import com.csjbot.blackgaga.cart.pactivity.evaluate.ServiceEvaluationActivity;
import com.csjbot.blackgaga.model.http.factory.RetrofitFactory;
import com.csjbot.blackgaga.model.http.product.ProductService;
import com.csjbot.coshandler.core.Robot;
import com.google.gson.Gson;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by xiasuhuei321 on 2017/12/12.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class NaviCommentPresenter implements NaviCommentContract.Presenter {
    NaviCommentContract.View view = null;

    @Override
    public NaviCommentContract.View getView() {
        return view;
    }

    @Override
    public void initView(NaviCommentContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        view = null;
    }

    @Override
    public void submitResult(int code) {
        String result = null;
        switch (code) {
            case NaviCommentContract.View.GOOD:
                result = "满意";
                break;
            case NaviCommentContract.View.VERY_GOOD:
                result = "非常满意";
                break;
            case NaviCommentContract.View.BAD:
                result = "不满意";
                break;
            default:
                break;
        }

        submit(result);
    }

    private void submit(String result) {
        ServiceEvaluationActivity.EvaluationBean evaluationBean =
                new ServiceEvaluationActivity.EvaluationBean(Robot.SN, result,
                        "abc", "anonymous", "23121321322", "nifdsafdsafasd");
        String json = new Gson().toJson(evaluationBean);

        RetrofitFactory.create(ProductService.class)
                .sendEvalution(RequestBody.create(MediaType.parse("Content-Type,application/json"), json))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AnswerResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AnswerResponse answerResponse) {
                        if ("200".equals(answerResponse.getStatus()))
                            view.success();
                        else view.failed();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.failed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
