package com.example.hdvideoplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.VideoModel2;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class utils {
    public static ArrayList<VideoModel2> folderarraylst = new ArrayList<>();

    public static String getFolderSize(ArrayList<VideoModel2> arrayList) {
        ArrayList<File> fileArrayList = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            fileArrayList.add(new File(arrayList.get(i).getImagepath()));
        }
        long temp = 0;
        for (int i = 0; i < fileArrayList.size(); i++) {
            temp += fileArrayList.get(i).length();

        }
        return getFileSize(temp);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static String getPath(Context context) {
        return "Pictures/." + context.getString(R.string.app_name);
    }

    public static String getPathTemp(Context context) {
        return "Pictures/" + context.getString(R.string.app_name);
    }

    public static final String UnhideImage = (Environment.getExternalStorageDirectory() + "/Pictures/");
    public static final String Temp = (Environment.getExternalStorageDirectory() + "/Pictures");

    public static String getHide(Context context) {
        return Environment.getExternalStorageDirectory() + "/Pictures/." + context.getString(R.string.app_name) + "/Video";
    }


    public static Uri getPath(File file, Context context) {

        return FileProvider.getUriForFile(context,
                context.getPackageName() + ".provider", file);
    }

    public static String getDuratiom(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Log.d("===", "" + duration);
        return milliSecondsToTimer(duration);
    }

    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = null;
        String secondsString = null;
        String minutesString = null;
        try {
            finalTimerString = "";
            secondsString = "";
            int hours = (int) (milliseconds / 3600000);
            int minutes = ((int) (milliseconds % 3600000)) / 60000;
            int seconds = (int) (((milliseconds % 3600000) % 60000L) / 1000);
            if (hours > 0) {
                finalTimerString = hours + ":";
            }
            if (seconds < 10) {
                secondsString = "0" + seconds;
            } else {
                secondsString = "" + seconds;
            }
            minutesString = "" + minutes;
            if (finalTimerString.length() > 0 && minutes < 10) {
                minutesString = "0" + minutes;
            }
            return finalTimerString + minutesString + ":" + secondsString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    @SuppressLint("WrongConstant")
    public static String share(ArrayList<VideoModel2> photosArrayList, Context context) {


        try {
            if (photosArrayList == null || photosArrayList.size() <= 0) {
                Toast.makeText(context, "Try after selecting images", Toast.LENGTH_SHORT).show();
                return "";
            }
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND_MULTIPLE");
            intent.putExtra("android.intent.extra.SUBJECT", "");
            intent.setType("*/*");
            ArrayList arrayList = new ArrayList();

            for (int i = 0; i < photosArrayList.size(); i++) {
                Uri uri;
                if (Build.VERSION.SDK_INT >= 23) {
                    uri = getPath(new File(photosArrayList.get(i).getImagepath()), context);
                    arrayList.add(uri);
                } else {
                    uri = Uri.fromFile(new File(photosArrayList.get(i).getImagepath()));
                    arrayList.add(uri);

                }
            }
            intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
            intent.addFlags(1);
            context.startActivity(intent);
        } catch (Exception e) {
            return "";

        }
        return "";

    }

    @SuppressLint("WrongConstant")
    public static String play(File file, Context context) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        Uri uri;
        if (Build.VERSION.SDK_INT >= 23) {
            uri = getPath(file.getAbsoluteFile(), context);
        } else {
            uri = Uri.fromFile(file);
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        String mimeType = mime.getMimeTypeFromExtension(getExtension(file.getAbsolutePath()).substring(1));

        intent.setDataAndType(uri, mimeType);
        intent.addFlags(1);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }


        return "";
    }

    public static String getExtension(String url) {


        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf("."));
            if (ext.contains("%")) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.contains("/")) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();
        }
    }


    public String size(long j) {
        double d = (double) j;
        Double.isNaN(d);
        double d2 = d / 1024.0d;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (d2 > 1.0d) {
            return decimalFormat.format(d2).concat(" MB");
        }
        return decimalFormat.format(j).concat(" KB");
    }

    public static String getFileSize(long size) {
        try {
            if (size <= 0)
                return "0";

            final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
            int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

            return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        return "" + imageHeight + "x" + imageWidth;
    }

    public static boolean isImage(String path) {
        String fileExtension = new File(path).getName().substring(new File(path).getName().lastIndexOf(".") + 1);

        if (fileExtension.equals("png") || fileExtension.equals("jpeg") || fileExtension.equals("jpg")) {
            return true;
        } else if (fileExtension.equals("mp4") || fileExtension.equals("3gp") || fileExtension.equals("wmv") || fileExtension.equals("avi") || fileExtension.equals("mkv")) {
            return false;
        }
        return false;
    }

    public static String getResolation(String path) {
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(path);
        String height = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String width = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        return height + "x" + width;
    }

    public static String getDateTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(date);
    }


    public static String getURLForResource(int resourceId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
    }

    private static SimpleDateFormat yyyy_MM_dd_HH_mm = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm", Locale.getDefault());
    private static SimpleDateFormat HHmm = new SimpleDateFormat("HH:mm",
            Locale.getDefault());/*from w  w  w .  j a  v  a  2s .  co m*/
    private static SimpleDateFormat MM_dd_HHmm = new SimpleDateFormat(
            "MM-dd HH:mm", Locale.getDefault());

    public static String date2DayTime(Date oldTime) {
        Date newTime = new Date();
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(newTime);
            Calendar oldCal = Calendar.getInstance();
            oldCal.setTime(oldTime);

            int oldYear = oldCal.get(Calendar.YEAR);
            int year = cal.get(Calendar.YEAR);
            int oldDay = oldCal.get(Calendar.DAY_OF_YEAR);
            int day = cal.get(Calendar.DAY_OF_YEAR);

            if (oldYear == year) {
                int value = oldDay - day;
                if (value == -1) {
                    return "Yesterday " + HHmm.format(oldTime);
                } else if (value == 0) {
                    return "Today " + HHmm.format(oldTime);
                } else if (value == 1) {
                    return "Tomorrow " + HHmm.format(oldTime);
                } else {
                    return MM_dd_HHmm.format(oldTime);
                }
            }
        } catch (Exception e) {

        }
        return yyyy_MM_dd_HH_mm.format(oldTime);
    }

    public static String FolderDetails(Context context, FolderModel folderModel) {
        final BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.SheetDialog);
        dialog.setContentView(R.layout.dialog_folder_details);
        dialog.setCancelable(true);
        TextView file_name = dialog.findViewById(R.id.file_name);
        TextView count = dialog.findViewById(R.id.count);
        TextView file_size = dialog.findViewById(R.id.file_size);
        TextView file_path = dialog.findViewById(R.id.file_path);
        TextView file_date = dialog.findViewById(R.id.file_date);
        TextView duration = dialog.findViewById(R.id.duration);

        File file = new File(folderModel.getAlbumpath());
        file_name.setText(folderModel.getAlbumname());
        file_path.setText(file.getParent());
        count.setText("" + folderModel.getPhotosDatas().size());

        file_date.setText(getDateTime(new Date(file.lastModified())));
        file_size.setText(getFolderSize(folderModel.getPhotosDatas()));

        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();


            }
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();
        return "";
    }




    public static String FileDetails(Context context, String path) {
        final BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.SheetDialog);
        dialog.setContentView(R.layout.dialog_file_details);
        dialog.setCancelable(true);
        TextView file_name = dialog.findViewById(R.id.file_name);
        TextView file_resouation = dialog.findViewById(R.id.file_resouation);
        TextView file_size = dialog.findViewById(R.id.file_size);
        TextView file_path = dialog.findViewById(R.id.file_path);
        TextView file_date = dialog.findViewById(R.id.file_date);
        TextView duration = dialog.findViewById(R.id.duration);

        File file = new File(path);
        file_name.setText(file.getName());
        file_path.setText(file.getAbsolutePath());
        try {
            duration.setText(getDuratiom(path));

        } catch (Exception e) {
            duration.setText("2:50");

        }


        try {

            file_resouation.setText(getResolation(file.getAbsolutePath()));
        } catch (Exception e) {
            file_resouation.setText("240 X 300");

        }

        file_date.setText(getDateTime(new Date(file.lastModified())));
        file_size.setText(getFileSize(file.length()));

        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();


            }
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();
        return "";
    }


    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }


    public static boolean recent_chek(String path, Context context) {
        RecentDatabaseAdapter databaseFavHelper = new RecentDatabaseAdapter(context);
        Cursor res = databaseFavHelper.getAllData();
        if (res.getCount() == 0) {
            return false;
        } else {
            while (res.moveToNext()) {
                if (res.getString(1).equals(path)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getId_recent(String path, Context context) {
        RecentDatabaseAdapter databaseFavHelper = new RecentDatabaseAdapter(context);
        Cursor res = databaseFavHelper.getAllData();
        if (res.getCount() == 0) {
            return null;
        } else {
            while (res.moveToNext()) {
                if (path.equals(res.getString(1))) {
                    return res.getString(0);
                }
            }
        }

        return null;
    }

    public static ArrayList<VideoModel2> tempvideo = new ArrayList<>();

    public static String video(ArrayList<VideoModel2> arrayList) {
        if (tempvideo.size() != 0) {
            tempvideo.clear();
        }
        tempvideo.addAll(arrayList);
        return "";
    }


    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable = AppCompatResources.getDrawable(context, drawableId);
        return getBitmap(drawable);
    }

    public static Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawableCompat || drawable instanceof VectorDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    public static long getId(String songPath, Context context) {
        long id = 0;
        ContentResolver cr = context.getContentResolver();

        String selection = MediaStore.Video.Media.DATA;
        String[] selectionArgs = {songPath};
        String[] projection = {MediaStore.Video.Media._ID};

        Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection + "=?", selectionArgs, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID);
                id = Long.parseLong(cursor.getString(idIndex));
                Log.d("Image", "" + id);
            }
        }

        return id;
    }

    public static boolean hasPermissions(Context context, String... strArr) {
        if (!(Build.VERSION.SDK_INT < 23 || context == null || strArr == null)) {
            for (String checkSelfPermission : strArr) {
                if (ContextCompat.checkSelfPermission(context, checkSelfPermission) != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
