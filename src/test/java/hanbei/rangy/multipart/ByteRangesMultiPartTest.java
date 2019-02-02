package hanbei.rangy.multipart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

class ByteRangesMultiPartTest {

    private ByteRangesMultiPart byteRangesMultiPart;

    @BeforeEach
    void set_up() {
        byteRangesMultiPart = new ByteRangesMultiPart();
    }

    @Test
    void mediatype_is_correctly_set() {
        assertThat(byteRangesMultiPart.getMediaType()).isEqualTo(new MediaType("multipart", "byteranges"));
    }

}