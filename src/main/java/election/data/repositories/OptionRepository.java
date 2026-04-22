package election.data.repositories;

import election.data.models.Option;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OptionRepository extends MongoRepository<Option, String> {
    boolean existsByElectionIdAndName(String electionId, String name);
    List<Option> findByElectionId(String electionId);
}
