package com.example.suportstudy.until

import android.util.Base64
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.KeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RSACipher {
    private const val CRYPTO_METHOD = "RSA"
    private const val CYPHER = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING"
    private const val CRYPTO_BITS = 2048
    private var PUB_KEY = "pubKey"
    private var PRIVATE_KEY = "privateKey"
    private const val CHARSET = "UTF-8"
    /*private final static int CRYPTO_BITS = 4096; This will encrypt in 4093bits, note however that is slower.*/

    /*private final static int CRYPTO_BITS = 4096; This will encrypt in 4093bits, note however that is slower.*/
    fun RSACipher() {
        val kp = getKeyPair()
        val publicKey = kp!!.public
        val publicKeyBytes = publicKey.encoded
        PUB_KEY = String(Base64.encode(publicKeyBytes, Base64.DEFAULT))
        //Save the public key so it is not generated each and every time
        val privateKey = kp.private
        val privateKeyBytes = privateKey.encoded
        PRIVATE_KEY = String(Base64.encode(privateKeyBytes, Base64.DEFAULT))
        //Also Save the private key so it is not generated each and every time
    }

    fun getKeyPair(): KeyPair? {
        var kp: KeyPair? = null
        try {
            val kpg = KeyPairGenerator.getInstance(CRYPTO_METHOD)
            kpg.initialize(CRYPTO_BITS)
            kp = kpg.generateKeyPair()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return kp
    }

    fun encrypt(clearText: String): String? {
        var encryptedBase64 = ""
        try {
            val keyFac = KeyFactory.getInstance(CRYPTO_METHOD)
            val keySpec: KeySpec = X509EncodedKeySpec(Base64.decode(PUB_KEY.trim { it <= ' ' }
                .toByteArray(), Base64.DEFAULT))
            val key: Key = keyFac.generatePublic(keySpec)
            val cipher = Cipher.getInstance(CYPHER)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val encryptedBytes = cipher.doFinal(clearText.toByteArray(charset(CHARSET)))
            encryptedBase64 = String(Base64.encode(encryptedBytes, Base64.DEFAULT))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return encryptedBase64.replace("(\\r|\\n)".toRegex(), "")
    }

    fun decrypt(encryptedBase64: String?): String? {
        var decryptedString: String? = ""
        try {
            val keyFac = KeyFactory.getInstance(CRYPTO_METHOD)
            val keySpec: KeySpec = PKCS8EncodedKeySpec(
                Base64.decode(PRIVATE_KEY.trim { it <= ' ' }.toByteArray(), Base64.DEFAULT)
            )
            val key: Key = keyFac.generatePrivate(keySpec)
            val cipher = Cipher.getInstance(CYPHER)
            cipher.init(Cipher.DECRYPT_MODE, key)
            val encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            decryptedString = String(decryptedBytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return decryptedString
    }
}