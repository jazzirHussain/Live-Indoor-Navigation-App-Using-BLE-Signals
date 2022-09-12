package com.sample_project.mitsmap;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.List;
import java.util.Timer;

import static android.content.Context.SENSOR_SERVICE;

public class SensorData implements SensorEventListener {

	private float accelerometerX;
	private float accelerometerY;
	private float accelerometerZ;
	private float magneticX;
	private float magneticY;
	private float magneticZ;
	private float light;
	private float rotationX;
	private float rotationY;
	private float rotationZ;
	private float[] rotation;
	private float[] inclination;
	//private static float[] orientation;
	static int button_status=0;
	private SensorManager sensorManager;
	// accelerometer
	public static double[] lastAcc = new double[] { 0.0, 0.0, 0.0 };
	private static final int sizeFV = 6;
	private double[] five_values = new double[sizeFV];
	private int fv_iterator = 0; // iterator for five_values
	double a = 0.5;
	private Context context;
	private List<Sensor> l_sensors;
	private Timer timer;
	public final long INTERVAL = (1000 / 30); // 33.33 ms
	private final long timeout = 600;
	private long lastStepDetectedTime = 0;
	public static int stepCounter = 0;
	public final double differential = 2.6; // experimental value
	double[] oldAcc = new double[3];
	private float gridSize = 20.0f;
	private float stepSize = 0.7f;

	// compass
	public double compValue[] = new double[] { 0.0, 0.0, 0.0 };
	private static double compassValue = -1.0;
	private SensorManager mSensorManager;
	private Sensor mRotationV, mAccelerometer, mMagnetometer;
	boolean haveSensor = false, haveSensor2 = false;
	float[] rMat = new float[9];
	float[] orientation = new float[3];
	private float[] mLastAccelerometer = new float[3];
	private float[] mLastMagnetometer = new float[3];
	private boolean mLastAccelerometerSet = false;
	private boolean mLastMagnetometerSet = false;

	// magnetic field
	private static double magneticValue = -1.0;
	private static float mCompass;
	public static float getCompassValue() {
		return mCompass;
	}

	public static double getMagneticValue() {
		return magneticValue;
	}

//	// sensor Event Listeners
//	public SensorEventListener myCompassEventListener = new SensorEventListener() {
//		@Override
//		public void onAccuracyChanged(Sensor sensor, int accuracy) {
//			;
//		}
//
//		@Override
//		public void onSensorChanged(SensorEvent sensorEvent) {
//			switch (sensorEvent.sensor.getType()) {
//				case Sensor.TYPE_ACCELEROMETER:
//					accelerometerX = sensorEvent.values[0];
//					accelerometerY = sensorEvent.values[1];
//					accelerometerZ= sensorEvent.values[2];
//					break;
//				case Sensor.TYPE_MAGNETIC_FIELD:
//					magneticX = sensorEvent.values[0];
//					magneticY = sensorEvent.values[1];
//					magneticZ = sensorEvent.values[2];
//					break;
//				case Sensor.TYPE_LIGHT:
//					light = sensorEvent.values[0];
//					break;
//				case Sensor.TYPE_ROTATION_VECTOR:
//					rotationX = sensorEvent.values[0];
//					rotationY = sensorEvent.values[1];
//					rotationZ = sensorEvent.values[2];
//					break;
//				default:
//					break;
//			}
//
//			SensorManager.getRotationMatrix(rotation, inclination,
//					new float[] {accelerometerX, accelerometerY, accelerometerZ},
//					new float[] {magneticX, magneticY, magneticZ});
//			orientation = SensorManager.getOrientation(rotation, orientation);
//
//			Log.i("Compass_data", "scan finished--" +
//					accelerometerX + "," + accelerometerY + "," + accelerometerZ +
//					"*" + magneticX + "," + magneticY + "," + magneticZ + "*" + light +
//					"*" + rotationX + "," + rotationY + "," + rotationZ + "*" +
//					orientation[0] + "," + orientation[1] + "," + orientation[2]
//			);
//		}
//
//	};
//
//	// accelerometer sensor Event Listener
//	public SensorEventListener myAccEventListener = new SensorEventListener() {
//
//		@Override
//		public void onSensorChanged(SensorEvent event) {
//			switch (event.sensor.getType()) {
//			case Sensor.TYPE_ACCELEROMETER:
//
//				// save last acc data values
//				lastAcc[0] = Inventory.lowpassFilter(lastAcc[0],
//						event.values[0], a);
//				lastAcc[1] = Inventory.lowpassFilter(lastAcc[1],
//						event.values[1], a);
//				lastAcc[2] = Inventory.lowpassFilter(lastAcc[2],
//						event.values[2], a);
//				break;
//			default:
//			}
//
//		}
//
//		@Override
//		public void onAccuracyChanged(Sensor sensor, int accuracy) {
//			// TODO Auto-generated method stub
//
//		}
//	};
//
//	// magnetic field sensor Event Listener
//	public SensorEventListener myMagneticListener = new SensorEventListener() {
//
//		@Override
//		public void onSensorChanged(SensorEvent event) {
//
//			switch (event.sensor.getType()) {
//			case Sensor.TYPE_MAGNETIC_FIELD:
//
//				magneticValue = Inventory.lowpassFilter(magneticValue,
//						event.values[0], a);
//				break;
//			default:
//			}
//		}
//
//		@Override
//		public void onAccuracyChanged(Sensor sensor, int accuracy) {
//			;
//		}
//	};
//
	public SensorData(Context ctx) {
		this.context = ctx;

	}

