package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;

        boolean isValid = false;

        if (method.equals("VOUCHER_CODE")) {
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode != null && voucherCode.length() == 16 && voucherCode.startsWith("ESHOP")) {
                int numCount = 0;
                for (char c : voucherCode.toCharArray()) {
                    if (Character.isDigit(c)) numCount++;
                }
                if (numCount == 8) {
                    isValid = true;
                }
            }
        } else if (method.equals("CASH_ON_DELIVERY")) {
            String address = paymentData.get("address");
            String deliveryFee = paymentData.get("deliveryFee");
            if (address != null && !address.isEmpty() && deliveryFee != null && !deliveryFee.isEmpty()) {
                isValid = true;
            }
        }

        if (isValid) {
            this.status = "SUCCESS";
        } else {
            this.status = "REJECTED";
        }
    }

}