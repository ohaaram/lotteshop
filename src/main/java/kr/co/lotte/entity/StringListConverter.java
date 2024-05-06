package kr.co.lotte.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StringListConverter implements AttributeConverter<List<Review>, String> {

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);


    @Override
    public String convertToDatabaseColumn(List<Review> attribute) {
        try {
            if (attribute == null) {
                return ""; // 또는 다른 적절한 기본값으로 변경할 수 있습니다.
            }
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<Review> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return new ArrayList<>();
        }
        try {
            // JSON from String to Object
            return mapper.readValue(dbData, new com.fasterxml.jackson.core.type.TypeReference<List<Review>>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }
}



