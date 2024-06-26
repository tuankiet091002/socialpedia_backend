package com.java.java_proj.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.core.env.Environment;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

@Converter
public class AttributeEncryptor implements AttributeConverter<String, String> {
    private static final String AES = "AES";
    private static final String CIPHER_MODE = "AES/CBC/PKCS5Padding";
    private static final String KEY_PATH = "spring.security.secret-key";
    private final Key key;
    private final IvParameterSpec iv = new IvParameterSpec("0123456789101112".getBytes());

    public AttributeEncryptor(Environment environment) {
        String secretKey = Objects.requireNonNull(environment.getProperty(KEY_PATH));
        if (secretKey.length() != 16) {
            throw new IllegalArgumentException("Key must be 16 characters long");
        }
        key = new SecretKeySpec(secretKey.getBytes(), AES);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException |
                 NoSuchPaddingException | InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException |
                 NoSuchPaddingException | InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        }
    }
}