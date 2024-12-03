package com.datn.hotelmanagement.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RoomDTO {
    private String typeRoom;
    private String description;
    private List<MultipartFile> images;
    private List<String> facilities;
    private int maxCapacity;
    private boolean allowPet;
    private double pricePerDay;
}

