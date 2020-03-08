package com.android.ocat.ui.hotspots;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HotspotsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HotspotsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is hotspots fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}