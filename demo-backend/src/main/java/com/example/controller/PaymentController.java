package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.auth.PaymentSession;
import com.example.entity.dto.common.Payment;
import com.example.entity.vo.response.PaymentVO;
import com.example.service.PaymentService;
import com.example.util.Const;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Resource
    private PaymentService service;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/all")
    public RestBean<List<PaymentVO>> all() {
        return RestBean.success(service.getPaymentList());
    }

    @GetMapping("/uid/{uid}")
    public RestBean<List<PaymentVO>> getByUid(@PathVariable("uid") Long uid) {
        return RestBean.success(service.getPaymentListByUserId(uid));
    }

    @GetMapping("/pid/{pid}")
    public RestBean<PaymentVO> getByPid(@PathVariable("pid") Long pid) {
        return RestBean.success(service.getPaymentById(pid));
    }

    @GetMapping("/qrcode/{sessionId}")
    public void generateQrCode(
            @PathVariable String sessionId,
            HttpServletResponse response
    ) throws IOException, WriterException {
        // 构造支付确认URL
        String payUrl = "http://192.168.43.155:8088/payment?sessionId=" + sessionId;

        // 生成二维码图片
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(payUrl, BarcodeFormat.QR_CODE, 200, 200);
        MatrixToImageWriter.writeToStream(matrix, "PNG", response.getOutputStream());
    }

    @PostMapping("/create-session")
    public RestBean<String> createPaymentSession(@RequestBody Payment request) throws JsonProcessingException {
        String sessionId = UUID.randomUUID().toString();
        PaymentSession session = new PaymentSession(request.getPayId(), request.getAmount(), request.getType());
        String sessionJson = objectMapper.writeValueAsString(session);
        // 存储会话信息到Redis，有效期5分钟
        stringRedisTemplate.opsForValue().set(
                Const.PAYMENT_SESSION + sessionId,
                sessionJson,
                Duration.ofMinutes(5)
        );
        return RestBean.success(sessionId);
    }

    @PostMapping("/confirm")
    public RestBean<String> confirmPayment(@RequestBody String sessionId) throws JsonProcessingException {
        // 从Redis获取会话信息
        String key = Const.PAYMENT_SESSION + sessionId.replace("\"", "");
        String sessionJson = stringRedisTemplate.opsForValue().get(key);
        if (sessionJson == null) {
            return RestBean.failure(400, "支付会话已过期或不存在");
        }
        // 解析会话信息JSON，转换为PaymentSession对象
        PaymentSession session = objectMapper.readValue(sessionJson, PaymentSession.class);
        // 根据PaymentSession中的pid获取支付信息
        Payment payment = service.getById(session.getPid());
        // 检查支付状态，如果未支付，则更新为已支付，并记录支付时间
        if (payment.getStatus().equals("unpaid")) {
            payment.setStatus("paid");
            payment.setPaymentTime(new Date());
            service.updatePayment(payment);
            stringRedisTemplate.delete(key);
            return RestBean.success("支付成功");
        }
        return RestBean.failure(400, "支付失败");
    }

    @PostMapping("/add")
    public RestBean<String> add(@RequestBody Payment payment, @RequestParam("userIds") List<Long> userIds) {
        String s = service.createPayment(payment, userIds);
        return s == null ? RestBean.success("发布账单成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    public RestBean<String> update(@RequestBody Payment payment) {
        String s = service.updatePayment(payment);
        return s == null ? RestBean.success("缴费成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    public RestBean<String> delete(@RequestBody Long pid) {
        String s = service.deletePayment(pid);
        return s == null ? RestBean.success("删除账单成功") : RestBean.failure(400, s);
    }
}
