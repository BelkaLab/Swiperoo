package belka.us.swiperoo.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import static android.view.LayoutInflater.from;

/**
 * Created by fabriziorizzonelli on 01/04/2017.
 */

public abstract class SwiperooViewHolder<T> extends RecyclerView.ViewHolder {

    public interface Factory {
        public abstract SwiperooViewHolder createViewHolder(Context context, ViewGroup parent, int viewType);
    }

    Button undoButton;
    RelativeLayout container;

    public SwiperooViewHolder(View itemView, Context context) {
        super(itemView);
        if (!(itemView instanceof RelativeLayout)) {
            throw new UnsupportedOperationException("Main layout must a Relative Layout");
        }

        container = (RelativeLayout) itemView;
        undoButton = (Button) from(context).inflate(R.layout.button_undo_swiperoo, container, false);

        container.addView(undoButton);
    }

    public abstract void bindViewHolder(T data);
}
