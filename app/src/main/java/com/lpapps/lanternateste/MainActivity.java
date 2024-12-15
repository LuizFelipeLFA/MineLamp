package com.lpapps.lanternateste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.lpapps.lanternateste.Songs;

public class MainActivity extends AppCompatActivity {

    //Chamando views.
    private ImageButton alavanca;
    private ImageButton pressurePlate;
    private ImageView lamp;
    private ImageButton botao;


    //Variaveis.
    boolean temFlash = false;
    boolean alavancaOn = false;
    boolean botaoOn = false;
    boolean pressureOn = false;

    //Inicio do Código.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Buscar view pelo ID.
        pressurePlate = findViewById(R.id.pressurePlate);
        alavanca = findViewById(R.id.alavanca);
        lamp = findViewById(R.id.lampada);
        botao = findViewById(R.id.botao);


        //Chamar flash do dispositivo.
        temFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);



        //Metodo para detectar alavanca Ligada ou Desligada.
        alavanca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temFlash){
                    if (alavancaOn){
                        alavancaOn = false;
                        try {
                            desligaFlash();
                            alavanca.setImageResource(R.drawable.al_off);
                        } catch (CameraAccessException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        alavancaOn = true;
                        try {
                            ligaFlash();
                            alavanca.setImageResource(R.drawable.al_on);
                        } catch (CameraAccessException e) {
                            throw new RuntimeException(e);
                        }

                    }
                } else {
                    ToastErr();

                }
            }
        });




        //Metodo para fazer o botao ligar o flash.
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temFlash){
                    startTimer();
                    Songs songs = new Songs(getApplicationContext());
                    songs.Bclick();
                    botao.setImageResource(R.drawable.button_2);
                    } else {
                    ToastErr();

                }
            }
        });

        pressurePlate.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, final MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            ligaFlash();
                            Songs songs = new Songs(getApplicationContext());
                            songs.Bclick();
                            pressurePlate.setImageResource(R.drawable.pressure_2);
                        } catch (CameraAccessException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (alavancaOn) {
                            Songs songs = new Songs(getApplicationContext());
                            songs.Bunclick();
                            pressurePlate.setImageResource(R.drawable.pressure_1);
                        } else {
                            try {
                                desligaFlash();
                                Songs songs = new Songs(getApplicationContext());
                                songs.Bunclick();
                                pressurePlate.setImageResource(R.drawable.pressure_1);
                            } catch (CameraAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                }

                return false;
            }
        });



    }



    //Sistema que liga e desliga o Flash.

    public void ligaFlash() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = cameraManager.getCameraIdList()[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cameraManager.setTorchMode(cameraId, true);
            lamp.setImageResource(R.drawable.lamp_on);
        }
    }

    public void desligaFlash() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = cameraManager.getCameraIdList()[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cameraManager.setTorchMode(cameraId, false);
            lamp.setImageResource(R.drawable.lamp_off);
        }
    }





    //Mensagem de Erro caso o Dispositivo não tenha flash.
    private void ToastErr() {
        Toast.makeText(MainActivity.this, "Seu Aparelho não tem Flash!", Toast.LENGTH_SHORT).show();

    }




    //Codigo pro butao funcionar.
    CountDownTimer btTimer = null;

    private void startTimer() {
        btTimer = new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
                try {
                    ligaFlash();
                } catch (CameraAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            public void onFinish() {
                if (alavancaOn) {
                    Songs songs = new Songs(getApplicationContext());
                    songs.Bunclick();
                    botao.setImageResource(R.drawable.button_1);
                } else {
                    try {
                        desligaFlash();
                        Songs songs = new Songs(getApplicationContext());
                        songs.Bunclick();
                        botao.setImageResource(R.drawable.button_1);
                    } catch (CameraAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        btTimer.start();
    }


}