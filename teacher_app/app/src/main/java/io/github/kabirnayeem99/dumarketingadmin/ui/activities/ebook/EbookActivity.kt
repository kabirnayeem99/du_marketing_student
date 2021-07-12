package io.github.kabirnayeem99.dumarketingadmin.ui.activities.ebook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.util.adapter.EbookDataAdapter
import io.github.kabirnayeem99.dumarketingadmin.viewmodel.EbookViewModel

@AndroidEntryPoint
class EbookActivity : AppCompatActivity() {

    private lateinit var rvEBooks: RecyclerView

    private val ebookViewModel: EbookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ebook)

        setUpBooksList()
    }

    private fun setUpBooksList() {
        rvEBooks = findViewById(R.id.rvEbooks)
        rvEBooks.apply {
            adapter = ebookAdapter
            layoutManager = LinearLayoutManager(this@EbookActivity)
        }

        ebookViewModel.getEbooks().observe(this, Observer { resources ->
            when (resources) {
                is Resource.Error -> {
                    Toast.makeText(
                        this,
                        "Could not get the books from the server.",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.e(TAG, "setUpBooksList: ${resources.message}")
                }

                is Resource.Success -> {
                    ebookAdapter.differ.submitList(resources.data)
                }
            }

        })
    }

    fun onFabUploadEbookClick(view: View) {
        Intent(this, UploadEbookActivity::class.java).also { intent ->
            startActivity(intent)
        }
    }


    private val ebookAdapter: EbookDataAdapter by lazy {
        EbookDataAdapter {
            deleteEbook(it)
        }
    }

    private fun deleteEbook(ebook: EbookData) {
        val deleteTask = ebookViewModel.deleteEbook(ebook)
        if (deleteTask == null) {
            Toast.makeText(this, "Could not delete ${ebook.title} file.", Toast.LENGTH_SHORT)
                .show()
            Log.e(TAG, "deleteEbook: the key of the ebook data object is null.")
        } else {

            deleteTask

                .addOnFailureListener { e ->
                    Toast.makeText(this, "Could not delete ${ebook.title}", Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }

                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully deleted ${ebook.title}", Toast.LENGTH_SHORT)
                        .show()
                }
        }

    }

    companion object {
        private const val TAG = "EbookActivity"
    }

}