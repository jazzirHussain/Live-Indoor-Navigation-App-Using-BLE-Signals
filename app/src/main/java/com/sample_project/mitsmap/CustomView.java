package com.sample_project.mitsmap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

public class CustomView extends View {
    static int[] selected_pathpoints_new={0};
    private Paint blackPaint = new Paint();
    private Paint whitePaint = new Paint();
    private Paint greePaint = new Paint();
    private Paint bluePaint = new Paint();
    private Path path;
    JSONObject finalData;
    private boolean flag=true;
    private static Bitmap bitmap_created, bm,bitmap1,drawableBitmap,bitmap2,bitmap4;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private static float sX, sY, eX, eY;
    private Canvas canvas;
    private static  int width=0,height=0;
    private static int destLoc;
    private static int counter=0;
    private static int start_floor_num=0,dest_floor_num=0,start_room_vertex=0,dest_room_vertex=0;
    private static String destBuilding;
    private final int[][] adjacencyMatrix_floor1 = {
            {0, 1, 0, 0, 0, 0, 0},
            {1, 0, 1, 1, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 1, 1, 0},
            {0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 1},
            {0, 0, 0, 0, 0, 1, 0}
};
    private final int[][] adjacencyMatrix_floor2={
                    {0, 1, 0, 0, 0},
                    {1, 0, 1, 0, 0},
                    {0, 1, 0, 1, 1},
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0}

    };
    private final static int[][] WAY_POINTS= {
            {404, 657},
            {369, 657},
            {369, 692},
            {311, 657},
            {311, 554},
            {265, 657},
            {265, 618},
    };
    private final static int[][] WAY_POINTS_FLOOR2={
            {255,664},
            {325,664},
            {325,459},
            {275,459},
            {325,440}
    };


    private static int[][] converted_way_points;
    public CustomView(Context context) throws JSONException {

        super(context);
        Log.i("CON","Inside constructor 1");
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) throws JSONException {
        super(context, attrs);
        Log.i("CON","Inside constructor 2");
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) throws JSONException {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() throws JSONException {

        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        bluePaint.setColor(Color.BLUE);
        greePaint.setColor(Color.GREEN);
        greePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        blackPaint.setColor(Color.RED);
        blackPaint.setStyle(Paint.Style.STROKE);
        blackPaint.setTextSize(18);


            canvas = new Canvas();
       // overlaybitmap = BitmapFactory.decodeResource(getResources(), R.drawable.loc);
//        float ratio = Math.min(
//                (float) getScreenWidth() / overlaybitmap.getWidth(),
//                (float) getScreenHeight() / overlaybitmap.getHeight());
//        width = (int) ( (overlaybitmap.getWidth())/ratio);
//        height = (int) ((overlaybitmap.getHeight())/ratio);
//        Log.i("Values","width= "+width+"height= "+height);
//        //overlaybitmap=Bitmap.createScaledBitmap(overlaybitmap,width/2,height/2,true);




       // invalidate();
//        Drawable d = new BitmapDrawable(getResources(), bitmap_created);
//        setBackground(d);
    }

    private void changeWayPointsToDeviceDimension(int[][] WAY_POINTS) {
        for(int i=0;i<WAY_POINTS.length;i++){
            converted_way_points[i][0]=(int)generalizePointsConversion(WAY_POINTS[i][0],0);
            converted_way_points[i][1]=(int)generalizePointsConversion(WAY_POINTS[i][1],1);
        }
    }

    private Bitmap BitmapCreation(Bitmap bitmap) throws JSONException {


        bitmap=rotateBitmap(bitmap,270);

        drawableBitmap = Bitmap.createScaledBitmap(bitmap, getScreenWidth(), getScreenHeight(), true);
        drawableBitmap = drawableBitmap.copy(Bitmap.Config.ARGB_8888, true);
//        canvas = new Canvas(drawableBitmap);

        return drawableBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static int getScreenWidth() {
       // Log.i("touch>>>screenwidth", "LOCATION:(X,Y)= \n" + Resources.getSystem().getDisplayMetrics().widthPixels);
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        //Log.i("touch>>>screenheight", "LOCATION:(X,Y)= \n" + Resources.getSystem().getDisplayMetrics().heightPixels);
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }



    @Override
    protected void onDraw(Canvas canvas) {

       // super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
        float screenWidthPixel  = this.getResources().getDisplayMetrics().widthPixels;
        float screenHeightPixel = this.getResources().getDisplayMetrics().heightPixels;

        float STROKE_WIDTH = screenWidthPixel * 0.0080f;
        bluePaint.setStrokeWidth(STROKE_WIDTH);
      // canvas.save();
if(counter==0){
    bitmap2= BitmapFactory.decodeResource(getResources(), R.drawable.reception_edited);
    try {
        bitmap_created = BitmapCreation(bitmap2);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    canvas.drawBitmap(bitmap_created, getMatrix(), null);
    converted_way_points=new int[WAY_POINTS.length][2];
    changeWayPointsToDeviceDimension(WAY_POINTS);

    Log.i("PATH", "Calling dij"+getDestRoomVertex());
    DijkstrasAlgorithm.dijkstra(adjacencyMatrix_floor1, 0, 6);
    selected_pathpoints_new=Path_Result.getIntegerPath();
    Log.i("PATH", Arrays.toString(selected_pathpoints_new));

    sX=converted_way_points[selected_pathpoints_new[0]][0];
    sY=converted_way_points[selected_pathpoints_new[0]][1];
    canvas.drawCircle(sX,sY, 20, greePaint);
    for(int i=1;i<selected_pathpoints_new.length;i++){
        Log.i("selected","eX= "+eX+"eY="+eY);
        eX=converted_way_points[selected_pathpoints_new[i]][0];
        eY=converted_way_points[selected_pathpoints_new[i]][1];
        canvas.drawLine(sX,sY,eX,eY,bluePaint);

        sX=eX;
        sY=eY;
    }
    greePaint.setColor(Color.RED);
    canvas.drawCircle(eX,eY, 30, greePaint);
//
//
    Log.i("Values","overlayX= "+eX+"overlayY= "+eY);
    //canvas.drawBitmap(overlaybitmap, eX-width/5,10+eY-height/2 ,null);
    Log.i("Values1", DateFormat.getDateTimeInstance().format(new Date()));
//
//
////
    Path_Result.refreshPath();
    Arrays.fill(selected_pathpoints_new,0);
    counter=1;
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
        public void run() {
            if(this!=null ){
                Log.i("Values3", "Message");
                invalidate();

            }
        }
    }, 6000);
}else if(counter==1){
    bitmap2= BitmapFactory.decodeResource(getResources(), R.drawable.second_floor_full);
    try {
        bitmap_created = BitmapCreation(bitmap2);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    canvas.drawBitmap(bitmap_created, getMatrix(), null);
    converted_way_points=new int[WAY_POINTS_FLOOR2.length][2];
    changeWayPointsToDeviceDimension(WAY_POINTS_FLOOR2);

    Log.i("PATH", "Calling dij"+getDestRoomVertex());
    DijkstrasAlgorithm.dijkstra(adjacencyMatrix_floor2, 0, getDestRoomVertex());
    selected_pathpoints_new=Path_Result.getIntegerPath();
    Log.i("PATH", Arrays.toString(selected_pathpoints_new));

    sX=converted_way_points[selected_pathpoints_new[0]][0];
    sY=converted_way_points[selected_pathpoints_new[0]][1];
    greePaint.setColor(Color.GREEN);
    canvas.drawCircle(sX,sY, 20, greePaint);
    for(int i=1;i<selected_pathpoints_new.length;i++){
        Log.i("selected","eX= "+eX+"eY="+eY);
        eX=converted_way_points[selected_pathpoints_new[i]][0];
        eY=converted_way_points[selected_pathpoints_new[i]][1];
        canvas.drawLine(sX,sY,eX,eY,bluePaint);

        sX=eX;
        sY=eY;
    }
    greePaint.setColor(Color.RED);
    canvas.drawCircle(eX,eY, 30, greePaint);
    counter=0;
    sX=0;sY=0;eX=0;eY=0;
    greePaint.setColor(Color.GREEN);
//
//
    Log.i("Values","overlayX= "+eX+"overlayY= "+eY);
    //canvas.drawBitmap(overlaybitmap, eX-width/5,10+eY-height/2 ,null);
    Log.i("Values1", DateFormat.getDateTimeInstance().format(new Date()));
}
        Path_Result.refreshPath();
        Arrays.fill(selected_pathpoints_new,0);

//        try {



    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Let the ScaleGestureDetector inspect all events.
        //mScaleDetector.onTouchEvent(event);
//
//        switch(event.getAction()){
//            // When user touches the screen
//            case MotionEvent.ACTION_DOWN:
//                sX=event.getX();
//                sY=event.getY();
//
//
//                break;
//        }
//        invalidate();
//        return  true;
        float x = event.getX();
        float y = event.getY();
//        switch(event.getAction())
//        {
//            case MotionEvent.ACTION_DOWN:
//                //Check if the x and y position of the touch is inside the bitmap
//                if( x > eX-width/5 && x < eX+width  && y > 10+eY-height/2 && y < eY  )
//                {
//                    Log.e("TOUCHED", "X: " + x + " Y: " + y);
//                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
//                    View bottomSheetView = LayoutInflater.from(getContext().getApplicationContext())
//                            .inflate(
//                                    R.layout.activity_bottomdialog,
//                                    findViewById(R.id.bottomSheetContainer));
//                    bottomSheetDialog.setContentView( bottomSheetView);
//                    bottomSheetDialog.show( );
//                    TextView name,desc,time,room;
//                    name = bottomSheetDialog.findViewById(R.id.textView2);
//                    desc = bottomSheetDialog.findViewById(R.id.textView3);
//                    time = bottomSheetDialog.findViewById(R.id.textView4);
//                    room = bottomSheetDialog.findViewById(R.id.textView5);
//                     try {
//                        name.setText(finalData.getString("title"));
//                        desc.setText(finalData.getString("content"));
//                        time.setText(finalData.getString("date"));
//                        room.setText(finalData.getString("venue"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//                return true;
//
//        }
//        invalidate();
        return false;
    }
    private void drawMyPath(float x1, float y1, float x2, float y2, Paint paint) {
        Log.i("draw_path",x1+","+y1+","+x2+","+y2);
        canvas.drawLine(x1,y1,x2,y2,paint);
    }
    private float generalizePointsConversion(float x1, int width_height) {
        float olddevicewidth = 480.0f;
        float olddeviceheigth = 800.0f;


//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);



        float currentDeviceWidth = getScreenWidth();
        float currentDeviceheight = getScreenHeight();


// check for x or y coordinates
        if(width_height==0){
            x1=(x1/olddevicewidth)*currentDeviceWidth;
        }else if(width_height==1){
            x1=(x1/olddeviceheigth)*currentDeviceheight;
        }

//
//        x2=(x2/olddevicewidth)*currentDeviceWidth;
//        y2=(y2/olddeviceheigth)*currentDeviceheight;
       Log.i("LOG_VALUES","x1="+x1);
        return x1;


    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
          }

    public int getStartFloorNum() {
        return start_floor_num;
    }
    public void setStartFloorNum(int floor_num) {

        this.start_floor_num = floor_num;
        Log.i("set_valu_pixelgrid",start_floor_num+"");

    }
    public int getDestFloorNum() {
        return dest_floor_num;
    }
    public void setDestFloorNum(int floor_num) {

        this.dest_floor_num = floor_num;
        Log.i("set_valu_pixelgrid",dest_floor_num+"");

    }
    public int getStartRoomVertex() {
        return start_room_vertex;
    }
    public void setStartRoomVertex(int room_vertex) {

        this.start_room_vertex = room_vertex;
        Log.i("set_valu_pixelgrid",start_room_vertex+"");

    }
    public int getDestRoomVertex() {
        return dest_room_vertex;
    }
    public void setDestRoomVertex(int room_vertex) {

        this.dest_room_vertex = room_vertex;
        Log.i("set_valu_pixelgrid",dest_room_vertex+"");

    }
    public void setMySelectedEvent(int i) {
        Log.i("DEST","Set Dest"+i);
        this.destLoc=i;
    }

    public int getMySelectedEvent() {
        Log.i("DEST","Get Dest"+destLoc);
        return this.destLoc;
    }
    public void setMySelectedBuilding(String i) {
        Log.i("DEST","Set Dest"+i);
        this.destBuilding=i;
    }

    public String getMySelectedBuilding() {
        Log.i("DEST","Get Dest"+destLoc);
        return this.destBuilding;
    }

    public void setMyEventData(JSONObject data) {
        this.finalData = data;

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }

}