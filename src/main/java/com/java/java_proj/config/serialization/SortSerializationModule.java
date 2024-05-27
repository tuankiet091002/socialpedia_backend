package com.java.java_proj.config.serialization;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.data.domain.Sort;

public class SortSerializationModule extends SimpleModule {

    public SortSerializationModule() {
        addSerializer(Sort.class, new SortSerializer());
        addDeserializer(Sort.class, new SortDeserializer());
    }
}
