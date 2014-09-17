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

import java.util.ArrayList;

public class PagesInfoTests extends ProviderTestCase2<DocumentsContentProvider> {

    private static final String TAG = PagesInfoTests.class.getSimpleName();

    private static MockContentResolver resolve;
    private ContentValues givenPagesInfoValues;
    private ContentValues givenUpdatedValues;
    private Uri givenPagesInfoUri;
    private Cursor result;
    private ArrayList<ContentProviderOperation> givenBatchOperations;

    public PagesInfoTests() {
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

    // TESTS

    public void testInsertSuccessfull() {
        givenPagesInfoValues();
        whenInsertIsCalled();
        thenDocumentUriIsReturned();
    }

    public void testInsertedAndRetrievedValuesMatch() {
        givenPagesInfoValues();
        whenInsertIsCalled();
        whenRetrievePagesInfoByIdsIsCalled();
        thenRetrievedValuesAreSameAsInsertedValues();
    }

    public void testUpdateSuccessful() {
        givenPagesInfoValues();
        givenUpdatedValues();

        whenInsertIsCalled();
        whenUpdateIsCalled();
        thenRetrievedValuesContainUpdatedValues();
    }

    public void testDeleteById() {
        givenPagesInfoValues();
        whenInsertIsCalled();
        whenRetrievePagesInfoByIdsIsCalled();
        thenTheDocumntCountIsRight(1);
        whenDeleteIsCalled();
        whenRetrievePagesInfoByIdsIsCalled();
        thenTheDocumntCountIsRight(0);
    }

    public void testBatchInsertShouldSucceed() {
        int numberOfOperations = 9;

        givenMultipleInsertBatchOperations(numberOfOperations);
        whenApplyBatchOperationsIsCalled();
        whenRetrievePagesInfoIsCalled();
        thenTheDocumntCountIsRight(numberOfOperations);
    }

    // GIVEN

    public void givenPagesInfoValues() {
        givenPagesInfoValues = createDefaultPagesInfoValues(1);
    }

    private void givenUpdatedValues() {
        givenUpdatedValues = createDefaultPagesInfoValues(1);
        givenUpdatedValues.put(DocumentsContract.PagesInfo.COUNT, Integer.valueOf(999));
    }

    private void givenMultipleInsertBatchOperations(int numberOfOperations) {
        givenBatchOperations = new ArrayList<ContentProviderOperation>();

        for (int i = 0; i < numberOfOperations; i++) {
            ContentValues values = createDefaultPagesInfoValues(i);
            givenBatchOperations.add(ContentProviderOperation.newInsert(DocumentsContract.PagesInfo.CONTENT_URI)
                    .withValues(values)
                    .build());
        }
    }

    // WHEN

    public void whenInsertIsCalled() {
        givenPagesInfoUri = resolve.insert(DocumentsContract.PagesInfo.CONTENT_URI, givenPagesInfoValues);
    }

    private void whenRetrievePagesInfoByIdsIsCalled() {
        String[] projection = DocumentsContract.PagesInfo.PROJECTION_ALL;
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = DocumentsContract.PagesInfo.SORT_ORDER_DEFAULT;

        result = resolve.query(givenPagesInfoUri, projection, selection, selectionArgs, sortOrder);
    }

    private void whenRetrievePagesInfoIsCalled() {
        Uri uri = DocumentsContract.PagesInfo.CONTENT_URI;
        String[] projection = DocumentsContract.PagesInfo.PROJECTION_ALL;
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = DocumentsContract.PagesInfo.SORT_ORDER_DEFAULT;

        result = resolve.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    public void whenUpdateIsCalled() {
        int updatedRowsCount = resolve.update(givenPagesInfoUri, givenUpdatedValues, null, null);
        Assert.assertEquals(1, updatedRowsCount);
    }

    private void whenDeleteIsCalled() {
        int deletedRowsCount = resolve.delete(givenPagesInfoUri, null, null);
        Assert.assertEquals(1, deletedRowsCount);
    }

    private void whenApplyBatchOperationsIsCalled() {
        try {
            resolve.applyBatch(DocumentsContract.AUTHORITY, givenBatchOperations);
        } catch (RemoteException e) {
            Assert.fail(e.getMessage());
        } catch (OperationApplicationException e) {
            Assert.fail(e.getMessage());
        }
    }

    // THEN

    public void thenDocumentUriIsReturned() {
        Assert.assertNotNull(givenPagesInfoUri);
        Log.i(TAG, "returned document URI " + givenPagesInfoUri.toString());
    }

    public void thenRetrievedValuesAreSameAsInsertedValues() {
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.getCount());

        int documentId = (Integer)givenPagesInfoValues.get(DocumentsContract.PagesInfo.DOCUMENT_ID);
        int count = (Integer)givenPagesInfoValues.get(DocumentsContract.PagesInfo.COUNT);
        String dimensions = (String)givenPagesInfoValues.get(DocumentsContract.PagesInfo.DIMENSIONS);
        String checksums = (String)givenPagesInfoValues.get(DocumentsContract.PagesInfo.CHECKSUMS);
        String sizes = (String)givenPagesInfoValues.get(DocumentsContract.PagesInfo.SIZES);

        if (result != null && result.moveToFirst()) {
            int retrievedDocumentId = result.getInt(result.getColumnIndex(DocumentsContract.PagesInfo.DOCUMENT_ID));
            int retrievedCount = result.getInt(result.getColumnIndex(DocumentsContract.PagesInfo.COUNT));
            String retrievedDimensions= result.getString(result.getColumnIndex(DocumentsContract.PagesInfo.DIMENSIONS));
            String retrievedDhecksums = result.getString(result.getColumnIndex(DocumentsContract.PagesInfo.CHECKSUMS));
            String retrievedSizes = result.getString(result.getColumnIndex(DocumentsContract.PagesInfo.SIZES));

            Assert.assertEquals(documentId, retrievedDocumentId);
            Assert.assertEquals(count, retrievedCount);
            Assert.assertEquals(dimensions, retrievedDimensions);
            Assert.assertEquals(checksums, retrievedDhecksums);
            Assert.assertEquals(sizes, retrievedSizes);

            result.close();
        }
    }

    private void thenRetrievedValuesContainUpdatedValues() {
        whenRetrievePagesInfoByIdsIsCalled();
        int updatedCount = -1;

        if (result != null && result.moveToFirst()) {
            updatedCount = result.getInt(result.getColumnIndex(DocumentsContract.PagesInfo.COUNT));
            result.close();
        }

        Assert.assertEquals(999, updatedCount);
    }

    private void thenTheDocumntCountIsRight(int expectedCount) {
        Assert.assertNotNull(result);
        Assert.assertEquals(expectedCount, result.getCount());
    }

    // HELPER

    private ContentValues createDefaultPagesInfoValues(int id) {
        ContentValues values = new ContentValues();

        Integer count = 3;
        String[] dimensions = "553,765".split(strSeparator);
        String[] checksums = "27e7ba1999c89ac9205d0bc8b2111bb5,d63d69b6db3923061b1f8248908f753d,fb2de190dc61a69835f744f2d7846e2e".split(strSeparator);
        String[] sizes = "158232,157691,67514".split(strSeparator);

        values.put(DocumentsContract.PagesInfo.DOCUMENT_ID, Integer.valueOf(id));
        values.put(DocumentsContract.PagesInfo.COUNT, Integer.valueOf(count));
        values.put(DocumentsContract.PagesInfo.DIMENSIONS, convertArrayToString(dimensions));
        values.put(DocumentsContract.PagesInfo.CHECKSUMS, convertArrayToString(checksums));
        values.put(DocumentsContract.PagesInfo.SIZES, convertArrayToString(sizes));

        return values;
    }

    // TODO: Extract in own helper class...

    public static String strSeparator = "__,__";
    public static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }
}
