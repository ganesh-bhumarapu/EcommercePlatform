package com.EMS.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private String userName;
    private List<OrderItemDTO> items;
    private Double totalAmount;
    private String address;
    private String phoneNumber;
    private LocalDateTime orderDate;
    private String status;
}
