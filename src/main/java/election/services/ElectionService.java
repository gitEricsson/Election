package election.services;

import election.data.models.Election;
import election.data.repositories.ElectionRepository;
import election.dtos.requests.ElectionRequest;
import election.dtos.responses.ElectionResponse;
import election.exceptions.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectionService {

    private final ElectionRepository electionRepository;

    public ElectionService(ElectionRepository electionRepository) {
        this.electionRepository = electionRepository;
    }

    public ElectionResponse createElection(ElectionRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new InvalidDateException("Invalid date range");
        }
        Election election = new Election(request.getTitle(), request.getStartDate(), request.getEndDate());
        Election savedElection = electionRepository.save(election);
        return mapToResponse(savedElection);
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

    public List<ElectionResponse> findAll() {
        return electionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ElectionResponse mapToResponse(Election election) {
        String status = "PENDING";
        if (election.isEnded()) {
            status = "ENDED";
        } else if (election.isStarted()) {
            status = "STARTED";
        }
        return new ElectionResponse(election.getId(), election.getTitle(), status, election.getEndDate());
    }
}