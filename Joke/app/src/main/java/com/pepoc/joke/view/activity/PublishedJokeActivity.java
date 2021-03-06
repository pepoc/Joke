package com.pepoc.joke.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pepoc.joke.R;
import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.presenter.PublishedJokePresenter;
import com.pepoc.joke.view.adapter.JokeListAdapter;
import com.pepoc.joke.view.iview.IPublishedJokeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PublishedJokeActivity extends BaseSwipeBackActivity implements SwipeRefreshLayout.OnRefreshListener, IPublishedJokeView<JokeContent> {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview_published_joke)
    RecyclerView recyclerviewPublishedJoke;
    @Bind(R.id.swiperefresh_published_joke)
    SwipeRefreshLayout swiperefreshPublishedJoke;

    private JokeListAdapter jokeListAdapter;

    private PublishedJokePresenter publishedJokePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_published_joke);
        ButterKnife.bind(this);
        publishedJokePresenter = new PublishedJokePresenter(this);
        init();
        publishedJokePresenter.getData(context, true);
    }

    @Override
    public void init() {
        super.init();

        toolbar.setTitle(R.string.menu_published);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        swiperefreshPublishedJoke.setColorSchemeResources(R.color.colorAccent);
        swiperefreshPublishedJoke.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerviewPublishedJoke.setLayoutManager(linearLayoutManager);
        jokeListAdapter = new JokeListAdapter(context);
        recyclerviewPublishedJoke.setAdapter(jokeListAdapter);

    }

    @Override
    public void onRefresh() {
        publishedJokePresenter.getData(context, true);
    }

    @Override
    public void onSuccess() {
        swiperefreshPublishedJoke.setRefreshing(false);
    }

    @Override
    public void onFail() {
        swiperefreshPublishedJoke.setRefreshing(false);
    }

    @Override
    public void updateData(List<JokeContent> datas, boolean isRefresh) {
        if (isRefresh) {
            jokeListAdapter.getDatas().clear();
        }
        jokeListAdapter.setDatas(datas);
        jokeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
