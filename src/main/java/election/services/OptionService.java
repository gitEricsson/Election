package election.services;

import election.data.models.Option;
import election.data.repositories.OptionRepository;
import election.data.repositories.ElectionRepository;
import election.exceptions.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ElectionRepository electionRepository;

    public OptionService(OptionRepository optionRepository, ElectionRepository electionRepository) {
        this.optionRepository = optionRepository;
        this.electionRepository = electionRepository;
    }

    public Option addOption(String electionId, Option option) {
        if (option.getName() == null || option.getName().trim().isEmpty()) {
            throw new InvalidOptionNameException("Option name cannot be empty");
        }
        if (!electionRepository.existsById(electionId)) {
            throw new ElectionNotFoundException();
        }
        if (optionRepository.existsByElectionIdAndName(electionId, option.getName())) {
            throw new DuplicateOptionException("Option already exists");
        }

        option.setElectionId(electionId);
        return optionRepository.save(option);
    }

    public List<Option> getOptionsByElection(String electionId) {
        return optionRepository.findByElectionId(electionId);
    }
}