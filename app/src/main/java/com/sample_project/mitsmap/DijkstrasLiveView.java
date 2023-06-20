package com.sample_project.mitsmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class DijkstrasLiveView extends View {
    private Canvas canvas;
    private Bitmap bitmap_created,bm;
    private int numColumns, numRows;
    private static int GridX=0,GridY=0;
    private float cellWidth, cellHeight;
    private SensorData sensorData;
    private Paint blackPaint = new Paint();
    private Paint whitePaint = new Paint();
    private Paint greePaint = new Paint();
    private Paint redPaint = new Paint();
    private Paint bluePaint = new Paint();
    private boolean[][] cellChecked;
    static  boolean myFlag=false;
    private Bitmap arrow;
    SharedPreferences sharedpreference_location ;
    SharedPreferences.Editor editor ;
    boolean sign_value=true;

    public DijkstrasLiveView(Context context) {
        super(context);
        init();
        Log.i("dij_CONSTRUCTOR","con1");
        //init();
    }

    public DijkstrasLiveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.i("dij_CONSTRUCTOR","con2");
        init();
    }
    private void init() {
        sharedpreference_location = getContext().getSharedPreferences("Nav_Location", 0);
       //editor = sharedpreference_location.edit();
        greePaint.setColor(Color.GREEN);
        greePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStyle(Paint.Style.STROKE);
        blackPaint.setTextSize(18);
        redPaint.setColor(Color.MAGENTA);
        redPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        redPaint.setStrokeWidth(10);
        bluePaint.setColor(Color.BLUE);
        bluePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bluePaint.setStrokeWidth(10);
        bitmap_created = BitmapCreation();
        arrow = Inventory.getArrow(getContext());
        canvas = new Canvas(bitmap_created);
        sensorData = new SensorData(getContext());
        sensorData.loadCompass();
       // sensorData.loadMagnetic();
        Drawable d = new BitmapDrawable(getResources(),bitmap_created);
        setBackground(d);
    }
    private Bitmap BitmapCreation(){
      //  Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.five_floor_1_app);
        Bitmap bitmap1 = null;
        int s1=sharedpreference_location.getInt("start_location", -1);
        int d1=sharedpreference_location.getInt("destination_location", -1);
        int start_floor=sharedpreference_location.getInt("Start_floor",-1);
        int dest_floor=sharedpreference_location.getInt("Dest_Floor",-1);
        Log.i("My_points","start="+s1+" destination="+d1);
        int check=s1-d1;
        float rot_angle;
        if(check>=0){
            sign_value=true;//negative=90
            rot_angle=180;
        }

        else{
            sign_value=false;//postivie=180
            rot_angle=0;
        }
        try{
            switch(DijkstrasActivity.getFloorNum()){
                case 1:  bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.reception_blue);
                    rot_angle=270;
                    sign_value=false;
                    break;
            case 2:  bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.second_floor_blue);
                    rot_angle=270;
                    sign_value=false;
                break;

                default: Toast.makeText(getContext(), "Choose fifth floor", Toast.LENGTH_SHORT).show();

            }
        }catch( Exception e){
            Log.i("Exception",e+"");
        }

       Log.i("s1d1","s1= "+s1+"and d1= "+d1);
        bitmap1=rotateBitmap(bitmap1,rot_angle);
        Bitmap drawableBitmap= Bitmap.createScaledBitmap(bitmap1,getScreenWidth(),getScreenHeight(),true);
        drawableBitmap = drawableBitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(drawableBitmap);

        return drawableBitmap;
    }
    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public int rotateX180(int a){
      int new_a=0;
      new_a=(numColumns-a);
      return new_a;
    }
    public int rotateY180(int b){
        int new_b=0;
        new_b=(numRows-b);
        return new_b;
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

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }
    public void setGridValue(String result) {
        Log.i("grid_val_1", result);
        String[] splitString = result.split(",");
        this.GridX= Integer.parseInt(splitString[0]);
        this.GridY= Integer.parseInt(splitString[1]);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDraw(Canvas canvas) {
        // canvas.drawColor(Color.WHITE);
        // canvas.drawLine(353.67252f,501.68427f,353.67252f,700f,greePaint);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
       // canvas.drawLine((9)*cellWidth,(47)*cellHeight,(12)*cellWidth,(47)*cellHeight,greePaint);
        if(myFlag==false ){
            myFlag=true;
            drawResultPath(canvas,cellWidth,cellHeight);
            drawSourceDestination(canvas,cellWidth,cellHeight);

           // drawArrow(canvas,GridX*cellWidth,GridY*cellHeight);
            //invalidate();
        }else{
            drawResultPath(canvas,cellWidth,cellHeight);
            drawSourceDestination(canvas,cellWidth,cellHeight);
//


            if (numColumns == 0 || numRows == 0) {
                return;
            }

            int width = getWidth();
            int height = getHeight();
            Log.i("Compass_before","x= "+GridX+"y= "+GridY);
            if(sign_value)
            {
                GridX=rotateX180(GridX);
                GridY=rotateY180(GridY);
            }
            cellChecked[GridX][GridY]=true;
            Log.i("Compass_after","x= "+GridX+"y= "+GridY);
            for (int i = 0; i < numColumns; i++) {
                for (int j = 0; j < numRows; j++) {
                    if (cellChecked[i][j]) {
                        Log.i("grid_val", "["+i+","+j+"] "+ String.valueOf(cellChecked[i][j])+"----->>"+java.time.LocalTime.now()  );
//
                        drawArrow(canvas,(i*cellWidth)+(cellWidth/2),(j*cellHeight)+(cellWidth/2));
                        cellChecked[i][j]=false;
                    }
                }
            }
//
//            for (int i = 1; i < numColumns; i++) {
//                canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, blackPaint);
//            }
//
//            for (int i = 1; i < numRows; i++) {
//                canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackPaint);
//            }
//            for (int i = 0; i <=numColumns; i++) {
//                for (int j = 0; j <=numRows; j++) {
//                    canvas.drawText((numRows-j)+1+"",i * cellWidth,(j)* cellHeight , blackPaint);
//
//                }
//            }

        }

    }

    private void drawSourceDestination(Canvas canvas, float cellWidth, float cellHeight) {
//        if(DijkstrasActivity.arrayReceived!=null){
            int start_x = DijkstrasActivity.arrayReceived[0][0];
            int start_y = DijkstrasActivity.arrayReceived[0][1];
            int end_x=DijkstrasActivity.arrayReceived[DijkstrasActivity.arrayReceived.length-1][0];
            int end_y=DijkstrasActivity.arrayReceived[DijkstrasActivity.arrayReceived.length-1][1];
            if(sign_value){
                start_x=rotateX180(start_x);
                start_y=rotateY180(start_y);
                end_x=rotateX180(end_x);
                end_y=rotateY180(end_y);
            }
            canvas.drawCircle((start_x*cellWidth)+(cellWidth/2),(start_y*cellHeight)+(cellHeight/2),15,greePaint);
            canvas.drawCircle((end_x*cellWidth)+(cellWidth/2),(end_y*cellHeight)+(cellHeight/2),18,redPaint);
//        }


    }

    private void drawResultPath(Canvas canvas, float cellWidth, float cellHeight) {
      //  cellWidth=cellWidth+(cellWidth/2);

//        Log.i("cellw_old", cellWidth+"");
//        Log.i("cellh_old", cellHeight+"");
//        cellHeight= (float) (cellHeight+0.5);
//        cellWidth= (float) (cellWidth+0.5);

            for(int i=1;i<DijkstrasActivity.arrayReceived.length;i++)
            {
                int x1=DijkstrasActivity.arrayReceived[i-1][0];
                int x2=DijkstrasActivity.arrayReceived[i][0];
                int y1=DijkstrasActivity.arrayReceived[i-1][1];
                int y2=DijkstrasActivity.arrayReceived[i][1];
                Log.i("cellx1", x1+"");
                Log.i("celly1", y1+"");
                Log.i("cellx2", x2+"");
                Log.i("celly2", y2+"");
                Log.i("c_b_grid_valx1", (x1)*cellWidth+"");
                Log.i("c_b_grid_valx1", (y1)*cellHeight+"");
                Log.i("c_b_grid_valx1", (x2)*cellWidth+"");
                Log.i("c_b_grid_valx1", (y2)*cellHeight+"");
                if(sign_value){
                    x1=rotateX180(x1);
                    x2=rotateX180(x2);
                    y1=rotateY180(y1);
                    y2=rotateY180(y2);

                }
                Log.i("c_a_grid_valx1", (x1)*cellWidth+"");
                Log.i("c_a_grid_valx1", (y1)*cellHeight+"");
                Log.i("c_a_grid_valx1", (x2)*cellWidth+"");
                Log.i("c_a_grid_valx1", (y2)*cellHeight+"");
                redPaint.setPathEffect(new DashPathEffect(new float[]{10.0f,5.0f},0));
                canvas.drawLine(((x1)*cellWidth)+(cellWidth/2),((y1)*cellHeight)+(cellHeight/2),((x2)*cellWidth)+(cellWidth/2),((y2)*cellHeight)+(cellHeight/2),redPaint);

            }


    }

    void drawArrow(Canvas c, float x, float y) {

        // log values of sensor s

//
//        c.drawText(String.valueOf(sensorData.getCompassValue()), 50, 50,
//                Inventory.text50BLUE());
//        c.drawText(String.valueOf(sensorData.getMagneticValue()), 50, 100,
//                Inventory.text50BLUE());
//
//
//        // setting boundary
//        if (Inventory.X >= 1080)
//            Inventory.X = 1080 - arrow.getWidth();
//        if (Inventory.Y >= 1720)
//            Inventory.Y = 1720 - arrow.getHeight();
//
//        if (Inventory.X <= 0)
//            Inventory.X = 0 + arrow.getWidth();
//        if (Inventory.Y <= 0)
//            Inventory.Y = 0 + arrow.getHeight();
//
//        Matrix m = new Matrix();
//
//        float x_compass= (float) sensorData.getCompassValue();
//        //Toast.makeText(getContext(),"Compass_value"+x_compass,Toast.LENGTH_SHORT).show();
//        if(sign_value){
//            x_compass=x_compass-180;
//        }
//        //Toast.makeText(getContext(),"Compass_value2"+x_compass,Toast.LENGTH_SHORT).show();
////        if (x_compass >= 0 )
////            x_compass=x_compass;
////    else
////            x_compass=x_compass + 360;
//
////        c.drawText(String.valueOf(x_compass), 50, 150,
////                Inventory.text50BLUE());
////        if(x_compass>0)
////            x_compass=x_compass+360;
//
//
//        m.setRotate((float) (x_compass),
//                arrow.getWidth() / 2.0f, arrow.getHeight() / 2.0f);
//
//        m.postTranslate(x- arrow.getWidth() / 2.0f, y
//                - arrow.getHeight() / 2.0f);
//        c.drawBitmap(arrow, m, null);
//c.drawCircle(x,y,15,bluePaint);
bluePaint.setStyle(Paint.Style.STROKE);
c.drawCircle(x,y,20,bluePaint);
//bluePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

}
