package com.example.movieappstage2;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
public class AdapterRevirw extends RecyclerView.Adapter<AdapterRevirw.ViewHolder> {
    private Movie[] movies;
    private TextView authorTV;
    private TextView contentsTV;
    private Context context;

    public AdapterRevirw(Movie[] movies, Context context) {
        this.context = context;
        this.movies = movies;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView authorTV;
        TextView contentsTV;
        Button reviewButton;

        public ViewHolder(ConstraintLayout itemView) {
            super (itemView);

            authorTV = (TextView) itemView.findViewById (R.id.reviewAuthorTextView);
            contentsTV = (TextView) itemView.findViewById (R.id.reviewContentTextView);
            reviewButton = (Button) itemView.findViewById (R.id.fullReviewButton);
        }
        // each data item is just a string in this case

    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterRevirw.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.authorTV.setText(String.valueOf(movies[position].getReviewAuthor()));
        holder.contentsTV.setText (String.valueOf (movies[position].getReviewContents()));

        holder.reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(movies[position].getReviewUrl()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (movies == null || movies.length == 0) {
            return -1;
        }
        return movies.length;
    }
}
