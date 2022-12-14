package com.example.rpmp2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpmp2.R;
import com.example.rpmp2.database.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>
{
    private List<Contact> contacts = new ArrayList<>();

    public void setContacts(List<Contact> contacts)
    {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position)
    {
        holder.setContactInfo(contacts.get(position));
    }

    @Override
    public int getItemCount()
    {
        return contacts.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder
    {
        TextView contactNameTextView, phoneNumberTextView, addressTextView;

        public ContactViewHolder(@NonNull View itemView)
        {
            super(itemView);
            contactNameTextView = itemView.findViewById(R.id.contact_name_text_view);
            phoneNumberTextView = itemView.findViewById(R.id.phone_number_text_view);
            addressTextView = itemView.findViewById(R.id.address_text_view);
        }

        public void setContactInfo(Contact contact)
        {
            contactNameTextView.setText(String.format("%s %s", contact.getFirstName(), contact.getLastName()));
            phoneNumberTextView.setText(contact.getPhoneNumber());
            addressTextView.setText(contact.getAddress());
        }
    }
}
