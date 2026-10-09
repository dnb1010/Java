/*Đồng Ngọc Bảo 202418849 */
package labequipment;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        int year = Year.now().getValue();
        Computer c1 = new Computer("MT01", "Dell", year - 3,
                new BigDecimal("20000000"), DeviceStatus.Active, 16, "Core i5", false);
        Computer c2 = new Computer("MT02", "Workstation", year - 6,
                new BigDecimal("30000000"), DeviceStatus.Active, 32, "Core i7", true);
        Printer p1 = new Printer("MI01", "Laser den trang", year - 2,
                new BigDecimal("5000000"), DeviceStatus.Active, PrinterType.Laser,
                80_000, false);
        NetworkPrinter p2 = new NetworkPrinter("MI02", "May in mau co mang", year - 1,
                new BigDecimal("8000000"), DeviceStatus.UnderMaintenance,
                PrinterType.Inkjet, 120_000, true);
        Projector projector = new Projector("MC01", "Epson", year - 4,
                new BigDecimal("15000000"), DeviceStatus.Active, 3_500, 3_500);

        LabRoom a = new LabRoom("A", "Phong lap trinh", 3);
        LabRoom b = new LabRoom("B", "Phong da phuong tien", 3);
        a.addDevice(c1);
        a.addDevice(p1);
        a.addDevice(projector);
        b.addDevice(c2);
        b.addDevice(p2);

        System.out.println("=== THU THEM THIET BI TRUNG MA ===");
        try {
            b.addDevice(new Computer("MT02", "May trung ma", year,
                    new BigDecimal("10000000"), DeviceStatus.Active, 8, "Core i3", false));
        } catch (IllegalArgumentException ex) {
            System.out.println("Tu choi dung: " + ex.getMessage());
        }

        NumberFormat money = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
        for (LabRoom room : Arrays.asList(a, b)) {
            System.out.println("\n=== " + room + " ===");
            for (Device device : room.getDevices()) {
                System.out.println(device);
                System.out.println("  Bao tri/nam: "
                        + money.format(device.calculateAnnualMaintenanceCost()));
            }
            System.out.println("TONG: " + money.format(room.calculateAnnualMaintenanceCost()));
            System.out.println("Thiet bi can bao tri:");
            if (room.getDevicesRequiringMaintenance().isEmpty())
                System.out.println("  Khong co theo dieu kien tai muc 5 cua de.");
            for (Device device : room.getDevicesRequiringMaintenance())
                System.out.println("  " + device.getDeviceId() + " - " + device.getName());
        }

        System.out.println("\n=== DA HINH QUA INTERFACE ===");
        List<Device> allDevices = Arrays.asList(c1, c2, p1, p2, projector);
        List<INetworkable> networkDevices = new ArrayList<>();
        for (Device device : allDevices)
            if (device instanceof INetworkable)
                networkDevices.add((INetworkable) device);

        int host = 10;
        for (INetworkable device : networkDevices) {
            device.connect("192.168.1." + host++);
            System.out.println("Ket noi: " + device.getIpAddress()
                    + ", isConnected=" + device.isConnected());
            device.disconnect();
            System.out.println("Sau ngat: IP='" + device.getIpAddress()
                    + "', isConnected=" + device.isConnected());
        }

        System.out.println("\n=== TIM / GO THIET BI VA KET TAP ===");
        System.out.println("Tim MT01: " + a.findDevice("MT01"));
        System.out.println("Go MT01: " + a.removeDevice("MT01"));
        System.out.println("Tim lai: " + a.findDevice("MT01"));
        System.out.println("Thiet bi van ton tai: " + c1.getName());
        b.addDevice(c1);
        System.out.println("Da chuyen MT01 sang phong B.");
    }
}
