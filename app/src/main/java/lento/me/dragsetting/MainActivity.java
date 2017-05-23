package lento.me.dragsetting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_to_file) {
            saveItemsToFile();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveItemsToFile() {
        final String filePath = getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "items.ser";
        Log.d(TAG, "file path : " + filePath);

        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            fout = new FileOutputStream(filePath);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(mItems);
            Log.d(TAG, "write object int file.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private List<Item> getItemsFromFile(File file) {
        if (file == null || !file.exists()) {
            return new ArrayList<>();
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            List<Item> items = (List<Item>) o;
            if (items != null && !items.isEmpty()) {
                return items;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void initData() {
        final String filePath = getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "items.ser";
        final File file = new File(filePath);
        Log.d(TAG, "file path : " + filePath);

        if (file.exists()) {
            List<Item> itemsFromFile = getItemsFromFile(file);
            mItems.clear();
            mItems.addAll(itemsFromFile);
            Log.d(TAG, "file exist now already, so read from file. list size = " + mItems.size() + ", itemsFromFile size = " + itemsFromFile.size());
        }

        if (!mItems.isEmpty()) {
            return;
        }

        Log.d(TAG, "init Data from new user!");

        Item item1 = new Item("Eren Yeager", 0, true, Item.TYPE_ADDED);
        Item item2 = new Item("Mikasa Ackerman", 1, false, Item.TYPE_ADDED);
        Item item3 = new Item("Armin Arlert", 2, true, Item.TYPE_ADDED);
        Item header = new Item("Investigations", 3, false, Item.TYPE_MORE_WIDGET_HEADER);
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
