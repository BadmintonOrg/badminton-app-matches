package si.fri.rso.badmintonappmatches.api.v1.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso.badmintonappmatches.lib.Match;
import si.fri.rso.badmintonappmatches.models.entities.MatchesEntity;
import si.fri.rso.badmintonappmatches.services.beans.MatchBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/matches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MatchesResource {

    private Logger log = Logger.getLogger(MatchesResource.class.getName());

    @Inject
    private MatchBean matchBean;

    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get all matches with data", summary = "Get all matches")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of matches",
                    content = @Content(schema = @Schema(implementation = Match.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getMatches() {
        List<Match> matches = matchBean.getMatches(uriInfo);

        return Response.status(Response.Status.OK).entity(matches).build();
    }

    @GET
    @Path("/{matchId}")
    public Response getMatch(@PathParam("matchId") Integer matchId) {

        Match comm = matchBean.getMatch(matchId);

        if (comm == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(comm).build();
    }

}
