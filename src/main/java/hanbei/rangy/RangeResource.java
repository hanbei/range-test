package hanbei.rangy;

import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;

@Path("")
public class RangeResource {

    @HEAD
    @Path("{file:.+}")
    public Response retrieveFileHead(@HeaderParam("Range") String range, @PathParam("file") String file) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("/home/hanbei/Dropbox/5DanPrüfungFranzose.mp4", "r");

        return Response.ok()
                .header("Accept-Ranges", "bytes")
                .header(HttpHeaders.CONTENT_LENGTH, raf.length())
                .build();
    }

    @GET
    @Path("{file:.+}")
    public Response retrieveFile(@HeaderParam("Range") String rangeHeader, @PathParam("file") String file) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("/home/hanbei/Dropbox/5DanPrüfungFranzose.mp4", "r");

        long length = raf.length();
        System.out.println(length);
        System.out.println(rangeHeader);
        List<Range> ranges = Range.parse(rangeHeader);
        System.out.println(ranges);

        if (ranges.isEmpty()) {
            return Response.ok()
                    .header("Accept-Ranges", "bytes")
                    .entity((StreamingOutput) output -> {
                        WritableByteChannel writableByteChannel = Channels.newChannel(output);
                        FileChannel channel = raf.getChannel();
                        channel.transferTo(0, length, writableByteChannel);
                        //writableByteChannel.close();
                    })
                    .type("video/mp4")
                    .build();
        }
        if (ranges.size() == 1) {
            Range range = ranges.get(0).withLength((int) length);
            return Response.status(206)
                    .header("Accept-Ranges", "bytes")
                    .header("Content-Range", bytesRangeHeader(range, length))
                    .entity(new MediaStreamer(range, raf)).type("video/mp4")
                    .build();
        }

        return Response.status(500).build();
    }


    private String bytesRangeHeader(Range r, long length) {
        return "bytes " + r.lower().orElse(0L) + "-" + r.upper().orElse(length) + "/" + length;
    }

    class MediaStreamer implements StreamingOutput {

        private final Range range;
        private RandomAccessFile raf;

        final byte[] buf = new byte[4096];

        public MediaStreamer(Range range, RandomAccessFile raf) {
            this.range = range;
            this.raf = raf;
        }

        @Override
        public void write(OutputStream output) throws IOException, WebApplicationException {
            WritableByteChannel writableByteChannel = Channels.newChannel(output);
            FileChannel channel = raf.getChannel();
            channel.transferTo(range.lower().orElse(0L), range.length(), writableByteChannel);
            channel.close();
        }

    }
}
