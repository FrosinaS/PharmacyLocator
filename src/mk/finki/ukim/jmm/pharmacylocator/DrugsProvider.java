package mk.finki.ukim.jmm.pharmacylocator;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class DrugsProvider extends ContentProvider{

		
	   static final String PROVIDER_NAME = "mk.finki.ukim.jmm.provider.pharmacy";
	   static final String URL = "content://" + PROVIDER_NAME + "/drugs";
	   static final Uri CONTENT_URI = Uri.parse(URL);

	   	public static final String KEY_ID            	= "drugId";
		public static final String KEY_DRUGNAME         = "drugName";
		public static final String KEY_DRUGLATINNAME     = "drugLatinName";
		public static final String KEY_DRUGDOSE  		= "drugDose";
		public static final String KEY_DRUGDETAILS  	= "drugDetails";
		

	   private static HashMap<String, String> DRUGS_PROJECTION_MAP;

	   static final int DRUGS = 1;
	   static final int DRUG_ID = 2;
	   
	   
	   static final UriMatcher uriMatcher;
	   static{
	      uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	      uriMatcher.addURI(PROVIDER_NAME, "drugs", DRUGS);
	      uriMatcher.addURI(PROVIDER_NAME, "drugs/#", DRUG_ID);
	   }
	   
	   private SQLiteDatabase db;
	   static final String DATABASE_NAME = "drugs";
	   static final String DRUGS_TABLE_NAME = "drugsTable";
	   static final int DATABASE_VERSION = 1;
	   static final String CREATE_DB_TABLE="CREATE TABLE drugsTable( drugId INTEGER PRIMARY KEY AUTOINCREMENT," +
				"drugName TEXT, drugLatinName TEXT, " +
				"drugDose TEXT, drugDetails TEXT) ";
	   
	   private static class DatabaseHelper extends SQLiteOpenHelper {
	       DatabaseHelper(Context context){
	          super(context, DATABASE_NAME, null, DATABASE_VERSION);
	       }

	       @Override
	       public void onCreate(SQLiteDatabase db)
	       {
	          db.execSQL(CREATE_DB_TABLE);
	       }
	       
	       @Override
	       public void onUpgrade(SQLiteDatabase db, int oldVersion, 
	                             int newVersion) {
	          db.execSQL("DROP TABLE IF EXISTS " +  DRUGS_TABLE_NAME);
	          onCreate(db);
	       }
	   }
	   
	@Override
	public boolean onCreate() {
		
		
		  Context context = getContext();
	      DatabaseHelper dbHelper = new DatabaseHelper(context);
	      db = dbHelper.getWritableDatabase();
	      db.delete(DRUGS_TABLE_NAME, null, null);
	      context.startService(new Intent(context, FillProviderService.class));
	      return (db == null)? false:true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	      qb.setTables(DRUGS_TABLE_NAME);
	      
	      switch (uriMatcher.match(uri)) {
	      case DRUGS:
	         qb.setProjectionMap(DRUGS_PROJECTION_MAP);
	         break;
	      case DRUG_ID:
	         qb.appendWhere( KEY_ID + "=" + uri.getPathSegments().get(1));
	         break;
	      default:
	         throw new IllegalArgumentException("Unknown URI " + uri);
	      }
	      if (sortOrder == null || sortOrder == ""){
	         /** 
	          * By default sort on student names
	          */
	         sortOrder = KEY_DRUGNAME;
	      }
	      Cursor c = qb.query(db,	projection,	selection, selectionArgs, 
	                          null, null, sortOrder);
	      /** 
	       * register to watch a content URI for changes
	       */
	      c.setNotificationUri(getContext().getContentResolver(), uri);

	      return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)){
	      /**
	       * Get all student records 
	       */
	      case DRUGS:
	         return "vnd.android.cursor.dir/vnd.pharmacy.drugs";
	      /** 
	       * Get a particular student
	       */
	      case DRUG_ID:
	         return "vnd.android.cursor.item/vnd.pharmacy.drugs";
	      default:
	         throw new IllegalArgumentException("Unsupported URI: " + uri);
	      }
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		  /**
	       * Add a new student record
	       */
	      long rowID = db.insert(	DRUGS_TABLE_NAME, "", values);
	      /** 
	       * If record is added successfully
	       */
	      if (rowID > 0)
	      {
	         Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
	         getContext().getContentResolver().notifyChange(_uri, null);
	         return _uri;
	      }
	      throw new SQLException("Failed to add a record into " + uri);

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;

	      switch (uriMatcher.match(uri)){
	      case DRUGS:
	         count = db.delete(DRUGS_TABLE_NAME, selection, selectionArgs);
	         break;
	      case DRUG_ID:
	         String id = uri.getPathSegments().get(1);
	         count = db.delete( DRUGS_TABLE_NAME, KEY_ID +  " = " + id + 
	                (!TextUtils.isEmpty(selection) ? " AND (" + 
	                selection + ')' : ""), selectionArgs);
	         break;
	      default: 
	         throw new IllegalArgumentException("Unknown URI " + uri);
	      }
	      
	      getContext().getContentResolver().notifyChange(uri, null);
	      return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
	      
	      switch (uriMatcher.match(uri)){
	      case DRUGS:
	         count = db.update(DRUGS_TABLE_NAME, values, 
	                 selection, selectionArgs);
	         break;
	      case DRUG_ID:
	         count = db.update(DRUGS_TABLE_NAME, values, KEY_ID + 
	                 " = " + uri.getPathSegments().get(1) + 
	                 (!TextUtils.isEmpty(selection) ? " AND (" +
	                 selection + ')' : ""), selectionArgs);
	         break;
	      default: 
	         throw new IllegalArgumentException("Unknown URI " + uri );
	      }
	      getContext().getContentResolver().notifyChange(uri, null);
	      return count;
	}


}
