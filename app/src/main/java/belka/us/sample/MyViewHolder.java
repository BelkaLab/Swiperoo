package belka.us.sample;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import belka.us.swiperoo.library.SwiperooViewHolder;

import static android.view.LayoutInflater.from;

/**
 * Created by fabriziorizzonelli on 01/04/2017.
 */

public class MyViewHolder extends SwiperooViewHolder<String> {

    TextView sampleItemTextView;

    static class Factory implements SwiperooViewHolder.Factory {

        @Override
        public SwiperooViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
            return new MyViewHolder(from(context).inflate(R.layout.item_sample_swiperoo, parent, false), context);
        }
    }

    public MyViewHolder(View itemView, Context context) {
        super(itemView, context);
        sampleItemTextView = (TextView) itemView.findViewById(R.id.sample_item_text_view);
    }

    @Override
    public void bindViewHolder(String data) {
        sampleItemTextView.setText(data);
    }
}
