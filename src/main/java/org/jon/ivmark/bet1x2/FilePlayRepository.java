package org.jon.ivmark.bet1x2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.jon.ivmark.bet1x2.api.RoundPlayDto;
import org.jon.ivmark.bet1x2.login.User;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FilePlayRepository implements PlayRepository {
    private final File playsDir;
    private final ObjectMapper objectMapper;

    public FilePlayRepository(String dataDir, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.playsDir = new File(dataDir, "plays");
    }

    @Override
    public void savePlays(User user, List<RoundPlayDto> plays) {
        try {
            objectMapper.writeValue(assertUserFileExists(user), plays);
        } catch (IOException e) {
            // TODO: Handle this better
            throw new RuntimeException("Failed to save rounds", e);
        }
    }

    @Override
    public List<RoundPlayDto> getPlays(User user) {
        try {
            File file = assertUserFileExists(user);
            return objectMapper.readValue(file, new TypeReference<List<RoundPlayDto>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to get plays", e);
        }
    }

    private File userFile(User user) {
        return new File(playsDir, user.username + ".json");
    }

    private File assertUserFileExists(User user) throws IOException {
        File userFile = userFile(user);
        if (!userFile.exists()) {
            FileUtils.touch(userFile);
            savePlays(user, Collections.<RoundPlayDto>emptyList());
        }
        return userFile;
    }
}
