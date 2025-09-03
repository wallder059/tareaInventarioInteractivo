package com.example.tarea2;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Declaramos las variables
    private ImageView imageViewGorra, imageViewChaqueta, imageViewPantalon;
    private Button btnGorra, btnChaqueta, btnPantalon;
    private ProgressBar progressBarGorra, progressBarChaqueta, progressBarPantalon;
    private Handler handler;
    // Botón para limpiar: al presionarlo oculta todas las prendas visibles
    private Button btnLimpiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);            // llama al constructor de la clase padre
        EdgeToEdge.enable(this);              // habilita pantalla completa
        setContentView(R.layout.activity_main);         // conecta el layout XML con esta clase
        
        // creamos el handler para usarlo en la UI o no se como se llama
        handler = new Handler();
        
        // inizializa todas las vistas
        initializeViews();
        
        // funciona para configurar los botones
        setupButtonListeners();
        
 
 
    }
 
 
    private void initializeViews() {
        // Conectamos cada ImageView  con la id del layout
        imageViewGorra = findViewById(R.id.imageViewGorra);
        imageViewChaqueta = findViewById(R.id.imageViewChaqueta);
        imageViewPantalon = findViewById(R.id.imageViewPantalon);
        
        // se busca el boton por id
        btnGorra = findViewById(R.id.btnGorra);
        btnChaqueta = findViewById(R.id.btnChaqueta);
        btnPantalon = findViewById(R.id.btnPantalon);
        // Intentamos obtener el botón Limpiar (puede no existir en todos los layouts)
        btnLimpiar = findViewById(R.id.btnLimpiar); // puede ser null en portrait si no existe
        
 
        progressBarGorra = findViewById(R.id.progressBarGorra);
        progressBarChaqueta = findViewById(R.id.progressBarChaqueta);
        progressBarPantalon = findViewById(R.id.progressBarPantalon);
        
        // la configuracion la ise aca para que esto sea el maximo
        progressBarGorra.setMax(100);
        progressBarChaqueta.setMax(100);
        progressBarPantalon.setMax(100);
    }

    private void setupButtonListeners() {
        // Configuramos cada botón para que llame a la función de descarga con sus parámetros específicos
        btnGorra.setOnClickListener(v -> startDownload(progressBarGorra, btnGorra, imageViewGorra, "Gorra"));           // Botón gorra
        btnChaqueta.setOnClickListener(v -> startDownload(progressBarChaqueta, btnChaqueta, imageViewChaqueta, "Chaqueta")); // Botón chaqueta
        btnPantalon.setOnClickListener(v -> startDownload(progressBarPantalon, btnPantalon, imageViewPantalon, "Pantalón")); // Botón pantalón

        // Botón Limpiar: si existe en el layout actual, al hacer click oculta todas las prendas
        // Nota: usamos View.INVISIBLE para que mantengan su espacio en el contenedor (quedan ocultas)
        if (btnLimpiar != null) { // evitamos NullPointerException en layouts donde no esté
            btnLimpiar.setOnClickListener(v -> {
                // Ocultamos gorra, chaqueta y pantalón
                imageViewGorra.setVisibility(View.INVISIBLE);
                imageViewChaqueta.setVisibility(View.INVISIBLE);
                imageViewPantalon.setVisibility(View.INVISIBLE);
            });
        }
    }

    // Función principal que maneja la descarga de cualquier item
    // Recibe parámetros: progressBar (barra de progreso), button (botón), imageView (imagen), itemName (nombre del item)
    private void startDownload(ProgressBar progressBar, Button button, ImageView imageView, String itemName) {
        // Deshabilitamos el botón para que no se pueda presionar durante la descarga
        button.setEnabled(false);
        
        // Creamos un array con un solo elemento para poder modificarlo desde el Runnable
        final int[] progress = {0};        // es para que inicie en 0
        
         // creacion de un runable cada 100ms
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                progress[0] += 5;                                  // se aumenta cada 5
                progressBar.setProgress(progress[0]);               // actualiza
                
                if (progress[0] < 100) {                          // para verificar si esta en 100
                    handler.postDelayed(this, 100);                // se ejecuta de nuevo en 100
                } else {                                           // llegamos al 100
                    onDownloadComplete(button, imageView, itemName); // llamamos a la funcion complet
                }
            }
        };
        
        // se ejecuta el runable para iniciar la descarga
        handler.post(runnable);
    }

    // esta funcion empieza cuando termina la descarga
    // eecibe parámetros
    private void onDownloadComplete(Button button, ImageView imageView, String itemName) {
        // habilitamos el botón nuevamente para que se pueda presionar
        button.setEnabled(true);
        
        // para hacer visible la imagen
        imageView.setVisibility(View.VISIBLE);
        
        // se cambia el texto para saber si esta completo
        button.setText(itemName + " ✓");                           // un check
    }
}