package si.fri.rso.badmintonappmatches.models.converters;

import si.fri.rso.badmintonappmatches.lib.Match;
import si.fri.rso.badmintonappmatches.models.entities.MatchesEntity;

public class MatchConverter {

    public static Match toDto(MatchesEntity entity) {

        Match dto = new Match();
        dto.setMatchId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setUserId1(entity.getUserId1());
        dto.setUserId2(entity.getUserId2());
        dto.setScore(entity.getScore());
        dto.setWinner(entity.getWinner());

        return dto;

    }

    public static MatchesEntity toEntity(Match dto) {

        MatchesEntity matchesEntity = new MatchesEntity();
        matchesEntity.setId(dto.getMatchId());
        matchesEntity.setDate(dto.getDate());
        matchesEntity.setUserId1(dto.getUserId1());
        matchesEntity.setUserId2(dto.getUserId2());
        matchesEntity.setScore(dto.getScore());
        matchesEntity.setWinner(dto.getWinner());

        return matchesEntity;

    }

}
