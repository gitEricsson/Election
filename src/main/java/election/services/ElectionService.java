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
    private final OptionService optionService;

    public ElectionService(ElectionRepository electionRepository, OptionService optionService) {
        this.electionRepository = electionRepository;
        this.optionService = optionService;
    }

    public ElectionResponse createElection(ElectionRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new InvalidDateException("Invalid date range");
        }
        Election election = new Election(request.getTitle(), request.getDescription(), request.getCreatorId(), request.getStartDate(), request.getEndDate());
        Election savedElection = electionRepository.save(election);
        
        if (request.getOptions() != null) {
            for (String optionName : request.getOptions()) {
                election.data.models.Option option = new election.data.models.Option(optionName);
                optionService.addOption(savedElection.getId(), option);
            }
        }
        
        return mapToResponse(savedElection);
    }

    public void startElection(String id, String userId) {
        Election election = getElection(id);
        if (!election.getCreatorId().equals(userId)) {
            throw new UnauthorizedAccessException("Unauthorized: You are not the creator of this election");
        }
        if (election.isStarted()) {
            throw new ElectionAlreadyStartedException("Election already started");
        }
        if (LocalDate.now().isAfter(election.getEndDate())) {
            throw new ElectionEndedException("Election ended");
        }
 
        election.setStarted(true);
        electionRepository.save(election);
    }

    public void endElection(String id, String userId) {
        Election election = getElection(id);
        if (!election.getCreatorId().equals(userId)) {
            throw new UnauthorizedAccessException("Unauthorized: You are not the creator of this election");
        }
        if (!election.isStarted()) {
            throw new ElectionNotStartedException("Election not started");
        }
        if (election.isEnded()) {
            throw new ElectionAlreadyEndedException("Election ended");
        }
 
        election.setEnded(true);
        electionRepository.save(election);
    }
 
    public void deleteElection(String id, String userId) {
        Election election = getElection(id);
        if (!election.getCreatorId().equals(userId)) {
            throw new UnauthorizedAccessException("Unauthorized: You are not the creator of this election");
        }
        electionRepository.deleteById(id);
    }

    public Election getElection(String id) {
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
        
        java.util.List<election.dtos.responses.OptionResponse> options = optionService.getOptionsByElection(election.getId())
                .stream()
                .map(o -> new election.dtos.responses.OptionResponse(o.getId(), o.getName()))
                .collect(java.util.stream.Collectors.toList());
 
        return new ElectionResponse(election.getId(), election.getTitle(), election.getDescription(), status, election.getEndDate(), options);
    }
}