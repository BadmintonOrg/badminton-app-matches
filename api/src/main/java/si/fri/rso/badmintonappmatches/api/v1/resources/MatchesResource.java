package si.fri.rso.badmintonappmatches.api.v1.resources;

import si.fri.rso.badmintonappmatches.lib.Match;
import si.fri.rso.badmintonappmatches.services.beans.MatchBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    @GET
    public Response getMatches() {
        List<Match> matches = matchBean.getMatches(uriInfo);

        return Response.status(Response.Status.OK).entity(matches).build();
    }

}
