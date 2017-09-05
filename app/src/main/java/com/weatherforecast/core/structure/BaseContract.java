package com.weatherforecast.core.structure;

import android.arch.lifecycle.LifecycleOwner;

public interface BaseContract {

    interface View {

        LifecycleOwner provideLifecycleOwner();

    }

    interface Action {
    }

}
