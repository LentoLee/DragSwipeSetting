package lento.me.dragsetting;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import lento.me.dragsetting.drag.ItemTouchHelperAdapter;
import lento.me.dragsetting.drag.ItemTouchHelperViewHolder;
import lento.me.dragsetting.drag.OnStartDragListener;
import lento.me.dragsetting.swipe.SwipeMenuLayout;

/**
 * Created by lento on 2017/5/22.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    private static final String TAG = "ItemAdapter";

    private List<Item> mData;
    private final OnStartDragListener mOnStartDragListener;

    private IOptItemListener mOptItemListener;

    public ItemAdapter(@NonNull List<Item> data, OnStartDragListener startDragListener) {
        this.mData = data;
        Collections.sort(mData);
        this.mOnStartDragListener = startDragListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Item.TYPE_REMOVED_HEADER) {
            return new ItemViewHolder(View.inflate(parent.getContext(), R.layout.item_setting_header, null));
        }
        return new ItemViewHolder(View.inflate(parent.getContext(), R.layout.item_setting, null));
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).viewType;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final Item item = mData.get(position);

        Log.d(TAG, "onBindViewHolder : pos = " + position + ", item desc = " + item.desc + ", item order = " + item.order);

        if (item.viewType == Item.TYPE_ADDED || item.viewType == Item.TYPE_REMOVED) {
            final boolean isAdded = item.viewType == Item.TYPE_ADDED;
            holder.optBtn.setImageResource(isAdded ? R.drawable.ic_remove_circle_black_24dp : R.drawable.ic_add_circle_black_24dp);
            holder.optBtn.setVisibility(item.canOpt ? View.VISIBLE : View.INVISIBLE);

            holder.itemDesc.setText(item.desc);
            holder.dragBtn.setVisibility(isAdded ? View.VISIBLE : View.INVISIBLE);
            if (isAdded) {
                holder.dragBtn.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnStartDragListener.onStartDrag(holder);
                        return false;
                    }
                });

                holder.optBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "click - btn : pos = " + position);
                        holder.menuLayout.smoothExpand();
                    }
                });

                holder.removeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "click remove btn : position = " + position);
                        holder.menuLayout.smoothClose();
                        v.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onItemClickRemove(position);
                            }
                        }, 200);//delay for better effect!
                    }
                });
            } else {
                holder.optBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "click + btn : pos = " + position);
                        onItemClickAdd(position);
                    }
                });
            }
            if (isAdded && item.canOpt) {
                holder.menuLayout.setSwipeEnable(true);
            } else {
                holder.menuLayout.setSwipeEnable(false);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onItemMove(int fromPos, int toPos) {
        final Item remove = mData.remove(fromPos);
        mData.add(toPos, remove);
        resetOrder(mData);

        notifyItemMoved(fromPos, toPos);
        notifyItemChanged(fromPos, getItemCount());
        //printData(mData);
    }

    private void resetOrder(List<Item> items) {
        Log.d(TAG, "-------begin to resetOrder---------");
        for (int i = 0; i < items.size(); i++) {
            final Item item = items.get(i);
            item.order = i;
            Log.d(TAG, "i = " + i + ", item : desc = " + item.desc + ", order = " + item.order);
        }
    }

    private void printData(List<Item> items) {
        Log.d(TAG, "-------begin to Print---------");
        for (Item item : items) {
            Log.d(TAG, "item : desc = " + item.desc + ", order = " + item.order);
        }
    }

    private void onItemClickAdd(int position) {
        final int headerPos = findHeaderPos();
        Log.d(TAG, "onItemClickAdd : header pos = " + headerPos);
        Item addItem = mData.remove(position);
        addItem.viewType = Item.TYPE_ADDED;
        mData.add(headerPos, addItem);
        resetOrder(mData);
        notifyDataSetChanged();
        showEmptyTipsIfNeed();
    }

    public void setOptItemListener(IOptItemListener listener) {
        this.mOptItemListener = listener;
    }


    private void showEmptyTipsIfNeed() {
        if (mOptItemListener == null) {
            return;
        }
        final Item lastItem = mData.get(mData.size() - 1);
        if (lastItem.viewType == Item.TYPE_REMOVED_HEADER) {
            mOptItemListener.showEmptyTips(true);
        } else {
            mOptItemListener.showEmptyTips(false);
        }
    }

    private int findHeaderPos() {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).viewType == Item.TYPE_REMOVED_HEADER) {
                return i;
            }
        }
        Log.e(TAG, "cannot be happened!!");
        return 1;
    }


    public void onItemClickRemove(int pos) {
        Log.d(TAG, "onItemClickRemove : pos = " + pos);
        final Item remove = mData.remove(pos);
        remove.viewType = Item.TYPE_REMOVED;
        mData.add(remove);
        resetOrder(mData);
        notifyDataSetChanged();
        showEmptyTipsIfNeed();
    }

    @Override
    public boolean canDropOver(int fromPos, int toPos) {
        final int fromViewType = mData.get(fromPos).viewType;
        final int toViewType = mData.get(toPos).viewType;

        Log.d(TAG, "拖动的是 fromPos = " + fromPos + ", fromType = " + fromViewType + "， toPos = " + toPos + ", toViewType = " + toViewType);

        if (fromViewType == Item.TYPE_ADDED && toViewType == Item.TYPE_ADDED) {
            return true;
        }
        return false;
    }


    static final class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        private ImageView optBtn;
        private TextView itemDesc;
        private ImageView dragBtn;
        private View removeView;
        private SwipeMenuLayout menuLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            optBtn = (ImageView) itemView.findViewById(R.id.iv_opt);
            itemDesc = (TextView) itemView.findViewById(R.id.tv_item_desc);
            dragBtn = (ImageView) itemView.findViewById(R.id.iv_drag);
            removeView = itemView.findViewById(R.id.rl_remove);
            menuLayout = (SwipeMenuLayout) itemView.findViewById(R.id.sml);
        }

        @Override
        public void onItemSelected() {
            Log.d(TAG, "onItemSelected...");
            // itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            Log.d(TAG, "onItemClear...");
            // itemView.setBackgroundColor(0);
        }
    }
}
