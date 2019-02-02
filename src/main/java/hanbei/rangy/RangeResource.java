package hanbei.rangy;

import com.google.common.base.Charsets;
import hanbei.rangy.multipart.ByteRangesMultiPart;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("")
public class RangeResource {

    @GET
    @Path("{file:.+}")
    public Response retrieveFile(@HeaderParam("Range") String range, @PathParam("file") String file) {
        List<Range> ranges = Range.parse(range);

        if (ranges.isEmpty()) {
            return Response.ok().header("Accept-Ranges", "bytes").build();
        }
        if (ranges.size() == 1) {
            return Response.status(206).header("Content-Range", bytesRangeHeader(ranges.get(0), 123123L)).build();
        }
        MultiPart multiPart = new ByteRangesMultiPart();
        ranges.stream().map(r -> bodyPart(r, 12313L)).forEach(multiPart::bodyPart);

        return Response.status(206).entity(multiPart).build();
    }

    private BodyPart bodyPart(Range r, long length) {
        BodyPart bodyPart = new BodyPart();
        bodyPart.getHeaders().add("Content-Range", bytesRangeHeader(r, length));
        bodyPart.setEntity(Entity.entity("some_string".getBytes(Charsets.UTF_8), MediaType.APPLICATION_OCTET_STREAM));
        return bodyPart;
    }

    private String bytesRangeHeader(Range r, long length) {
        return "bytes " + r.lower() + "-" + r.upper() + "/" + length;
    }

}
