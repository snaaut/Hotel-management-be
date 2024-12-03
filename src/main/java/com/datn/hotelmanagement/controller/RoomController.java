package com.datn.hotelmanagement.controller;

import com.datn.hotelmanagement.Repository.RoomRepository;
import com.datn.hotelmanagement.common.domain.ApiResponse;
import com.datn.hotelmanagement.dto.request.RoomDTO;
import com.datn.hotelmanagement.entity.Room;
import com.datn.hotelmanagement.service.RoomService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final Path root = Paths.get("uploads");
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomRepository roomRepository;

    private static String UPLOAD_DIR = "uploads/";


    @PostMapping("/create-room")
    public Room createRoom(@RequestParam("typeRoom") String typeRoom,
                           @RequestParam("roomNumber") String roomNumber,
                           @RequestParam("description") String description,
                           @RequestParam("pricePerDay") double pricePerDay,
                           @RequestParam("maxCapacity") int maxCapacity,
                           @RequestParam("allowPet") boolean allowPet,
                           @RequestParam("facilities") List<String> facilities,
                           @RequestParam("images") List<MultipartFile> images) {

        List<String> imageFileNames = new ArrayList<>();

        // Create the upload directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Save images to the directory and get the filenames
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                String fileName = image.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                // Check if the file already exists
                if (Files.exists(filePath)) {
                    // If the file exists, just add the filename to the list
                    imageFileNames.add(fileName);
                } else {
                    // If the file does not exist, upload it and add the filename to the list
                    try {
                        Files.copy(image.getInputStream(), filePath);
                        imageFileNames.add(fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Create Room object
        Room room = Room.builder()
                .typeRoom(typeRoom)
                .roomNumber(roomNumber)
                .description(description)
                .pricePerDay(pricePerDay)
                .maxCapacity(maxCapacity)
                .allowPet(allowPet)
                .facilities(facilities)
                .images(imageFileNames)
                .build();

        return roomService.saveRoom(room);
    }


    @PutMapping("/update-room/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id,
                                           @RequestParam("typeRoom") String typeRoom,
                                           @RequestParam("roomNumber") String roomNumber,
                                           @RequestParam("description") String description,
                                           @RequestParam("pricePerDay") double pricePerDay,
                                           @RequestParam("maxCapacity") int maxCapacity,
                                           @RequestParam("allowPet") boolean allowPet,
                                           @RequestParam("facilities") List<String> facilities,
                                           @RequestParam(value = "images", required = false) List<MultipartFile> images) {

        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (!optionalRoom.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Room room = optionalRoom.get();

        List<String> imageFileNames = new ArrayList<>();

        // Tạo thư mục nếu chưa tồn tại
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        // Lưu ảnh mới vào thư mục và lấy tên ảnh
        if (images != null) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String fileName = image.getOriginalFilename();
                    Path filePath = uploadPath.resolve(fileName);
                    try {
                        // Nếu tệp đã tồn tại, chỉ thêm tên tệp vào danh sách
                        if (!Files.exists(filePath)) {
                            Files.copy(image.getInputStream(), filePath);
                        }
                        imageFileNames.add(fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                }
            }
        }

        // Cập nhật thông tin phòng
        room.setTypeRoom(typeRoom);
        room.setRoomNumber(roomNumber);
        room.setDescription(description);
        room.setPricePerDay(pricePerDay);
        room.setMaxCapacity(maxCapacity);
        room.setAllowPet(allowPet);
        room.setFacilities(facilities);
        if (!imageFileNames.isEmpty()) {
            room.setImages(imageFileNames);
        }

        Room updatedRoom = roomService.saveRoom(room);
        return ResponseEntity.ok(updatedRoom);
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }
    @GetMapping("/list-id")
    public List<Long> getAllRoomIds() {
        return roomService.getAllRoomIds();
    }
    @GetMapping("/get-room/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Optional<Room> room = roomService.getRoomById(id);
        return room.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    @PutMapping("/update/{id}")
    public ApiResponse updateRoom(@PathVariable Long id, @RequestBody Room updatedRoom) {
        Room updatedRoomEntity = roomService.updateRoom(id, updatedRoom);
        return ApiResponse.success("Successfully updated room");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        Optional<Room> room = roomService.getRoomById(id);
        if (room.isPresent()) {
            roomService.deleteRoom(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms(
            @RequestParam("checkIn") String checkInStr,
            @RequestParam("checkOut") String checkOutStr) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date checkIn = sdf.parse(checkInStr);
            Date checkOut = sdf.parse(checkOutStr);

            java.sql.Date sqlCheckIn = new java.sql.Date(checkIn.getTime());
            java.sql.Date sqlCheckOut = new java.sql.Date(checkOut.getTime());

            List<Room> availableRooms = roomService.getAvailableRooms(sqlCheckIn, sqlCheckOut);
            return ResponseEntity.ok(availableRooms);
        } catch (ParseException e) {
            // Handle the exception as needed
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/img/{image}", produces = "image/*")
    public void fetchSpecialistImage(@PathVariable("image") String image, HttpServletResponse response) {
        System.out.println(image);
        Resource resource = roomService.load(image);
        if(resource != null){
            try (InputStream in = resource.getInputStream()) {
                ServletOutputStream out = response.getOutputStream();
                FileCopyUtils.copy(in, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/count-room")
    public ResponseEntity<Long> countRooms() {
        long count = roomService.countRooms();
        return ResponseEntity.ok(count);
    }

}


