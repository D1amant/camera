package br.com.mycamera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity implements View.OnClickListener, Camera.ShutterCallback, Camera.PictureCallback {
    ThumbIds t;
    GridView gridView;
    List<Uri> foto ;
    AlertDialog.Builder builder;
    int posicao;
    private CameraController cameraController;
    private boolean emCamera;
    private Button ok;
    private AlertDialog alerta;
    static final int liminte = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);
        foto = new ArrayList<Uri>();
        emCamera = true;
        cameraController = new CameraController(this, R.id.surfaceView);

        ok = (Button) findViewById(R.id.button1);

        ok.setOnClickListener(this);

        builder = new AlertDialog.Builder(this);
        gridView = (GridView) findViewById(R.id.gridView1);

        t = new ThumbIds();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
               if (foto.size() < liminte) {
                if (emCamera) {
                    cameraController.tirarFoto(this, null, this);
                } else {
                    emCamera = true;
                    ok.setText(R.string.foto_bt_fotografar2);
                    cameraController.iniciarVisualizacao();
                }
            }else{

                Toast.makeText(this, "Limite atigindo" ,Toast.LENGTH_LONG).show();
            }
                break;
        }
    }

    @Override
    public void onPictureTaken(byte[] bytes, final Camera camera) {


        Uri uri = cameraController.gravarArquivoExterno(bytes);                                         //cria arquivo e retorna o caminho

        foto.add(uri);                                                                                  // Adiciona caminho na lista
        Log.e("URI" , foto.toString());
        t.setContext(getApplicationContext());
        t.setImagem(foto);

            gridView.setAdapter(t);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(foto.get(i),"image/jpg");

                try {
                    //startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Aplicativo necessário não encontrado.", Toast.LENGTH_SHORT);
                }
            }
        });


        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("onItemSelected", "foi");
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
              //  Log.e("onItemSelected", "foi");
            }
        });


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> itens = new ArrayList<String>();
                itens.add("Remover");

                ArrayAdapter adapter = new ArrayAdapter(getApplication().getApplicationContext(), R.layout.list_view, itens);
                posicao = i;

                builder.setTitle("opções:");
                //define o diálogo como uma lista, passa o adapter.
                builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        switch (arg1) {
                            case 0:
                               if(cameraController.removeArquivo(foto.get(posicao))) {
                                   foto.remove(posicao);
                                   t.setImagem(foto);
                                   gridView.setAdapter(t);
                               }else {
                                   Toast.makeText(getApplicationContext(),"Erro ao Excluír aquivo" ,Toast.LENGTH_LONG).show();
                               }
                                break;

                        }
                        alerta.dismiss();
                    }
                });

                alerta = builder.create();
                alerta.show();

                Log.e("setOnItemLongClickListener", "foi");

                return false;
            }
        });

        //  cameraController.pararVisualizacao();


    }

    @Override
    public void onShutter() {
        ok.setText(R.string.foto_bt_fotografar);
        emCamera = false;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i("MyActivity",
                    "MainActivity.onConfigurationChanged (ORIENTATION_PORTRAIT)");
            // setting orientation portrait


        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("MyActivity",
                    "MainActivity.onConfigurationChanged (ORIENTATION_LANDSCAPE)");

        }


    }
}
