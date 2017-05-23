package lento.me.dragsetting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

import lento.me.dragsetting.drag.OnStartDragListener;
import lento.me.dragsetting.drag.SimpleItemTouchHelperCallback;

public class MainActivity extends AppCompatActivity implements OnStartDragListener {
    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private ItemAdapter mItemAdapter;
    private ItemTouchHelper mItemTouchHelper;

    private final List<Item> mItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_set);
        mRecyclerView.setHasFixedSize(true);
        mItemAdapter = new ItemAdapter(mItems, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mItemAdapter);

        final SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(mItemAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void initData() {
        Item item1 = new Item("Eren Yeager", 0, true, Item.TYPE_ADDED);
        Item item2 = new Item("Mikasa Ackerman", 1, false, Item.TYPE_ADDED);
        Item item3 = new Item("Armin Arlert", 2, true, Item.TYPE_ADDED);
        Item header = new Item("Reiner Braun", 3, false, Item.TYPE_MORE_WIDGET_HEADER);
        Item item4 = new Item("Bertolt Hoover", 4, true, Item.TYPE_REMOVED);
        Item item5 = new Item("BAnnie Leonhart", 5, true, Item.TYPE_REMOVED);
        mItems.add(header);
        mItems.add(item2);
        mItems.add(item4);
        mItems.add(item3);
        mItems.add(item5);
        mItems.add(item1);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
