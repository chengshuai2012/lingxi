package com.link.cloud.contract;

import com.link.cloud.base.AbsAPICallback;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.RetrunLessons;
import com.link.cloud.core.BasePresenter;
import com.link.cloud.core.MvpView;
import com.link.cloud.utils.ReservoirUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2017/8/2.
 */

public class UserLessonContract extends BasePresenter<UserLessonContract.UserLesson> {

    public interface UserLesson extends MvpView{
        void selectLessonSuccess(RetrunLessons usersMessage);
        void eliminateSuccess(RetrunLessons lessonResponse);
    }
    public ReservoirUtils reservoirUtils;

    public UserLessonContract() {
        this.reservoirUtils = new ReservoirUtils();
    }

    public void eliminateLesson(String deviceID,int type,String memberID, String coachID, String clerkID){
        this.mCompositeSubscription.add(this.mDataManager.eliminateLesson(deviceID,type,memberID,coachID,clerkID)
                .subscribe(new AbsAPICallback<RetrunLessons>() {
                    @Override
                    public void onCompleted() {
                        if (UserLessonContract.this.mCompositeSubscription != null)
                            UserLessonContract.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
                        UserLessonContract.this.getMvpView().onError(e);
                        Logger.e("eliminateLesson===onError"+e.getMessage());
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        UserLessonContract.this.getMvpView().onPermissionError(e);
                        Logger.e("eliminateLesson===onPermissionError"+e.getMessage());
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        UserLessonContract.this.getMvpView().onResultError(e);
                        Logger.e("eliminateLesson===onResultError"+e.getMessage());
                    }
                    @Override
                    public void onNext(RetrunLessons lesson) {
                        UserLessonContract.this.getMvpView().eliminateSuccess(lesson);
                        Logger.e("eliminateLesson"+lesson.toString());
                    }
                }));
    }
    public void selectLesson (String deviceID,int type,String lessonID,String memberID,String coachID,String clerkID,String CardNo,int count){
        this.mCompositeSubscription.add(this.mDataManager.selectLesson(deviceID,type,lessonID,memberID,coachID,clerkID,CardNo,count)
                .subscribe(new AbsAPICallback<RetrunLessons>() {
                    @Override
                    public void onCompleted() {
                        if (UserLessonContract.this.mCompositeSubscription != null)
                            UserLessonContract.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
                        UserLessonContract.this.getMvpView().onError(e);
                        Logger.e("eliminateLesson===onError"+e.getMessage());
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        UserLessonContract.this.getMvpView().onPermissionError(e);
                        Logger.e("eliminateLesson===onPermissionError"+e.getMessage());
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        UserLessonContract.this.getMvpView().onResultError(e);
                        Logger.e("eliminateLesson===onResultError"+e.getMessage());
                    }
                    @Override
                    public void onNext(RetrunLessons lesson) {
                        UserLessonContract.this.getMvpView().selectLessonSuccess(lesson);
                        Logger.e("eliminateLesson"+lesson.toString());
                    }
                }));
    }



}
