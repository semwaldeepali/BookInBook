package drawrite.booknet;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import drawrite.booknet.entity.Book;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {


    // list of Books; assigned to new list else might cause action on null datalist
    private List<Book> books = new ArrayList<>();
    private OnBookClickListener mOnBookClickListener;
    private Context mContext;

    //TODO : change the local variable nomenclature to mSomthing
    public BookAdapter(List<Book> books, OnBookClickListener onBookClickListener, Context context){
        this.books = books;
        this.mOnBookClickListener = onBookClickListener;
        this.mContext = context;

    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.ol_book_item, parent, false);
        return new BookHolder(view, mOnBookClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position){
        Book currentBook = books.get(position);
        holder.title.setText(currentBook.getTitle());
        holder.author.setText(currentBook.getAuthor());
        if(currentBook.getSubTitle()!=null)
            holder.subtitle.setText(String.valueOf(currentBook.getSubTitle()));
        else
            holder.subtitle.setVisibility(View.GONE);
        holder.ivBookCover.setImageResource(R.drawable.ic_book_cover);
        holder.primaryId = currentBook.getId();
        // DIRTY way of loading book cover
        // Populate image data
        // cannot directly set picasso without getting activity context
        Picasso.with(this.mContext).load(Uri.parse("http://covers.openlibrary.org/b/olid/" + currentBook.getOlid() + "-L.jpg?default=false")).error(R.drawable.ic_book_cover).into(holder.ivBookCover);


    }

    @Override
    public int getItemCount(){

        return books.size();
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    //add new books
    public void addBooks(List<Book> books) {
        this.books.addAll(books);
        notifyDataSetChanged();
    }

    class BookHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        public final View mView;
        TextView title;
        TextView author;
        TextView subtitle;
        ImageView ivBookCover;
        OnBookClickListener onBookClickListener;
        private Integer primaryId;

        BookHolder(View itemView, OnBookClickListener onBookClickListener){
            super(itemView);
            mView = itemView;
            title = mView.findViewById(R.id.tvTitle);
            subtitle = mView.findViewById(R.id.tvSubtitle);
            author = mView.findViewById(R.id.tvAuthor);
            ivBookCover = mView.findViewById(R.id.ivBookCover);
            this.onBookClickListener = onBookClickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view)
        {

            onBookClickListener.OnBookClick(getAdapterPosition(), primaryId);
        }
    }

    public interface OnBookClickListener{
        void OnBookClick(int position, Integer primaryId);
    }


}
