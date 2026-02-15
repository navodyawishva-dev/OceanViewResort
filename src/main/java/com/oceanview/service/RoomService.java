package com.oceanview.service;

import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.RoomTypeDAO;
import com.oceanview.exception.InvalidRoomException;
import com.oceanview.model.Room;
import com.oceanview.model.RoomType;
import com.oceanview.util.IdGenerator;
import com.oceanview.util.ValidationUtil;

import java.util.Date;
import java.util.List;


public class RoomService {

    private final RoomDAO     roomDAO     = new RoomDAO();
    private final RoomTypeDAO roomTypeDAO = new RoomTypeDAO();




    public List<RoomType> getAllRoomTypes() {
        return roomTypeDAO.findAll();
    }


    public RoomType getRoomTypeById(String typeId) {
        return roomTypeDAO.findById(typeId);
    }


    public void addRoomType(String typeName, double pricePerNight,
                            String description, int maxOccupancy) throws Exception {


        if (!ValidationUtil.isNotEmpty(typeName)) {
            throw new Exception("Room type name is required");
        }
        if (!ValidationUtil.isValidPrice(pricePerNight)) {
            throw new Exception("Price must be a positive value");
        }
        if (!ValidationUtil.isPositive(maxOccupancy)) {
            throw new Exception("Max occupancy must be at least 1");
        }


        if (roomTypeDAO.typeNameExists(typeName.trim())) {
            throw new Exception("Room type '" + typeName + "' already exists");
        }


        RoomType roomType = new RoomType();
        roomType.setTypeId(IdGenerator.generateRoomTypeId());
        roomType.setTypeName(typeName.trim());
        roomType.setPricePerNight(pricePerNight);
        roomType.setDescription(description != null ? description.trim() : "");
        roomType.setMaxOccupancy(maxOccupancy);

        if (!roomTypeDAO.insert(roomType)) {
            throw new Exception("Failed to add room type. Please try again.");
        }
    }


    public void updateRoomType(String typeId, String typeName, double pricePerNight,
                               String description, int maxOccupancy) throws Exception {


        RoomType existing = roomTypeDAO.findById(typeId);
        if (existing == null) {
            throw new Exception("Room type not found: " + typeId);
        }


        if (!ValidationUtil.isNotEmpty(typeName)) {
            throw new Exception("Room type name is required");
        }
        if (!ValidationUtil.isValidPrice(pricePerNight)) {
            throw new Exception("Price must be a positive value");
        }
        if (!ValidationUtil.isPositive(maxOccupancy)) {
            throw new Exception("Max occupancy must be at least 1");
        }


        RoomType nameCheck = roomTypeDAO.findByName(typeName.trim());
        if (nameCheck != null && !nameCheck.getTypeId().equals(typeId)) {
            throw new Exception("Room type name '" + typeName + "' already exists");
        }


        existing.setTypeName(typeName.trim());
        existing.setPricePerNight(pricePerNight);
        existing.setDescription(description != null ? description.trim() : "");
        existing.setMaxOccupancy(maxOccupancy);

        if (!roomTypeDAO.update(existing)) {
            throw new Exception("Failed to update room type. Please try again.");
        }
    }


    public void deleteRoomType(String typeId) throws Exception {
        RoomType existing = roomTypeDAO.findById(typeId);
        if (existing == null) {
            throw new Exception("Room type not found: " + typeId);
        }

        // ── Block delete if rooms are using this type ──
        if (roomTypeDAO.hasRooms(typeId)) {
            throw new Exception(
                    "Cannot delete '" + existing.getTypeName() +
                            "' — rooms are still assigned to this type. " +
                            "Reassign or delete those rooms first.");
        }

        if (!roomTypeDAO.delete(typeId)) {
            throw new Exception("Failed to delete room type. Please try again.");
        }
    }


    public List<Room> getAllRooms() {
        return roomDAO.findAll();
    }


    public Room getRoomById(String roomId) {
        return roomDAO.findById(roomId);
    }


