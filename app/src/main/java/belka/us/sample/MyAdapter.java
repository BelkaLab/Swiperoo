package belka.us.sample;

import android.content.Context;

import java.util.List;

import belka.us.swiperoo.library.SwiperooAdapter;
import belka.us.swiperoo.library.SwiperooViewHolder;

/**
 * Created by fabriziorizzonelli on 01/04/2017.
 */

public class MyAdapter extends SwiperooAdapter<String> {

    public MyAdapter(Context context, List<String> items, Listener listener, SwiperooViewHolder.Factory factory) {
        super(context, items, listener, factory);
    }
}
