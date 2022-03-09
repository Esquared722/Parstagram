package com.example.parstagram.fragments;

import android.util.Log;

import com.example.parstagram.adapters.PostsAdapter;
import com.example.parstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends HomeFragment {

    @Override
    protected void queryPosts(int skip) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER)
                .addDescendingOrder(Post.KEY_CREATED_AT)
                .whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
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
