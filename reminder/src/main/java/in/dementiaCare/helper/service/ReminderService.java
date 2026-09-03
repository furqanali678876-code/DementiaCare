package in.dementiaCare.helper.service;

import in.dementiaCare.helper.dto.*;
import in.dementiaCare.helper.entity.Reminder;
import in.dementiaCare.helper.entity.ReminderStatus;
import in.dementiaCare.helper.repository.ReminderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;

    // Spring hands us the repository automatically — this is constructor injection
    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    // 1. Create a single reminder (caregiver setting one up)
    public ReminderResponseDto createReminder(ReminderRequestDto request) {

        Reminder reminder = Reminder.builder()
                .id(request.getId() != null ? request.getId() : UUID.randomUUID())
                .patientId(request.getPatientId())
                .type(request.getType())
                .title(request.getTitle())
                .description(request.getDescription())
                .scheduledTime(request.getScheduledTime())
                .isRecurring(request.getIsRecurring())
                .recurrenceRule(request.getRecurrenceRule())
                .status(ReminderStatus.PENDING)
                .build();

        Reminder saved = reminderRepository.save(reminder);
        return toResponseDTO(saved);
    }

    // 2. Get all reminders for a patient (used to load into the app, or dashboard)
    public List<ReminderResponseDto> getRemindersForPatient(UUID patientId) {
        return reminderRepository.findByPatientId(patientId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 3. Get only pending reminders (what voice assistant reads aloud)
    public List<ReminderResponseDto> getPendingReminders(UUID patientId) {
        return reminderRepository.findByPatientIdAndStatus(patientId, ReminderStatus.PENDING)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 4. Handle a batch sync from an offline device
    public void processSyncBatch(ReminderSyncRequestDto syncRequest) {

        // Save any reminders that were created while offline
        if (syncRequest.getNewReminders() != null) {
            for (ReminderRequestDto newReminderDto : syncRequest.getNewReminders()) {
                createReminder(newReminderDto);
            }
        }

        // Apply any status changes that happened while offline
        if (syncRequest.getStatusUpdates() != null) {
            for (ReminderStatusUpdateDto update : syncRequest.getStatusUpdates()) {
                reminderRepository.findById(update.getId()).ifPresent(reminder -> {
                    reminder.setStatus(update.getStatus());
                    reminder.setCompletedAt(update.getCompletedAt());
                    reminder.setLastSyncedAt(LocalDateTime.now());
                    reminderRepository.save(reminder);
                });
            }
        }
    }

    // 5. Simple adherence stats for the caregiver dashboard
    public double getAdherencePercentage(UUID patientId) {
        List<Reminder> all = reminderRepository.findByPatientId(patientId);

        if (all.isEmpty()) return 0.0;

        long completedCount = all.stream()
                .filter(r -> r.getStatus() == ReminderStatus.COMPLETED)
                .count();

        return (completedCount * 100.0) / all.size();
    }

    // Helper: converts an Entity into a Response DTO — keeps this logic in one place
    private ReminderResponseDto toResponseDTO(Reminder reminder) {
        return ReminderResponseDto.builder()
                .id(reminder.getId())
                .patientId(reminder.getPatientId())
                .type(reminder.getType())
                .title(reminder.getTitle())
                .description(reminder.getDescription())
                .scheduledTime(reminder.getScheduledTime())
                .isRecurring(reminder.getIsRecurring())
                .recurrenceRule(reminder.getRecurrenceRule())
                .status(reminder.getStatus())
                .completedAt(reminder.getCompletedAt())
                .build();
    }
}
