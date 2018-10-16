package examples.springdata.geode.server.kt.wan.transport.transport

import org.apache.geode.cache.wan.GatewayTransportFilter
import org.springframework.stereotype.Component

import java.io.InputStream
import java.io.OutputStream
import java.util.zip.Adler32
import java.util.zip.CheckedInputStream
import java.util.zip.CheckedOutputStream

@Component
class WanTransportEncryptionListenerKT : GatewayTransportFilter {
    //    private Cipher cipherEnc = null;
    //    private Cipher cipherDec = null;
    //
    //    @PostConstruct
    //    private void init() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
    //        String algorithm = "AES";
    //        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
    //        int keySize = 128;
    //        keyGenerator.init(keySize);
    //        Key key = keyGenerator.generateKey();
    //        String transformation = "AES/CBC/PKCS5Padding";
    //        cipherEnc = Cipher.getInstance(transformation);
    //        cipherDec = Cipher.getInstance(transformation);
    //        cipherEnc.init(Cipher.ENCRYPT_MODE, key);
    //        cipherDec.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(cipherEnc.getIV()));
    //    }
    //
    //    @Override
    //    public InputStream getInputStream(InputStream stream) {
    //        return new BufferedInputStream(new CipherInputStream(stream, cipherDec));
    //    }
    //
    //    @Override
    //    public OutputStream getOutputStream(OutputStream stream) {
    //        return new BufferedOutputStream(new CipherOutputStream(stream, cipherEnc));
    //    }
    //
    //    @Override
    //    public InputStream getInputStream(InputStream inputStream) {
    //        try {
    //            return new GZIPInputStream(inputStream);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        return null;
    //    }
    //
    //    @Override
    //    public OutputStream getOutputStream(OutputStream outputStream) {
    //        try {
    //            return new GZIPOutputStream(outputStream);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        return null;
    //    }

    private val checksum = Adler32()

    override fun getInputStream(stream: InputStream) = CheckedInputStream(stream, checksum)

    override fun getOutputStream(stream: OutputStream)= CheckedOutputStream(stream, checksum)
}