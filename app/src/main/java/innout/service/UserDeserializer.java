// File: innout/service/UserDeserializer.java
package innout.service;

import com.google.gson.*;
import innout.model.Admin;
import innout.model.User;
import innout.model.Users;

import java.lang.reflect.Type;

public class UserDeserializer implements JsonDeserializer<User> {

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement typeElement = jsonObject.get("typeUser");

        if (typeElement == null) {
            throw new JsonParseException("Missing 'typeUser' field in user JSON object.");
        }

        String type = typeElement.getAsString();

        switch (type) {
            case "Admin":
                return context.deserialize(jsonObject, Admin.class);
            case "User":
                return context.deserialize(jsonObject, Users.class);
            default:
                throw new JsonParseException("Unknown user type: " + type);
        }
    }
}