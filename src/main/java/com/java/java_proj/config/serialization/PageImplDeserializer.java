package com.java.java_proj.config.serialization;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;

@JsonComponent
public class PageImplDeserializer extends JsonDeserializer<PageImpl<?>> {
    private static final String CONTENT = "content";
    private static final String NUMBER = "number";
    private static final String SIZE = "size";
    private static final String TOTAL_ELEMENTS = "totalElements";
    private static final String SORT = "sort";

    @Override
    public PageImpl<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        // int value
        int pageNumber = node.get(NUMBER).asInt();
        int pageSize = node.get(SIZE).asInt();
        int total = node.get(TOTAL_ELEMENTS).asInt();

        // automatic list of generic content
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new PageSerializationModule());
        objectMapper = objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);

        TypeReference<List<?>> typeReferenceList = new TypeReference<List<?>>() {
        };
        List<?> content = objectMapper.readValue(node.get(CONTENT).traverse(), typeReferenceList);

//        Sort sort = objectMapper.treeToValue(node.get(SORT), Sort.class);

        return new PageImpl<>(content, PageRequest.of(pageNumber, pageSize), total);
    }

}
