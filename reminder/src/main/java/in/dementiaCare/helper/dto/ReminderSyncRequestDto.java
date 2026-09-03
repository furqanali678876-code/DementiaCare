package in.dementiaCare.helper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderSyncRequestDto {

    private List<ReminderRequestDto> newReminders;
    private List<ReminderStatusUpdateDto> statusUpdates;
}