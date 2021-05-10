package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.github.kabirnayeem99.dumarketingstudent.data.vo.FacultyData
import io.github.kabirnayeem99.dumarketingstudent.data.vo.FacultyData.Companion.toFacultyDataList
import io.github.kabirnayeem99.dumarketingstudent.util.Constants.FACULTY_STORAGE_PATH
import io.github.kabirnayeem99.dumarketingstudent.util.Resource

class FacultyRepository {

    private val db = FirebaseFirestore.getInstance()
    private val facultyDataListLiveData = MutableLiveData<Resource<List<FacultyData>>>()

    // this mess needs to be cleaned soon
    fun getAllFacultyList(): MutableLiveData<Resource<List<FacultyData>>> {

        val ref = db.collection(FACULTY_STORAGE_PATH)
            .orderBy("name", Query.Direction.DESCENDING)
        ref.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    facultyDataListLiveData.value = Resource.Error("", null)
                    return@EventListener
                }

                val facultyList = value?.toFacultyDataList()

                if (facultyList == null) {
                    facultyDataListLiveData.value = Resource.Error("Empty List")
                } else {
                    facultyDataListLiveData.value = Resource.Success(facultyList)
                }
            }
        )


        return facultyDataListLiveData
    }


    companion object {
        private const val TAG = "FacultyRepository"
    }
}