package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private OrderService orderService;

    private Order order;
    private Payment payment;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setId("product-001");
        product.setName("Test Product");
        product.setQuantity(1);

        List<Product> products = new ArrayList<>();
        products.add(product);

        order = new Order("order-001", products, 123456789L, "Jessica");

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        paymentData.put("orderId", order.getId());

        payment = new Payment("payment-001", "VOUCHER_CODE", paymentData);
    }

    @Test
    void testPaymentDetailFormGet() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetailForm"));
    }

    @Test
    void testPaymentDetailGet() throws Exception {
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/" + payment.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("payment"))
                .andExpect(view().name("paymentDetail"));
    }

    @Test
    void testAdminPaymentListGet() throws Exception {
        List<Payment> payments = new ArrayList<>();
        payments.add(payment);
        when(paymentService.getAllPayments()).thenReturn(payments);

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("payments"))
                .andExpect(view().name("paymentAdminList"));
    }

    @Test
    void testAdminPaymentDetailGet() throws Exception {
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/" + payment.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("payment"))
                .andExpect(view().name("paymentAdminDetail"));
    }

    @Test
    void testAdminSetPaymentStatusPost() throws Exception {
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);
        when(paymentService.setStatus(any(Payment.class), anyString())).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/" + payment.getId())
                        .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/detail/" + payment.getId()));
    }
}