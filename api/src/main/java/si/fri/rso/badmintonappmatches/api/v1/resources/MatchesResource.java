package si.fri.rso.badmintonappmatches.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso.badmintonappmatches.lib.Match;
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

@Log
@ApplicationScoped
@Path("/matches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, HEAD, DELETE, OPTIONS, PUT")
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

    @Operation(description = "Get data for a match.", summary = "Get data for a match")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Match",
                    content = @Content(
                            schema = @Schema(implementation = Match.class))
            )})
    @GET
    @Path("/{matchId}")
    public Response getMatch(@Parameter(description = "Match ID.", required = true)
                             @PathParam("matchId") Integer matchId) {

        log.info("Get info for match with id " + matchId);

        Match match = matchBean.getMatch(matchId);

        if (match == null) {
            log.info("No match found.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        log.info("Returning data for match with id " + matchId);
        return Response.status(Response.Status.OK).entity(match).build();
    }

    @Operation(description = "Add match.", summary = "Add match")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Match successfully added."
            ),
            @APIResponse(responseCode = "405", description = "Validation error .")
    })
    @POST
    public Response createMatch(@RequestBody(
            description = "DTO object with match data.",
            required = true, content = @Content(
            schema = @Schema(implementation = Match.class))) Match match) {

        log.info("Called method for new match");
        if (match.getDate() == null || match.getScore() == null || match.getWinner() == null || match.getUserId1() == null
                || match.getUserId2() == 0) {
            log.info("New match not added. Bad request.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            match = matchBean.createMatch(match);
        }

        log.info("New match added");
        return Response.status(Response.Status.CONFLICT).entity(match).build();

    }

    @Operation(description = "Delete match.", summary = "Delete match")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "match successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{matchId}")
    public Response deleteMatch(@Parameter(description = "Match ID.", required = true)
                                @PathParam("matchId") Integer matchId){

        log.info("Called method to delete match");
        boolean deleted = matchBean.deleteMatch(matchId);

        if (deleted) {
            log.info("Match not deleted. Bad request.");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            log.info("Deleted match with id " + matchId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Operation(description = "Update data for a match.", summary = "Update match")
    @APIResponses({
            @APIResponse(
                    responseCode = "301",
                    description = "Match successfully updated."
            )
    })
    @PUT
    @Path("{matchId}")
    public Response putMatch(@Parameter(description = "Match ID.", required = true)
                             @PathParam("matchId") Integer matchId,
                             @RequestBody(
                                     description = "DTO object with match data.",
                                     required = true,
                                     content = @Content(schema = @Schema(implementation = Match.class)))
                                     Match match){

        log.info("Called method to update match");
        match = matchBean.putMatch(matchId, match);

        if (match == null) {
            log.info("Match not updated. Bad request.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        log.info("Updated match with id " + matchId);
        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

}
