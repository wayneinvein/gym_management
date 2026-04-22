package com.gym.management.system.enums;

/**
 * Represents the current status of a membership subscription.
 *
 * ACTIVE    — Membership is currently valid and member has gym access
 * EXPIRED   — Membership end date has passed
 * CANCELLED — Membership was manually cancelled by admin
 */
public enum MembershipStatus {
    ACTIVE,
    EXPIRED,
    CANCELLED
}