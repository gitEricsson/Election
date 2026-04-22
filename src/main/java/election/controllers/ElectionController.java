package election.controllers;

import election.dtos.requests.ElectionRequest;
import election.dtos.responses.ElectionResponse;
import election.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import election.services.ElectionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void startElection(@PathVariable("id") String id, @RequestParam("userId") String userId) {
        electionService.startElection(id, userId);
    }

    @PatchMapping("/{id}/end")
    public void endElection(@PathVariable("id") String id, @RequestParam("userId") String userId) {
        electionService.endElection(id, userId);
    }
 
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteElection(@PathVariable("id") String id, @RequestParam("userId") String userId) {
        electionService.deleteElection(id, userId);
    }

    @GetMapping
    public List<ElectionResponse> getAllElections() {
        return electionService.findAll();
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedAccess(UnauthorizedAccessException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(ElectionNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleElectionNotFound(ElectionNotFoundException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ElectionAlreadyStartedException.class)
    public ResponseEntity<Map<String, String>> handleElectionAlreadyStarted(ElectionAlreadyStartedException e) {
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

    @ExceptionHandler(ElectionNotStartedException.class)
    public ResponseEntity<Map<String, String>> handleElectionNotStarted(ElectionNotStartedException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ElectionAlreadyEndedException.class)
    public ResponseEntity<Map<String, String>> handleElectionAlreadyEnded(ElectionAlreadyEndedException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDate(InvalidDateException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}