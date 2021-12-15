package si.fri.rso.badmintonappmatches.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.badmintonappmatches.lib.Match;
import si.fri.rso.badmintonappmatches.models.converters.MatchConverter;
import si.fri.rso.badmintonappmatches.models.entities.MatchesEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class MatchBean {

    private Logger log = Logger.getLogger(MatchBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Match> getMatches(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, MatchesEntity.class, queryParameters).stream()
                .map(MatchConverter::toDto).collect(Collectors.toList());
    }

}
