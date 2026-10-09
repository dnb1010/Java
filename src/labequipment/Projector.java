/*Đồng Ngọc Bảo 202418849 */
package labequipment;

import java.math.BigDecimal;

public class Projector extends Device{
    private final int brightnessLumens;
    private long lampHoursUsed;

    public Projector(String id, String name, int year, BigDecimal price, DeviceStatus status, int brightnessLumens, long lampHoursUsed){
        super(id, name, year, price, status);
        if (brightnessLumens <= 0)
            throw new IllegalArgumentException("Do sang phai lon hon 0");
        this.brightnessLumens = brightnessLumens;
        setLampHoursUsed(lampHoursUsed);
    }

    public int getBrightnessLumens(){
        return  brightnessLumens;
    }
    public long getLampHoursUsed(){
        return lampHoursUsed;
    }
    public final void setLampHoursUsed(long value){
        if (value < 0) throw new IllegalArgumentException("Gio su dung den khong duoc am");
        lampHoursUsed = value;
    }

    @Override 
    public BigDecimal calculateAnnualMaintenanceCost(){
        BigDecimal cost = getPurchasePrice().multiply(new BigDecimal("0.03"));
        if (lampHoursUsed > 3000) cost = cost.add(new BigDecimal("1500000"));
        return cost;
    }
    @Override
    public String toString() {
        return "Projector " + super.toString() + String.format(" | %d lumen | Bong den: %,d gio", brightnessLumens, lampHoursUsed);
    }
}
