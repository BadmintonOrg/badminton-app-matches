package si.fri.rso.badmintonappmatches.services.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import si.fri.rso.badmintonappmatches.lib.Match;
import si.fri.rso.badmintonappmatches.lib.User;
import si.fri.rso.badmintonappmatches.models.converters.MatchConverter;
import si.fri.rso.badmintonappmatches.models.entities.MatchesEntity;
import si.fri.rso.badmintonappmatches.services.config.RestProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class MatchBean {

    private Logger log = Logger.getLogger(MatchBean.class.getName());

    @Inject
    @DiscoverService(value = "badmiton-app-users-service", version = "1.0.0", environment = "dev")
    private Optional<WebTarget> targetUser;

    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    @Inject
    private RestProperties restProperties;

    @Inject
    private EntityManager em;

    @PostConstruct
    private void init() {
        httpClient = HttpClientBuilder.create().build();
        objectMapper = new ObjectMapper();
    }

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

        Match mat = MatchConverter.toDto(matchesEntity);

        log.log(Level.INFO,String.valueOf(restProperties.getUserDiscovery()));
        if(targetUser.isPresent()&&restProperties.getUserDiscovery()){
            User usr = getUserFromService(mat.getUserId1());
            if(usr!=null)
                mat.setUser1(usr);

            User usr2 = getUserFromService(mat.getUserId2());
            if(usr2!=null)
                mat.setUser2(usr2);
        }

        return mat;
    }

    public User getUserFromService(Integer id){
        WebTarget service = targetUser.get().path("v1/users");
        log.log(Level.INFO,String.valueOf(service.getUri()));
        try {
            HttpGet request = new HttpGet(String.valueOf(service.getUri()) + "?filter=id:EQ:" + id);
            HttpResponse response = httpClient.execute(request);
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                if (entity != null)
                    return getOneUser(EntityUtils.toString(entity));
            } else {
                String msg = "Remote server '"  + "' is responded with status " + status + ".";
                log.log(Level.SEVERE,msg);
                return null;
            }
        } catch (IOException e) {
            log.log(Level.SEVERE,e.getMessage());

            return null;
        }
        return null;
    }

    public User getOneUser(String json){
        try {
            ArrayList<User> ar = objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
            if(ar.size()>0)
                return ar.get(0);
        } catch (JsonProcessingException e) {
            log.log(Level.SEVERE,e.getMessage());
            return null;
        }
        return null;
    }

}
