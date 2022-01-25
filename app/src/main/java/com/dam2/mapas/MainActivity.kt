package com.dam2.mapas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray

import org.json.JSONObject
import org.json.JSONTokener

// https://leafy-winter-331508-default-rtdb.europe-west1.firebasedatabase.app


class MainActivity : AppCompatActivity() {

    private val TAG = "RealTime"
    private lateinit var database: DatabaseReference
    lateinit var miTexto: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // [START initialize_database_ref]
        // si la base de datos esta en una region diferente a uscentral1
        database = Firebase.database("https://leafy-winter-331508-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Profesionales")



        // [END initialize_database_ref]

        // boton para escribir
        val miBoton: Button = findViewById(R.id.miBoton)
        miBoton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                writeNewData("OtroMas",7.04,1.07)
            }

        })

        // texto para recoger
        miTexto = findViewById(R.id.miTexto)

        // leer los datos cuando cambien
        // defino listener
        val datoListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get dato object and use the values to update the UI
                val dato = dataSnapshot.getValue()
                Log.d(TAG,"cambio: " + dato.toString())
                // convierto json a object
                val gson = Gson()
                val stringJson = gson.toJson(dato)
                val sType = object : TypeToken<List<Profesionales>>() { }.type
                val losPros: List<Profesionales> = gson.fromJson<List<Profesionales>>(stringJson, sType)
                miTexto.text = dato.toString()

                //val jsonArray = JSONTokener(dato).nextValue() as JSONArray

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting dato failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        // añado listener a la ref de la bd
        database.addValueEventListener(datoListener)
    }

    fun writeNewData(nombre: String, lt: Double, lg: Double) {

        Log.d(TAG, "Escribiendo datos")
        val pro = Profesionales(nombre, lt, lg)
        // defino el push
        val refPush = database.push()
        // obtengo la clave
        val miKey: String? = refPush.key
        // muestro la clave
        Log.d(TAG, "mi Key:" + miKey)
        // Añado datos con esa clave en la BD
        refPush.setValue(pro)

    }

}