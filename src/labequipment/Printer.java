package labequipment;

import java.math.BigDecimal;

public class Printer extends Device {

    private final PrinterType printerType;
    private long numberPrintedPage;
    private final  boolean isColorPrinter;
    
    public Printer(String id, String name, int yearInUse, BigDecimal price, DeviceStatus status,
        PrinterType printerType, long numberPrintedPage, boolean isColorPrinter) {
            super(id, name, yearInUse, price, status);
            if (printerType == null) throw new IllegalArgumentException("Loai may in khong duoc null");
            this.printerType = printerType;
            setPagesPrinted(numberPrintedPage);
            this.isColorPrinter = isColorPrinter;
        }

    public Printer(String id, String name, int yearInUse, BigDecimal price, DeviceStatus status,
        PrinterType printerType, boolean isColorPrinter, int numberPrintedPage) {
            this(id, name, yearInUse, price, status, printerType, numberPrintedPage, isColorPrinter);
        }
    
    public PrinterType getPrinterType(){
        return printerType;
    }

    public long getPagesPrinted(){
        return numberPrintedPage;
    }

    public final void setPagesPrinted(long value){
        if (value < 0) throw new IllegalArgumentException("So trang khong duoc am");
        numberPrintedPage = value;
    }

    public boolean isColor(){
        return isColorPrinter;
    }

    public boolean supportsNetwork(){
        return false;
    }

    @Override 
    public BigDecimal calculateAnnualMaintenanceCost(){
        BigDecimal cost = getPurchasePrice().multiply(new BigDecimal("0.04"));
        if (numberPrintedPage > 100_000) cost = cost.add(new BigDecimal("500000"));
        if (isColorPrinter) cost = cost.add(new  BigDecimal("300000"));
        return cost;
    }

    @Override
    public String toString() {
        return "Printer " + super.toString() + String.format(" | Loai: %s | Mau: %s | Da in: %,d trang",
                printerType, isColorPrinter ? "Co" : "Khong", numberPrintedPage);
    }

}
