package com.datn.hotelmanagement.service;


import com.datn.hotelmanagement.Repository.BookedRoomRepository;
import com.datn.hotelmanagement.Repository.RoomRepository;
import com.datn.hotelmanagement.dto.request.RoomDTO;
import com.datn.hotelmanagement.entity.BookedRoom;
import com.datn.hotelmanagement.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookedRoomRepository bookedRoomRepository;



    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(Long idRoom) {
        return roomRepository.findById(idRoom);
    }

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room updateRoom(Room room) {
        return roomRepository.save(room);
    }

    public void deleteRoom(Long idRoom) {
        roomRepository.deleteById(idRoom);
    }
    public List<Long> getAllRoomIds() {
        return roomRepository.findAll().stream().map(Room::getId).collect(Collectors.toList());
    }


    public Room updateRoom(Long id, Room updatedRoom) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));

        return roomRepository.save(room);
    }

    private final String UPLOAD_DIR = "uploads/";

    public Room createRoom(String typeRoom, String description, List<MultipartFile> images, List<String> facilities, int maxCapacity, boolean allowPet, double pricePerDay) {

        List<String> imagePaths = images.stream().map(image -> {
            try {
                return saveImage(image);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file " + image.getOriginalFilename(), e);
            }
        }).collect(Collectors.toList());

        Room room = Room.builder()
                .typeRoom(typeRoom)
                .description(description)
                .images(imagePaths)
                .facilities(facilities)
                .maxCapacity(maxCapacity)
                .allowPet(allowPet)
                .pricePerDay(pricePerDay)
                .build();

        return roomRepository.save(room);
    }

    private String saveImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }
        String filename = System.currentTimeMillis() + "-" + image.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + filename);
        Files.copy(image.getInputStream(), path);
        return filename;
    }

    public List<Room> getAvailableRooms(Date checkIn, Date checkOut) {
        List<Room> allRooms = roomRepository.findAll();
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : allRooms) {
            boolean isAvailable = true;
            List<BookedRoom> bookedRooms = bookedRoomRepository.findByRoomId(room.getId());

            for (BookedRoom bookedRoom : bookedRooms) {
                if ((checkIn.before(bookedRoom.getCheckOut()) && checkIn.after(bookedRoom.getCheckIn())) ||
                        (checkOut.before(bookedRoom.getCheckOut()) && checkOut.after(bookedRoom.getCheckIn())) ||
                        (checkIn.before(bookedRoom.getCheckIn()) && checkOut.after(bookedRoom.getCheckOut()))||
                        (checkIn.equals(bookedRoom.getCheckIn()) || checkOut.equals(bookedRoom.getCheckOut()))||
                        (checkIn.equals(bookedRoom.getCheckOut()) || checkOut.equals(bookedRoom.getCheckIn()))) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }
    public Resource load(String fileName) {
        File filePath = new File("D:/DATN/BE/hotel-management/uploads", fileName);
        if(filePath.exists())
            return new FileSystemResource(filePath);
        return null;
    }
    public long countRooms() {
        return roomRepository.count();
    }
}