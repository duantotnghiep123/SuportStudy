package com.example.suportstudy

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.suportstudy.authencation.RegisterActivity
import com.example.suportstudy.home.HomeActivity
import com.example.suportstudy.model.Question
import com.example.suportstudy.retrofit.APIService
import com.example.suportstudy.until.Until
import kotlinx.android.synthetic.main.activity_quizz.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class QuizzActivity : AppCompatActivity() {
    val data = MutableLiveData<List<Question>>()
    var score=0;
    var listIndexQuestion = ArrayList<Int?>()

    val context=this@QuizzActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz)

        var retrofit = APIService.getClient();
        val quizzApi = retrofit?.create(APIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val responce = quizzApi!!.getAllQuestion()
            if (responce.isSuccessful) {
                data.postValue(responce.body())
            }
        }
        data.observe(this, {
            for (i in it.indices) {
                listIndexQuestion.add(i)
            }
            Collections.shuffle(listIndexQuestion)

            var cau1: Int? = listIndexQuestion.get(0)
            var cau2: Int? = listIndexQuestion.get(1)
            var cau3: Int? = listIndexQuestion.get(2)
            var cau4: Int? = listIndexQuestion.get(3)
            var cau5: Int? = listIndexQuestion.get(4)


            txtQ1.text = it[cau1!!].title
            txtQ2.text = it[cau2!!].title
            txtQ3.text = it[cau3!!].title
            txtQ4.text = it[cau4!!].title
            txtQ5.text = it[cau5!!].title

            radio1_op1.text = it.get(cau1!!).option1
            radio1_op2.text = it.get(cau1!!).option2
            radio1_op3.text = it.get(cau1!!).option3
            radio1_op4.text = it.get(cau1!!).option4


            radio2_op1.text = it.get(cau2!!).option1
            radio2_op2.text = it.get(cau2!!).option2
            radio2_op3.text = it.get(cau2!!).option3
            radio2_op4.text = it.get(cau2!!).option4


            radio3_op1.text = it.get(cau3!!).option1
            radio3_op2.text = it.get(cau3!!).option2
            radio3_op3.text = it.get(cau3!!).option3
            radio3_op4.text = it.get(cau3!!).option4

            radio4_op1.text = it.get(cau4!!).option1
            radio4_op2.text = it.get(cau4!!).option2
            radio4_op3.text = it.get(cau4!!).option3
            radio4_op4.text = it.get(cau4!!).option4

            radio5_op1.text = it.get(cau5!!).option1
            radio5_op2.text = it.get(cau5!!).option2
            radio5_op3.text = it.get(cau5!!).option3
            radio5_op4.text = it.get(cau5!!).option4

            var list = it


            btnSubmit.setOnClickListener {
                val indexq1: Int =
                    question1.indexOfChild(findViewById(question1.getCheckedRadioButtonId())) + 1

                val indexq2: Int =
                    question2.indexOfChild(findViewById(question2.getCheckedRadioButtonId())) + 1

                val indexq3: Int =
                    question3.indexOfChild(findViewById(question3.getCheckedRadioButtonId())) + 1

                val indexq4: Int =
                    question4.indexOfChild(findViewById(question4.getCheckedRadioButtonId())) + 1

                val indexq5: Int =
                    question5.indexOfChild(findViewById(question5.getCheckedRadioButtonId())) + 1

                if (indexq1 == list.get(cau1!!).trueOption) {
                    score++
                }

                if (indexq2 == list.get(cau2!!).trueOption) {
                    score++
                }

                if (indexq3 == list.get(cau3!!).trueOption) {
                    score++
                }
                if (indexq4 == list.get(cau4!!).trueOption) {
                    score++
                }
                if (indexq5 == list.get(cau5!!).trueOption) {
                    score++
                }


                val dialog = Dialog(context)
                if(score>3){
                    dialog.setContentView(R.layout.dialog_quizz_win)
                }else{
                    dialog.setContentView(R.layout.dialog_quizz_lose)
                }
                dialog!!.window!!.attributes.windowAnimations = R.style.DialogTheme
                val window = dialog!!.window
                window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                if (dialog != null && dialog.window != null) {
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
                dialog!!.setCancelable(false)
                val btnOK = dialog!!.findViewById<Button>(R.id.btnOK)
                val txtScore = dialog!!.findViewById<TextView>(R.id.txtScore)
                txtScore.setText("Điểm của bạn là ${score}")
                btnOK.setOnClickListener {
                    dialog.dismiss()
                    if(score>3){
                        var isTutor=true
                        var intent= Intent(context,RegisterActivity::class.java)
                        intent.putExtra("isTutor",isTutor)
                        startActivity(intent)
                        finish()
                    }else{
                        Until.nextActivity(context,MainActivity::class.java)
                        finish()
                    }
                    score=0
                }
                dialog.show()


                question1.clearCheck()
                question2.clearCheck()
                question3.clearCheck()
                question4.clearCheck()
                question5.clearCheck()
            }


        })



    }


}