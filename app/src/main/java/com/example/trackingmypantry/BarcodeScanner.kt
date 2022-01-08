package com.example.trackingmypantry

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode

private const val CAMERA_REQUEST_CODE = 101

//Classe che implementa la scansione dei barcode tramite fotocamera
class BarcodeScanner : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scanner)
        //Controllo i permessi
        setupPermission()
        //Setup dello scanner
        codeScanner()
    }
    private fun codeScanner(){
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)

        // Parametri dello scanner)
        codeScanner.camera = CodeScanner.CAMERA_BACK //Uso la camera posteriore
        codeScanner.formats = CodeScanner.ALL_FORMATS // Formato del barcode
        codeScanner.autoFocusMode = AutoFocusMode.SAFE //Autofocus della camera
        codeScanner.scanMode = ScanMode.SINGLE //Dopo il primo scan blocco la preview
        codeScanner.isAutoFocusEnabled = true //Abilito l'autofocus
        codeScanner.isFlashEnabled = false //Disabilito lo scanner

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            //Mando il barcode alla funzione getProducts
            HTTPcalls().getProducts(it.text,this,supportFragmentManager)
        }
        codeScanner.errorCallback = ErrorCallback {
            //Errore di inizializzazione della camera
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        //Avvio lo scanner
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    //Controllo i permessi della camera
    private fun setupPermission() {
        val permission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    //Richiede i permessi della camera
    private fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    //Ritorna i permessi della camera
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CAMERA_REQUEST_CODE ->{
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Camera permission needed",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
