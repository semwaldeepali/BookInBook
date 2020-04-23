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
    private List<Book> mBooksList = new ArrayList<>();
    private OnItemClickListener mOnBookClickListener;
    private Context mContext;


    public interface OnItemClickListener {

        // Handling the click on item
        void onBookClick(int position, Integer primaryId);

        // Handling click on Bin Image
        void onDeleteClick(int position, Integer primaryId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnBookClickListener = listener;
    }


    class BookHolder extends RecyclerView.ViewHolder {

        public final View mView;
        TextView title;
        TextView author;
        //TextView subtitle;
        ImageView ivBookCover;
        ImageView mDeleteItem;
        OnItemClickListener onBookClickListener;
        private Integer primaryId;

        BookHolder(View itemView, final OnItemClickListener onBookClickListener) {
            super(itemView);
            mView = itemView;
            title = mView.findViewById(R.id.tvTitle);
            //subtitle = mView.findViewById(R.id.tvSubtitle);
            author = mView.findViewById(R.id.tvAuthor);
            ivBookCover = mView.findViewById(R.id.ivBookCover);
            mDeleteItem = mView.findViewById(R.id.ivDelete);
            this.onBookClickListener = onBookClickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onBookClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onBookClickListener.onBookClick(position, primaryId);
                        }
                    }
                }
            });


            mDeleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onBookClickListener != null) {

                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onBookClickListener.onDeleteClick(position, primaryId);
                        }

                    }

                }
            });

        }

    }

    //TODO : change the local variable nomenclature to mSomthing
    public BookAdapter(List<Book> books, OnItemClickListener onBookClickListener, Context context) {
        this.mBooksList = books;
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
        Book currentBook = mBooksList.get(position);
        holder.author.setText("by " + currentBook.getAuthor());
        String completeTitle = currentBook.getTitle();
        String subtitle = currentBook.getSubTitle();
        subtitle = subtitle.trim();
        if (subtitle != null && !subtitle.isEmpty())
            completeTitle = completeTitle + ": " + String.valueOf(currentBook.getSubTitle());
        holder.title.setText(completeTitle);
        holder.ivBookCover.setImageResource(R.drawable.ic_bookcover_front);
        holder.primaryId = currentBook.getId();

        // DIRTY way of loading book cover
        // Populate image data
        // cannot directly set picasso without getting activity context
        Picasso.with(this.mContext).load(Uri.parse("http://covers.openlibrary.org/b/olid/" + currentBook.getOlid() + "-L.jpg?default=false")).error(R.drawable.ic_bookcover_front).into(holder.ivBookCover);


    }

    @Override
    public int getItemCount(){

        return mBooksList.size();
    }

    public void setBooks(List<Book> books) {
        this.mBooksList = books;
        notifyDataSetChanged();
    }

    //add new books
    public void addBooks(List<Book> books) {
        this.mBooksList.addAll(books);
        notifyDataSetChanged();
    }





}
