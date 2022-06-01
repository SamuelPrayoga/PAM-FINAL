package com.project.delcanteen.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.delcanteen.R
import com.project.delcanteen.adapter.AdapterKurir
import com.project.delcanteen.app.ApiConfigAlamat
import com.project.delcanteen.helper.Helper
import com.project.delcanteen.model.Alamat
import com.project.delcanteen.model.rajaongkir.Costs
import com.project.delcanteen.model.rajaongkir.ResponOngkir
import com.project.delcanteen.model.rajaongkir.Result
import com.project.delcanteen.room.MyDatabase
import com.project.delcanteen.util.ApiKey
import kotlinx.android.synthetic.main.activity_pengiriman.*
import kotlinx.android.synthetic.main.activity_pengiriman.btn_tambahAlamat
import kotlinx.android.synthetic.main.activity_tambah_alamat.*
import kotlinx.android.synthetic.main.toolbar_custom.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengirimanActivity : AppCompatActivity(){
    lateinit var myDB : MyDatabase
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengiriman)
        Helper().setToolbar(this, toolbar, "Pengiriman")
        val myDB = MyDatabase.getInstance(this)!!
        mainButton()

        setSpiner()

    }

    fun setSpiner(){
        val arryString = ArrayList<String>()
        arryString.add("JNE")
        arryString.add("POS")
        arryString.add("TIKI")

        val adapter = ArrayAdapter<Any>(this, R.layout.item_spiner, arryString.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_kurir.adapter = adapter
        spn_kurir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0){
                    getOngkir(spn_kurir.selectedItem.toString())
                }
            }

        }
    }



    @SuppressLint("SetTextI18n")
    fun checkAlamat(){

        if(myDB.daoAlamat().getByStatus(true) != null){
            div_alamat.visibility = View.VISIBLE
            div_kosong.visibility = View.GONE
            div_metodePengiriman.visibility = View.VISIBLE

            val a = myDB.daoAlamat().getByStatus(true)!!
            tv_nama.text = a.name
            tv_phone.text = a.phone
            tv_alamat.text = a.alamat + ", " + a.kota + "," + a.kecamatan + "," + a.kodepos + ", (" + a.type + ")"
            btn_tambahAlamat.text = "Ubah alamat"

            getOngkir("JNE")
        }else{
            div_alamat.visibility = View.GONE
            div_kosong.visibility = View.VISIBLE
            btn_tambahAlamat.text = "tambah alamat"
        }
    }

    private fun mainButton(){
        btn_tambahAlamat.setOnClickListener{
            startActivity(Intent(this, ListAlamatActivity::class.java))
        }
    }

    private fun getOngkir(kurir:String){

        val alamat = myDB.daoAlamat().getByStatus(true)

        val origin = "501"
        val destination = "" + alamat!!.id_kota.toString()
        val berat = 1000

        ApiConfigAlamat.instanceRetrofit.ongkir(ApiKey.key, origin, destination, berat, kurir.toLowerCase()).enqueue(object :
            Callback<ResponOngkir> {
            override fun onFailure(call: Call<ResponOngkir>, t: Throwable) {
                Log.d("Error", "gagal memuat data:" + t.message)
            }

            override fun onResponse(call: Call<ResponOngkir>, response: Response<ResponOngkir>) {

                if (response.isSuccessful){

                    Log.d("Succes", "berhasil memuat data")
                    val result = response.body()!!.rajaongkir.result
                    if (result.isNotEmpty()){
                        displayOngkir(result[0].code.toUpperCase(), result[0].costs)
                    }


                }else{
                    Log.d("Error", "gagal memuat data:" + response.message())

                }
            }
        })
    }

    private fun displayOngkir(kurir:String, arrayList : ArrayList<Costs>){

        val layoutManager1 = LinearLayoutManager(this)
        layoutManager1.orientation = LinearLayoutManager.VERTICAL
        rv_metode.adapter = AdapterKurir(arrayList, kurir, object : AdapterKurir.Listeners{
            override fun onClicked(data: Costs, index: Int) {
            }

        })
        rv_metode.layoutManager = layoutManager1
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        checkAlamat()
        super.onResume()
    }
}