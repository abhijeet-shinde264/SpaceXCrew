package com.intern.spacex;

public interface ResponseCallback {
    void onSuccessful();
    void onFailed(String error);
}
