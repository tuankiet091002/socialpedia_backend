package com.java.java_proj.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.core.env.Environment;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;
import java.util.Objects;

@Converter
public class AttributeEncryptor implements AttributeConverter<String, String> {
    private static final String AES = "AES";
    private static final String CIPHER_MODE = "AES/CBC/PKCS5Padding";
    private static final String KEY_PATH = "spring.security.secret-key";

    private final Key key;
    private final Cipher cipher;

    public AttributeEncryptor(Environment environment) throws Exception {
        key = new SecretKeySpec(Objects.requireNonNull(environment.getProperty(KEY_PATH)).getBytes(), AES);
        cipher = Cipher.getInstance(CIPHER_MODE);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalStateException(e);
        }
    }
}
