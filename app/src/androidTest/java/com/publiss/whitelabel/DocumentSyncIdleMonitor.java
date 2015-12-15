package com.publiss.whitelabel;

import android.support.test.espresso.IdlingResource;

import com.publiss.core.PublissCoreApplication;
import com.publiss.core.events.SyncFinishedEvent;

import java.util.concurrent.atomic.AtomicBoolean;

import de.greenrobot.event.EventBus;

//took from https://github.com/Nilhcem/android-priority-jobqueue-sample/
public class DocumentSyncIdleMonitor implements IdlingResource {

    private AtomicBoolean mWasIdle = new AtomicBoolean(true);
    private volatile ResourceCallback mResourceCallback;
    private SyncFinishedEvent event = new SyncFinishedEvent();

    public DocumentSyncIdleMonitor() {
        EventBus.getDefault().registerSticky(this);
    }

    public void onEvent(SyncFinishedEvent event) {
        this.event = event;
    }

    @Override
    public String getName() {
        return DocumentSyncIdleMonitor.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        boolean isIdle = event != null;
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
