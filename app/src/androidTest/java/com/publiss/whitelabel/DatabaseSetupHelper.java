package com.publiss.whitelabel;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.publiss.core.provider.DocumentsContract;


public class DatabaseSetupHelper {
    public static Uri givenThisDocument(ContentResolver contentResolver, ContentValues contentValues) {
        return contentResolver.insert(DocumentsContract.Documents.getContentUri(), contentValues);
    }

    public static Uri givenAnExistingDocument(ContentResolver contentResolver) {
        return contentResolver.insert(DocumentsContract.Documents.getContentUri(), givenDocumentContentValues());
    }

    public Uri givenAnPaidDocument(ContentResolver contentResolver) {
        ContentValues contentValues = givenDocumentContentValues();
        contentValues.put(DocumentsContract.Documents.PAID, 1);
        return contentResolver.insert(DocumentsContract.Documents.getContentUri(), contentValues);
    }

    public Uri givenAnFeaturedDocument(ContentResolver contentResolver) {
        ContentValues contentValues = givenDocumentContentValues();
        contentValues.put(DocumentsContract.Documents.FEATURED, 1);
        return contentResolver.insert(DocumentsContract.Documents.getContentUri(), contentValues);
    }

    private static ContentValues givenDocumentContentValues() {
        return createDefaultDocumentValues();
    }

    private static ContentValues createDefaultDocumentValues() {
        ContentValues values = new ContentValues();

        values.put(DocumentsContract.Documents.NAME, "Name");
        values.put(DocumentsContract.Documents.DESCRIPTION, "Description");
        values.put(DocumentsContract.Documents.COVER_IMAGE_PATH, "www.apple.com/watch");
        values.put(DocumentsContract.Documents.PAID, Integer.valueOf(0));
        values.put(DocumentsContract.Documents.FILE_SIZE, Integer.valueOf(123));
        values.put(DocumentsContract.Documents.PDF_DOCUMENT_PATH, "www.apple.com/watch");
        values.put(DocumentsContract.Documents.PRIORITY, Integer.valueOf(123));
        values.put(DocumentsContract.Documents.UPDATED_AT, "2014-03-11T15:41:26Z");
        values.put(DocumentsContract.Documents.FEATURED, Integer.valueOf(0));
        values.put(DocumentsContract.Documents.FEATURE_IMAGE_PATH, "www.apple.com/watch");
        values.put(DocumentsContract.Documents.FEATURED_UPDATED_AT, "2014-03-11T15:41:26Z");
        values.put(DocumentsContract.Documents.SHOW_IN_KIOSK, Integer.valueOf(1));
        values.putNull(DocumentsContract.Documents.LINKED_TAG);
        values.putNull(DocumentsContract.Documents.LANGUAGE_TAG);
        values.putNull(DocumentsContract.Documents.LANGUAGE_TITLE);

        return values;
    }
}
