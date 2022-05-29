package com.project.delcanteen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.delcanteen.R
import com.project.delcanteen.helper.Helper
import kotlinx.android.synthetic.main.toolbar_custom.*

class TambahAlamatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_alamat)
        Helper().setToolbar(this, toolbar, "Tambah Alamat")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}