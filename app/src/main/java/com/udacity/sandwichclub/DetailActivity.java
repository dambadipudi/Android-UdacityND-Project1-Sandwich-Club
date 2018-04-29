package com.udacity.sandwichclub;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.databinding.ActivityDetailBinding;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    //Create a data binding instance
    ActivityDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

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

        // Added code snippet from https://stackoverflow.com/questions/24072176/picasso-image-load-callback
        // in order to handle errors in image loading
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv, new com.squareup.picasso.Callback() {

                    TextView errorMessageTextView = findViewById(R.id.image_error_tv);

                    @Override
                    public void onSuccess() {
                        errorMessageTextView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        errorMessageTextView.setVisibility(View.VISIBLE);
                    }
                });

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
        StringBuilder alsoKnownAsString = new StringBuilder();
        for(String curAlsoKnownAs : sandwich.getAlsoKnownAs()) {
            if(alsoKnownAsString.length() != 0) {
                alsoKnownAsString.append("\n");
            }
            alsoKnownAsString.append(curAlsoKnownAs);
        }
        mBinding.alsoKnownTv.setText(alsoKnownAsString);

        // Populate the Place of Origin information
        mBinding.originTv.setText(sandwich.getPlaceOfOrigin());

        // Populate the Ingredients List information
        StringBuilder ingredientsString = new StringBuilder();
        for(String ingredientString : sandwich.getIngredients()) {
            if(ingredientsString.length() != 0) {
                ingredientsString.append("\n");
            }
            ingredientsString.append(ingredientString);
        }
        mBinding.ingredientsTv.setText(ingredientsString);

        // Populate the Description information
        mBinding.descriptionTv.setText(sandwich.getDescription());
    }

    /**
     * This method is called when the Also Known As label is clicked
     */
    public void onAlsoKnownLabelClicked(View view) {
        toggleFields(view,
                    mBinding.alsoKnownAsDownArrow,
                    mBinding.alsoKnownAsUpArrow,
                    mBinding.alsoKnownTv);
    }

    /**
     * This method is called when the Place of Origin label is clicked
     */
    public void onOriginClicked(View view) {
        toggleFields(view,
                    mBinding.placeOfOriginDownArrow,
                    mBinding.placeOfOriginUpArrow,
                    mBinding.originTv);
    }

    /**
     * This method is called when the Ingredients label is clicked
     */
    public void onIngredientsClicked(View view) {
        toggleFields(view,
                    mBinding.ingredientsDownArrow,
                    mBinding.ingredientsUpArrow,
                    mBinding.ingredientsTv);
    }

    /**
     * This method is called when the Description label is clicked
     */
    public void onDescriptionClicked(View view) {
        toggleFields(view,
                    mBinding.descriptionDownArrow,
                    mBinding.descriptionUpArrow,
                    mBinding.descriptionTv);
    }

    /**
     * Method to expand and collapse the information field
     */
    public void toggleFields(View view, ImageView down_arrow, ImageView up_arrow, TextView informationTextView) {
        if(view.getTag().equals("down")) {
            down_arrow.setVisibility(View.VISIBLE);
            up_arrow.setVisibility(View.INVISIBLE);
            informationTextView.setVisibility(View.GONE);
            view.setTag("up");
        }
        else {
            down_arrow.setVisibility(View.INVISIBLE);
            up_arrow.setVisibility(View.VISIBLE);
            informationTextView.setVisibility(View.VISIBLE);
            view.setTag("down");
        }
    }
}
