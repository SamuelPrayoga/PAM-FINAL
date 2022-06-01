package com.project.delcanteen.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.project.delcanteen.R
import com.project.delcanteen.adapter.AdapterProduct
import com.project.delcanteen.adapter.AdapterProdukTransaksi
import com.project.delcanteen.adapter.AdapterRiwayat
import com.project.delcanteen.helper.Helper
import com.project.delcanteen.model.rajaongkir.DetailTransaksi
import com.project.delcanteen.model.rajaongkir.Transaksi
import kotlinx.android.synthetic.main.activity_detail_transaksi.*
import kotlinx.android.synthetic.main.activity_riwayat.*
import kotlinx.android.synthetic.main.toolbar_custom.*

class DetailTransaksiActivity : AppCompatActivity() {

    var transaksi = Transaksi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaksi)
        Helper().setToolbar(this, toolbar, "Detail Transaksi")

        val json = intent.getStringExtra("transaksi")
        transaksi = Gson().fromJson(json, Transaksi::class.java)

        setData(transaksi)
        displayProduk(transaksi.details)

    }

    fun setData(t: Transaksi) {
        tv_status.text = t.status
        tv_tgl.text = t.created_at
        tv_penerima.text = t.name + " - " + t.phone
        tv_alamat.text = t.detail_lokasi
        tv_kodeUnik.text = Helper().changeRupiah(t.kode_unik)
        tv_totalBelanja.text = Helper().changeRupiah(t.total_harga)
        tv_ongkir.text = Helper().changeRupiah(t.ongkir)
        tv_total.text = Helper().changeRupiah(t.total_transfer)

    }

    fun displayProduk(transaksis: ArrayList<DetailTransaksi>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_produk.adapter = AdapterProdukTransaksi(transaksis)
        rv_produk.layoutManager = layoutManager
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}