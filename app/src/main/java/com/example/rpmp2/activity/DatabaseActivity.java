package com.example.rpmp2.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpmp2.R;
import com.example.rpmp2.adapter.WheatAdapter;
import com.example.rpmp2.database.DatabaseManager;
import com.example.rpmp2.database.model.Wheat;

import java.util.List;

public class DatabaseActivity extends AppCompatActivity
{

    private EditText yearEditText;
    private EditText productionEditText;
    private EditText priceEditText;
    private Button addButton;
    private RecyclerView recyclerView;
    private CheckBox selectionCheckBox;
    private TextView selectionValueTextView;
    private DatabaseManager databaseManager;
    private WheatAdapter wheatAdapter = new WheatAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheats);

        this.databaseManager = new DatabaseManager(this);
        this.yearEditText = findViewById(R.id.database_year_edit_text);
        this.productionEditText = findViewById(R.id.database_production_edit_text);
        this.priceEditText = findViewById(R.id.database_price_edit_text);
        this.addButton = findViewById(R.id.database_add_button);
        this.recyclerView = findViewById(R.id.database_recycler_view);
        this.selectionCheckBox = findViewById(R.id.database_selection_check_box);
        this.selectionValueTextView = findViewById(R.id.database_selection_value_text_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());

        this.productionEditText.setHint(getString(R.string.production_hint));
        this.priceEditText.setHint(getString(R.string.price_hint));
        this.selectionCheckBox.setText(String.format(getString(R.string.database_selection_text), DatabaseManager.SELECTION_WHEAT_PRODUCTION));
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.addItemDecoration(itemDecoration);
        this.recyclerView.setAdapter(this.wheatAdapter);
        this.wheatAdapter.setOnItemClickListener(id -> new AlertDialog.Builder(this)
                .setMessage(getString(R.string.database_delete_text))
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> new Thread(() ->
                {
                    this.databaseManager.deleteWheat(id);
                    updateList();
                }).start()).show());

        this.addButton.setOnClickListener(view ->
        {
            String yearString = this.yearEditText.getText().toString();
            String productionString = this.productionEditText.getText().toString();
            String priceString = this.priceEditText.getText().toString();

            if (yearString.isEmpty())
            {
                this.yearEditText.setError(getString(R.string.empty_row_error));
                return;
            }

            if (productionString.isEmpty())
            {
                this.productionEditText.setError(getString(R.string.empty_row_error));
                return;
            }

            if (priceString.isEmpty())
            {
                this.priceEditText.setError(getString(R.string.empty_row_error));
                return;
            }

            int year, production, price;

            try
            {
                year = Integer.parseInt(yearString);
            }
            catch (NumberFormatException e)
            {
                this.yearEditText.setError(getString(R.string.positive_int_error));
                return;
            }

            try
            {
                production = Integer.parseInt(productionString);
            }
            catch (NumberFormatException e)
            {
                this.yearEditText.setError(getString(R.string.positive_int_error));
                return;
            }

            try
            {
                price = Integer.parseInt(priceString);
            }
            catch (NumberFormatException e)
            {
                this.yearEditText.setError(getString(R.string.positive_int_error));
                return;
            }

            if (year == 0)
            {
                this.yearEditText.setError(getString(R.string.non_zero_int_error));
                return;
            }

            if (production < 0)
            {
                this.productionEditText.setError(getString(R.string.positive_int_error));
                return;
            }

            if (price < 0)
            {
                this.priceEditText.setError(getString(R.string.positive_int_error));
                return;
            }

            new Thread(() ->
            {
                this.databaseManager.insertWheat(new Wheat(year, production, price));
                updateList();
                this.yearEditText.setText("");
                this.productionEditText.setText("");
                this.priceEditText.setText("");
            }).start();
        });

        this.selectionCheckBox.setOnCheckedChangeListener((compoundButton, b) -> new Thread(() -> updateList()).start());

        new Thread(() -> updateList()).start();
    }

    private void updateList()
    {
        List<Wheat> wheats;

        if (this.selectionCheckBox.isChecked())
            wheats = this.databaseManager.getSelectionWheat();
        else
            wheats = this.databaseManager.getAllWheat();

        runOnUiThread(() ->
        {
            this.wheatAdapter.setWheats(wheats);
            setSelectionValue(this.databaseManager.getSelectionWheatValue());
        });
    }

    private void setSelectionValue(int selectionValue)
    {
        this.selectionValueTextView.setText(String.format(getString(R.string.database_selection_value_text), selectionValue));
    }
}