package br.com.mycamera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luis on 08/08/14.
 */
public class CameraController implements SurfaceHolder.Callback  {

    private android.hardware.Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean previewing = false;



    public CameraController(Activity atividade, int surfaceView) {
        this.surfaceView = (SurfaceView) atividade.findViewById(surfaceView);
        surfaceHolder = this.surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    public CameraController() {
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = android.hardware.Camera.open();
        try {
            camera.setPreviewDisplay(surfaceHolder);
           // camera.enableShutterSound(false);                                             //tira som da camera
        } catch (IOException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
           e.printStackTrace();
        }
         camera.setDisplayOrientation(90);
        Camera.Parameters parameters = camera.getParameters();
        parameters.set("camera-id", 2);
        parameters.set("rotation",90);
       // parameters.set("orientation", "portrait");
        //parameters.setPreviewSize(300, 300);

        camera.setParameters(parameters);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (previewing) {
            pararVisualizacao();
        }

        if (camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                iniciarVisualizacao();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        pararVisualizacao();
        camera.release();
        camera = null;

    }



    public void tirarFoto(Camera.ShutterCallback shutter, Camera.PictureCallback raw, Camera.PictureCallback jpeg) {
        camera.takePicture(shutter, raw, jpeg);
    }

    public void iniciarVisualizacao() {
        previewing = true;
        camera.startPreview();

    }

    public void pararVisualizacao() {
        camera.stopPreview();
        previewing = false;
    }

    public Camera getCameraControler() {
        return camera;
    }


    public Uri gravarArquivoExterno(byte[] bytes) {
        File mediaFile = null;

        try {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());   // nome do aruqivo
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    Log.e("MyCameraApp", "failed to create directory");

                }
            }


            mediaFile = new File(mediaStorageDir+"/"+timeStamp+"_imagem.jpg");


            FileOutputStream fos = new FileOutputStream(mediaFile);
            fos.write(bytes);
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

            return  Uri.fromFile(mediaFile);
    }


    public Boolean removeArquivo(Uri uri){

        File file = new File(uri.getPath());
        return file.delete();

    }








}
