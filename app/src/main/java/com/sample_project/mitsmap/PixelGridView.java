package com.sample_project.mitsmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

//import androidx.appcompat.content.res.AppCompatResources;
//import androidx.core.content.ContextCompat;

public class PixelGridView extends View {
    private Canvas canvas;

    private int numColumns, numRows,numFloor;
    private static Bitmap bitmap_created, bm,bitmap4,drawableBitmap,bitmap2,overlaybitmap,bitmap5;

    private float cellWidth, cellHeight;
    private Paint blackPaint = new Paint();
    private Paint whitePaint = new Paint();
    private Paint greePaint = new Paint();
    private Paint bluePaint = new Paint();
    private boolean[][] cellChecked;

    Bitmap bitmap1 = null;

    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
    public PixelGridView(Context context) {
        this(context, null);
//        Log.i("bitmap1",this.floornum+"---"+numColumns);
//        init();
    }
    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
       // Log.i("bitmap2",GridActivity.getFloorNum()+"---"+numColumns);
        //  blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        greePaint.setColor(Color.GREEN);
        greePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bluePaint.setColor(Color.BLUE);
        bluePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        blackPaint.setTextSize(18);
        init();
    }
    private void init() {

//        bitmap_created = BitmapCreation();
//        Log.i("bitmap3",GridActivity.getFloorNum()+"---"+numColumns);
        //canvas = new Canvas(bitmap_created);
        // detectBluetoothSignal=new DetectBluetoothSignal(page_context);
        // myPath=new Path_Result();
        //to set background of image in the imageView
//        Drawable d = new BitmapDrawable(getResources(),bitmap_created);
////        dis=d.getIntrinsicHeight();
////        Log.i("screen_heig",dis+"");
//        setBackground(d);
    }
    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
    private Bitmap BitmapCreation(Bitmap bitmap){
        bitmap=rotateBitmap(bitmap,270);
        drawableBitmap = Bitmap.createScaledBitmap(bitmap, getScreenWidth(), getScreenHeight(), true);

        drawableBitmap = drawableBitmap.copy(Bitmap.Config.ARGB_8888, true);
//       // bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.fourth_floor_ramanujan_2);
//
//    //    Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.fourth_floor_ramanujan_color_modified);
//        Drawable d = null;
//        try{
//           Log.i("set_value_get",GridActivity.getFloorNum()+"");
//
//       }catch( Exception e){
//           Log.i("Exception",e+"");
//       }
//
//        bitmap1 = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//
//        Log.i("pic1", "LOCATION:(X,Y)= \n" + Resources.getSystem().getDisplayMetrics().widthPixels);
//        Log.i("pic2", "LOCATION:(X,Y)= \n" + Resources.getSystem().getDisplayMetrics().heightPixels);
//        Log.i("pic","width="+bitmap1.getWidth()+"   height="+bitmap1.getHeight());
//         bitmap1=rotateBitmap(bitmap1,270);
//        int nh = (int) ( bitmap1.getHeight() * (getScreenWidth() / bitmap1.getWidth()) );
//       // Bitmap drawableBitmap= Bitmap.createScaledBitmap(bitmap1,getScreenWidth(),nh,true);
//       Bitmap drawableBitmap=Bitmap.createScaledBitmap(bitmap1,getScreenWidth(),getScreenHeight(),true);
////        drawableBitmap = drawableBitmap.copy(Bitmap.Config.ARGB_8888, true);
//       // canvas = new Canvas(drawableBitmap);

        return drawableBitmap;
    }



    public static Bitmap rotateBitmap(Bitmap source, float angle) {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public static int getScreenWidth() {
        Log.i("touch>>>screenwidth", "LOCATION:(X,Y)= \n" + Resources.getSystem().getDisplayMetrics().widthPixels);
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        Log.i("touch>>>screenheight", "LOCATION:(X,Y)= \n" + Resources.getSystem().getDisplayMetrics().heightPixels);
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public int getNumColumns() {
        return numColumns;
    }
    public void setSelectedFloor(int floor_num) {

        this.numFloor = floor_num;
        Log.i("set_valu_pixelgrid",numFloor+"");
        calculateDimensions();
    }

    public int getSelectedFloor() {
        return numFloor;
    }
    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    public int getNumRows() {
        return numRows;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }

        cellWidth = getWidth() / (float)numColumns;
        cellHeight = (getHeight() / (float)(numRows));
Log.i("cell","width="+cellWidth+" height="+cellHeight);
        cellChecked = new boolean[numColumns][numRows];

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
       // canvas.drawColor(Color.WHITE);
       // canvas.drawLine(353.67252f,501.68427f,353.67252f,700f,greePaint);
        //Log.i("bitmap4",GridActivity.getFloorNum()+"---"+numColumns);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
        switch(1){
            case 1:   bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.reception_full);
                bitmap_created = BitmapCreation(bitmap4);
                break;
            case 2:   bitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.second_floor_edited);
                bitmap_created = BitmapCreation(bitmap5);
                break;
            default: Toast.makeText(getContext(), "Choose fifth floor", Toast.LENGTH_SHORT).show();

        }
        canvas.drawBitmap(bitmap_created, getMatrix(), null);
        if (numColumns == 0 || numRows == 0) {
            return;
        }

        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (cellChecked[i][j]) {

                    canvas.drawRect(i * cellWidth, j * cellHeight,
                            (i + 1) * cellWidth, (j + 1) * cellHeight,
                            greePaint);
                }
            }
        }

        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, blackPaint);
        }

        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackPaint);
        }
        for (int i = 0; i <=numColumns; i++) {
            for (int j = 0; j <=numRows; j++) {
                canvas.drawText((j)+"",i * cellWidth,(j)* cellHeight , blackPaint);

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int column = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);

            Log.i("col_row","row= "+row+" ;column= "+column+"--x="+event.getX()+" ,y="+event.getY());
            Log.i("col_row_para","cell_width"+cellWidth+" ;cell_height"+cellHeight);

            cellChecked[column][row] = !cellChecked[column][row];
            invalidate();
//            alertDialogBuilder.setTitle("Confirm Grid");
//            alertDialogBuilder.setMessage("Does the allocated grid and selected grid are the same?");
//            alertDialogBuilder.setCancelable(false);
//            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface arg0, int arg1) {
//                   // Toast.makeText(getContext(),"You clicked over Yes", Toast.LENGTH_SHORT).show();
//                    Intent newintent=new Intent(getContext(),CollectionActivity.class);
//                    Log.i("Floor_Selected",GridActivity.getFloorNum()+"");
//
//                    newintent.putExtra("grid_data",column+","+row);
//                    newintent.putExtra("floor_selected",GridActivity.getFloorNum());
//                    getContext().startActivity(newintent);
//                    cellChecked[column][row] = !cellChecked[column][row];
//                    invalidate();
//                }
//            });
//
//            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(getContext(),"Please select the appropriate grid", Toast.LENGTH_SHORT).show();
//                    cellChecked[column][row] = !cellChecked[column][row];
//                    invalidate();
//                }
//            });

//
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
        }

        return true;
    }



}

