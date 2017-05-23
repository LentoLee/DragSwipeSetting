package lento.me.dragsetting;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by lento on 2017/5/22.
 */

public class Item implements Parcelable, Comparable<Item> {
    public static final int TYPE_ADDED = 0;
    public static final int TYPE_MORE_WIDGET_HEADER = 1;
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


    protected Item(Parcel in) {
        desc = in.readString();
        order = in.readInt();
        canOpt = in.readByte() != 0;
        viewType = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(desc);
        dest.writeInt(order);
        dest.writeByte((byte) (canOpt ? 1 : 0));
        dest.writeInt(viewType);
    }

    @Override
    public int compareTo(@NonNull Item item) {
        return this.order.compareTo(item.order);
    }
}
