package com.gym.management.system.enums;

/**
 * Represents the current status of a gym member.
 *
 * ACTIVE    — Member has an active membership and full gym access
 * INACTIVE  — Member's membership has expired or they have left
 * SUSPENDED — Member has been temporarily suspended by admin
 */
public enum MemberStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED
}
