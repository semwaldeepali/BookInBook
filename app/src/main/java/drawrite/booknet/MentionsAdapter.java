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
import drawrite.booknet.entity.Mentions;


public class MentionsAdapter extends RecyclerView.Adapter<MentionsAdapter.MentionHolder> {


    // list of Mentions; assigned to new list else might cause action on null datalist
    private List<Mentions> mentions = new ArrayList<>();

    @NonNull
    @Override
    public MentionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.ol_mentions_item, parent, false);
        return new MentionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MentionHolder holder, int position){
        Mentions currentMention = mentions.get(position);
        holder.mainBookId.setText(String.valueOf(currentMention.getMainId()));
        holder.mentionBookId.setText(String.valueOf(currentMention.getMentionsId()));

    }

    @Override
    public int getItemCount(){
        return mentions.size();
    }

    public void setMentions(List<Mentions> mentions) {
        this.mentions = mentions;
        notifyDataSetChanged();
    }

    class MentionHolder extends  RecyclerView.ViewHolder{

        public final View mView;
        TextView mainBookId;
        TextView mentionBookId;

        MentionHolder(View itemView){
            super(itemView);
            mView = itemView;
            mainBookId = mView.findViewById(R.id.tvMain);
            mentionBookId = mView.findViewById(R.id.tvMentioned);

        }
    }


}
