package com.gym.management.system.enums;

/**
 * Current status of a payment.
 *
 * PAID    — Payment has been received
 * PENDING — Payment is expected but not yet received
 * OVERDUE — Payment was due but not paid on time
 */
public enum PaymentStatus {
    PAID,
    PENDING,
    OVERDUE
}