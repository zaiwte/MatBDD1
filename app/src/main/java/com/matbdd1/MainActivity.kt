package com.matbdd1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    lateinit var texto:String
    var matricula_List_db = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val app = applicationContext as ModuloRoom


        val mutListMatriculas = getArrayListFromSharedPreferences("mutListMatriculas")

        val listViewMatricula = listaDeMatriculasView(this,mutListMatriculas)


        listViewMatricula?.setOnItemClickListener { _, _, position, _ ->
            val itemValue = listViewMatricula.getItemAtPosition(position) as String
            var texMatricula = findViewById<EditText>(R.id.entrada_busqueda_et)
            texMatricula.setText("$itemValue")
        }
    }

    fun vistaCamara(view: View) {
        val vistaCamara = Intent(this, CamaraActivity::class.java)
        startActivity(vistaCamara)
        finish()
    }

    fun agregarMatricula(view: View){
        //obtener texto string del EditText
        var texMatricula = findViewById<EditText>(R.id.entrada_busqueda_et)
        texto = texMatricula.text.toString()
        texMatricula.text.clear()

        //agregar en la base de datos
        //obtengo
        matricula_List_db = getArrayListFromSharedPreferences("mutListMatriculas")
        //agrego
        matricula_List_db.add(texto)
        //salvo
        saveArrayListToSharedPreferences("mutListMatriculas",matricula_List_db)
        //actualizar en el ListView
        println(matricula_List_db)
        listaDeMatriculasView(this,matricula_List_db)
    }

    fun eliminarMatricula(view: View){
        //obtener texto string del EditText
        var texMatricula = findViewById<EditText>(R.id.entrada_busqueda_et)
        texto = texMatricula.text.toString()
        texMatricula.text.clear()

        //eliminar de la base de datos
        //obtengo
        matricula_List_db = getArrayListFromSharedPreferences("mutListMatriculas")
        //elimino
        matricula_List_db.remove(texto)
        //salvo
        saveArrayListToSharedPreferences("mutListMatriculas",matricula_List_db)
        //actualizar en el ListView
        println(matricula_List_db)
        listaDeMatriculasView(this,matricula_List_db)
    }

    fun listaDeMatriculasView(context: Context, lista:MutableList<String>): ListView? {
        var arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(context, R.layout.item_lista, lista)
        var listViewMatricula = findViewById<ListView>(R.id.id_lista_matriculas_lv)
        listViewMatricula.adapter = arrayAdapter

        return listViewMatricula
    }

    private fun saveArrayListToSharedPreferences(key: String, arrayList: MutableList<String>) {
        // Convert the ArrayList to a Set of strings
        val mySet = arrayList.toSet()

        // Get an instance of the SharedPreferences
        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        // Get an instance of the SharedPreferences.Editor
        val editor = sharedPreferences.edit()

        // Put the Set of strings into the SharedPreferences.Editor
        editor.putStringSet(key, mySet)

        // Apply the changes to the SharedPreferences
        editor.apply()
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

}