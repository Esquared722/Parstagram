package com.example.parstagram.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parstagram.R;
import com.example.parstagram.databinding.ItemPostBinding;
import com.example.parstagram.models.Post;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static final String TAG = "PostsAdapter";
    Context context;
    List<Post> posts;
    public static final int POST_LIMIT = 20;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        int old_size = posts.size();
        posts.clear();
        notifyItemRangeRemoved(0, old_size);
    }

    public void addAll(List<Post> list, int startIndex) {
        posts.addAll(list);
        notifyItemRangeInserted(startIndex, POST_LIMIT);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemPostBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemPostBinding.bind(itemView);
        }

        public void bind(Post post) {
            binding.setPost(post);

            binding.tvStamp.setText(DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime()));
            binding.ivPost.setParseFile(post.getImage());
            binding.ivPost.loadInBackground();
        }


    }
}
