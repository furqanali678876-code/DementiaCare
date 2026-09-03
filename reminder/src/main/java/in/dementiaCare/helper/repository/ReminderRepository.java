package in.dementiaCare.helper.repository;



import in.dementiaCare.helper.entity.Reminder;
import in.dementiaCare.helper.entity.ReminderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReminderRepository extends JpaRepository<Reminder, UUID> {

    List<Reminder> findByPatientId(UUID patientId);

    List<Reminder> findByPatientIdAndStatus(UUID patientId, ReminderStatus status);

    List<Reminder> findByPatientIdAndScheduledTimeBetween(
            UUID patientId, LocalDateTime start, LocalDateTime end);
}
