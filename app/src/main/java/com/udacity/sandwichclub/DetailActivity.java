package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Added code to populate all the fields in the layout from the sandwich object
     *
     * @param sandwich
     */

    private void populateUI(Sandwich sandwich) {
        // Populate the Also Known As information
        TextView alsoKnownAsTextView = (TextView) findViewById(R.id.also_known_tv);
        StringBuilder alsoKnownAsString = new StringBuilder();
        for(String curAlsoKnownAs : sandwich.getAlsoKnownAs()) {
            if(alsoKnownAsString.length() != 0) {
                alsoKnownAsString.append("\n");
            }
            alsoKnownAsString.append(curAlsoKnownAs);
        }
        alsoKnownAsTextView.setText(alsoKnownAsString);

        // Populate the Place of Origin information
        TextView placeOfOriginTextView = (TextView) findViewById(R.id.origin_tv);
        placeOfOriginTextView.setText(sandwich.getPlaceOfOrigin());

        // Populate the Ingredients List information
        TextView ingredientsTextView = (TextView) findViewById(R.id.ingredients_tv);
        StringBuilder ingredientsString = new StringBuilder();
        for(String ingredientString : sandwich.getIngredients()) {
            if(ingredientsString.length() != 0) {
                ingredientsString.append("\n");
            }
            ingredientsString.append(ingredientString);
        }
        ingredientsTextView.setText(ingredientsString);

        // Populate the Description information
        TextView descriptionTextView = (TextView) findViewById(R.id.description_tv);
        descriptionTextView.setText(sandwich.getDescription());
    }
}
