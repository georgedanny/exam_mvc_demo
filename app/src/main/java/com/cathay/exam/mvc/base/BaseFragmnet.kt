package com.cathay.exam.mvc.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.cathay.exam.mvc.data.database.AppDataBase
import com.cathay.exam.mvc.data.database.RoomDao

abstract class BaseFragmnet:Fragment(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        listen()
    }

    abstract fun initView()



    abstract fun listen()



    protected fun nav(): NavController {
        return NavHostFragment.findNavController(this)
    }

    protected fun getRoomDao(): RoomDao {
       return AppDataBase(requireContext()).examInfoDao()
    }



}