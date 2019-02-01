package hanbei.rangy;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("")
public class RangeResource {

    private final RangeParser parser;

    public RangeResource() {
        parser = new RangeParser();
    }

    @GET
    @Path("{file:.+}")
    public Response retrieveFile(@HeaderParam("Range") String range, @PathParam("file") String file) {
        List<Range> ranges = parser.parse(range);

        return Response.ok().build();
    }

}
