package fr.uha.jacquey.hospitalbed.helper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Picture {

    static final String TAG = Picture.class.getSimpleName();

    private File photoFile;

    static private String createUniqueName(String prefix, String extension) {
        StringBuilder tmp = new StringBuilder();
        tmp.append(prefix);
        tmp.append("-");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        tmp.append(formatter.format(Calendar.getInstance().getTime()));
        if (extension != null) {
            tmp.append(".");
            tmp.append(extension);
        }
        return tmp.toString();
    }

    public void createPhotoFile (Context context) {
        String filename = createUniqueName("photo", "jpg");
        File folder = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (folder != null) {
            photoFile = new File (folder, filename);
        } else {
            photoFile = null;
        }
    }

    private static final String FILE_PROVIDER = "fr.uha.hassenforder.teams.fileprovider";
    public Uri getPhotoURI (Context context) {
        if (photoFile == null) return null;
        return FileProvider.getUriForFile(context, FILE_PROVIDER, photoFile);
    }

    public String getPhotoPath() {
        if (photoFile == null) return null;
        return photoFile.getAbsolutePath();
    }

    static private String getSimpleName (String path) {
        int lastSegmentPosition = path.lastIndexOf('/');
        if (lastSegmentPosition != -1) {
            int extensionPosition = path.lastIndexOf('.');
            if (extensionPosition != -1) {
                return path.substring(lastSegmentPosition+1, extensionPosition-1);
            } else {
                return path.substring(lastSegmentPosition+1);
            }
        } else {
            return path;
        }
    }

    static public Uri addToGallery(Context context, String photoPath) {
        if (photoPath == null) return null;
        Log.d(TAG, photoPath);
        String name = getSimpleName(photoPath);
        Log.d(TAG, name);
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, name);
        values.put(MediaStore.Images.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATA, photoPath);
        Uri base = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.insert(base, values);
    }

    static private InputStream getInputStream (Context context, String filename) {
        try {
            FileInputStream is = new FileInputStream(new File(filename));
            return is;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }

    static public Bitmap getBitmapFromUri(Context context, String filename, int targetW, int targetH) {
        try {
            if (filename == null) return null;
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            InputStream is = getInputStream(context, filename);
            BitmapFactory.decodeStream(is, null, bmOptions);
            is.close();

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            InputStream is2 = getInputStream(context, filename);
            Bitmap bitmap = BitmapFactory.decodeStream(is2, null, bmOptions);
            is2.close();
            return bitmap;
        } catch (Exception e) {
        }
        return null;
    }

}
