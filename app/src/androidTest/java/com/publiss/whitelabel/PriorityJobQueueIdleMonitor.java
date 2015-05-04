package com.publiss.whitelabel;

import android.support.test.espresso.IdlingResource;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobHolder;
import com.path.android.jobqueue.JobQueue;
import com.publiss.core.PublissCoreApplication;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

//took from https://github.com/Nilhcem/android-priority-jobqueue-sample/
public class PriorityJobQueueIdleMonitor implements IdlingResource {

    private AtomicBoolean mWasIdle = new AtomicBoolean(true);
    private volatile ResourceCallback mResourceCallback;

    @Override
    public String getName() {
        return PriorityJobQueueIdleMonitor.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        boolean isIdle = PublissCoreApplication.getInstance().getDownloadJobManager().count() == 0;
        boolean wasIdle = mWasIdle.getAndSet(isIdle);
        if (isIdle && !wasIdle && mResourceCallback != null) {
            mResourceCallback.onTransitionToIdle();
            mResourceCallback = null;
        }
        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(final ResourceCallback callback) {
        mResourceCallback = callback;
    }
}
