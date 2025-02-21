package com.example.clicker

import android.animation.ValueAnimator
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.clicker.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var mediaPlayer:MediaPlayer? = null
    var countInt:Int = 0
    val textScore:String ="You score: "
    lateinit var binding: ActivityMainBinding
    // Константа для имени SharedPreferences
    private val PREFS_NAME = "MyPrefs"
    // Ключ для сохраняемого значения
    private val KEY_MY_INT = "my_int"

    private var priceSecondButton:Int = 50
    private val priceThirdButton:Int = 500

    private var canClickSecondButton:Boolean = false
    private var canClickThirdButton:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // create mediaplayer
        mediaPlayer = MediaPlayer.create(this, R.raw.music1)
        countInt = loadSavedInt()
        canClickThirdButton = loadSavedBoolean()
        binding = ActivityMainBinding.inflate(layoutInflater)
        priceAlpha()
        binding.textView.setText("$textScore  $countInt")
        setContentView(binding.root)
    }

    fun alertDialog(text:String):AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage("ФункцияСообщить(Кому и зачем?)")
            .setTitle(text)
            .setPositiveButton("Da, i hochu") { dialog, which ->
               canClickThirdButton = true
                priceAlpha()
            }
            .setNegativeButton("No, im poor student from OmSTU") { dialog, which ->
                canClickThirdButton = false
                Toast.makeText(this,"Nu i idi otsuda",Toast.LENGTH_SHORT).show()
            }

        val dialog: AlertDialog = builder.create()
        return dialog
        //dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveInt(countInt)
        saveBoolean(canClickThirdButton)
    }
    override fun onPause() {
        super.onPause()
        saveInt(countInt) // Сохраняем данные перед паузой
        saveBoolean(canClickThirdButton)
    }

    override fun onStop() {
        super.onStop()
        saveInt(countInt) // Сохраняем данные перед остановкой
        saveBoolean(canClickThirdButton)
    }

    fun priceAlpha()
    {
        if(priceSecondButton > countInt )
        {
            binding.imageButton2.alpha = 0.1f
            canClickSecondButton = false
        }
        else{
            canClickSecondButton = true
            binding.imageButton2.alpha = 1f
        }

        if (!canClickThirdButton) {
            binding.imageButton3.alpha = 0.1f
        }
        else {
            binding.imageButton3.alpha = 1f
        }
    }

    fun click1(view:View){
        music(R.raw.music1)
        onDisplay(1)
        priceAlpha()

    }
    fun click2(view:View){

        if(canClickSecondButton)
        {
            music(R.raw.music2)
            onDisplay(50)
        }
        else{
            Toast.makeText(this,"набей ещё ${50-countInt} очков", Toast.LENGTH_SHORT).show()

        }

    }
    fun click3(view:View){
        if(canClickThirdButton){
            music(R.raw.music3)
            onDisplay(100)
        }
        else{

            alertDialog("Do you want to buy Наш Слоняра button?").show()

        }



    }
    fun resetButton(view:View){
        resetInt()
        onDisplay(countInt * (-1))
        canClickThirdButton = false
        priceAlpha()

    }
    fun onDisplay(plusCount:Int){
        priceAlpha()
        countInt = countInt + plusCount
        animation(500)
        binding.textView.setText("$textScore  $countInt")
    }

    fun music(resourceId:Int){
    // Если медиаплеер уже существует и играет, останавливаем его
    if (mediaPlayer?.isPlaying == true) {
        mediaPlayer?.stop()
    }

    // Освобождаем ресурсы предыдущего MediaPlayer
    mediaPlayer?.release()

    // Создаем новый экземпляр MediaPlayer для нового трека
    mediaPlayer = MediaPlayer.create(this, resourceId)

    // Начинаем воспроизведение
    mediaPlayer?.start()

    }
    private fun animation(timeAnimation:Long){
        val colorAnimation = ValueAnimator.ofArgb(
            resources.getColor(android.R.color.holo_green_light, null),
            resources.getColor(android.R.color.black, null)
        )
        colorAnimation.duration = timeAnimation // Длительность анимации
        colorAnimation.addUpdateListener { animator ->
            binding.textView.setTextColor(animator.animatedValue as Int)
        }
        colorAnimation.start()
    }

    // Метод для сохранения значения
    private fun saveInt(value: Int) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_MY_INT, value)
        editor.apply() // Сохраняем изменения
    }
    // Метод для загрузки значения
    private fun loadSavedInt(): Int {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_MY_INT, 0) // Возвращаем 0, если значение не найдено
    }
    // Метод для обнуления значения
    private fun resetInt() {
        countInt = 0
        onDisplay(0)
        saveInt(0) // Просто сохраняем 0
    }

    // Константа для имени SharedPreferences
    private val PREFS_BOOL = "MyPrefs"
    // Ключ для сохраняемого значения
    val KEY_MY_BOOLEAN = "My bolean"
    // Метод для сохранения значения boolean
    private fun saveBoolean(value: Boolean) {
        val sharedPreferences = getSharedPreferences(PREFS_BOOL, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_MY_BOOLEAN, value)
        editor.apply() // Сохраняем изменения
    }
    // Метод для загрузки значения
    private fun loadSavedBoolean(): Boolean {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_MY_BOOLEAN, false) // Возвращаем false,
    // если значение не найдено
    }







}