package hanbei.rangy.multipart;

import org.glassfish.jersey.media.multipart.MultiPart;

import javax.ws.rs.core.MediaType;

public class ByteRangesMultiPart extends MultiPart {

    public ByteRangesMultiPart() {
        super(MediaType.valueOf("multipart/byteranges"));
    }
}
