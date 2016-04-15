package fr.domurado.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import fr.domurado.library.bo.Book;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {
    private Book[] mBooks;

    public BookListAdapter(Book[] books) {
        mBooks = books;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_view, parent, false);
        return new BookViewHolder(v);

    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = mBooks[position];
        holder.mTitle.setText(book.getTitle());
        holder.mPrice.setText(holder.mPrice.getContext().getString(R.string.price, book.getPrice()));

        Bitmap cover = null;
        try {
            cover = new GetCoverTask().execute(book.getCover()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        holder.mCover.setImageBitmap(cover);
    }

    @Override
    public int getItemCount() {
        return mBooks.length;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public ImageView mCover;
        public TextView mTitle;
        public TextView mPrice;

        public BookViewHolder(View itemView) {
            super(itemView);
            mCover = (ImageView) itemView.findViewById(R.id.imgCover);
            mTitle = (TextView) itemView.findViewById(R.id.lblTitle);
            mPrice = (TextView) itemView.findViewById(R.id.lblPrice);
        }
    }

    private class GetCoverTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... resources) {

            URL url;
            HttpURLConnection urlConnection = null;
            InputStream in = null;
            Bitmap cover = null;
            try {
                url = new URL(resources[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
                cover = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return cover;
        }
    }
}
