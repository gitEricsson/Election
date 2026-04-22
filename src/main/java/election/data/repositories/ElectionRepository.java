package election.data.repositories;

import election.data.models.Election;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionRepository extends MongoRepository<Election, String> {
}