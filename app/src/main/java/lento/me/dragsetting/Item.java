package lento.me.dragsetting;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by lento on 2017/5/22.
 */

public class Item implements Serializable, Comparable<Item> {
    private static final long serialVersionUID = 4056392726631386552L;
    public static final int TYPE_ADDED = 0;
    public static final int TYPE_REMOVED_HEADER = 1;
    public static final int TYPE_REMOVED = 2;
    String desc;
    Integer order;
    boolean canOpt;
    int viewType;

    /**
     * @param desc     描述
     * @param order    位置
     * @param canOpt   是否含有左侧的操作按钮
     * @param viewType
     */
    public Item(String desc, Integer order, boolean canOpt, int viewType) {
        this.desc = desc;
        this.order = order;
        this.canOpt = canOpt;
        this.viewType = viewType;
    }

    @Override
    public int compareTo(@NonNull Item item) {
        return this.order.compareTo(item.order);
    }
}
