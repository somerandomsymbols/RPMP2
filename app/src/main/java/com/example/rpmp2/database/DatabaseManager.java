package com.example.rpmp2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.rpmp2.database.model.Contact;
import com.example.rpmp2.database.model.Wheat;
import com.example.rpmp2.database.table.ContactTable;
import com.example.rpmp2.database.table.WheatTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseManager extends SQLiteOpenHelper
{
    public static final int SELECTION_WHEAT_PRODUCTION = 25000000;
    public static final String SELECTION_CONTACT_ENDING = "а";
    private static final String QUERY_CREATE_CONTACT_TABLE =
            "CREATE TABLE " + ContactTable.TABLE_NAME + " (" +
                    ContactTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    ContactTable.COLUMN_NAME_FIRST_NAME + " TEXT," +
                    ContactTable.COLUMN_NAME_LAST_NAME + " TEXT," +
                    ContactTable.COLUMN_NAME_NUMBER + " TEXT," +
                    ContactTable.COLUMN_NAME_ADDRESS + " TEXT)";
    private static final String QUERY_CREATE_STUDENT_TABLE =
            "CREATE TABLE " + WheatTable.TABLE_NAME + " (" +
                    WheatTable.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    WheatTable.COLUMN_YEAR + " INTEGER," +
                    WheatTable.COLUMN_PRODUCTION + " INTEGER," +
                    WheatTable.COLUMN_PRICE + " INTEGER)";

    public DatabaseManager(@Nullable Context context)
    {
        super(context, "wheat.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(QUERY_CREATE_STUDENT_TABLE);
        sqLiteDatabase.execSQL(QUERY_CREATE_CONTACT_TABLE);

        Wheat[] wheats = {
                new Wheat(1992, 19507424, 30926),
                new Wheat(1993, 21831008, 37980),
                new Wheat(1994, 13856600, 30745),
                new Wheat(1995, 16273300, 29699),
                new Wheat(1996, 13547100, 22993),
                new Wheat(1997, 18403900, 28277),
                new Wheat(1998, 14936600, 26479),
                new Wheat(1999, 13585300, 22903),
                new Wheat(2000, 10197000, 19756),
                new Wheat(2001, 21348000, 31020),
                new Wheat(2002, 20556000, 30455),
                new Wheat(2003, 11430884, 29385),
                new Wheat(2004, 17520200, 31661),
                new Wheat(2005, 18699200, 28457),
                new Wheat(2006, 13947300, 25308),
                new Wheat(2007, 13937700, 23420),
                new Wheat(2008, 25885400, 36698),
                new Wheat(2009, 20886400, 30930),
                new Wheat(2010, 16851300, 26816),
                new Wheat(2011, 22323600, 33533),
                new Wheat(2012, 15762600, 27999),
                new Wheat(2013, 22279300, 33931),
                new Wheat(2014, 24113970, 40119),
                new Wheat(2015, 26532100, 38792),
                new Wheat(2016, 26098830, 42056),
                new Wheat(2017, 26208980, 41097),
                new Wheat(2018, 24652840, 37242),
                new Wheat(2019, 28370280, 41566),
                new Wheat(2020, 24912350, 37950),
        };

        for (Wheat wheat : wheats)
            this.insertWheat(wheat, sqLiteDatabase);

        Contact[] contacts = {
                new Contact("Тарас", "Мельник", "123-456", "вул. Хрещатик, 24"),
                new Contact("Віктор", "Шевченко", "789-012", "вул. Васильківська, 37"),
                new Contact("Олена", "Коваленко", "345-678", "вул. Богдана Хмельницького, 65"),
                new Contact("Богдан", "Бондаренко", "901-234", "вул. Голосіївська, 15"),
                new Contact("Володимир", "Бойко", "567-890", "вул. Тараса Шевченка, 149"),
        };

        for (Contact contact : contacts)
            insertContact(contact, sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }

    public void insertWheat(Wheat wheat, SQLiteDatabase database)
    {
        ContentValues values = new ContentValues();

        values.put(WheatTable.COLUMN_YEAR, wheat.getYear());
        values.put(WheatTable.COLUMN_PRODUCTION, wheat.getProduction());
        values.put(WheatTable.COLUMN_PRICE, wheat.getPrice());
        database.insert(WheatTable.TABLE_NAME, null, values);
    }

    public void insertWheat(Wheat wheat)
    {
        try (SQLiteDatabase database = getWritableDatabase())
        {
            this.insertWheat(wheat, database);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteWheat(int id)
    {
        try (SQLiteDatabase database = getWritableDatabase())
        {
            database.delete(WheatTable.TABLE_NAME, WheatTable.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<Wheat> getAllWheat()
    {
        String query = String.format(Locale.getDefault(), "SELECT * FROM %s", WheatTable.TABLE_NAME);

        try (
                SQLiteDatabase database = getReadableDatabase();
                Cursor cursor = database.rawQuery(query, null)
        )
        {
            ArrayList<Wheat> wheats = new ArrayList<>();

            while (cursor.moveToNext())
            {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(WheatTable.COLUMN_ID));
                int year = cursor.getInt(cursor.getColumnIndexOrThrow(WheatTable.COLUMN_YEAR));
                int production = cursor.getInt(cursor.getColumnIndexOrThrow(WheatTable.COLUMN_PRODUCTION));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(WheatTable.COLUMN_PRICE));

                Wheat wheat = new Wheat(id, year, production, price);
                wheats.add(wheat);
            }

            return wheats;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public List<Wheat> getSelectionWheat()
    {
        String query = String.format(Locale.getDefault(), "SELECT %s, %s FROM %s WHERE %s > '%d'", WheatTable.COLUMN_YEAR, WheatTable.COLUMN_PRODUCTION, WheatTable.TABLE_NAME, WheatTable.COLUMN_PRODUCTION, SELECTION_WHEAT_PRODUCTION);

        try (
                SQLiteDatabase database = getReadableDatabase();
                Cursor cursor = database.rawQuery(query, null)
        )
        {
            ArrayList<Wheat> wheats = new ArrayList<>();

            while (cursor.moveToNext())
            {
                int year = cursor.getInt(cursor.getColumnIndexOrThrow(WheatTable.COLUMN_YEAR));
                int production = cursor.getInt(cursor.getColumnIndexOrThrow(WheatTable.COLUMN_PRODUCTION));
                Wheat wheat = new Wheat(year, production);

                wheats.add(wheat);
            }

            return wheats;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public int getSelectionWheatValue()
    {
        String selectedValue = "selectedValue";
        String query = String.format(
                Locale.getDefault(), "SELECT SUM(%s) / COUNT(%s) AS %s FROM %s", WheatTable.COLUMN_PRICE, WheatTable.COLUMN_PRICE, selectedValue, WheatTable.TABLE_NAME
        );

        try (
                SQLiteDatabase database = getReadableDatabase();
                Cursor cursor = database.rawQuery(query, null)
        )
        {
            if (cursor.moveToNext())
                return cursor.getInt(cursor.getColumnIndexOrThrow(selectedValue));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    public void insertContact(Contact contact, SQLiteDatabase database)
    {
        ContentValues values = new ContentValues();

        values.put(ContactTable.COLUMN_NAME_FIRST_NAME, contact.getFirstName());
        values.put(ContactTable.COLUMN_NAME_LAST_NAME, contact.getLastName());
        values.put(ContactTable.COLUMN_NAME_NUMBER, contact.getPhoneNumber());
        values.put(ContactTable.COLUMN_NAME_ADDRESS, contact.getAddress());
        database.insert(ContactTable.TABLE_NAME, null, values);
    }

    public List<Contact> getAllContacts()
    {
        try (
                SQLiteDatabase database = getReadableDatabase();
                Cursor cursor = database.rawQuery("SELECT * FROM " + ContactTable.TABLE_NAME, null)
        )
        {
            ArrayList<Contact> contacts = new ArrayList<>();

            while (cursor.moveToNext())
            {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_LAST_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_NUMBER));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_ADDRESS));
                Contact contact = new Contact(firstName, lastName, phoneNumber, address);

                contacts.add(contact);
            }

            return contacts;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public List<Contact> getContactsWithName()
    {
        try (
                SQLiteDatabase database = getReadableDatabase();
                Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s LIKE '%%%s'", ContactTable.TABLE_NAME, ContactTable.COLUMN_NAME_FIRST_NAME, DatabaseManager.SELECTION_CONTACT_ENDING), null)
        )
        {
            ArrayList<Contact> contacts = new ArrayList<>();

            while (cursor.moveToNext())
            {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_LAST_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_NUMBER));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_ADDRESS));
                Contact contact = new Contact(firstName, lastName, phoneNumber, address);

                contacts.add(contact);
            }

            return contacts;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
