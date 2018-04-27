package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {

        try {

            JSONObject sandwichJSON = new JSONObject(json);

            JSONObject nameJSON = sandwichJSON.getJSONObject("name");

            String mainName = nameJSON.getString("mainName");

            JSONArray alsoKnownAsJSONArray = nameJSON.getJSONArray("alsoKnownAs");

            List<String> alsoKnownAsList = convertJSONArrayToListOfStrings(alsoKnownAsJSONArray);

            String placeOfOrigin = sandwichJSON.getString("placeOfOrigin");

            String description = sandwichJSON.getString("description");

            String image = sandwichJSON.getString("image");

            JSONArray ingredientJSONArray = sandwichJSON.getJSONArray("ingredients");

            List<String> ingredientList = convertJSONArrayToListOfStrings(ingredientJSONArray);

            Sandwich sandwich = new Sandwich (mainName,
                                              alsoKnownAsList,
                                              placeOfOrigin,
                                              description,
                                              image,
                                              ingredientList);

            return sandwich;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> convertJSONArrayToListOfStrings(JSONArray jsonArray) throws JSONException {
        List<String> listOfStrings = new ArrayList<>();

        for(int index = 0; index < jsonArray.length(); index++) {
            listOfStrings.add(jsonArray.getString(index));
        }

        return listOfStrings;
    }
}