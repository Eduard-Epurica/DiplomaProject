package com.example.ver1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String TEMPERATURE_TABLE = "TEMPERATURE_TABLE";
    public static final String TEMPERATURE_VALUE = "TEMPERATURE_VALUE";
    public static final String CO_VALUE = "CO_VALUE";
    public static final String CO_TABLE = "CO_TABLE";
    public static final String CO2_VALUE = "CO2_VALUE";
    public static final String CO2_TABLE = "CO2_TABLE";
    public static final String LPG_TABLE = "LPG_TABLE";
    public static final String LPG_VALUE = "LPG_VALUE";
    public static final String TIME_VALUE = "TIME_VALUE";
    public static final String HUMIDITY_TABLE = "HUMIDITY_TABLE";
    public static final String HUMIDITY_VALUE = "HUMIDITY_VALUE";
    public static final String TEMPERATURE_ID = "ID";
    private static final String TAG = "EMQTT";
    public static final String HUMIDITY_ID = "ID";
    public static final String CO_ID = "ID";
    public static final String CO2_ID = "ID";
    public static final String LPG_ID = "ID";



    //creating constructors

    public DataBaseHelper(@Nullable Context context) {
        super(context, "temperature.db", null, 1);
    }

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //onCreate called first time we access the database object. Here is the code we use to generate a new table
    @Override
    public void onCreate(SQLiteDatabase db) {


        String createTableStatement = "CREATE TABLE " + TEMPERATURE_TABLE + " (" + TEMPERATURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TEMPERATURE_VALUE + " DOUBLE, " + TIME_VALUE + " TEXT)";
        //Creating a table statement. This is going to use SQL code with SQL commands such as "Create table".
        //The table we will create will contain 3 columns, the ID of the value, the actual value of our sensor and the timestamp of when the value was taken
        db.execSQL(createTableStatement);
        //execSQL will create our table using the table statement previously written.
        //The table creation process will now be repeated for different sensors (Humidity, Carbon Monoxide, Carbon Dioxide)
        createTableStatement = "CREATE TABLE " + HUMIDITY_TABLE + " (" + HUMIDITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + HUMIDITY_VALUE + " DOUBLE," + TIME_VALUE + " TEXT)";

        db.execSQL(createTableStatement);

        createTableStatement = "CREATE TABLE " + CO_TABLE + " (" + CO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CO_VALUE + " DOUBLE, " + TIME_VALUE + " TEXT)";

        db.execSQL(createTableStatement);

        createTableStatement = "CREATE TABLE " + CO2_TABLE + " (" + CO2_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CO2_VALUE + " DOUBLE," + TIME_VALUE + " TEXT)";

        db.execSQL(createTableStatement);

        createTableStatement = "CREATE TABLE " + LPG_TABLE + " (" + LPG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LPG_VALUE + " DOUBLE," + TIME_VALUE + " TEXT)";

        db.execSQL(createTableStatement);

    }

    //Used when the database version changes.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String createTableStatement = "CREATE TABLE " + CO2_TABLE + " (" + CO2_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CO2_VALUE + " DOUBLE, " + TIME_VALUE + " TEXT)";

        db.execSQL(createTableStatement);
    }

    public boolean addTemp(tempClass tempClass){
        //The method recevies as argument an object of type "tempClass" containing the ID,value and timestamp
        SQLiteDatabase db = this.getWritableDatabase(); //Getting the database object
        ContentValues cv = new ContentValues();  //Creating a new contentvalues object

        String createTableStatement = "CREATE TABLE " + TEMPERATURE_TABLE + " (" + TEMPERATURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TEMPERATURE_VALUE + " DOUBLE, " + TIME_VALUE + " TEXT)";
        try{
        db.execSQL(createTableStatement);}
        catch (SQLException e){}
        //Putting in the contentvalues object the temperature and timestamp
        cv.put(TEMPERATURE_VALUE,tempClass.getValue());
        cv.put(TIME_VALUE,tempClass.getTime());

        //Inserting in the temperature table the new values
        long insert = db.insert(TEMPERATURE_TABLE, TEMPERATURE_VALUE, cv);


        long count = DatabaseUtils.queryNumEntries(db, TEMPERATURE_TABLE);

        //If the insertion has been sucessful return false, otherwise return true
        if (insert ==1){

            db.close();
            return false;
        }
        else {db.close();
              return true;}

    }

    public double getLast(String tableName){

        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + tableName + " WHERE ID = (SELECT MAX(ID)  FROM " + tableName + ")";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToLast();
        double value = cursor.getDouble(1);
        return value;

    }

    public long sizeTemp(){
        SQLiteDatabase db = this.getReadableDatabase();
        //Getting the database object
        long count = DatabaseUtils.queryNumEntries(db, TEMPERATURE_TABLE);
        //Using the queryNumEntries() method to obtain the number of table entries for temperature
        db.close();
        //Closing the database as we are done with it
        return count;
        //Returning the number of entries
    }

    public boolean addHum(tempClass tempClass){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String createTableStatement = "CREATE TABLE " + HUMIDITY_TABLE + " (" + HUMIDITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + HUMIDITY_VALUE + " DOUBLE," + TIME_VALUE + " TEXT)";
        try{
            db.execSQL(createTableStatement);}
        catch (SQLException e){}
        cv.put(HUMIDITY_VALUE,tempClass.getValue());
        cv.put(TIME_VALUE,tempClass.getTime());

        long insert = db.insert(HUMIDITY_TABLE, HUMIDITY_VALUE, cv);

        if (insert ==1){
            return false;
        }
        else return true;

    }

    public boolean addCO(tempClass tempClass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues cv = new ContentValues();
        String createTableStatement = "CREATE TABLE " + CO_TABLE + " (" + CO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CO_VALUE + " DOUBLE, " + TIME_VALUE + " TEXT)";

        try{
            db.execSQL(createTableStatement);}
        catch (SQLException e){}
        cv.put(CO_VALUE,tempClass.getValue());
        cv.put(TIME_VALUE,tempClass.getTime());

        long insert = db.insert(CO_TABLE, CO_VALUE, cv);

        if (insert ==1){
            return false;
        }
        else return true;
    }


    public boolean addLPG(tempClass tempClass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LPG_VALUE,tempClass.getValue());
        cv.put(TIME_VALUE,tempClass.getTime());
        String createTableStatement = "CREATE TABLE " + LPG_TABLE + " (" + LPG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LPG_VALUE + " DOUBLE," + TIME_VALUE + " TEXT)";

        try{
            db.execSQL(createTableStatement);}
        catch (SQLException e){}
        long insert = db.insert(LPG_TABLE, LPG_VALUE, cv);
        if (insert ==1){
            return false;
        }
        else return true;
    }


    public boolean addCO2(tempClass tempClass)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String createTableStatement = "CREATE TABLE " + CO2_TABLE + " (" + CO2_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CO2_VALUE + " DOUBLE," + TIME_VALUE + " TEXT)";

        try{
            db.execSQL(createTableStatement);}
        catch (SQLException e){}
        cv.put(CO2_VALUE,tempClass.getValue());
        cv.put(TIME_VALUE,tempClass.getTime());

        long insert = db.insert(CO2_TABLE, CO2_VALUE, cv);

        if (insert ==1){
            return false;
        }
        else return true;
    }

    public void deleteExcess()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //Getting the database object
        Cursor cursor = db.query(TEMPERATURE_TABLE,null,null,null,null,null,null);
       //Creating a cursor for the Temperature table
        cursor.moveToLast();
        //Moving the cursor to the last row of the table
        long p = cursor.getLong(cursor.getColumnIndex(TEMPERATURE_ID)) - 200;
        //Selecting the row that denotes the 200th most recent value.
        String rowID = String.valueOf(p);
        //Saving the row ID in a string
        Log.i(TAG,rowID);
        db.delete(TEMPERATURE_TABLE,TEMPERATURE_ID+ "<=" + rowID,null);
        //Deleting all values older than the 200th most recent value
        db.close();
        //Closing the database
    }

    public void deleteExcessCO2()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(CO2_TABLE,null,null,null,null,null,null);
        cursor.moveToLast();
        long p = cursor.getLong(cursor.getColumnIndex(CO2_ID)) - 1400;
        String rowID = String.valueOf(p);
        Log.i(TAG,rowID);
        db.delete(CO2_TABLE,CO2_ID+ "<=" + rowID,null);
        db.close();
    }

    public void deleteExcessCO()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(CO_TABLE,null,null,null,null,null,null);
        cursor.moveToLast();
        long p = cursor.getLong(cursor.getColumnIndex(CO_ID)) - 1400;
        String rowID = String.valueOf(p);
        Log.i(TAG,rowID);
        db.delete(CO_TABLE,CO_ID+ "<=" + rowID,null);
        db.close();
    }

    public void deleteExcessLPG()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(LPG_TABLE,null,null,null,null,null,null);
        cursor.moveToLast();
        long p = cursor.getLong(cursor.getColumnIndex(LPG_ID)) - 1400;
        String rowID = String.valueOf(p);
        Log.i(TAG,rowID);
        db.delete(LPG_TABLE,LPG_ID+ "<=" + rowID,null);
        db.close();
    }

    public void deleteExcessHum()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(HUMIDITY_TABLE,null,null,null,null,null,null);
        cursor.moveToLast();
        long p = cursor.getLong(cursor.getColumnIndexOrThrow(HUMIDITY_ID)) - 200;
        String rowID = String.valueOf(p);
        Log.i(TAG,"Hum id " +rowID);
        db.delete(HUMIDITY_TABLE,HUMIDITY_ID+ "<=" + rowID,null);
        db.close();
    }

    public boolean deleteOne(tempClass tempClass){
        //delete a temperature and delete a true statement
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + TEMPERATURE_TABLE + "WHERE " + TEMPERATURE_ID + "= " + tempClass.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        return cursor.moveToFirst();


    }

    public List<tempClass> getAllTemp()
    {
        List<tempClass> returnList = new ArrayList<>();
        //Creating an arrayList made out of tempClass type objects
        String queryString = "SELECT * FROM " + TEMPERATURE_TABLE;
        //Getting data from the database
        SQLiteDatabase db = this.getReadableDatabase();
        //Getting the database object
        Cursor cursor = db.rawQuery(queryString,null);
        //Creating cursor for the database
        if(cursor.moveToFirst()){
            //loop thorugh the cursor(result set) and create new tempClass object for each row
            do {
                int temperatureID = cursor.getInt(0);
                double temperatureValue = cursor.getDouble(1);
                String timestamp = cursor.getString(2);

                tempClass newTemp = new tempClass(temperatureID,temperatureValue,timestamp);
                returnList.add(newTemp);

            }while(cursor.moveToNext());
        }
        else
        {
                //failure so dont add to the list.
        }
        cursor.close();
        //closing the cursor
        db.close();
        //closing the database
        return returnList;
        //returning the list
    }

    public List<tempClass> getAllHum()
    {
        List<tempClass> returnList = new ArrayList<>();

        //get data from database

        String queryString = "SELECT * FROM " + HUMIDITY_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            //loop thorugh the cursor(result set) and create new customer object for each row
            do {
                int humidityID = cursor.getInt(0);
                double humidityValue = cursor.getDouble(1);
                String timestamp = cursor.getString(2);

                tempClass newHum = new tempClass(humidityID,humidityValue,timestamp);
                returnList.add(newHum);

            }while(cursor.moveToNext());
        }
        else
        {
            //failure so dont add to the list.
        }

        cursor.close();
        db.close();
        return returnList;
    }

    public List<tempClass> getAllCO2()
    {

        List<tempClass> returnList = new ArrayList<>();

        //get data from database

        String queryString = "SELECT * FROM " + CO2_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            //loop thorugh the cursor(result set) and create new customer object for each row
            do {
                int co2ID = cursor.getInt(0);
                double co2Value = cursor.getDouble(1);
                String timestamp = cursor.getString(2);

                tempClass newCO2 = new tempClass(co2ID,co2Value,timestamp);
                returnList.add(newCO2);

            }while(cursor.moveToNext());
        }
        else
        {
            //failure so dont add to the list.
        }

        cursor.close();
        db.close();
        return returnList;
    }

    public List<tempClass> getAllCO()
    {

        List<tempClass> returnList = new ArrayList<>();

        //get data from database

        String queryString = "SELECT * FROM " + CO_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            //loop thorugh the cursor(result set) and create new customer object for each row
            do {
                int coID = cursor.getInt(0);
                double coValue = cursor.getDouble(1);
                String timestamp = cursor.getString(2);

                tempClass newCO = new tempClass(coID,coValue,timestamp);
                returnList.add(newCO);

            }while(cursor.moveToNext());
        }
        else
        {
            //failure so dont add to the list.
        }

        cursor.close();
        db.close();
        return returnList;
    }

    public List<tempClass> getAllLPG()
    {

        List<tempClass> returnList = new ArrayList<>();

        //get data from database

        String queryString = "SELECT * FROM " + LPG_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            //loop thorugh the cursor(result set) and create new customer object for each row
            do {
                int coID = cursor.getInt(0);
                double coValue = cursor.getDouble(1);
                String timestamp = cursor.getString(2);

                tempClass newCO = new tempClass(coID,coValue,timestamp);
                returnList.add(newCO);

            }while(cursor.moveToNext());
        }
        else
        {
            //failure so dont add to the list.
        }

        cursor.close();
        db.close();
        return returnList;
    }


}
