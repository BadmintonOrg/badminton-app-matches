package si.fri.rso.badmintonappmatches.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.badmintonappmatches.lib.Match;
import si.fri.rso.badmintonappmatches.models.converters.MatchConverter;
import si.fri.rso.badmintonappmatches.models.entities.MatchesEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
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

    public Match getMatch(Integer id) {

        MatchesEntity matchesEntity = em.find(MatchesEntity.class, id);

        if (matchesEntity == null) {
            throw new NotFoundException();
        }

        Match match = MatchConverter.toDto(matchesEntity);

        return match;
    }

    public Match createMatch(Match match) {

        MatchesEntity matchesEntity = MatchConverter.toEntity(match);

        try {
            beginTx();
            em.persist(matchesEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (matchesEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return MatchConverter.toDto(matchesEntity);
    }

    public boolean deleteMatch(Integer id) {

        MatchesEntity cort = em.find(MatchesEntity.class, id);

        if (cort != null) {
            try {
                beginTx();
                em.remove(cort);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    public Match putMatch(Integer id, Match match) {

        MatchesEntity c = em.find(MatchesEntity.class, id);

        if (c == null) {
            return null;
        }

        MatchesEntity updatedMatchEntity = MatchConverter.toEntity(match);

        try {
            beginTx();
            updatedMatchEntity.setId(c.getId());
            updatedMatchEntity = em.merge(updatedMatchEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return MatchConverter.toDto(updatedMatchEntity);
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

}
