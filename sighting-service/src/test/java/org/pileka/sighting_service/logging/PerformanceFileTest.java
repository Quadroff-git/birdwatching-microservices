package org.pileka.sighting_service.logging;

import org.junit.jupiter.api.Test;
import org.pileka.birdwatching_exception_commons.exception.EntityDoesntExistException;
import org.pileka.sighting_service.service.SightingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class PerformanceFileTest {

    @Autowired
    private SightingService sightingService;

    @Test
    void writesPerformanceLineToMainLog() throws Exception {
        assertThatThrownBy(() -> sightingService.getById(999_999L))
                .isInstanceOf(EntityDoesntExistException.class);

        Path mainLog = Path.of("logs/application.log");
        assertThat(mainLog).exists();
        assertThat(Files.readString(mainLog))
                .contains("PERFORMANCE")
                .contains("operation=db.sighting.findById");
    }
}
