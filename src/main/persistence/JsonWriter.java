package persistence;

/*
This class implements a JSON writer which writes down a given TankGame into the format of a stored
JSON file.

ACKNOWLEDGEMENT:
THIS CLASS CODE WAS CREATED BASED ON THE "JsonSerializationDemo" FILE PROVIDED AS AN EXAMPLE.
I TRULY APPRECIATE THE WISDOM GAINED FROM THE ORIGINAL CODE.
    CODES WERE TAKEN FROM THE "JsonWriter" CLASS IN:
https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

LAST REVISED: 03/31/2022
*/

import model.tankgame.TankGame;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private final String destination;

    //EFFECTS: creates a JSON writer object which writes information on the destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    //MODIFIES: this
    //EFFECTS: creates a writer object which will handle the writing on the file; or throw
    // a FileNotFoundException if the file cannot be found or opened for writing purposes
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    //MODIFIES: this
    //EFFECTS: writes the information on the destination file that corresponds to the
    // given TankGame object
    public void write(TankGame tankGameToBeSaved) {
        JSONObject tankGameInJsonForm = tankGameToBeSaved.toJson();
        saveToFile(tankGameInJsonForm.toString(TAB));
    }

    //MODIFIES: this
    //EFFECTS: closes the writer object
    public void close() {
        writer.close();
    }

    //MODIFIES: this
    //EFFECTS: writes the information in the given String object directly into the file
    private void saveToFile(String jsonDataInString) {
        writer.print(jsonDataInString);
    }

}
