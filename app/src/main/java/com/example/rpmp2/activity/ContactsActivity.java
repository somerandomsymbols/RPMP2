package com.example.rpmp2.activity;

import android.os.Bundle;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpmp2.R;
import com.example.rpmp2.adapter.ContactsAdapter;
import com.example.rpmp2.database.DatabaseManager;
import com.example.rpmp2.database.model.Contact;

import java.util.List;

public class ContactsActivity extends AppCompatActivity
{
    private CheckBox selectionContactsCheckBox;
    private RecyclerView contactsRecyclerView;
    private DatabaseManager databaseManager;
    private ContactsAdapter contactsAdapter = new ContactsAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        this.databaseManager = new DatabaseManager(this);
        this.selectionContactsCheckBox = findViewById(R.id.selection_contacts_check_box);
        this.contactsRecyclerView = findViewById(R.id.contacts_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());

        this.selectionContactsCheckBox.setText(String.format(getString(R.string.selection_contacts_text), DatabaseManager.SELECTION_CONTACT_ENDING));
        this.contactsRecyclerView.setLayoutManager(layoutManager);
        this.contactsRecyclerView.addItemDecoration(itemDecoration);
        this.contactsRecyclerView.setAdapter(contactsAdapter);
        this.selectionContactsCheckBox.setOnCheckedChangeListener((compoundButton, b) -> new Thread(() -> updateList()).start());

        new Thread(() -> updateList()).start();
    }

    private void updateList()
    {
        List<Contact> contacts;

        if (this.selectionContactsCheckBox.isChecked())
            contacts = this.databaseManager.getContactsWithName();
        else
            contacts = this.databaseManager.getAllContacts();

        runOnUiThread(() -> this.contactsAdapter.setContacts(contacts));
    }
}