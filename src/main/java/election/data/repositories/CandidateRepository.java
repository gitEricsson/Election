package election.data.repositories;

import election.data.models.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CandidateRepository extends MongoRepository<Candidate, Long> {
    boolean existsByElectionIdAndName(Long electionId, String name);
    List<Candidate> findByElectionId(Long electionId);
}

