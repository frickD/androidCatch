package hochschule.maicatch;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import hochschule.maicatch.ocr.IOCRCallBack;
import hochschule.maicatch.ocr.OCRAsyncTask;


public class MainActivity extends Activity implements IOCRCallBack {

    ImageView imageView;
    Context context = null;
    Activity activity = null;


    private String mAPiKey = "cbb3cc55e988957"; //TODO Add your own Registered API key
    private boolean isOverlayRequired;
    private String mImageUrl;
    private String mLanguage;
    private EditText mTxtResult;
    private IOCRCallBack mIOCRCallBack;
    TableLayout imageTable = null;


    public ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
    ArrayList<String> imageList = new ArrayList<String>();
    ArrayList<Drawable> imageDrawable = new ArrayList<Drawable>();
    String path = "";

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        openFirstLayout();
    }

    //start new Activity
    public void onSendToC4C(View view){
        String grabDataText = mTxtResult.getText().toString();
        Intent starnewAct = new Intent(MainActivity.this, ContactCreator.class);
        starnewAct.putExtra("grabDataText", grabDataText);
        startActivity(starnewAct);


    }

    public void onClear(){
        imageList.clear();
        imageDrawable.clear();
        bitmapList.clear();
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

    private void openFirstLayout(){
        setContentView(R.layout.main_layout);
        activity = MainActivity.this;
        context = MainActivity.this;
        mIOCRCallBack = this;
        mLanguage = "eng"; //Language
        isOverlayRequired = true;
    }

    private void openSecondLayout(){
        setContentView(R.layout.photoview_layout);
        imageTable = (TableLayout) findViewById(R.id.imageTable);
    }


    @Override
    public void onBackPressed(){
        openFirstLayout();
        onClear();
    }


    public void takePhoto(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat",
                Bitmap.CompressFormat.PNG.toString());
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
                    tableRow.addView(image, 700, 1244);
                }
                imageTable.addView(tableRow);
            }
        }
    }

    public Drawable loadImagefromurl(Bitmap icon){
        Drawable d = new BitmapDrawable(icon);
        return d;
    }

    @Override
    public void getOCRCallBackResult(String response) {
        setContentView(R.layout.ocrresult_layout);
        mTxtResult = (EditText) findViewById(R.id.result);
        mTxtResult.setText(response);
    }


    public void onStartOCR(View view){
        ProgressDialog mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle("Encoding the Picture....");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        Bitmap bm = bitmapList.get(0);
        String encodedImage = "data:image/jpeg;base64," + encodeToBase64(bm, Bitmap.CompressFormat.JPEG, 100);

        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(MainActivity.this, mAPiKey, isOverlayRequired, encodedImage, mLanguage,mIOCRCallBack);
        oCRAsyncTask.execute();
    }


    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
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
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap = Bitmap.createScaledBitmap(rotatedBitmap, 500, 899, true);
                bitmapList.add(bitmap);
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

