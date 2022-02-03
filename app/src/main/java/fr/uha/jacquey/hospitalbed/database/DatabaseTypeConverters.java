package fr.uha.jacquey.hospitalbed.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class DatabaseTypeConverters {

    @TypeConverter
    public static Date long2Date (long time) {
        if (time == -1) return null;
        return new Date (time);
    }

    @TypeConverter
    public static long date2Long (Date date) {
        if (date == null) return -1;
        return date.getTime();
    }

    @TypeConverter
    public static Bitmap toBitmap(byte [] content) {
        if (content == null) return null;
        Bitmap bmp = BitmapFactory.decodeByteArray(content, 0, content.length);
        return bmp;
    }

    @TypeConverter
    public static byte [] fromBitmap (Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

}
