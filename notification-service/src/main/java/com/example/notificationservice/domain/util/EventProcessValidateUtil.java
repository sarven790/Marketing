package com.example.notificationservice.domain.util;

import com.example.notificationservice.dao.enums.ProcessStatus;
import com.example.notificationservice.domain.model.enums.ProcessDecision;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EventProcessValidateUtil {

    public static boolean decisionIsEqualsToSkipAlreadySent(ProcessDecision decision) {
        return decision == ProcessDecision.SKIP_ALREADY_SENT;
    }

    public static boolean decisionIsEqualsToSkipInProgress(ProcessDecision decision) {
        return decision == ProcessDecision.SKIP_IN_PROGRESS;
    }

    public static boolean statusIsEqualsToSent(ProcessStatus status) {
        return status == ProcessStatus.SENT;
    }

    public static boolean statusIsEqualsToPending(ProcessStatus status) {
        return status == ProcessStatus.PENDING;
    }

}
