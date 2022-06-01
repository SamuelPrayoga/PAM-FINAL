package com.project.delcanteen.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.project.delcanteen.R
import com.project.delcanteen.adapter.AdapterBank
import com.project.delcanteen.app.ApiConfig
import com.project.delcanteen.helper.Helper
import com.project.delcanteen.model.Bank
import com.project.delcanteen.model.Chekout
import com.project.delcanteen.model.ResponModel
import com.project.delcanteen.model.rajaongkir.Transaksi
import kotlinx.android.synthetic.main.activity_pembayaran.*
import kotlinx.android.synthetic.main.toolbar_custom.*

class PembayaranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)
        Helper().setToolbar(this, toolbar, "Pembayaran")

        displayBank()
    }

    fun displayBank() {
        val arrBank = ArrayList<Bank>()
        arrBank.add(Bank("BCA", "0123456789", "Noel Simanjuntak", R.drawable.logo_bca))
        arrBank.add(Bank("BRI", "9876543210", "Noel Simanjuntak", R.drawable.logo_bri))
        arrBank.add(Bank("Mandiri", "1230984567", "Noel Simanjuntak", R.drawable.logo_madiri))

        val layoutManager1 = LinearLayoutManager(this)
        layoutManager1.orientation = LinearLayoutManager.VERTICAL
        rv_data.layoutManager = layoutManager1
        rv_data.adapter = AdapterBank(arrBank, object : AdapterBank.Listeners {
            override fun onClicked(data: Bank, index: Int) {
                bayar(data)
            }
        })
    }

    fun bayar(bank: Bank) {
        val json = intent.getStringExtra("extra")!!.toString()
        val chekout = Gson().fromJson(json, Chekout::class.java)
        chekout.bank = bank.nama

        ApiConfig.instanceRetrofit.Checkout(chekout).enqueue(object :
            retrofit2.Callback<ResponModel> {
            override fun onFailure(call: retrofit2.Call<ResponModel>, t: Throwable) {
                //Toast.makeText(this@PembayaranActivity, "Error:"+t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: retrofit2.Call<ResponModel>, response: retrofit2.Response<ResponModel>) {
                val respon = response.body()!!
                if (respon.success == 1){

                    val jsBank = Gson().toJson(bank, Bank::class.java)
                    val jsTransaksi = Gson().toJson(respon.transaksi, Transaksi::class.java)

                    val intent = Intent(this@PembayaranActivity, SuccessActivity::class.java)
                    intent.putExtra("bank", jsBank)
                    intent.putExtra("transaksi", jsTransaksi)
                    startActivity(intent)

                } else{
                    Toast.makeText(this@PembayaranActivity, "Error:"+respon.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}