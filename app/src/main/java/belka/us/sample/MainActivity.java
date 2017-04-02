package belka.us.sample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import belka.us.swiperoo.library.SwiperooAdapter;

public class MainActivity extends AppCompatActivity implements SwiperooAdapter.Listener<String> {

    RecyclerView mSwiperooRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<String> items = new ArrayList<>();
        for (int i = 1; i < 11; i++)
            items.add("Sample item " + i);

        mSwiperooRecyclerView = (RecyclerView) findViewById(R.id.swiperoo_recycler_view);
        mSwiperooRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(this, items, this, new MyViewHolder.Factory());

        mSwiperooRecyclerView.setAdapter(adapter);

        adapter.addSupportToSwipeToDelete(this, mSwiperooRecyclerView);
    }

    @Override
    public void onItemDeleted(String item) {
        Snackbar.make(mSwiperooRecyclerView, item, Snackbar.LENGTH_LONG).show();
    }
}
