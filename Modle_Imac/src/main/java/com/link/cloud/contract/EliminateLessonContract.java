package com.link.cloud.contract;

import com.link.cloud.bean.RetrunLessons;
import com.orhanobut.logger.Logger;
import com.link.cloud.base.AbsAPICallback;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.LessonResponse;
import com.link.cloud.core.BasePresenter;
import com.link.cloud.core.MvpView;

/**
 * Created by Administrator on 2017/11/29.
 */

public class EliminateLessonContract extends BasePresenter <EliminateLessonContract.VerifyTemplate>{
    public interface VerifyTemplate extends MvpView {
        void eliminateSuccess(RetrunLessons lessonResponse);
    }
    public void eliminateLesson(String deviceID,int type,String memberID, String coachID, String clerkID){
        this.mCompositeSubscription.add(this.mDataManager.eliminateLesson(deviceID,type,memberID,coachID,clerkID)
                .subscribe(new AbsAPICallback<RetrunLessons>() {
                    @Override
                    public void onCompleted() {
                        if (EliminateLessonContract.this.mCompositeSubscription != null)
                            EliminateLessonContract.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
                        EliminateLessonContract.this.getMvpView().onError(e);
                        Logger.e("eliminateLesson===onError"+e.getMessage());
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        EliminateLessonContract.this.getMvpView().onPermissionError(e);
                        Logger.e("eliminateLesson===onPermissionError"+e.getMessage());
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        EliminateLessonContract.this.getMvpView().onResultError(e);
                        Logger.e("eliminateLesson===onResultError"+e.getMessage());
                    }
                    @Override
                    public void onNext(RetrunLessons lesson) {
                        EliminateLessonContract.this.getMvpView().eliminateSuccess(lesson);
                        Logger.e("eliminateLesson"+lesson.toString());
                    }
                }));
    }

}
