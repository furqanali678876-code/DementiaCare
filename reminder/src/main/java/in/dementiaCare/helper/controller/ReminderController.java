package in.dementiaCare.helper.controller;

import in.dementiaCare.helper.dto.*;
import in.dementiaCare.helper.service.ReminderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    // Create a single reminder
    @PostMapping
    public ResponseEntity<ReminderResponseDto> createReminder(@RequestBody ReminderRequestDto request) {
        ReminderResponseDto created = reminderService.createReminder(request);
        return ResponseEntity.ok(created);
    }

    // Get all reminders for a patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ReminderResponseDto>> getRemindersForPatient(@PathVariable UUID patientId) {
        List<ReminderResponseDto> reminders = reminderService.getRemindersForPatient(patientId);
        return ResponseEntity.ok(reminders);
    }

    // Get only pending reminders (for voice assistant to read aloud)
    @GetMapping("/patient/{patientId}/pending")
    public ResponseEntity<List<ReminderResponseDto>> getPendingReminders(@PathVariable UUID patientId) {
        List<ReminderResponseDto> pending = reminderService.getPendingReminders(patientId);
        return ResponseEntity.ok(pending);
    }

    // Batch sync from an offline device
    @PostMapping("/sync")
    public ResponseEntity<String> syncReminders(@RequestBody ReminderSyncRequestDto syncRequest) {
        reminderService.processSyncBatch(syncRequest);
        return ResponseEntity.ok("Sync completed");
    }

    // Adherence percentage for the caregiver dashboard
    @GetMapping("/patient/{patientId}/adherence")
    public ResponseEntity<Double> getAdherence(@PathVariable UUID patientId) {
        double adherence = reminderService.getAdherencePercentage(patientId);
        return ResponseEntity.ok(adherence);
    }
}