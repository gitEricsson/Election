package election.controllers;

import election.dtos.requests.ElectionRequest;
import election.dtos.responses.ElectionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import election.services.ElectionService;

import java.util.List;

@RestController
@RequestMapping("/api/elections")
@RequiredArgsConstructor
public class ElectionController {

    private final ElectionService electionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ElectionResponse createElection(@RequestBody ElectionRequest request) {
        return electionService.createElection(request);
    }

    @PatchMapping("/{id}/start")
    public void startElection(@PathVariable Long id) {
        electionService.startElection(id);
    }

    @PatchMapping("/{id}/end")
    public void endElection(@PathVariable Long id) {
        electionService.endElection(id);
    }

    @GetMapping
    public List<ElectionResponse> getAllElections() {
        return electionService.findAll();
    }
}