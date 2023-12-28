package com.java.java_proj.entities.enums;

import com.java.java_proj.exceptions.HttpException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum PermissionAccessType {
    NO_ACCESS(1),
    SELF(2),
    VIEW(3),
    SEND(4),
    MODIFY(5);

    private final Integer value;

    public static PermissionAccessType valueOf(int value) {
        return Arrays.stream(values())
                .filter(t -> t.value == value)
                .findFirst().orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Invalid enum value"));
    }

}