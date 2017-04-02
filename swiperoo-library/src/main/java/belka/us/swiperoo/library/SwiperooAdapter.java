package belka.us.swiperoo.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fabriziorizzonelli on 01/04/2017.
 */

public abstract class SwiperooAdapter<T> extends RecyclerView.Adapter<SwiperooViewHolder> {

    private static final int PENDING_REMOVAL_TIMEOUT = 2000; //2 sec

    public interface Listener<T> {
        void onItemDeleted(T item);
    }

    private final Context mContext;
    private final Listener mListener;
    private SwiperooViewHolder.Factory mFactory;

    private List<T> items;
    private List<T> itemsPendingRemoval;

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    private HashMap<T, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public SwiperooAdapter(Context context, List<T> items, Listener listener, SwiperooViewHolder.Factory factory) {
        this.mContext = context;
        this.items = items;
        this.mListener = listener;
        this.mFactory = factory;
        this.itemsPendingRemoval = new ArrayList<>();
    }

    @Override
    public SwiperooViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mFactory.createViewHolder(mContext, parent, viewType);
    }

    @Override
    public void onBindViewHolder(SwiperooViewHolder holder, int position) {
        final T item = items.get(position);

        holder.bindViewHolder(item);

        if (itemsPendingRemoval.contains(item)) {
            holder.container.setBackgroundColor(Color.RED);

            for (int i = 0; i < holder.container.getChildCount(); i++) {
                View v = holder.container.getChildAt(i);
                v.setVisibility(View.INVISIBLE);
            }
            holder.undoButton.setVisibility(View.VISIBLE);
            holder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                    pendingRunnables.remove(item);
                    if (pendingRemovalRunnable != null)
                        handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(item);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(items.indexOf(item));
                }
            });
        } else {
            holder.container.setBackgroundColor(Color.TRANSPARENT);

            for (int i = 0; i < holder.container.getChildCount(); i++) {
                View v = holder.container.getChildAt(i);
                v.setVisibility(View.VISIBLE);
            }
            holder.undoButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public boolean isPendingRemoval(int position) {
        T item = items.get(position);
        return itemsPendingRemoval.contains(item);
    }

    public void pendingRemoval(int position) {
        final T item = items.get(position);

        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(items.indexOf(item));
                    mListener.onItemDeleted(item);
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        T item = items.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (items.contains(item)) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addSupportToSwipeToDelete(final Context context, final RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(context, R.drawable.ic_delete_sweep_black_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) context.getResources().getDimension(R.dimen.default_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                SwiperooAdapter adapter = (SwiperooAdapter) recyclerView.getAdapter();
                if (adapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                SwiperooAdapter adapter = (SwiperooAdapter) recyclerView.getAdapter();
//                boolean undoOn = adapter.isUndoOn();
//                if (undoOn) {
//                    adapter.pendingRemoval(swipedPosition);
//                } else {
                adapter.pendingRemoval(swipedPosition);
//                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
