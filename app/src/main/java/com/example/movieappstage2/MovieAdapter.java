package com.example.movieappstage2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.*;
  public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static Movie[] mMovies;
    private Context mContext;

    public MovieAdapter(Context mContext, Movie[] mMovies) {
        this.mMovies = mMovies;

        if (mContext == null) {
            try{
                Thread.sleep(1000);
            } catch(InterruptedException exception){
                exception.printStackTrace();
            }
        } else {
            this.mContext = mContext;
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ViewHolder(ImageView v) {
            super(v);
            mImageView = v;
        }
    }
    @NonNull
    @Override
    // Create new views (Invoked by the Layout Manager)
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        ImageView v = (ImageView) LayoutInflater.from(parent.getContext ())
                .inflate (R.layout.image_review, parent, false);

        ViewHolder vh = new ViewHolder (v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso picasso = Picasso.get();
        picasso.load(mMovies[position].getPosterPath())
                .fit()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher_round)
                .into((ImageView) holder.mImageView.findViewById (R.id.image_view));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,Details.class);
                intent.putExtra("movie", mMovies[position]);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (mMovies == null || mMovies.length == 0) {
            return -1;
        }
        return mMovies.length;
    }

    public void setMovies(Movie [] movies) {
        mMovies = movies;
    }

}