package election.data.repositories;

import election.data.models.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VoteRepository extends MongoRepository<Vote, Long> {
    boolean existsByUserIdAndElectionId(Long userId, Long electionId);
    int countByElectionIdAndCandidateId(Long electionId, Long candidateId);
    List<Vote> findByElectionId(Long electionId);
}