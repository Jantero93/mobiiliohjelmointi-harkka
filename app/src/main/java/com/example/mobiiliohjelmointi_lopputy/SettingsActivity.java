package com.example.mobiiliohjelmointi_lopputy;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private String m_selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /* Click on select category header creates radioinput list dialog */
        TextView selectCategory_TextView = (TextView)findViewById(R.id.selectCategory_TextView);
        selectCategory_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectQuizCategory_Dialog();
            }
        });


    }

    /* Select right category for API call */
    private void selectQuizCategory_Dialog() {
        // get from API
        String[] items = {"Cate", "test", "moro!"};

        AlertDialog.Builder selectCategory_Dialog = new AlertDialog.Builder(this );
        selectCategory_Dialog.setTitle("Choose category");

        selectCategory_Dialog.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selectedItem) {
                m_selectedCategory = items[selectedItem];
               // Toast.makeText( SettingsActivity.this, m_selectedCategory, Toast.LENGTH_LONG).show();
            }
        });

        selectCategory_Dialog.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        selectCategory_Dialog.show();
    }
}