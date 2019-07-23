package com.bestCatHustlers.sukodublitz.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

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
        return compress(out.toByteArray());
    }

    public static Object deserialize(byte[] bytes)
    {
        ByteArrayInputStream in = new ByteArrayInputStream(decompress(bytes));
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

    private static byte[] compress(byte[] bytes)
    {
        Deflater deflate = new Deflater();
        deflate.setLevel(Deflater.BEST_COMPRESSION);
        deflate.setInput(bytes);
        deflate.finish();
        ByteArrayOutputStream out = new ByteArrayOutputStream(bytes.length);
        byte[] buffer = new byte[1024];
        while (!deflate.finished())
        {
            int count = deflate.deflate(buffer);
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }

    private static byte[] decompress(byte[] bytes)
    {
        Inflater inflate = new Inflater();
        inflate.setInput(bytes);
        ByteArrayOutputStream out = new ByteArrayOutputStream(bytes.length);
        byte[] buffer = new byte[1024];
        try
        {
            while (!inflate.finished())
            {
                int count = inflate.inflate(buffer);
                out.write(buffer, 0, count);
            }
        }
        catch (Exception e)
        {}
        return out.toByteArray();
    }
}
