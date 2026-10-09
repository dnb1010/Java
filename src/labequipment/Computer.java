/*Đồng Ngọc Bảo 202418849 */
package labequipment;

import java.math.BigDecimal;

public class Computer extends Device implements INetworkable{
    private final int ramGB;
    private final String processorType;
    private final boolean hasDedicatedGPU;
    private final NetworkConnection network = new NetworkConnection();

    public Computer(String id, String name, int year, BigDecimal price, DeviceStatus status, int ramGB, String processorType, boolean hasDedicatedGPU){
        super(id, name, year, price, status);
        if (ramGB <= 0)
            throw new IllegalArgumentException("Dung luong RAM phai lon hon 0");
        if (processorType == null || processorType.trim().isEmpty())
            throw new IllegalArgumentException("Loai CPU khong duoc rong");
        this.ramGB = ramGB;
        this.processorType = processorType.trim();
        this.hasDedicatedGPU = hasDedicatedGPU;
    }

    public int getRamGB(){
        return ramGB;
    }

    public String getProcessorType(){
        return processorType;
    }

    public boolean hasDedicatedGPU(){
        return hasDedicatedGPU;
    }

    @Override
    public BigDecimal calculateAnnualMaintenanceCost(){
        BigDecimal rate = new BigDecimal("0.05");
        if (hasDedicatedGPU) rate = rate.add(new BigDecimal("0.02"));
        if (getAge() > 5) rate = rate.add(new BigDecimal("0.01"));
        return getPurchasePrice().multiply(rate);
    }

    @Override
    public String getIpAddress(){
        return network.getIpAddress();
    }

    @Override
    public void connect(String ipAddress){
        network.connect(ipAddress);
    }

    @Override
    public void disconnect(){
        network.disconnect();
    }

    @Override
    public boolean isConnected(){
        return network.isConnected();
    }

    @Override
    public String toString() {
        return "Computer " + super.toString() + String.format(" | RAM: %dGB | CPU: %s | GPU roi: %s",
                ramGB, processorType, hasDedicatedGPU ? "Co" : "Khong");
    }
}
