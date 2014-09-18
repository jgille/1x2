package org.jon.ivmark.bet1x2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.jon.ivmark.bet1x2.api.RoundDto;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FileRoundRepository implements RoundRepository {

    private final File dataFile;
    private final ObjectMapper objectMapper;

    public FileRoundRepository(String dataDir, ObjectMapper objectMapper) throws IOException {
        this.dataFile = new File(dataDir, "rounds.json");
        this.objectMapper = objectMapper;
        assertDataFileExists();
    }

    private void assertDataFileExists() throws IOException {
        if (!dataFile.exists()) {
            FileUtils.touch(dataFile);
            saveRounds(Collections.<RoundDto>emptyList());
        }
    }

    @Override
    public void saveRounds(List<RoundDto> rounds) {
        try {
            objectMapper.writeValue(dataFile, rounds);
        } catch (IOException e) {
            // TODO: Handle this better
            throw new RuntimeException("Failed to save rounds", e);
        }
    }

    @Override
    public List<RoundDto> getRounds() {
        try {
            List<RoundDto> rounds = objectMapper.readValue(dataFile, new TypeReference<List<RoundDto>>() {});
            for (RoundDto roundDto : rounds) {
                roundDto.completed = Cutoff.isAfterCutOff(roundDto.cut_off);
            }
            return rounds;
        } catch (IOException e) {
            // TODO: Handle this better
            throw new RuntimeException("Failed to get rounds", e);
        }
    }
}
