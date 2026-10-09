package labequipment;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Objects;

public abstract class Device {
    private final String deviceId;
    private String name;
    private int yearInUse;
    private BigDecimal purchasePrice;
    private DeviceStatus status;

    protected Device(String deviceId, String name, int yearInUse, BigDecimal purchasePrice, DeviceStatus status) {
        if (deviceId == null || deviceId.trim().isEmpty())
            throw  new IllegalArgumentException("Ma thiet bi khong duoc rong");
        this.deviceId = deviceId.trim();
        setName(name);
        setYearInUse(yearInUse);
        setPurchasePrice(purchasePrice);
        setStatus(status);
    }

    public final String getDeviceId(){
        return deviceId;
    }

    public String getName(){
        return name;
    }

    public int getYearInUse(){
        return yearInUse;
    }

    public BigDecimal getPurchasePrice(){
        return purchasePrice;
    }

    public DeviceStatus getStatus(){
        return status;
    }


    public final void setName(String name){
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Ten thiet bi khong duoc rong");
        this.name = name.trim();
    }

    public final void setYearInUse(int yearInUse){
        if (yearInUse > Year.now().getValue())
            throw new IllegalArgumentException("Nam dua vao su dung khong duoc lon hon nam hien tai");
        this.yearInUse = yearInUse;
    }

    public final void setPurchasePrice(BigDecimal purchasePrice){
        if (purchasePrice == null || purchasePrice.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Gia mua phai lon hon 0");
        this.purchasePrice = purchasePrice;
    }

    public final void setStatus(DeviceStatus status){
        this.status = Objects.requireNonNull(status, "Trang thai khong duoc rong");
    }

    /* So nam su dung */
    public int getAge(){
        return Year.now().getValue() - yearInUse;
    }

    /*Chi phi bao tri */
    public abstract BigDecimal calculateAnnualMaintenanceCost();

    protected BigDecimal percentOfPrice(String percent){
        return purchasePrice.multiply(new BigDecimal(percent));
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Nam: %d | Gia: %,.0f | Trang thai: %s | Bao tri/nam: %,.0f",
                deviceId, name, yearInUse, purchasePrice, status, calculateAnnualMaintenanceCost());
    }
}
