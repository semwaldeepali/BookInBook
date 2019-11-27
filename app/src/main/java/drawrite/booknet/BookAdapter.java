package drawrite.booknet;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import drawrite.booknet.entity.Book;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {


    // list of Books; assigned to new list else might cause action on null datalist
    private List<Book> books = new ArrayList<>();

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.ol_book_item, parent, false);
        return new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position){
        Book currentBook = books.get(position);
        holder.title.setText(currentBook.getTitle());
        holder.author.setText(currentBook.getAuthor());

    }

    @Override
    public int getItemCount(){
        return books.size();
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    class BookHolder extends  RecyclerView.ViewHolder{

        public final View mView;
        TextView title;
        TextView author;

        BookHolder(View itemView){
            super(itemView);
            mView = itemView;
            title = mView.findViewById(R.id.tvTitle);
            author = mView.findViewById(R.id.tvAuthor);

        }
    }


}
