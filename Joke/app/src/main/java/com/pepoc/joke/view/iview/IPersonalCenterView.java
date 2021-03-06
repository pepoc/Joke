package com.pepoc.joke.view.iview;

import java.util.List;

/**
 * Created by yangchen on 15-12-28.
 */
public interface IPersonalCenterView<T> {

    void onSuccess();

    void onFail();

    void updateData(List<T> datas, boolean isRefresh);

    void showLoading();

    void hideLoading();
}
