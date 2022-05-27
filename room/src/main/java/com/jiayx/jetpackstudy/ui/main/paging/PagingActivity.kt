package com.jiayx.jetpackstudy.ui.main.paging

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jiayx.jetpackstudy.R
import com.jiayx.jetpackstudy.ui.main.viewmodel.PersonViewModel

/**
 *Created by yuxi_
on 2022/3/8
 */
class PagingActivity : AppCompatActivity() {
    private val pagingViewModel: PersonViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        )[PersonViewModel::class.java]

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paging_activity)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add ->  pagingViewModel.insert()
            R.id.menu_manage -> { pagingViewModel.manageListener?.invoke()}
        }
        return true
    }
}