package com.java.java_proj.util.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonComponent
public class PageDeserializer extends JsonDeserializer<PageImpl<?>> {
    private static final String CONTENT = "content";
    private static final String NUMBER = "number";
    private static final String SIZE = "size";
    private static final String TOTAL_ELEMENTS = "totalElements";
    private static final String SORT = "sort";
    private static final String SORT_PROPERTY = "property";
    private static final String SORT_DIRECTION = "direction";

    @Override
    public PageImpl<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        // int value
        int pageNumber = node.get(NUMBER).asInt();
        int pageSize = node.get(SIZE).asInt();
        int total = node.get(TOTAL_ELEMENTS).asInt();

        // automatic list of generic content
        TypeReference<List<?>> typeReferenceList = new TypeReference<List<?>>() {
        };
        List<?> content = new ObjectMapper().readValue(node.get(CONTENT).traverse(), typeReferenceList);

        // manual Sort
        List<Sort.Order> orderList = new ArrayList<>();
        for (final JsonNode orderNode : node.get(SORT)) {
            if (Objects.equals(orderNode.get(SORT_DIRECTION).asText(), "ASC")) {
                orderList.add(new Sort.Order(Sort.Direction.ASC, orderNode.get(SORT_PROPERTY).asText()));
            } else {
                orderList.add(new Sort.Order(Sort.Direction.DESC, orderNode.get(SORT_PROPERTY).asText()));
            }
        }

        return new PageImpl<>((List<?>) content.get(1), PageRequest.of(pageNumber, pageSize, Sort.by(orderList)), total);
    }

}
