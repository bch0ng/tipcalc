package edu.us.ischool.bchong.tipcalc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var tipInput: EditText
    private lateinit var tipSpinner: Spinner
    private lateinit var tipButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tipInput = findViewById(R.id.tipInput)
        tipSpinner = findViewById(R.id.tipSpinner)
        tipButton = findViewById(R.id.tipButton)

        val tipValues = ArrayList<String>()
            tipValues.add("10%")
            tipValues.add("15%")
            tipValues.add("18%")
            tipValues.add("20%")
        val tipDecimals = ArrayList<BigDecimal>()
            tipDecimals.add(BigDecimal(0.10))
            tipDecimals.add(BigDecimal(0.15))
            tipDecimals.add(BigDecimal(0.18))
            tipDecimals.add(BigDecimal(0.20))

        val dataAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipValues)
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tipSpinner.adapter = dataAdapter

        var tipPercent: BigDecimal = BigDecimal.valueOf(0.1)
        tipSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long) {
                tipPercent = tipDecimals[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        tipInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {
                tipInput.removeTextChangedListener(this)
                /*
                    Limits amount to two decimal places
                 */
                if (tipInput.text.toString().contains(".")) {
                    if ((tipInput.text.length - 1) - tipInput.text.toString().indexOf(".") > 2) {
                        tipInput.setText(tipInput.text.toString().substring(0, tipInput.text.length - 1))
                    }
                }
                /*
                    Makes sure dollar sign ($) is always at the front of
                    the amount.
                 */
                if (tipInput.text.toString().contains("$")) {
                    if (tipInput.text.toString() ==  "$") {
                        tipInput.setText("")
                    } else {
                        tipInput.setText("$" + tipInput.text.toString().replace("$", ""))
                    }
                } else {
                    tipInput.setText("$" + tipInput.text)
                }
                tipInput.setSelection(tipInput.text.length)     // Places cursor at the end
                tipButton.isEnabled = tipInput.text.isNotEmpty()
                tipInput.addTextChangedListener(this)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        tipButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val amount = tipInput.text.substring(1, tipInput.text.length).toBigDecimal()
                Toast.makeText(this@MainActivity,
                    moneyFormat(calcTip(amount, tipPercent)).toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Returns given amount in USD currency format.
     *
     * @precondition    given @param amount must be numbers only
     *
     * @param amount    monetary value
     * @return          USD currency format
     */
    fun moneyFormat(amount: Any): String {
        val format = NumberFormat.getCurrencyInstance(Locale.US)
        return format.format(amount.toString().toBigDecimal())
    }

    /**
     * Calculates and returns tip when given the cost of the bill (in dollars)
     * and the tip percentage.
     *
     * @param amount    total cost before tipping
     * @param tip       tip percentage
     * @return          tip in dollars
     */
    fun calcTip(amount: BigDecimal, tip: BigDecimal): BigDecimal {
        return (amount * tip)
    }
}
