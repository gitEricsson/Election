package election.data.repositories;

import election.data.models.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VoteRepository extends MongoRepository<Vote, String> {
    boolean existsByUserIdAndElectionId(String userId, String electionId);
    int countByElectionIdAndOptionId(String electionId, String optionId);
    List<Vote> findByElectionId(String electionId);
}