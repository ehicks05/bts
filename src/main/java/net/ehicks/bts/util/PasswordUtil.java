package net.ehicks.bts.util;

import org.apache.catalina.realm.MessageDigestCredentialHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;

public class PasswordUtil
{
    private static final Logger log = LoggerFactory.getLogger(PasswordUtil.class);
    private static int cryptoIterations = 200_000;

    public static String digestPassword(String password)
    {
        long start = System.currentTimeMillis();

        try
        {
            MessageDigestCredentialHandler credentialHandler = new MessageDigestCredentialHandler();
            credentialHandler.setAlgorithm("SHA-256");
            credentialHandler.setIterations(cryptoIterations);
            credentialHandler.setSaltLength(32);

            String digested = credentialHandler.mutate(password);

            log.debug("{} sha-256 iterations in {} ms", cryptoIterations, (System.currentTimeMillis() - start));
            return digested;
        }
        catch (NoSuchAlgorithmException e)
        {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
