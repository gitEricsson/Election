package election.services;

import election.exceptions.DuplicateCandidateException;
import election.exceptions.ElectionNotFoundException;
import election.exceptions.InvalidCandidateNameException;
import election.data.models.Candidate;
import election.data.repositories.CandidateRepository;
import election.data.repositories.ElectionRepository;
import election.exceptions.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ElectionRepository electionRepository;

    public CandidateService(CandidateRepository candidateRepository, ElectionRepository electionRepository) {
        this.candidateRepository = candidateRepository;
        this.electionRepository = electionRepository;
    }

    public Candidate addCandidate(Long electionId, Candidate candidate) {
        if (candidate.getName() == null || candidate.getName().trim().isEmpty()) {
            throw new InvalidCandidateNameException("Candidate name cannot be empty");
        }
        if (!electionRepository.existsById(electionId)) {
            throw new ElectionNotFoundException();
        }
        if (candidateRepository.existsByElectionIdAndName(electionId, candidate.getName())) {
            throw new DuplicateCandidateException("Candidate already exists");
        }

        candidate.setElectionId(electionId);
        return candidateRepository.save(candidate);
    }

    public List<Candidate> getCandidatesByElection(Long electionId) {
        return candidateRepository.findByElectionId(electionId);
    }
}