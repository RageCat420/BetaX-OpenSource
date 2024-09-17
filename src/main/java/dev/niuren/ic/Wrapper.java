package dev.niuren.ic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public interface Wrapper {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
}
