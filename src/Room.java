public class Room {

    String name;
    int number;
    int capacity;


    Room(String name, int number, int health, int weight, int dodge) {
        this.name = name;
        this.number = number;
        this.capacity = capacity;
        if (capacity >= 0 && capacity <= 100) {
            this.capacity = capacity;
        } else {
            this.capacity = 0;
        }


    }

}
