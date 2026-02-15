package com.oceanview.model;


public class Room {

    private String   roomId;
    private String   roomNumber;
    private RoomType roomType;
    private int      floor;
    private String   status;
    private String   description;


    public static final String STATUS_AVAILABLE    = "Available";
    public static final String STATUS_OCCUPIED     = "Occupied";
    public static final String STATUS_RESERVED     = "Reserved";
    public static final String STATUS_MAINTENANCE  = "Maintenance";


    public Room() {}


    public Room(String roomId, String roomNumber, RoomType roomType,
                int floor, String status, String description) {
        this.roomId      = roomId;
        this.roomNumber  = roomNumber;
        this.roomType    = roomType;
        this.floor       = floor;
        this.status      = status;
        this.description = description;
    }


    public String   getRoomId()      { return roomId; }
    public String   getRoomNumber()  { return roomNumber; }
    public RoomType getRoomType()    { return roomType; }
    public int      getFloor()       { return floor; }
    public String   getStatus()      { return status; }
    public String   getDescription() { return description; }


    public void setRoomId(String roomId)           { this.roomId = roomId; }
    public void setRoomNumber(String roomNumber)   { this.roomNumber = roomNumber; }
    public void setRoomType(RoomType roomType)     { this.roomType = roomType; }
    public void setFloor(int floor)                { this.floor = floor; }
    public void setStatus(String status)           { this.status = status; }
    public void setDescription(String description) { this.description = description; }


    public double getPricePerNight() {
        return roomType != null ? roomType.getPricePerNight() : 0.0;
    }


    public boolean isAvailable() {
        return STATUS_AVAILABLE.equals(this.status);
    }

    @Override
    public String toString() {
        return "Room{id='" + roomId + "', number='" + roomNumber +
                "', floor=" + floor + ", status='" + status + "'}";
    }
}