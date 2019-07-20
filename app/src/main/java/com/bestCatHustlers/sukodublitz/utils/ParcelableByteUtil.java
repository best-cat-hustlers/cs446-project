package com.bestCatHustlers.sukodublitz.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is to help send data through bluetooth, specifically exchange between Parcelable
 * objects and bytes
 *
 * reference from https://stackoverflow.com/questions/18000093
 */
public class ParcelableByteUtil {
    /**
     * marshall(Parcelable parceable)
     * Usage:
     *
     * BoardGame model = myModel;
     * byte[] bytes = ParcelableByteUtil.marshall(model);
     */
    public static byte[] marshall(Parcelable parceable) {
        Parcel parcel = Parcel.obtain();
        parceable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    /**
     * unmarshall(byte[] bytes)
     * Usage:
     *
     * byte[] bytes = someBytes;
     * Parcel parcel = ParcelableByteUtil.unmarshall(bytes);
     * BoardGame model = new BoardGame(parcel);
     */
    public static Parcel unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        return parcel;
    }

    /**
     * unmarshall(byte[] bytes, Parcelable.Creator<T> creator)
     * Usage:
     *
     * byte[] bytes = someBytes;
     * BoardGame model = ParcelableByteUtil.unmarshall(bytes, BoardGame.CREATOR);
     */
    public static <T> T unmarshall(byte[] bytes, Parcelable.Creator<T> creator) {
        Parcel parcel = unmarshall(bytes);
        T result = creator.createFromParcel(parcel);
        parcel.recycle();
        return result;
    }
}
