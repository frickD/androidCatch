package hochschule.maicatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


public class MainActivity extends Activity {

    ImageView imageView;
    Context context = null;
    Activity activity = null;


    TableLayout imageTable = null;

    ArrayList<String> imageList = new ArrayList<String>();
    ArrayList<Drawable> imageDrawable = new ArrayList<Drawable>();
    String path = "";

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;
        context = MainActivity.this;
    }

    public void onClear(View view){
        imageList.clear();
        imageDrawable.clear();
        deletePhotos();
        updateImageTable();
    }

    public void deletePhotos(){
        String folder = Environment.getExternalStorageDirectory() + "/LoadImg";
        File f = new File(folder);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            Log.v("Load Image", "Total Files to Delete =====>>>>" +files.length);
            for (int i = 0; i < files.length; i++) {
                String fpath = folder + File.separator + files[i].getName().toString().trim();
                System.out.println("File Full Path ------->" + fpath);
                File nf = new File(fpath);
                if(nf.exists()){
                    nf.delete();
                }
            }
        }
    }

    public void onTakePhoto(View view) {
        openSecondLayout();
        Toast.makeText(context, "Selected Photo", Toast.LENGTH_SHORT).show();
        takePhoto();

    }

    public void onChooseGallery(View view){
        openSecondLayout();
        Toast.makeText(context, "Selected Gallery", Toast.LENGTH_SHORT).show();
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    private void openSecondLayout(){
        setContentView(R.layout.secon_layout);
        imageTable = (TableLayout) findViewById(R.id.imageTable);
    }

    public void takePhoto(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File folder = new File(Environment.getExternalStorageDirectory() + "/LoadImg");

        if (!folder.exists()){
            folder.mkdir();
        }
        final Calendar c = Calendar.getInstance();
        String newDate = c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.MONTH) + "SEK" + c.get(Calendar.SECOND);
        path = String.format(Environment.getExternalStorageDirectory() + "/LoadImg/%s.png", "LoadImg("+newDate+")");
        File photo = new File(path);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            Uri photoUri = data.getData();
            if (photoUri != null){
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(photoUri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                Log.v("Load Image", "Gallery File Path ---->" + filePath);
                imageList.add(filePath);
                Log.v("Load Image", "Image List Size ---->" + imageList.size());

                new GetImages().execute();
            }
        }
        if (requestCode == 2) {
            Log.v("Load Image", "Camera File Path ---->" + path);
            imageList.add(path);
            Log.v("Load Image", "Image List Size ---->" + imageList.size());
            new GetImages().execute();
        }
    }

    public void updateImageTable(){
        imageTable.removeAllViews();

        if (imageDrawable.size() >0) {
            for (int i = 0; i < imageDrawable.size(); i++){
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
                tableRow.setPadding(5, 5, 5, 5);
                for (int j = 0; j <1; j++){
                    ImageView image = new ImageView(this);
                    image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    image.setBackgroundDrawable(imageDrawable.get(i));
                    tableRow.addView(image, 200, 200);
                }
                imageTable.addView(tableRow);
            }
        }
    }

    public Drawable loadImagefromurl(Bitmap icon){
        Drawable d = new BitmapDrawable(icon);
        return d;
    }

    public  class GetImages extends AsyncTask<Void, Void, Void>{
        public ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            progressDialog = progressDialog.show(context, "", "Loading...", true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            imageDrawable.clear();
            for (int i=0; i < imageList.size(); i++){
                Bitmap bitmap = BitmapFactory.decodeFile(imageList.get(i).toString().trim());
                bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
                Drawable d = loadImagefromurl(bitmap);
                imageDrawable.add(d);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            updateImageTable();
        }
    }
}
