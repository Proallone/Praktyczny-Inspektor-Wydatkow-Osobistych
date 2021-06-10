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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
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
        //getExpensesForPieChart("111")

    }

    private fun getExpenses() {

        val expenses : ArrayList<BarEntry> = ArrayList()
        val labels : ArrayList<String> = ArrayList()
        val barGraph : BarChart = findViewById(R.id.main_graph)
        val pieChart :PieChart = findViewById(R.id.pie_chart)
        val expensesSummary : TextView = findViewById(R.id.summary_value)


        val expensesPie = ArrayList<PieEntry>()
        pieChart.setUsePercentValues(true)

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

                    expensesPie.add(PieEntry(e.value.toFloat() ,e.category))

                    index++
                }

                val sumExpS=String.format("%.2f", sumExp)
                expensesSummary.text = sumExpS + "zł"

                val map: Map<String, String> =
                    expArrayCat.zip(expArrayVal)
                        .toMap()

                val test = map.toList().distinct()

                val catIndex : Int = map.size
                val uniqueCatsSummary = Array<Float>(map.size){0f}

//                for(c in responseBody){
//                    for(i in map) {
//                        expF = String.format("%.2f", c.value.toFloat())
//                        expArrayVal[index] = expF
//                        //expArrayCat[index] = map
//                        sumExp += expArrayVal[index].toFloat()
//
//                        expensesPie.add(PieEntry(sumExp, c.category))
//                        index++
//                    }
//                }

//                Log.d("LIST", test.toString())
//                Log.d("MAP",map.toString())
//                Log.d("keys",map.keys.toString())
//                Log.d("size",map.size.toString())



                /*
                BAR GRAPH
                * */
                val barDataSet = BarDataSet(expenses, "Wydatki")
                barDataSet.valueTextSize = 12f

                barDataSet.setColors(getResources().getColor(R.color.PastelleDeepOrange),
                    getResources().getColor(R.color.PastelleDeepBlue),
                    getResources().getColor(R.color.PastelleLightBlue),
                    getResources().getColor(R.color.GoblinGreen2),
                    getResources().getColor(R.color.GoblinGreen),
                    getResources().getColor(R.color.DarkSeaGreen),
                    getResources().getColor(R.color.AntiqueBrown),
                    getResources().getColor(R.color.GoblinBrown),
                    getResources().getColor(R.color.PastelleLightOrange))

                val data = BarData(barDataSet)
                //barDataSet.setColor(getResources().getColor(R.color.PastelleDeepOrange)); //resolved color
                data.setBarWidth(0.9f) // set custom bar width
                barGraph.setData(data)
                barGraph.xAxis.isEnabled = false
                barGraph.axisLeft.isEnabled = false
                barGraph.axisRight.isEnabled = false
                barGraph.setScaleEnabled(false)
                val description: Description = Description()
                description.setText("Wydatki użytkownika " + LoggedUser.userName)
                description.textSize = 11f
                description.textColor = R.color.PastelleDeepBlue
                barGraph.description = description

                barGraph.setFitBars(true) // make the x-axis fit exactly all bars
                barGraph.invalidate() // refresh



                /*
                PIE CHART
                * */
                val dataSet = PieDataSet(expensesPie, "")
                val dataPie = PieData(dataSet)
                dataPie.setValueTextSize(15f)
                dataPie.setValueFormatter(PercentFormatter())
                dataPie.setValueTextColor(getColor(R.color.PastelleWhite))

                dataSet.setColors(getResources().getColor(R.color.PastelleDeepOrange),
                    getResources().getColor(R.color.PastelleDeepBlue),
                    getResources().getColor(R.color.PastelleLightBlue),
                    getResources().getColor(R.color.GoblinGreen2),
                    getResources().getColor(R.color.GoblinGreen),
                    getResources().getColor(R.color.DarkSeaGreen),
                    getResources().getColor(R.color.AntiqueBrown),
                    getResources().getColor(R.color.GoblinBrown),
                    getResources().getColor(R.color.PastelleLightOrange))

                pieChart.data = dataPie
                pieChart.isDrawHoleEnabled = false
                pieChart.setTouchEnabled(false)
                pieChart.description.text = ""
                pieChart.setDrawEntryLabels(false)
                pieChart.setUsePercentValues(true)
                pieChart.invalidate() // refresh


            }

            override fun onFailure(call: Call<List<UserExpenseJSONItem>?>, t: Throwable) {
                Toast.makeText(this@ExpensesGraphs,"Coś poszło nie tak", Toast.LENGTH_LONG).show()
            }
        })

    }
    private fun getExpensesForPieChart(cat : String){


//        val retrofitBuilderCat = Retrofit.Builder()
//            .baseUrl(URL.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(APIRequest::class.java)
//
//        val responseCat = retrofitBuilderCat.userCategories(LoggedUser.userId!!)
//
//        responseCat.enqueue(object : Callback<List<UserCategoryJSONItem>?> {
//            @SuppressLint("SetTextI18n")
//            override fun onResponse(
//                call: Call<List<UserCategoryJSONItem>?>,
//                response: Response<List<UserCategoryJSONItem>?>
//            ) {
//                val responseBody = response.body()!!
//                val catArrayID = Array<String>(responseBody.size){"0"}
//                val catArrayCat = Array<String>(responseBody.size){"null"}
//                var index = 0
//
//                for(e in responseBody){
//                    catArrayID[index] = e.id.toString()
//                    catArrayCat[index] = e.category
//                    index++
//                }
//            }
//            override fun onFailure(call: Call<List<UserCategoryJSONItem>?>, t: Throwable) {
//                Toast.makeText(this@ExpensesGraphs,"Coś poszło nie tak",Toast.LENGTH_LONG).show()
//            }
//        })






        val pieChart :PieChart = findViewById(R.id.pie_chart)
        val expensesPie = ArrayList<PieEntry>()
        pieChart.setUsePercentValues(true)

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)

        val expensesSummary = findViewById<TextView>(R.id.summary_value)
        val response = retrofitBuilder.userAllExpenses(LoggedUser.userId!!, cat)

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
                    if (e.category == cat){
                        expArrayID[index] = e.id.toString()
                        expF=String.format("%.2f", e.value.toFloat())
                        expArrayVal[index] = expF
                        expArrayCat[index] = e.category
                        expArrayDate[index] = e.date
                        expArrayDel[index] = e.deleted
                        sumExp+=expArrayVal[index].toFloat()
                        expensesPie.add(PieEntry(sumExp ,cat))
                    }
                    index++
                }
                val sumExpS=String.format("%.2f", sumExp)
                expensesSummary.text = sumExpS + "zł"



                /*
                PIE CHART
                * */
                val dataSet = PieDataSet(expensesPie, "Wydatki")
                val dataPie = PieData(dataSet)
                // In Percentage
                dataPie.setValueFormatter(PercentFormatter())
                dataSet.setColors(getResources().getColor(R.color.PastelleDeepOrange),
                    getResources().getColor(R.color.PastelleDeepBlue),
                    getResources().getColor(R.color.PastelleLightBlue),
                    getResources().getColor(R.color.PastelleLightOrange))

                pieChart.data = dataPie
                pieChart.isDrawHoleEnabled = false
                //pieChart.setTouchEnabled(false)
                pieChart.invalidate() // refresh

            }

            override fun onFailure(call: Call<List<UserExpenseJSONItem>?>, t: Throwable) {
                Toast.makeText(this@ExpensesGraphs,"Coś poszło nie tak",Toast.LENGTH_LONG).show()
            }
        })
    }
}