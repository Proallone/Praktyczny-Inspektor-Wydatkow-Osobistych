package com.example.projekt_zespolowy_ezi.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt_zespolowy_ezi.APIRequest
import com.example.projekt_zespolowy_ezi.R
import com.example.projekt_zespolowy_ezi.api.UserExpenseJSONItem
import com.example.projekt_zespolowy_ezi.constants.LoggedUser
import com.example.projekt_zespolowy_ezi.constants.URL
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ExpensesGraphs : AppCompatActivity() {

    // https://weeklycoding.com/mpandroidchart-documentation/setting-data/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses_graphs)

        getExpenses()

    }
    private fun getExpenses() {

        val expenses : ArrayList<BarEntry> = ArrayList()
        val labels : ArrayList<String> = ArrayList()
        val barGraph : BarChart = findViewById(R.id.main_graph)
        val expensesSummary : TextView = findViewById(R.id.summary_value)


        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)


        val response = retrofitBuilder.userExpenses(LoggedUser.userId!!)

        response.enqueue(object : Callback<List<UserExpenseJSONItem>?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<List<UserExpenseJSONItem>?>,
                response: Response<List<UserExpenseJSONItem>?>
            ) {
                val responseBody = response.body()!!
                /*
                Pola poniżej służą do populacji adaptera
                 */
                val expArrayID = Array<String>(responseBody.size){"0"}
                val expArrayVal = Array<String>(responseBody.size){"null"}
                val expArrayCat = Array<String>(responseBody.size){"null"}
                val expArrayDate = Array<String>(responseBody.size){"null"}
                val expArrayDel = Array<Int>(responseBody.size){0}
                var sumExp = 0.0F
                var index = 0
                var expF = ""
                for(e in responseBody){
                    expArrayID[index] = e.id.toString()
                    expF=String.format("%.2f", e.value.toFloat())
                    expArrayVal[index] = expF
                    expArrayCat[index] = e.category
                    sumExp+=expArrayVal[index].toFloat()
                    expenses.add(BarEntry(index.toFloat(),expArrayVal[index].toFloat()))
                    labels.add(expArrayCat[index])
                    index++
                }
                val sumExpS=String.format("%.2f", sumExp)
                expensesSummary.text = sumExpS + "zł"
                val barDataSet = BarDataSet(expenses, "Wydatki")
                barDataSet.valueTextSize = 12f
                //barDataSet.color = R.color.GoblinGreen2
                val data = BarData(barDataSet)
                barDataSet.setColor(getResources().getColor(R.color.PastelleDeepOrange)); //resolved color

                //barGraph.setDescription("Set Bar Chart Description")
                data.setBarWidth(0.9f) // set custom bar width
                barGraph.setData(data)
                barGraph.xAxis.isEnabled = false
                barGraph.axisLeft.isEnabled = false
                barGraph.axisRight.isEnabled = false
                val description: Description = Description()
                description.setText("Wydatki użytkownika " + LoggedUser.userName)
                description.textSize = 15f
                description.textColor = R.color.PastelleDeepBlue
                barGraph.setDescription(description)
                barGraph.setFitBars(true) // make the x-axis fit exactly all bars
                barGraph.invalidate() // refresh

            }

            override fun onFailure(call: Call<List<UserExpenseJSONItem>?>, t: Throwable) {
                Toast.makeText(this@ExpensesGraphs,"Coś poszło nie tak", Toast.LENGTH_LONG).show()
            }
        })

    }
}