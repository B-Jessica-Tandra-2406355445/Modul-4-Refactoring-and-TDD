package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentTest {
    Map<String, String> paymentVoucher;
    Map<String, String> paymentCOD;

    @BeforeEach
    void setUp() {
        paymentVoucher = new HashMap<>();
        paymentVoucher.put("voucherCode", "ESHOP1234ABC5678");

        paymentCOD = new HashMap<>();
        paymentCOD.put("address", "Jalan Margonda Raya");
        paymentCOD.put("deliveryFee", "10000");
    }

    @Test
    void testCreatePaymentVoucherSuccess() {
        Payment payment = new Payment("1", "VOUCHER_CODE", paymentVoucher);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejectedNot16Chars() {
        paymentVoucher.put("voucherCode", "ESHOP1234ABC567");
        Payment payment = new Payment("2", "VOUCHER_CODE", paymentVoucher);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejectedNotStartWithEshop() {
        paymentVoucher.put("voucherCode", "OSHEP1234ABC5678");
        Payment payment = new Payment("3", "VOUCHER_CODE", paymentVoucher);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejectedNot8Numbers() {
        paymentVoucher.put("voucherCode", "ESHOP123ABCDEFGH");
        Payment payment = new Payment("4", "VOUCHER_CODE", paymentVoucher);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentCODSuccess() {
        Payment payment = new Payment("5", "CASH_ON_DELIVERY", paymentCOD);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentCODRejectedMissingAddress() {
        paymentCOD.put("address", "");
        Payment payment = new Payment("6", "CASH_ON_DELIVERY", paymentCOD);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentCODRejectedNullDeliveryFee() {
        paymentCOD.put("deliveryFee", null);
        Payment payment = new Payment("7", "CASH_ON_DELIVERY", paymentCOD);
        assertEquals("REJECTED", payment.getStatus());
    }
}