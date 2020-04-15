package drawrite.booknet;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import drawrite.booknet.model.OLBook;

public class OLBookAdapter extends ArrayAdapter<OLBook> {

    private static class ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvSubtitle;
        public TextView tvAuthor;
    }

    public OLBookAdapter(Context context, ArrayList<OLBook> aBooks) {

        super(context, 0, aBooks);
    }

    // Translates a particular `OLBook` given a position
    // into a relevant row within an AdapterView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final OLBook book = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ol_book_item, parent, false);
            viewHolder.ivCover = (ImageView)convertView.findViewById(R.id.ivBookCover);
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
            viewHolder.tvSubtitle = (TextView)convertView.findViewById(R.id.tvSubTitle);
            viewHolder.tvAuthor = (TextView)convertView.findViewById(R.id.tvAuthor);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object

        viewHolder.tvTitle.setText(book.getTitle());
        viewHolder.tvAuthor.setText(book.getAuthor());
        String subtitle = book.getSubTitle();
        if(!subtitle.isEmpty() && subtitle!=null){
            viewHolder.tvSubtitle.setText(subtitle);
        }
        else {
            viewHolder.tvSubtitle.setVisibility(View.GONE);
        }

        Picasso.with(getContext()).load(Uri.parse(book.getCoverUrl())).error(R.drawable.ic_nocover).into(viewHolder.ivCover);

        // Return the completed view to render on screen
        return convertView;
    }
}