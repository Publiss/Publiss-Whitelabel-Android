package com.publiss.publissplaystore;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.publiss.core.provider.DocumentsContentProvider;
import com.publiss.core.provider.DocumentsContract;

import junit.framework.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class DocumentsContentProviderTest extends ProviderTestCase2<DocumentsContentProvider> {

    private static final String TAG = DocumentsContentProviderTest.class.getName();

    private static MockContentResolver resolve; // in the test case scenario, we use the MockContentResolver to make queries
    private Uri givenDocumentUri;
    private ContentValues givenDocumentValues;
    private Cursor result;
    private ContentValues givenUpdatedValues;
    private ArrayList<ContentProviderOperation> givenPatchOperations;
    private Date givenDate;
    private String givenDateSting;
    private String givenDatePattern;

    public DocumentsContentProviderTest() {
        super(DocumentsContentProvider.class, DocumentsContract.AUTHORITY);
    }

    @Override
    public void setUp() {
        try {
            Log.i(TAG, "Entered Setup");
            super.setUp();
            resolve = this.getMockContentResolver();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Assert.fail("Content Repository setUp failed");
        }
    }

    @Override
    public void tearDown() {
        try {
            super.tearDown();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Assert.fail("Content Repository tearDown failed");
        }
    }

    public void testInsertSuccessfully() {
        givenDocumentContentValues();
        whenInsertIsCalled();
        thenDocumentUriIsReturned();
    }

    public void testRetrieveAllDocumentsShouldReturnEmptyResult() {
        whenRetrieveDocumentsIsCalled();
        thenResultIsEmptyWithCorrectFormat();
    }


    public void testRetrieveDocumentsWithExistingDocumentShouldReturnResult() {
        givenAnExistingDocument();
        whenRetrieveDocumentsIsCalled();
        thenResultContainsDocument();
    }

    public void testRetrieveDocumentWithExistingDocumentIdShouldReturnResult() {
        givenMultipleDocuments();
        whenRetrieveDocumentByIdIsCalled();
        thenResultContainsDocument();
    }

    public void testRetrieveDocumentWithNotExistingDocumentIdShouldReturnEmptyResult() {
        givenMultipleDocuments();
        givenNotExistingDocumentId();
        whenRetrieveDocumentByIdIsCalled();
        thenResultIsEmptyWithCorrectFormat();
    }

    public void testUpdateDocumentShouldSucceedForExistingDocument() {
        givenMultipleDocuments();
        givenUpdatedValues();
        whenUpdateDocumentIsCalled();
        thenDocumentContainsUpdatedValue();
    }

    public void testDeleteDocumentByIdShouldSucceed() {
     givenMultipleDocuments();
     whenDeleteDocumentIsCalled();
     thenDocumentIsRemoved();
    }
    
    public void testBatchInsertShouldSucceed() {
        givenThreeBatchOperations();
        whenApplyPatchOperationsIsCalled();
        thenResultContainsThreeDocuments();
    }

    public void testRetrieveRightDateFormatShouldSucceed() {
        givenAnExistingDocument();
        givenAnRFCFormattedDate();
        whenDateIsSetWithTheRightFormat();
        thenTheDateCanBeParsedFromThePersistedValue();
    }

    private void givenAnRFCFormattedDate() {
        givenDateSting = "2014-03-11T15:41:26Z";
        givenDateSting = givenDateSting.replace("Z", "GMT+00:00");
        givenDatePattern = "yyyy-MM-dd'T'HH:mm:ssz";
        SimpleDateFormat format = new SimpleDateFormat(givenDatePattern);
        try {
            givenDate = format.parse(givenDateSting);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    private void whenDateIsSetWithTheRightFormat() {
        givenDocumentValues.put(DocumentsContract.Documents.UPDATED_AT, givenDateSting);
    }

    private void thenTheDateCanBeParsedFromThePersistedValue() {
        whenRetrieveDocumentByIdIsCalled();
        String updatedDescription = "";

        if (result != null && result.moveToFirst()) {
            updatedDescription = result.getString(7);
            result.close();
        }

        Date retrievedDate = new Date();

        SimpleDateFormat format = new SimpleDateFormat(givenDatePattern);
        try {
            retrievedDate = format.parse(givenDateSting);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }

        SimpleDateFormat formatter = new SimpleDateFormat(givenDatePattern, Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateStringFromRetrievedDate = formatter.format(retrievedDate);

        Assert.assertEquals(givenDate.getTime(), retrievedDate.getTime());
        Assert.assertEquals(givenDateSting, dateStringFromRetrievedDate);
    }

    private void whenApplyPatchOperationsIsCalled() {
        try {
            resolve.applyBatch(DocumentsContract.AUTHORITY, givenPatchOperations);
        } catch (RemoteException e) {
            Assert.fail(e.getMessage());
        } catch (OperationApplicationException e) {
            Assert.fail(e.getMessage());
        }
    }

    private void givenThreeBatchOperations() {
        givenPatchOperations = new ArrayList<ContentProviderOperation>();
        givenPatchOperations.add(ContentProviderOperation.newInsert(DocumentsContract.Documents.CONTENT_URI)
                .withValues(createDefaultDocumentValues())
                .build());
        givenPatchOperations.add(ContentProviderOperation.newInsert(DocumentsContract.Documents.CONTENT_URI)
                .withValues(createDefaultDocumentValues())
                .build());
        givenPatchOperations.add(ContentProviderOperation.newInsert(DocumentsContract.Documents.CONTENT_URI)
                .withValues(createDefaultDocumentValues())
                .build());
    }

    private void thenResultContainsThreeDocuments() {
        whenRetrieveDocumentsIsCalled();
        Assert.assertEquals(3, result.getCount());
    }

    private void thenDocumentIsRemoved() {
        whenRetrieveDocumentByIdIsCalled();
        Assert.assertEquals(0, result.getCount());
        whenRetrieveDocumentsIsCalled();
        Assert.assertEquals(1, result.getCount());
    }

    private void whenDeleteDocumentIsCalled() {
        resolve.delete(givenDocumentUri, null, null);
    }

    private void whenRetrieveDocumentByIdIsCalled() {
        String[] projection = DocumentsContract.Documents.PROJECTION_ALL;
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = DocumentsContract.Documents.SORT_ORDER_DEFAULT;

        result = resolve.query(givenDocumentUri, projection, selection, selectionArgs, sortOrder);
    }

    private void givenNotExistingDocumentId() {
        givenDocumentUri = Uri.withAppendedPath(DocumentsContract.Documents.CONTENT_URI, "66666");
    }

    private void givenMultipleDocuments() {
        givenAnExistingDocument();
        givenAnExistingDocument();
    }

    private void givenUpdatedValues() {
        givenUpdatedValues = createDefaultDocumentValues();
        givenUpdatedValues.put(DocumentsContract.Documents.DESCRIPTION, "updated description");
    }

    private void thenDocumentContainsUpdatedValue() {
        whenRetrieveDocumentByIdIsCalled();
        String updatedDescription = "";

        if (result != null && result.moveToFirst()) {
            updatedDescription = result.getString(3);
            result.close();
        }

        Assert.assertEquals("updated description", updatedDescription);
    }

    private void whenUpdateDocumentIsCalled() {
        int updatedRowsCount = resolve.update(givenDocumentUri, givenUpdatedValues, null, null);
        Assert.assertEquals(1, updatedRowsCount);
    }

    private void thenResultIsEmptyWithCorrectFormat() {
        Assert.assertEquals(0, result.getCount());
        Assert.assertEquals(10, result.getColumnCount());
    }

    private void whenRetrieveDocumentsIsCalled() {
        String[] projection = DocumentsContract.Documents.PROJECTION_ALL;
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = DocumentsContract.Documents.SORT_ORDER_DEFAULT;

        result = resolve.query(DocumentsContract.Documents.CONTENT_URI, projection, selection, selectionArgs, sortOrder);
    }

    private void thenResultContainsDocument() {
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.getCount());

        result.moveToNext();
        Log.i(TAG, "retrieved data: ");
        for (int columnIndex = 0; columnIndex < result.getColumnCount(); columnIndex++) {
            Log.i(TAG, result.getColumnName(columnIndex) + ": " + result.getString(columnIndex));
        }
        Assert.assertEquals(givenDocumentUri.getLastPathSegment(), String.valueOf(result.getLong(0)));
    }

    private void thenDocumentUriIsReturned() {
        Assert.assertNotNull(givenDocumentUri);
        Log.i(TAG, "returned document URI " + givenDocumentUri.toString());
    }

    private void whenInsertIsCalled() {
        givenDocumentUri = resolve.insert(DocumentsContract.Documents.CONTENT_URI, givenDocumentValues);
    }

    private void givenAnExistingDocument() {
        givenDocumentContentValues();
        givenDocumentUri = resolve.insert(DocumentsContract.Documents.CONTENT_URI, givenDocumentValues);
    }

    private void givenDocumentContentValues() {
        givenDocumentValues = createDefaultDocumentValues();
    }

    private ContentValues createDefaultDocumentValues() {
        ContentValues values = new ContentValues();

        values.put(DocumentsContract.Documents.NAME, "Name");
        values.put(DocumentsContract.Documents.DESCRIPTION, "Description");
        values.put(DocumentsContract.Documents.COVER_IMAGE_PATH, "www.apple.com/watch");
        values.put(DocumentsContract.Documents.PAID, Integer.valueOf(123));
        values.put(DocumentsContract.Documents.SIZE, Integer.valueOf(123));
        values.put(DocumentsContract.Documents.FILE_SIZE, Integer.valueOf(123));
        values.put(DocumentsContract.Documents.PRIORITY, Integer.valueOf(123));
        values.put(DocumentsContract.Documents.UPDATED_AT, "2014-03-11T15:41:26Z");

        return values;
    }

}
