package com.bestCatHustlers.sukodublitz.bluetooth;

import com.bestCatHustlers.sukodublitz.utils.SerializableUtils;

import java.io.Serializable;

public class BluetoothMessage implements Serializable {
    public String tag;
    public Object payload;

    public BluetoothMessage(String tag, Object payload) {
        this.tag = tag;
        this.payload = payload;
    }

    public byte[] serialized() {
        return SerializableUtils.serialize(this);
    }
}
