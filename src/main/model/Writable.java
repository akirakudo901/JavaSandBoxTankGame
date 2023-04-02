package model;

/*
This class describes a "writable" object which main characteristic is to be convert-able
into a JSON object in their specific way, to be written into a JSON file.

ACKNOWLEDGEMENT:
THIS CLASS CODE WAS CREATED BASED ON THE "JsonSerializationDemo" FILE PROVIDED AS AN EXAMPLE.
I TRULY APPRECIATE THE WISDOM GAINED FROM THE ORIGINAL CODE.
    CODES WERE TAKEN FROM THE "Writable" CLASS IN:
https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

LAST REVISED: 03/31/2022
 */

import org.json.JSONObject;

public interface Writable {

    //EFFECTS: converts this object into a JSON object
    JSONObject toJson();

}
