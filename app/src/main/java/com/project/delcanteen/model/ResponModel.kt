package com.project.delcanteen.model

class ResponModel {
    var success = 0
    lateinit var message:String
    var user = User()
    var produks: ArrayList<Produk> = ArrayList()

    var rajaongkir = ModelAlamat()

    var provinsi: ArrayList<ModelAlamat> = ArrayList()
    var kota_kabupaten: ArrayList<ModelAlamat> = ArrayList()
    var kecamatan: ArrayList<ModelAlamat> = ArrayList()
}