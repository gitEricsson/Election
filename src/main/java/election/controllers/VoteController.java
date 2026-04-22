package election.controllers;

import election.dtos.requests.VoteRequest;
import election.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import election.services.VoteService;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String castVote(@RequestBody VoteRequest request) {
        voteService.castVote(request);
        return "Vote cast successfully";
    }

    @ExceptionHandler(DuplicateVoteException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateVote(DuplicateVoteException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ElectionNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleElectionNotFound(ElectionNotFoundException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ElectionNotStartedException.class)
    public ResponseEntity<Map<String, String>> handleElectionNotStarted(ElectionNotStartedException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ElectionEndedException.class)
    public ResponseEntity<Map<String, String>> handleElectionEnded(ElectionEndedException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(OptionNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOptionNotFound(OptionNotFoundException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidOptionForElectionException.class)
    public ResponseEntity<Map<String, String>> handleInvalidOptionForElection(InvalidOptionForElectionException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