    public List<Room> getRoomsByStatus(String status) {
        return roomDAO.findByStatus(status);
    }


    public List<Room> getAvailableRooms(Date checkIn, Date checkOut) throws Exception {
        if (!ValidationUtil.isValidDateRange(checkIn, checkOut)) {
            throw new Exception("Check-out date must be after check-in date");
        }
        return roomDAO.findAvailableRooms(checkIn, checkOut);
    }


    public void addRoom(String roomNumber, String typeId,
                        int floor, String description) throws Exception {


        if (!ValidationUtil.isValidRoomNumber(roomNumber)) {
            throw new Exception("Valid room number is required");
        }
        if (!ValidationUtil.isNotEmpty(typeId)) {
            throw new Exception("Room type is required");
        }
        if (floor < 0) {
            throw new Exception("Floor number cannot be negative");
        }


        RoomType roomType = roomTypeDAO.findById(typeId);
        if (roomType == null) {
            throw new Exception("Selected room type does not exist");
        }


        if (roomDAO.roomNumberExists(roomNumber.trim())) {
            throw new Exception("Room number '" + roomNumber + "' already exists");
        }


        Room room = new Room();
        room.setRoomId(IdGenerator.generateRoomId());
        room.setRoomNumber(roomNumber.trim());
        room.setRoomType(roomType);
        room.setFloor(floor);
        room.setStatus(Room.STATUS_AVAILABLE);
        room.setDescription(description != null ? description.trim() : "");

        if (!roomDAO.insert(room)) {
            throw new Exception("Failed to add room. Please try again.");
        }
    }


    public void updateRoom(String roomId, String roomNumber, String typeId,
                           int floor, String status, String description) throws Exception {


        Room existing = roomDAO.findById(roomId);
        if (existing == null) {
            throw new Exception("Room not found: " + roomId);
        }


        if (!ValidationUtil.isValidRoomNumber(roomNumber)) {
            throw new Exception("Valid room number is required");
        }
        if (!ValidationUtil.isNotEmpty(typeId)) {
            throw new Exception("Room type is required");
        }


        RoomType roomType = roomTypeDAO.findById(typeId);
        if (roomType == null) {
            throw new Exception("Selected room type does not exist");
        }


        Room numberCheck = roomDAO.findByRoomNumber(roomNumber.trim());
        if (numberCheck != null && !numberCheck.getRoomId().equals(roomId)) {
            throw new Exception("Room number '" + roomNumber + "' already exists");
        }

        // ── Update fields ──
        existing.setRoomNumber(roomNumber.trim());
        existing.setRoomType(roomType);
        existing.setFloor(floor);
        existing.setStatus(status);
        existing.setDescription(description != null ? description.trim() : "");

        if (!roomDAO.update(existing)) {
            throw new Exception("Failed to update room. Please try again.");
        }
    }


    public void deleteRoom(String roomId) throws Exception {
        Room existing = roomDAO.findById(roomId);
        if (existing == null) {
            throw new Exception("Room not found: " + roomId);
        }
        if (!roomDAO.delete(roomId)) {
            throw new Exception(
                    "Cannot delete room. It may have existing reservations.");
        }
    }


    public void updateRoomStatus(String roomId, String status) throws Exception {
        Room existing = roomDAO.findById(roomId);
        if (existing == null) {
            throw new Exception("Room not found: " + roomId);
        }
        if (!roomDAO.updateStatus(roomId, status)) {
            throw new Exception("Failed to update room status.");
        }
    }


    public int[] getRoomCounts() {
        int available   = roomDAO.countByStatus(Room.STATUS_AVAILABLE);
        int occupied    = roomDAO.countByStatus(Room.STATUS_OCCUPIED);
        int reserved    = roomDAO.countByStatus(Room.STATUS_RESERVED);
        int maintenance = roomDAO.countByStatus(Room.STATUS_MAINTENANCE);
        int total       = available + occupied + reserved + maintenance;
        return new int[]{total, available, occupied, reserved, maintenance};
    }
};