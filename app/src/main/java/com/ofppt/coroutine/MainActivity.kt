package com.ofppt.coroutine


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //===============================
        //GlobalScope
        //===============================
        Toast.makeText(this," before coroutine", Toast.LENGTH_LONG).show()

        GlobalScope.launch {

            delay(2000)
            runOnUiThread {
                Toast.makeText(this@MainActivity," inside coroutine", Toast.LENGTH_LONG).show()

            }

        }
        Toast.makeText(this," after coroutine", Toast.LENGTH_LONG).show()

        //===============================
        //coroutineScope
        //===============================
        Toast.makeText(this," before coroutine", Toast.LENGTH_LONG).show()

        GlobalScope.launch {
            coroutineScope { launch {
                delay(2000)
                runOnUiThread {
                    Toast.makeText(this@MainActivity," inside coroutine", Toast.LENGTH_LONG).show()

                }
            }
            }

        }

        Toast.makeText(this," after coroutine", Toast.LENGTH_LONG).show()

        //===============================
        // runBlocking
        //===============================

        Toast.makeText(this," before coroutine", Toast.LENGTH_LONG).show()

        runBlocking (Dispatchers.Default){

            delay(2000)
            runOnUiThread {
                Toast.makeText(this@MainActivity," inside coroutine", Toast.LENGTH_LONG).show()

            }

        }

        Toast.makeText(this," after coroutine", Toast.LENGTH_LONG).show()


        //===============================
        // runBlocking 2
        //===============================
        Toast.makeText(this," before coroutine", Toast.LENGTH_LONG).show()

        runBlocking {

            CoroutineScope(Dispatchers.Default).launch {
                delay(2000)
                runOnUiThread {
                    Toast.makeText(this@MainActivity," inside coroutine", Toast.LENGTH_LONG).show()

                }
            }


        }

        Toast.makeText(this," after coroutine", Toast.LENGTH_LONG).show()


        //===============================
        //Job
        //===============================

        Toast.makeText(this," before coroutine", Toast.LENGTH_LONG).show()

        runBlocking(Dispatchers.Default) {
            val job = launch {

                delay(2000)
                runOnUiThread {
                    Toast.makeText(this@MainActivity," inside coroutine", Toast.LENGTH_LONG).show()

                }

            }
            job.invokeOnCompletion {
                runOnUiThread {
                    Toast.makeText(this@MainActivity," job completed", Toast.LENGTH_LONG).show()
                }
            }
            delay(1000)
            runOnUiThread {
                Toast.makeText(this@MainActivity," job will be canceled", Toast.LENGTH_LONG).show()
            }
            job.cancel()

        }

        Toast.makeText(this," after coroutine", Toast.LENGTH_LONG).show()



        // ==================================
        //  Récupérer la valeur d'un Job
        //  ==================================


        Toast.makeText(this," before coroutine", Toast.LENGTH_LONG).show()

        runBlocking(Dispatchers.Default) {
            launch {

                val value1 = async { getFirstValue() }
                val value2 = async { getSecondValue() }

                val firstValue = value1.await()
                val secondeValue = value2.await()

                runOnUiThread()
                {
                    Toast.makeText(this@MainActivity," first value  " +firstValue +" seconde value "+secondeValue, Toast.LENGTH_LONG).show()

                }

            }
        }

        Toast.makeText(this," after coroutine", Toast.LENGTH_LONG).show()



        // ===============================
        // Exception
        // ===============================
        runBlocking {

            try {

                delay(2000)

            } catch (e : Exception)
            {
                Toast.makeText(this@MainActivity,"test",Toast.LENGTH_LONG).show()
            }

        }


        //=============================
        //SupervisorJob
        //=============================
        runBlocking {
            launch(SupervisorJob()) {
                delay(1000)
            }
        }

        GlobalScope.launch() {

            CoroutineScope(SupervisorJob()).launch {
                delay(1000)
            }

        }

        runBlocking {
            sendPrimes().collect{
                Toast.makeText(this@MainActivity, " number $it", Toast.LENGTH_LONG).show()
            }
        }
    }



}


suspend fun getFirstValue() : Int
{
    return 10
}

suspend fun getSecondValue() : Int
{
    return 34
}

fun sendPrimes(): Flow<Int> = flow {
    val primeList = listOf(2,7,8,9,1)

    primeList.forEach{
        delay(1000)
        emit(it)
    }
}