	// Hook up accelerometer
//	public void loadAcc() {
//
//		sensorManager = (SensorManager) context
//				.getSystemService(SENSOR_SERVICE);
//		l_sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
//
//		for (int i = 0; i < l_sensors.size(); i++) {
//			// Register only compass and accelerometer
//			if (l_sensors.get(i).getType() == Sensor.TYPE_ACCELEROMETER) {
//				sensorManager.registerListener(myAccEventListener,
//						l_sensors.get(i), SensorManager.SENSOR_DELAY_FASTEST);
//			}
//		}
//
//		// Register timer
//		timer = new Timer("UpdateData", false);
//		TimerTask task = new TimerTask() {
//
//			@Override
//			public void run() {
//
//				Update();
//			}
//		};
//		timer.schedule(task, 0, INTERVAL);
//	}

	// Hook up compass
	public void loadCompass() {

//		sensorManager = (SensorManager) context
//				.getSystemService(Context.SENSOR_SERVICE);
//		l_sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
//
//
//			// Register only compass and accelerometer
//			sensorManager.registerListener(myAccEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
//		sensorManager.registerListener(myMagneticListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
		mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
			if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
				Log.i("Sensor_status","No sernsors available");
			}
			else {
				mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
				mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
				haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
				haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
			}
		}
		else{
			mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
			haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_GAME);
		}
	}

//	// Hook up magnetic sensor
//	public void loadMagnetic() {
//		sensorManager = (SensorManager) context
//				.getSystemService(SENSOR_SERVICE);
//		l_sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
//
//		for (int i = 0; i < l_sensors.size(); i++) {
//			// Register only compass and accelerometer
//			if (l_sensors.get(i).getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//				sensorManager.registerListener(myMagneticListener,
//						l_sensors.get(i), SensorManager.SENSOR_DELAY_FASTEST);
//			}
//		}
//	}
//
//	// unload sensors
//	public void unloadComp() {
//
//		sensorManager.unregisterListener(myCompassEventListener);
//	}
//
//	public void unloadAcc() {
//
//		sensorManager.unregisterListener(myAccEventListener);
//		lastStepDetectedTime = 0;
//		stepCounter = 0;
//	}
//
//	public void unloadMagnetic() {
//
//		sensorManager.unregisterListener(myMagneticListener);
//	}
//
//	protected void Update() {
//
//		long currentime = System.currentTimeMillis();
//		double[] oldAcc = new double[3];
//		System.arraycopy(lastAcc, 0, oldAcc, 0, 3);
//		storeZdata(oldAcc[2]);
//
//		// if current time - last step detected time > timeout range
//		if (((currentime - lastStepDetectedTime) > timeout)
//				&& checkStep(differential)) {
//			lastStepDetectedTime = currentime;
//			stepCounter++;
//			updateUserPosition();
//		}
//	}
//
//	public void updateUserPosition() {
//
//		// user displacement by taking x and y component using the angle
//
//		double x = Inventory.X;
//		double y = Inventory.Y;
//
//		// convert angle to radians
////		double radvaluecomp = Math.PI - (com.example.ToolBox.SensorData.compassValue * Math.PI)
////				/ 180;
////
////		Inventory.X = (float) (x + (Math.sin(radvaluecomp) * stepSize * gridSize));
////		Inventory.Y = (float) (y + (Math.cos(radvaluecomp) * stepSize * gridSize));
//	}
//
//	// stores previous values of acc's Z component
//	private void storeZdata(double value) {
//		five_values[fv_iterator % sizeFV] = value;
//		fv_iterator++;
//		fv_iterator = fv_iterator % sizeFV; // fv_iterator never > sizeFV
//	}
//
//	private boolean checkStep(double peak) {
//
//		int itr = 5;
//
//		double val = five_values[(fv_iterator - 1 + sizeFV) % sizeFV];
//
//		for (int u = 1; u < itr; u++) {
//
//			double val_delta = five_values[(fv_iterator - 1 - u + sizeFV + sizeFV)
//					% sizeFV];
//			if ((val - val_delta) > peak) {
//				return true;
//			}
//		}
//
//		return false;
//	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			SensorManager.getRotationMatrixFromVector(rMat, event.values);
			mCompass = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
		}

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
			mLastAccelerometerSet = true;
		} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
			mLastMagnetometerSet = true;
		}
		if (mLastAccelerometerSet && mLastMagnetometerSet) {
			SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
			SensorManager.getOrientation(rMat, orientation);
			mCompass = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
		}

		mCompass = Math.round(mCompass);
		//compass_img.setRotation(-mCompass);

		String where = "NW";

		if (mCompass >= 350 || mCompass <= 10)
			where = "N";
		if (mCompass < 350 && mCompass > 280)
			where = "NW";
		if (mCompass <= 280 && mCompass > 260)
			where = "W";
		if (mCompass <= 260 && mCompass > 190)
			where = "SW";
		if (mCompass <= 190 && mCompass > 170)
			where = "S";
		if (mCompass <= 170 && mCompass > 100)
			where = "SE";
		if (mCompass <= 100 && mCompass > 80)
			where = "E";
		if (mCompass <= 80 && mCompass > 10)
			where = "NE";


	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
}
