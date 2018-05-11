package cn.jlhd.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;



public class JsonUtil {
    public static String objToJSONString(Object obj) {
        if (obj == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("");
            }
        });
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonNode getJsonNode(String jsonStr) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
        JsonNode node = null;
		try {
			node = mapper.readTree(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return node;
    }

    public static ObjectNode getJsonNode(Object obj) {
        if (obj == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(obj);
    }


    public static ObjectNode createEmptyObjectNode() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createObjectNode();
    }
    public static  String findNode(String key, JsonNode node) {
        try {
            return node.get(key).asText();
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return null;
    }
}
