package com.project.delcanteen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.project.delcanteen.R
import com.project.delcanteen.app.ApiConfigAlamat
import com.project.delcanteen.helper.Helper
import com.project.delcanteen.model.Alamat
import com.project.delcanteen.model.ModelAlamat
import com.project.delcanteen.model.ResponModel
import com.project.delcanteen.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_tambah_alamat.*
import kotlinx.android.synthetic.main.activity_tambah_alamat.pb
import kotlinx.android.synthetic.main.toolbar_custom.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahAlamatActivity : AppCompatActivity() {

    var provinsi = ModelAlamat()
    var kota = ModelAlamat()
    var kecamatan = ModelAlamat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_alamat)
        Helper().setToolbar(this, toolbar, "Tambah Alamat")

        mainButton()
        getProvinsi()
    }

    private fun mainButton(){
        btn_simpan.setOnClickListener{
            simpan()
        }
    }

    private fun simpan(){
        when {
            edt_nama.text.isEmpty() -> {
                error(edt_nama)
                return
            }
            edt_type.text.isEmpty() -> {
                error(edt_type)
                return
            }
            edt_phone.text.isEmpty() -> {
                error(edt_phone)
                return
            }
            edt_alamat.text.isEmpty() -> {
                error(edt_alamat)
                return
            }
            edt_kodePos.text.isEmpty() -> {
                error(edt_kodePos)
                return
            }
        }

        if (provinsi.id == 0){
            toast("Silahkan pilih provinsi")
            return
        }

        if (kota.id == 0){
            toast("Silahkan pilih kota")
            return
        }

        if (kecamatan.id == 0){
            toast("Silahkan pilih kecamatan")
            return
        }

        val alamat = Alamat()
        alamat.name = edt_nama.text.toString()
        alamat.type = edt_type.text.toString()
        alamat.phone = edt_phone.text.toString()
        alamat.alamat = edt_alamat.text.toString()
        alamat.kodepos = edt_kodePos.text.toString()

        alamat.id_provinsi = provinsi.id
        alamat.provinsi = provinsi.nama
        alamat.id_kota = kota.id
        alamat.kota = kota.nama
        alamat.id_kecamatan = kecamatan.id
        alamat.kecamatan = kecamatan.nama

        insert(alamat)
    }

    fun toast(string: String){
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    fun error(editText: EditText){
        editText.error = "Kolom Password tidak boleh kosong"
        editText.requestFocus()
    }

    private fun getProvinsi(){
        ApiConfigAlamat.instanceRetrofit.getProvinsi().enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful){
                    pb.visibility = View.GONE
                    div_provinsi.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Provinsi")
                    val listProvinsi = res.provinsi
                    for(prov in res.provinsi){
                        arryString.add(prov.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spiner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_provinsi.adapter = adapter
                    spn_provinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                        override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0){
                                val idProv = listProvinsi[position - 1].id
                                Log.d("respon", "provinsi id:" + idProv + " " + "name:" + listProvinsi[position - 1].nama)
                                getKota(idProv)
                            }
                        }
                    }
                }else{
                    Log.d("Error", "gagal memuat data:" + response.message())

                }
            }
        })
    }

    private fun getKota(id:Int){
        ApiConfigAlamat.instanceRetrofit.getKota(id).enqueue(object : Callback<ResponModel>{
            override fun onFailure(call: Call<ResponModel>, t: Throwable){

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if(response.isSuccessful) {

                    pb.visibility = View.GONE
                    div_kota.visibility = View.VISIBLE

                    val res = response.body()!!
                    val listArray = res.kota_kabupaten
                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Kota")
                    for (prov in res.kota_kabupaten){
                        arryString.add(prov.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spiner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kota.adapter = adapter
                    spn_kota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                        override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0){
                                kota = listArray[position - 1]
                                val idKota= listArray[position - 1].id
                                Log.d("respon", "provinsi id:" + idKota + " " + "name:" + listArray[position - 1].nama)
                                getKecamatan(idKota)
                            }
                        }

                    }
                }else{
                    Log.d("Error", "gagal memuat data:" + response.message())
                }
            }
        })
    }

    private fun getKecamatan(id:Int){
        pb.visibility = View.VISIBLE
        ApiConfigAlamat.instanceRetrofit.getKecamatan(id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful){
                    pb.visibility = View.GONE
                    div_kecamatan.visibility = View.VISIBLE

                    val res = response.body()!!
                    val listArray = res.kecamatan

                    val arryString = ArrayList<String>()
                    arryString.add("Pilih kecamatan")
                    for(data in listArray){
                        arryString.add(data.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spiner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kecamatan.adapter = adapter
                    spn_kecamatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                        override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0){
                                kecamatan = listArray[position - 1]
                            }
                        }
                    }

                }else{
                    Log.d("Error", "gagal membuat data: " + response.message())
                }

            }
        })
    }

    private fun insert(data:Alamat) {
        val myDb = MyDatabase.getInstance(this)!!
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().insert(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                toast("Insert data success")
                onBackPressed()
            })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}