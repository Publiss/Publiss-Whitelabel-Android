package com.publiss.whitelabel;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.view.View;

import com.android.vending.billing.Base64;
import com.android.vending.billing.Base64DecoderException;
import com.android.vending.billing.IabHelper;
import com.android.vending.billing.IabResult;
import com.android.vending.billing.Purchase;
import com.publiss.core.DocumentsWithPendingPDFDownloads;
import com.publiss.core.data.model.PagesInfo;
import com.publiss.core.data.model.PublishedDocument;
import com.publiss.core.ui.PreviewActivity;

import junit.framework.Assert;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(AndroidJUnit4.class)
@SmallTest
public class IabAcceptanceTest {

    private static final String TAG = IabAcceptanceTest.class.getSimpleName();
    private static final int DOCUMENT_ID = 898;
    //    private PreviewActivity mActivity;
    @Rule public ActivityTestRule<PreviewActivity> activityTestRule = new ActivityTestRule<>(PreviewActivity.class);
    @InjectMocks
    private IabHelper iabHelper;

    private static final String JSON_MESSAGE = "{\"packageName\":\"com.example\",\"orderId\":\"transactionId.android.test.purchased\",\"productId\":\"android.test.purchased\",\"developerPayload\":\"1\",\"purchaseTime\":0,\"purchaseState\":0,\"purchaseToken\":\"inapp:com.example:android.test.purchased\"}";
    private static final String PRIVATE_KEY = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAsNh2Am1ZYx9G3PJ/P9d/lUDaTXTkmVbt/QsUCl/67t5E8yAEWMXgBHIaARvvRHDxi5l6PBAlx/C2HQ7HFgx4SQIDAQABAkABL0IDHCZoIpJ/8mPl0pS5NDkCIdFSMaHgew2EUEZHCVVEg8Gcr12pC8wRw45s2MBt2Kp3qlI8RZlKb97bJg8BAiEA3ee1QeQtRHzL7akgG4NMLbAH6k6PS0+9bL893APVhqkCIQDMBGfFHSMxvZ1b1ZPq2F3Al95aqyXw3cOT/OuSbBGIoQIgSCovbzNGaWxwYWTL9UaYwo7ptBBCV4qiHrh+5Is2qKkCIBddwczPo4JE50rnUUOqeEJgonTb+UJ3A7llVE221uNBAiAKjhSI70RYMed49JF+QTWsyoEojwDgPDOoT+xgOnCxCA==";
    private static final String PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALDYdgJtWWMfRtzyfz/Xf5VA2k105JlW7f0LFApf+u7eRPMgBFjF4ARyGgEb70Rw8YuZejwQJcfwth0OxxYMeEkCAwEAAQ==";
    private static final String KEY_FACTORY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";


    private Bundle mArgsBundle;
    private Instrumentation mInstrumentation;
    private Context mTestAppContext;
    private Context mTargetContext;

//    @Before
//    public void accessAllTheThings() {
//        mArgsBundle = InstrumentationRegistry.getArguments();
//        mInstrumentation = InstrumentationRegistry.getInstrumentation();
//        mTestAppContext = InstrumentationRegistry.getContext();
//        mTargetContext = InstrumentationRegistry.getTargetContext();
//    }

    @Before
    public void init() {
//        mArgsBundle = InstrumentationRegistry.getArguments();
        mInstrumentation = InstrumentationRegistry.getInstrumentation();
//        mTestAppContext = InstrumentationRegistry.getContext();
//        mTargetContext = InstrumentationRegistry.getTargetContext();
//        MockitoAnnotations.initMocks(this);
//        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
//        initMocks(this);
//        iabHelper = mock(IabHelper.class);

//        MockitoAnnotations.initMocks(activityTestRule);

        Intent intent = new Intent();
        PublishedDocument document = new PublishedDocument();
        document.setId(DOCUMENT_ID);
        document.setName("Acceptance Test");
        document.setDescription("Dummy document for testing");
        document.setPagesInfo(new PagesInfo());
        document.setUpdatedAt(new Date());
        document.setPaid(true);
        document.setGooglePlayProductId("android.test.purchased");
        document.deletePdfDocument();
        intent.putExtra(PreviewActivity.ARG_PUBLISHED_DOCUMENT, (Parcelable) document);
        Espresso.registerIdlingResources(new PriorityJobQueueIdleMonitor());
//        activityTestRule.getActivity().setIntent(intent);
        activityTestRule.launchActivity(intent);
//        mInstrumentation.startActivitySync(intent);
//        mActivity = getActivity();
    }

    @Test
    public void changeText_sameActivity() {
        IabHelper iabHelper = mock(IabHelper.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IabHelper.OnIabPurchaseFinishedListener) invocation.getArguments()[3]).onIabPurchaseFinished(new IabResult(IabHelper.BILLING_RESPONSE_RESULT_OK, "dummy ok"), new Purchase(IabHelper.ITEM_TYPE_INAPP, getPurchaseJson(), signWithPrivateKey(getPurchaseJson())));
                return null;
            }
        }).when(iabHelper).launchPurchaseFlow(any(Activity.class), eq("android.test.purchased"), anyInt(), any(IabHelper.OnIabPurchaseFinishedListener.class));
        activityTestRule.getActivity().iabHelper = iabHelper;

        onView(withId(R.id.buttonBuy))
                .perform(click());
        onView(isRoot()).perform(waitId(R.id.buttonBuy, 1000));
//        onData(eq(DocumentsWithPendingPDFDownloads.getInstance().hasPendingPDFDownload(DOCUMENT_ID)))

        Assert.assertTrue("Document should be in Download list", eq(DocumentsWithPendingPDFDownloads.getInstance().hasPendingPDFDownload(DOCUMENT_ID)));
        onView(withId(R.id.buttonBuy)).check(matches(withText(R.string.download_button_loading)));
                ;
//        onView(withId(R.id.changeTextButton)).perform(click());

    }

    public static PrivateKey generatePrivateKey(String encodedPrivateKey) {
        try {
            byte[] decodedKey = Base64.decode(encodedPrivateKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, "Invalid key specification.");
            throw new IllegalArgumentException(e);
        } catch (Base64DecoderException e) {
            Log.e(TAG, "Base64 decoding failed.");
            throw new IllegalArgumentException(e);
        }
    }

    private static String signWithPrivateKey(String dataToSign) {
        PrivateKey privateKey = generatePrivateKey(PRIVATE_KEY);
        Signature sig;
        try {
            sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initSign(privateKey);
            sig.update(dataToSign.getBytes());
            return Base64.encode(sig.sign());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "NoSuchAlgorithmException.");
        } catch (InvalidKeyException e) {
            Log.e(TAG, "Invalid key specification.");
        } catch (SignatureException e) {
            Log.e(TAG, "Signature exception.");
        }
        return "";
    }

    public String getPurchaseJson() {
        return JSON_MESSAGE;
    }

    /** Perform action of waiting for a specific view id. */
    public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

}
