package com.java.java_proj.config.serialization;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

public class PageSerializationModule extends SimpleModule {

    public PageSerializationModule() {
        addSerializer(PageImpl.class, new PageImplSerializer());
        addDeserializer(PageImpl.class, new PageImplDeserializer());
        addSerializer(Sort.class, new SortSerializer());
        addDeserializer(Sort.class, new SortDeserializer());
    }
}
