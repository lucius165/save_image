package com.hoangtp165.quickcall

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by lucius on 04/11/2017.
 */
class FragmentHelper {
    companion object {
        fun <T : Fragment> newInstance(entityClass: Class<T>, bundle: Bundle? = null): T {
            val page = entityClass.newInstance()
            page.arguments = bundle
            return page
        }
    }
}