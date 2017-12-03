package com.coffeeanddistractions.kotlincoroutinesexample

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_fibonacci_sequence.*
import kotlinx.android.synthetic.main.content_fibonacchi_sequence.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.run

class FibonacciSequenceActivity : AppCompatActivity() {
    var currentFibonacciNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fibonacci_sequence)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            fab.isEnabled = false
            progressBar.visibility = View.VISIBLE
            currentFibonacciNumber += 1
            Snackbar.make(view, "Calculating fibonacci number $currentFibonacciNumber ", Snackbar.LENGTH_LONG).show()

            // create a new coroutine block that will run on the main thread
            launch(UI) {
                // suspend the coroutine execution until run block returns the next fibonacci value
                val nextFibonacciValue = run(CommonPool) {
                    getFibonacciNumber(currentFibonacciNumber)
                }

                // safely update ui after the nextFibonacciValue is calculated
                val currentSequenceValue = "Fibonacci number $currentFibonacciNumber: value $nextFibonacciValue"
                currentFibonacciValue.text = currentSequenceValue
                progressBar.visibility = View.INVISIBLE
                fab.isEnabled = true
            }
        }
    }

    suspend fun getFibonacciNumber(position: Int): Int = when (position) {
        1 -> 0
        2 -> 1
        else -> getFibonacciNumber(position - 1) + getFibonacciNumber(position - 2)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_fibonacci_sequence, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> true
        else -> super.onOptionsItemSelected(item)
    }
}
