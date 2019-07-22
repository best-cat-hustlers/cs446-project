package com.bestCatHustlers.sukodublitz.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableUtils
{
    // For some reason, Java uses the extends keyword for enforcing interfaces as well
    public static <T extends Serializable> byte[] serialize(T obj)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(obj);
            os.flush();
        }
        catch (IOException e)
        {}
        return out.toByteArray();
    }

    public static Object deserialize(byte[] bytes)
    {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        Object ret = null;
        try
        {
            ObjectInputStream is = new ObjectInputStream(in);
            ret = is.readObject();
        }
        catch (Exception e)
        {}
        return ret;
    }
}
