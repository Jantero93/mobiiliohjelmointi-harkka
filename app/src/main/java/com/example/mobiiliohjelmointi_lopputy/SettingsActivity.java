package com.example.mobiiliohjelmointi_lopputy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    private int m_amount = 1;
    private String m_category = "";
    private String m_difficulty = "Any Difficulty";
    private String m_type = "Any Type";
    private HashMap<String, Integer> m_all_categories;
    private String generatedLink = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // get passed values from main activity
        Intent intent = getIntent();
        m_all_categories =(HashMap<String, Integer>)intent.getSerializableExtra("ALL_CATEGORIES_HASHMAP");

    //    Button saveButton = (Button)findViewById(R.id.save_Button);
     //   saveButton.setEnabled(false);


        // Only run once on start up, initializes all needed functions
        initializeAllClickListenersOnStart();
    }

    //generate api link, make api call and return to main menu
    public void saveSettings(View view) {

        // order for api link question base link, amount, category id, difficulty, type (boolean / multiple)
        final String apiLink = "https://opentdb.com/api.php?";
        // get amount from text edit
        EditText numInput = (EditText)findViewById(R.id.numberOfQuestions_editNumber);
        String linkAmount = "amount=" + numInput.getText();

        // Play with all categories if input not selected
        String linkCategoryId = "";
        if (m_all_categories.containsKey(m_category))
            linkCategoryId = "&category=" + m_all_categories.get(m_category).toString();


        String linkDiff = "";
        // difficulty on right format
        switch (m_difficulty)
        {
            case "Any Difficulty":
                linkDiff = "";
                break;
            case "Easy":
                linkDiff = "&difficulty=easy";
                break;
            case "Medium":
                linkDiff = "&difficulty=medium";
                break;
            case "Hard":
                linkDiff = "&difficulty=hard";
                break;
            default: linkDiff = "";
                break;
        }

        // multiple questions / true & false on right format
        String linkType = "";
        switch (m_type)
        {
            case "Any Type":
                linkType = "";
                break;
            case "Multiple Choice":
                linkType = "&type=multiple";
                break;
            case "True / False":
                linkType = "&type=boolean";
                break;
            default:
                linkType = "";
                break;
        }

        // generate api link
        String gameLink = apiLink + linkAmount + linkCategoryId + linkDiff + linkType;

        if (validInput()) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("GAME_LINK", gameLink);
            setResult(RESULT_OK, resultIntent);
            Toast.makeText(this,gameLink,Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            numInput.setError("Must be 1 - 50");
            Toast.makeText(this, "ERROR INVALID INPUT", Toast.LENGTH_LONG).show();
        }



    }



    /* Select right category for API call */
    private void selectQuizCategory_Dialog() {
        // test data (get list from API)
       // String[] test = m_api.getAllCategorysAPI();
        String[] items = parseAllCategoriesFromHashMap();

        AlertDialog.Builder selectCategory_Dialog = new AlertDialog.Builder(this );
        selectCategory_Dialog.setTitle("Choose category");

        // set input checked on item which is selected/default
        int checkedItem = Arrays.asList(items).indexOf(m_category);
        selectCategory_Dialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selectedItem) {
                // set selected item on TextView under header
                m_category = items[selectedItem];
                TextView selectedItem_TextView = (TextView)findViewById(R.id.selectedItemCate_TextView);
                selectedItem_TextView.setText(m_category);
               // Toast.makeText( SettingsActivity.this, m_selectedCategory, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        selectCategory_Dialog.show();
    }

    private String[] parseAllCategoriesFromHashMap() {
        ArrayList<String> items = new ArrayList<String>();
        for (String key : m_all_categories.keySet()) {
            items.add(key);
        }
        return items.toArray(new String[items.size()]);
    }

    private void selectDifficultyDialog() {
        String[] items = {"Any Difficulty", "Easy", "Medium", "Hard"};

        AlertDialog.Builder inputDialog = new AlertDialog.Builder(this );
        inputDialog.setTitle("Choose difficulty");
        // set input checked on item which is selected/default
        int checkedItem = Arrays.asList(items).indexOf(m_difficulty);

        inputDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selectedItem) {
                // set selected item on TextView under header
                m_difficulty = items[selectedItem];
                TextView selectedDifficulty_TextView = (TextView)findViewById(R.id.selectedDifficultyItem_TextView);
                selectedDifficulty_TextView.setText(m_difficulty);
                // Toast.makeText( SettingsActivity.this, m_selectedCategory, Toast.LENGTH_LONG).show();

                dialog.dismiss();
            }
        });

        inputDialog.show();
    }

    private void selectTypeDialog() {
       String[] items = {"Any Type", "Multiple Choice", "True / False"};

        AlertDialog.Builder inputDialog = new AlertDialog.Builder( this );
        inputDialog.setTitle("Choose Type");

        // set checked item which was last time selected/default
        int checkedItem = Arrays.asList(items).indexOf(m_type);

        inputDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selectedItem) {
            // set selected item on TextView under header
            m_type = items[selectedItem];
            TextView selectedType_TextView = (TextView)findViewById(R.id.selectedTypeItem_TextView);
            selectedType_TextView.setText(m_type);
            dialog.dismiss();
            }
        });

        inputDialog.show();
    }

    private void initializeAllClickListenersOnStart() {
        /* Click on select category header creates radio input list dialog */
        TextView selectCategory_TextView = (TextView)findViewById(R.id.selectCategory_TextView);
        selectCategory_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectQuizCategory_Dialog();
            }
        });

        /* Click on selected category item creates radio input list dialog */
        TextView selectedCategoryItem_TextView = (TextView)findViewById(R.id.selectedItemCate_TextView);
        selectedCategoryItem_TextView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectQuizCategory_Dialog();
            }
        });

        /* Click on select difficulty header creates input dialog */
        TextView selectDifficultyHeader_TextView = (TextView)findViewById(R.id.selectDifficultyHead_TextView);
        selectDifficultyHeader_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDifficultyDialog();
            }
        });

        /* Click on selected difficulty item creates input dialog */
        TextView selectedDifficultyItem = (TextView)findViewById(R.id.selectedDifficultyItem_TextView);
        selectedDifficultyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDifficultyDialog();
            }
        });

        /* Click on type header creates input dialog */
        TextView selectTypeHead = (TextView)findViewById(R.id.selectTypeHead_TextView);
        selectTypeHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTypeDialog();
            }
        });

        /* Click on selected type item creates input dialog */
        TextView selectedTypeItem = (TextView)findViewById(R.id.selectedTypeItem_TextView);
        selectedTypeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTypeDialog();
            }
        });
    }

    private boolean validInput(){
        EditText numInput_EditText = (EditText)findViewById(R.id.numberOfQuestions_editNumber);

        if (numInput_EditText.getText().toString().isEmpty())
            return false;

        String inputSTRING = numInput_EditText.getText().toString();
        int input = Integer.parseInt(inputSTRING);
        return (input >= 1 && input <= 50);
    }


}