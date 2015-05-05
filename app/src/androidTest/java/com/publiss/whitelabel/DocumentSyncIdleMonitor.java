package com.publiss.whitelabel;

import android.support.test.espresso.IdlingResource;

import com.publiss.core.PublissCoreApplication;
import com.publiss.core.events.SyncEvent;

import java.util.concurrent.atomic.AtomicBoolean;

import de.greenrobot.event.EventBus;

//took from https://github.com/Nilhcem/android-priority-jobqueue-sample/
public class DocumentSyncIdleMonitor implements IdlingResource {

    private AtomicBoolean mWasIdle = new AtomicBoolean(true);
    private volatile ResourceCallback mResourceCallback;
    private SyncEvent event = new SyncEvent(SyncEvent.SyncStatus.START);

    public DocumentSyncIdleMonitor() {
        EventBus.getDefault().registerSticky(this);
    }

    public void onEvent(SyncEvent event) {
        this.event = event;
    }

    @Override
    public String getName() {
        return DocumentSyncIdleMonitor.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        boolean isIdle = event.getSyncStatus() == SyncEvent.SyncStatus.END;
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
