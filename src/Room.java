public class Room {

    String roomId;
    String roomName;
    String building;
    int number;
    String capacity;


    Room(String roomId, String roomName, String building, int number, String capacity) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.building = building;
        this.number = number;
        this.capacity = capacity;
    }

    public Room(String id) {
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

}
