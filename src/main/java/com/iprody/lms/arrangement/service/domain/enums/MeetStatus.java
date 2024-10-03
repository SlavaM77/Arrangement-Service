package com.iprody.lms.arrangement.service.domain.enums;

import com.iprody.lms.arrangement.service.domain.model.Meet;

/**
 * <p>This enum defines the possible statuses of a {@link Meet} during its lifecycle.</p>
 * <ul>
 *   <li>{@link #SCHEDULED} - A meet will have to happen at date and time originally set.</li>
 *   <li>{@link #RESCHEDULED} - A meet will have to happen at updated date and time changed.
 *   Original date and time were changed.</li>
 *   <li>{@link #HAPPENED} - A meet took a place.</li>
 * </ul>
 */

public enum MeetStatus {
    SCHEDULED, RESCHEDULED, HAPPENED
}
