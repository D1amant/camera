package br.com.mycamera;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by luis on 11/08/14.
 */
public class ThumbIds extends BaseAdapter {

    private Context context;
    private List<Uri> imagem;



    @Override
    public int getCount() {
        return  imagem.size();
    }

    @Override
    public Object getItem(int i) {
        return imagem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {




        ImageView imageView = new ImageView(this.context);
        Bitmap dstBmp = null;
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
        imageView.setBackground(context.getResources().getDrawable(R.drawable.botao_foto));

        dstBmp =  getBitmatp(this.imagem.get(i)) ;
        imageView.setImageBitmap(dstBmp);


            Animation animationGirar = animation();

        imageView.startAnimation(animationGirar);

        return imageView;
    }


    public ThumbIds(Context context, List<Uri> imagem) {
        this.context = context;
        this.imagem = imagem;
    }
    public ThumbIds() {

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Uri> getImagem() {
        return imagem;
    }

    public void setImagem(List<Uri> imagem) {
        this.imagem = imagem;
    }


public Bitmap getBitmatp(Uri uri){
    Bitmap bitmap = null;
    Bitmap dstBmp = null;
    try {
        bitmap = BitmapFactory.decodeStream(this.context.getContentResolver().openInputStream(uri));


        if (bitmap.getWidth() >= bitmap.getHeight()){

            dstBmp = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth()/2 - bitmap.getHeight()/2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );

        }else{

            dstBmp = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight()/2 - bitmap.getWidth()/2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }

    } catch (FileNotFoundException e) {
        e.printStackTrace();
        Log.e("Arquivo" ,  e.toString());
    }

    return dstBmp;
}


    public Animation animation(){
        Animation animationGirar = new AlphaAnimation(0.0f, 1.0f);;

        animationGirar.setDuration(3000);
        animationGirar.setFillAfter(true);

        animationGirar.setRepeatCount(0);
        animationGirar.setRepeatMode(0);


        return animationGirar;

    }



}
