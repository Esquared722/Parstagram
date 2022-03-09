package com.example.parstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parstagram.R;
import com.example.parstagram.adapters.PostsAdapter;
import com.example.parstagram.models.Post;
import com.example.parstagram.utils.EndlessRecyclerViewScrollListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    EndlessRecyclerViewScrollListener scrollListener;
    RecyclerView rvPosts;
    SwipeRefreshLayout swipeContainer;
    PostsAdapter adapter;
    List<Post> posts;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        rvPosts = view.findViewById(R.id.rvPosts);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts(0);
            }
        });

        posts = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                queryPosts(page * PostsAdapter.POST_LIMIT);
                Log.i(TAG, String.format("LOAD MORE\npage: %d, totalItems: %d\n", page, totalItemsCount));
            }
        };
        Log.i(TAG, "here");
        adapter = new PostsAdapter(getContext(), posts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(linearLayoutManager);
        rvPosts.addOnScrollListener(scrollListener);
        queryPosts(0);
    }

    protected void queryPosts(int skip) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER)
                .addDescendingOrder(Post.KEY_CREATED_AT)
                .setSkip(skip)
                .setLimit(PostsAdapter.POST_LIMIT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue in find!", e);
                    return;
                }
                for (Post post : objects) {
                    Log.i(TAG, "Post: " + post.getDescription() + "\nUsername: " + post.getUser().getUsername());
                }
                if (skip == 0) {
                    adapter.clear();
                    swipeContainer.setRefreshing(false);
                }
                adapter.addAll(objects, skip);
            }
        });
    }
}