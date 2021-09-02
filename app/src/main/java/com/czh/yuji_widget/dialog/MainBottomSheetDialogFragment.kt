package com.czh.yuji_widget.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.czh.yuji_widget.R
import com.czh.yuji_widget.databinding.DialogBottomSheetBinding
import com.czh.yuji_widget.db.City
import com.czh.yuji_widget.vm.MainVM
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MainBottomSheetDialogFragment(val city: City) : BottomSheetDialogFragment() {

    private var binding: DialogBottomSheetBinding? = null
    private val vm by viewModels<MainVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.BottomSheetDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBottomSheetBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialog()
        initView()
    }

    private fun initDialog() {
        dialog?.apply {
            setOnShowListener {
                val bottomSheet =
                    findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                val behavior: BottomSheetBehavior<FrameLayout> =
                    BottomSheetBehavior.from(bottomSheet)
                behavior.skipCollapsed = true
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun initView() {
        binding?.apply {
            tvWidget.setOnClickListener {
                vm.makeCityToWidget(city)
                dismiss()
            }
            tvDelete.setOnClickListener {
                vm.deleteCity(city)
                dismiss()
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}