package com.java.java_proj.util.serialization;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class PageSerializationModule extends SimpleModule {

    public PageSerializationModule() {
        addSerializer(PageImpl.class, new PageSerializer());
        addDeserializer(PageImpl.class, new PageDeserializer());
    }
}
