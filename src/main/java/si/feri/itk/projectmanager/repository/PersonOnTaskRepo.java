package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import si.feri.itk.projectmanager.model.PersonOnTask;

import java.util.List;
import java.util.UUID;

public interface PersonOnTaskRepo extends JpaRepository<PersonOnTask, UUID> {
    List<PersonOnTask> findAllByTaskIdAndPersonId(UUID taskId, UUID personId);
}
