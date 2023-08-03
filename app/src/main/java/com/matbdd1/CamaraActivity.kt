package com.matbdd1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.set
import androidx.core.view.drawToBitmap
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import java.net.URL
import java.net.URLConnection
import java.util.concurrent.Executors


class CamaraActivity : AppCompatActivity() {
    lateinit var imagenATexto: ImagenATexto
    lateinit var imagen: ImageView
    lateinit var imagenMatricula:ImageView
    lateinit var texto:TextView
    lateinit var check:TextView
    lateinit var bitmap:Bitmap
    lateinit var bitmapMatricula:Bitmap
    val h=1200.0//bitmap.height
    val w=1600.0//bitmap.width
    val A=800.0
    val B=250.0
    var bol = true

    //@SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camara)
        println("hola camara")
        if(OpenCVLoader.initDebug()){
            println("true open")
        }

        imagenATexto = ImagenATexto(applicationContext)
        imagen =  findViewById<ImageView>(R.id.imagen_i)
        imagenMatricula = findViewById<ImageView>(R.id.imagen_m)
        texto = findViewById<TextView>(R.id.texto_matricula_cam_tv)
        check = findViewById<TextView>(R.id.check_tv)
        var botonImagen = findViewById<ImageButton>(R.id.abrir_ib)
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        lifecycleScope.launch(Dispatchers.Main) {
            executor.execute {
                val mat = Mat()
                while(bol){
                    try{
                        val url = URL("http://192.168.5.123/cam.jpg")
                        val conn: URLConnection = url.openConnection()
                        bitmap = BitmapFactory.decodeStream(conn.getInputStream())
                        handler.post {
                            Utils.bitmapToMat(bitmap,mat)
                            Imgproc.rectangle(mat,Point(w/2-A/2,h/2-B/2),Point((w/2-A/2)+A,(h/2-B/2)+B),Scalar(255.0,0.0,0.0,200.0),5)
                            //Imgproc.cvtColor(mat,mat,Imgproc.COLOR_BGR2GRAY)
                            //Imgproc.blur(mat, mat, Size(3.0, 3.0))
                            //Imgproc.Canny(mat,mat,50.0,80.0,3,true)
                            //Imgproc.dilate(mat,mat,mat,Point(-1.0,-1.0),1)
                            //Imgproc.findContours(mat, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)
                            //Imgproc.findContours(mat, ArrayList(3), mat, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)
                            Utils.matToBitmap(mat,bitmap)
                            imagen.setImageBitmap(bitmap)
                            }
                    }catch(e:Exception)
                    {
                        e.printStackTrace()
                        handler.post {
                            imagen.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.camera))
                        }

                    }
                    //Thread.sleep(10)
                }

            }
        }
    }

    fun vistaCasa(view: View){
        val vistaCasa = Intent(this, MainActivity::class.java)
        startActivity(vistaCasa)
        finish()
    }


    fun procesarImagenURL(view: View){
        if(imagen.drawable != null){
            //Imgproc.cvtColor(color, gray, Imgproc.COLOR_BGR2GRAY)
            //Imgproc.Canny(gray, wide, 50.0, 150.0, 3, false)
            lifecycleScope.launch {
                //var imagen = findViewById<ImageView>(R.id.imagen_i)
                //imagen.setImageBitmap()
                //val client = HttpClient(CIO)
                //val urlImg = Url("http://192.168.5.123/cam.jpg")
                //val response: HttpResponse = client.get(urlImg)
                //println(response)
                //imageBitMap = BitmapFactory.decodeStream(urlImg.decode(),)
                //val imagenLoader = ImageLoader(applicationContext)
                //val imagenrequest = ImageRequest.Builder(applicationContext)
                //    .data("http://192.168.5.123/cam.jpg")
                //    .build()
                //val resultado:BitmapDrawable = (imagenLoader.execute(imagenrequest) as SuccessResult).drawable
                //texto.text=imagenATexto.procesarImagen( (resultado as BitmapDrawable).bitmap,"spa_old")
                //texto.text=imagenATexto.procesarImagen(resultado,"spa_old")
                //image = cv2.imread('auto001.jpg')
                //gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
                //gray = cv2.blur(gray,(3,3))
                //canny = cv2.Canny(gray,150,200)
                //canny = cv2.dilate(canny,None,iterations=1)
                //val bitmapDrawable: BitmapDrawable = imagen.drawable as BitmapDrawable
                //val mat = Mat()
                //Utils.bitmapToMat(imagen.drawToBitmap(),mat)
                //Imgproc.cvtColor(mat,mat,Imgproc.COLOR_BGR2GRAY)
                //texto.text = imagenATexto.procesarImagen(bitmapDrawable.bitmap, "eng")
                //bol = false
                val mat = Mat()
                Utils.bitmapToMat(bitmap,mat)
                Imgproc.cvtColor(mat,mat,Imgproc.COLOR_BGR2GRAY)
                Imgproc.threshold(mat,mat,100.0,255.0,Imgproc.THRESH_BINARY)
                //bgsegm.createBackgroundSubtractorMOG()
                //val rec = Rect( Point(w/2-A/2,h/2-B/2),Point((w/2-A/2)+A,(h/2-B/2)+B) )
                //val mat2 = Mat(mat1, rec)
                //val cutImage = Mat()
                //src_roi.copyTo(cutImage)
                Utils.matToBitmap(mat,bitmap)
                bitmapMatricula = createBitmap(bitmap,(w/2-A/2).toInt(),(h/2-B/2).toInt(),A.toInt(),B.toInt())
                imagenMatricula.setImageBitmap(bitmapMatricula)
                var textomatricula = imagenATexto.procesarImagen(imagenMatricula.drawToBitmap(), "eng")

                //verificando String de la matricula obtenida
                if(textomatricula.length==7){
                    texto.text = textomatricula

                    val mutListMatriculas = getArrayListFromSharedPreferences("mutListMatriculas")
                    for(e in mutListMatriculas){
                        if(e==texto.text){
                            check.setBackgroundColor(Color.GREEN)
                            check.text = e
                            abrirPorton(view)
                            break
                        }else{
                            check.setBackgroundColor(Color.YELLOW)
                            check.text = "denegado"
                        }
                    }

                }else{
                    texto.text = "incorrecto"
                    check.setBackgroundColor(Color.RED)
                    check.text = "denegado"
                }

            }
        }
    }

    fun onFlash(view: View){
        val executor = Executors.newSingleThreadExecutor()
        lifecycleScope.launch {
        executor.execute {
        //var imagen = findViewById<ImageView>(R.id.imagen_i)
        //imagen.setImageBitmap()
        //val client = HttpClient(CIO)
        //val response: HttpResponse = client.get("http://192.168.5.123/cam.on.flash")
        //println(response)
            try{
                val url = URL("http://192.168.5.123/cam.on.flash")
                val conn: URLConnection = url.openConnection()
                conn.content
                println(conn)
            }catch(e:Exception){
                e.printStackTrace()
            }
        }
        }
    }

    fun offFlash(view: View){
        val executor = Executors.newSingleThreadExecutor()
        lifecycleScope.launch {
        executor.execute {
            //var imagen = findViewById<ImageView>(R.id.imagen_i)
            //imagen.setImageBitmap()
            //val client = HttpClient(CIO)
            //val response: HttpResponse = client.get("http://192.168.5.123/cam.on.flash")
            //println(response)
            try{
                val url = URL("http://192.168.5.123/cam.off.flash")
                val conn: URLConnection = url.openConnection()
                conn.content
                println(conn)
            }catch(e:Exception) {
                e.printStackTrace()
            }
        }
        }
    }

    fun abrirPorton(view:View){
        val botonImagenAP = findViewById<ImageButton>(R.id.abrir_ib)
        botonImagenAP.setClickable(false)

        val executor = Executors.newSingleThreadExecutor()
        lifecycleScope.launch {
            executor.execute {
                try{
                    val url = URL("http://192.168.5.2:5000/a")
                    val conn: URLConnection = url.openConnection()
                    conn.content
                    println(conn)
                }catch(e:Exception) {
                    e.printStackTrace()
                }
                try {
                    Thread.sleep(5000)
                } catch (e:InterruptedException) {
                    e.printStackTrace()
                }
                try{
                    val url = URL("http://192.168.5.2:5000/c")
                    val conn: URLConnection = url.openConnection()
                    conn.content
                    println(conn)
                }catch(e:Exception){
                    e.printStackTrace()
                }
            }
            botonImagenAP.setClickable(true)
        }
    }

    private fun getArrayListFromSharedPreferences(key: String): MutableList<String> {
        // Get an instance of the SharedPreferences
        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        // Retrieve the Set of strings from SharedPreferences
        val mySet = sharedPreferences.getStringSet(key, emptySet())

        // Convert the Set of strings back to an ArrayList
        val myArrayList = arrayListOf<String>()
        myArrayList.addAll(mySet!!)

        return myArrayList
    }

    override fun onDestroy() {
        super.onDestroy()
        imagenATexto.reciclar()
    }

}