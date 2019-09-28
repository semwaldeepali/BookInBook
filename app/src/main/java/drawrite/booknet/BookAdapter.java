package drawrite.booknet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import drawrite.booknet.model.Book;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> dataList; // list of Books
    private Context context;

    public BookAdapter(Context context,List<Book> dataList){
        this.context = context;
        this.dataList = dataList;
    }
    class BookViewHolder extends  RecyclerView.ViewHolder{

        public final View mView;
        TextView bookTitle;

        BookViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            bookTitle = (TextView) mView.findViewById(R.id.bookTitle);

        }
    }
    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.book_row, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position){
        holder.bookTitle.setText(dataList.get(position).getTitle());

    }

    @Override
    public int getItemCount(){
        return dataList.size();
    }

}
