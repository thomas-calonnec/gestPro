package com.thomas.gestPro.repository;

import com.thomas.gestPro.model.Workspace;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional
public class WorkspaceRepositoryTest {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Test
    public void WorkspaceRepository_SaveAll_ReturnSavedWorkspace(){
        //Arrange
        Workspace workspace = Workspace.builder().name("Test Workspace").build();

        //Act
        Workspace savedWorkspace = this.workspaceRepository.save(workspace);

        //Assert
        Assertions.assertThat(savedWorkspace).isNotNull();
        Assertions.assertThat(savedWorkspace.getId()).isGreaterThan(0);

    }


}
