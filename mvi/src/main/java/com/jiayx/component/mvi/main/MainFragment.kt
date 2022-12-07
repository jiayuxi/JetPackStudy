package com.jiayx.component.mvi.main

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jiayx.component.mvi.databinding.FragmentMainBinding
import com.jiayx.component.mvi.model.LoginAction
import com.jiayx.component.mvi.model.LoginViewEvent
import com.jiayx.component.mvi.model.LoginViewState
import com.jiayx.component.mvi.utils.observeEvent
import com.jiayx.component.mvi.utils.observeState
import com.jiayx.component.mvi.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch

/**
 *  author : Jia yu xi
 *  date : 2022/12/7 21:45:45
 *  description :
 */
class MainFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel>()
    private val viewBinding by lazy {
        FragmentMainBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = viewBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewStates()
        initViewEvents()
    }

    private fun initView() {
        viewBinding.editTextTextPersonName.addTextChangedListener {
            sendNewIntent(LoginAction.UpdateUserName(it.toString()))
        }
        viewBinding.editTextNumberPassword.addTextChangedListener {
            sendNewIntent(LoginAction.UpdatePassword(it.toString()))
        }
        viewBinding.button.setOnClickListener {
            sendNewIntent(LoginAction.Login)
        }

    }

    private fun initViewStates() {
        viewModel.viewState.observeState(viewLifecycleOwner, LoginViewState::userName) {
            viewBinding.editTextTextPersonName.setText(it)
            viewBinding.editTextTextPersonName.setSelection(it.length)
        }
        viewModel.viewState.observeState(viewLifecycleOwner, LoginViewState::password) {
            viewBinding.editTextNumberPassword.setText(it)
            viewBinding.editTextNumberPassword.setSelection(it.length)
        }
        viewModel.viewState.observeState(viewLifecycleOwner, LoginViewState::isLoginEnable) {
            viewBinding.button.isEnabled = it
            viewBinding.button.alpha = if (it) 1f else 0.5f
        }
        viewModel.viewState.observeState(viewLifecycleOwner, LoginViewState::passwordTipVisible) {
            viewBinding.tvLabel.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun initViewEvents() {
        viewModel.viewEvent.observeEvent(viewLifecycleOwner) {
            when (it) {
                is LoginViewEvent.ShowLoadingDialog -> {
                    showLoadingDialog()
                }
                is LoginViewEvent.ShowToast -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is LoginViewEvent.DismissLoadingDialog -> {
                    dismissLoadingDialog()
                }
            }
        }
    }

    private fun sendNewIntent(intent: LoginAction) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.channel.send(intent)
        }
    }

    private var progressDialog: ProgressDialog? = null

    private fun showLoadingDialog() {
        if (progressDialog == null) progressDialog = ProgressDialog(requireContext())
        progressDialog?.show()
    }

    private fun dismissLoadingDialog() {
        progressDialog?.takeIf { it.isShowing }?.dismiss()
    }
}