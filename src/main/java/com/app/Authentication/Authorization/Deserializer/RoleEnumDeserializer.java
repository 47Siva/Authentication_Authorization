package com.app.Authentication.Authorization.Deserializer;

import java.io.IOException;

import com.app.Authentication.Authorization.enumeration.Role;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;



public class RoleEnumDeserializer extends JsonDeserializer<Role> {

    @Override
    public Role deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = p.getText().toUpperCase();
        try {
//            return Role.valueOf(value);
        	return Role.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid role: " + value, e);
        }
    }
}
