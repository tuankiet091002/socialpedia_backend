package com.java.java_proj.config.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonComponent
public class SortDeserializer extends JsonDeserializer<Sort> {
    private static final String SORT_PROPERTY = "property";
    private static final String SORT_DIRECTION = "direction";

    @Override
    public Sort deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        // manual Sort
        List<Sort.Order> orderList = new ArrayList<>();
        for (final JsonNode orderNode : node) {
            if (Objects.equals(orderNode.get(SORT_DIRECTION).asText(), "ASC")) {
                orderList.add(new Sort.Order(Sort.Direction.ASC, orderNode.get(SORT_PROPERTY).asText()));
            } else {
                orderList.add(new Sort.Order(Sort.Direction.DESC, orderNode.get(SORT_PROPERTY).asText()));
            }
        }

        return Sort.by(orderList);
    }
}
