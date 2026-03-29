package election.services;

import election.data.models.Election;
import election.data.repositories.ElectionRepository;
import election.exceptions.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class ElectionService {

    private final ElectionRepository electionRepository;

    public ElectionService(ElectionRepository electionRepository) {
        this.electionRepository = electionRepository;
    }

    public Election createElection(Election election) {
        if (election.getStartDate().isAfter(election.getEndDate())) {
            throw new InvalidDateException("Invalid date range");
        }
        return electionRepository.save(election);
    }

    public void startElection(Long id) {
        Election election = getElection(id);
        if (election.isStarted()) {
            throw new ElectionAlreadyStartedException("Election already started");
        }
        if (LocalDate.now().isAfter(election.getEndDate())) {
            throw new ElectionEndedException("Election ended");
        }

        election.setStarted(true);
        electionRepository.save(election);
    }

    public void endElection(Long id) {
        Election election = getElection(id);
        if (!election.isStarted()) {
            throw new ElectionNotStartedException("Election not started");
        }
        if (election.isEnded()) {
            throw new ElectionAlreadyEndedException("Election ended");
        }

        election.setEnded(true);
        electionRepository.save(election);
    }

    public Election getElection(Long id) {
        return electionRepository.findById(id).orElseThrow(ElectionNotFoundException::new);
    }
}