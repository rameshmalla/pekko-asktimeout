package sample.cluster;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pekko.actor.ExtendedActorSystem;
import org.apache.pekko.serialization.jackson.JacksonCborSerializer;
import org.apache.pekko.serialization.jackson.JacksonSerializer;
import org.apache.pekko.actor.*;
import org.apache.pekko.serialization.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CustomJacksonCborSerializer extends JSerializer {

    @Override
    public Object fromBinaryJava(final byte[] bytes, final Class<?> manifest) {
        return null;
    }

    @Override
    public int identifier() {
        return 100;
    }

    @Override
    public byte[] toBinary(final Object o) {
        if(o!=null){
            throw new RuntimeException("Cannot serialize");
        }
        return new byte[0];
    }

    @Override
    public boolean includeManifest() {
        return false;
    }
}
