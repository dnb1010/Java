package labequipment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LabRoom {
    private final String roomId;
    private final String name;
    private final int capacity;
    private final List<Device> devices = new ArrayList<>();

    public LabRoom(String roomId, String name, int capacity){
        if (roomId == null || roomId.trim().isEmpty())
            throw new IllegalArgumentException("Ma phong khong duoc rong");
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Ten phong khong duoc de trong");
        if (capacity <= 0)
            throw new IllegalArgumentException("Suc chua phai lon hon khong");

        this.roomId = roomId.trim();
        this.name = name.trim();
        this.capacity = capacity;
    }

    public String getRoomId(){
        return roomId;
    }
    public String getName(){
        return name;
    }
    public int getCapacity(){
        return capacity;
    }

    public List<Device> getDevices(){
        return Collections.unmodifiableList(devices);
    }


    public void addDevice(Device device){
        Objects.requireNonNull(device, "Thiet bi khong duoc null");
        if (findDevice(device.getDeviceId()) != null)
            throw new IllegalArgumentException("Trung ma thiet bi");
        if (devices.size() >= capacity)
            throw new IllegalStateException("Phong da du suc chua");
        devices.add(device);
    }
    
    public Device findDevice(String deviceId) {
        return devices.stream()
                .filter(d -> d.getDeviceId().equals(deviceId))
                .findFirst()
                .orElse(null);
    }

    public boolean removeDevice(String deviceId) {
        return devices.removeIf(d -> d.getDeviceId().equals(deviceId));
    }
    
    public BigDecimal calculateAnnualMaintenanceCost() {
        BigDecimal total = BigDecimal.ZERO;
        for (Device d : devices) total = total.add(d.calculateAnnualMaintenanceCost());
        return total;
    }
 
    public List<Device> getDevicesRequiringMaintenance() {
        List<Device> result = new ArrayList<>();
        for (Device device : devices) {
            if (device.getStatus() == DeviceStatus.UnderMaintenance
                    || device.getAge() > 5)
                result.add(device);
        }
        return result;
    }
 
    @Override
    public String toString() {
        return String.format("Phong %s - %s (suc chua %d, %d thiet bi)", roomId, name, capacity, devices.size());
    }
}
