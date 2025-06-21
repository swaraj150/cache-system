package com.cachesystem.utils;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AES {
    private static final SecretKey secretKey;
    private static final int IV_SIZE = 12;
    private static final byte[] keyBytes= Base64.getDecoder().decode("4Ld7z/9U32cdK8uow0IJDb3g4PihpMDZBMWWbK30e6w=");
    private static final int TAG_LENGTH_BIT = 128;
    static{
        try {
            secretKey=new SecretKeySpec(keyBytes,"AES");
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static byte[] encrypt(byte[] plaintextBytes) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher encryptionCipher=Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        encryptionCipher.init(Cipher.ENCRYPT_MODE,secretKey,new GCMParameterSpec(TAG_LENGTH_BIT,iv));
        byte[] encrypted= encryptionCipher.doFinal(plaintextBytes);
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encrypted.length);
        byteBuffer.put(iv);
        byteBuffer.put(encrypted);
        return byteBuffer.array();
    }

    public static byte[] decrypt(byte[] ciphertext) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, BufferUnderflowException {
        Cipher decryptionCipher=Cipher.getInstance("AES/GCM/NoPadding");
        ByteBuffer byteBuffer = ByteBuffer.wrap(ciphertext);
        byte[] iv = new byte[IV_SIZE];
        byteBuffer.get(iv);
        byte[] encrypted = new byte[byteBuffer.remaining()];
        byteBuffer.get(encrypted);

        decryptionCipher.init(Cipher.DECRYPT_MODE,secretKey,new GCMParameterSpec(TAG_LENGTH_BIT,iv));
        return decryptionCipher.doFinal(encrypted);
    }

}
