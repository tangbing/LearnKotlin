package com.permissionx.tb

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment


typealias PermissionCallback = (Boolean, List<String>) -> Unit

class InvisibleFragment : Fragment() {
    private var callback: PermissionCallback? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            val deniedList = results.filterValues { !it }.keys.toList()
            callback?.invoke(deniedList.isEmpty(), deniedList)
            callback = null
        }

    fun requestNow(cb: PermissionCallback, vararg permissions: String) {
        if (permissions.isEmpty()) {
            cb(true, emptyList())
            return
        }

        callback = cb
        requestPermissionLauncher.launch(permissions.asList().toTypedArray())
    }

}
