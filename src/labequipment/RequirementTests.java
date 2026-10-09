package labequipment;

import java.math.BigDecimal;
import java.time.Year;

/** Kiểm thử tự chạy, không cần JUnit hoặc bật cờ -ea. */
public class RequirementTests {
    private static int passed;
    private static final int YEAR = Year.now().getValue();

    private static void check(boolean result, String name) {
        if (!result) throw new AssertionError(name);
        passed++;
    }
    private static void money(BigDecimal actual, String expected, String name) {
        check(actual.compareTo(new BigDecimal(expected)) == 0, name + ": " + actual);
    }
    private static void fails(Class<? extends Throwable> type, Runnable action, String name) {
        try { action.run(); }
        catch (Throwable error) {
            if (type.isInstance(error)) { passed++; return; }
            throw new AssertionError(name + ": sai loại lỗi", error);
        }
        throw new AssertionError(name + ": chưa từ chối dữ liệu");
    }
    private static Computer computer(String id, int age, boolean gpu) {
        return new Computer(id, "PC", YEAR - age, new BigDecimal("10000000"),
                DeviceStatus.Active, 16, "CPU", gpu);
    }
    private static Printer printer(long pages, boolean color) {
        return new Printer("P", "Printer", YEAR, new BigDecimal("10000000"),
                DeviceStatus.Active, PrinterType.Laser, pages, color);
    }
    private static Projector projector(long hours) {
        return new Projector("J", "Projector", YEAR, new BigDecimal("10000000"),
                DeviceStatus.Active, 3000, hours);
    }
    private static void testNetwork(INetworkable device) {
        check(!device.isConnected() && device.getIpAddress().isEmpty(), "Mạng ban đầu");
        fails(IllegalArgumentException.class, () -> device.connect(" "), "IP rỗng");
        fails(IllegalArgumentException.class, () -> device.connect(null), "IP null");
        check(!device.isConnected(), "Kết nối lỗi không đổi trạng thái");
        device.connect("192.168.1.10");
        check(device.isConnected() && device.getIpAddress().equals("192.168.1.10"),
                "Kết nối thành công");
        fails(IllegalStateException.class, () -> device.connect("192.168.1.11"), "Kết nối lại");
        check(device.getIpAddress().equals("192.168.1.10"), "Giữ IP khi lỗi");
        device.disconnect();
        check(!device.isConnected() && device.getIpAddress().isEmpty(), "Ngắt xóa IP");
        device.disconnect();
        check(!device.isConnected(), "Ngắt lần hai an toàn");
        device.connect("192.168.1.12");
        check(device.isConnected(), "Kết nối sau khi ngắt");
    }

    public static void main(String[] args) throws Exception {
        money(computer("C", 5, false).calculateAnnualMaintenanceCost(), "500000", "Đúng 5 năm");
        money(computer("C", 6, false).calculateAnnualMaintenanceCost(), "600000", "Trên 5 năm");
        money(computer("C", 5, true).calculateAnnualMaintenanceCost(), "700000", "GPU");
        money(computer("C", 6, true).calculateAnnualMaintenanceCost(), "800000", "GPU và cũ");
        money(printer(100000, false).calculateAnnualMaintenanceCost(), "400000", "Đúng 100000 trang");
        money(printer(100001, false).calculateAnnualMaintenanceCost(), "900000", "Trên 100000 trang");
        money(printer(100000, true).calculateAnnualMaintenanceCost(), "700000", "Máy in màu");
        money(printer(100001, true).calculateAnnualMaintenanceCost(), "1200000", "Màu và nhiều trang");
        money(projector(3000).calculateAnnualMaintenanceCost(), "300000", "Đúng 3000 giờ");
        money(projector(3001).calculateAnnualMaintenanceCost(), "1800000", "Trên 3000 giờ");

        fails(IllegalArgumentException.class, () -> computer(" ", 0, false), "Mã trắng");
        fails(IllegalArgumentException.class, () -> computer(null, 0, false), "Mã null");
        Computer c = computer("C", 5, false);
        fails(IllegalArgumentException.class, () -> c.setPurchasePrice(BigDecimal.ZERO), "Giá 0");
        fails(IllegalArgumentException.class, () -> c.setPurchasePrice(new BigDecimal("-1")), "Giá âm");
        fails(IllegalArgumentException.class, () -> c.setPurchasePrice(null), "Giá null");
        fails(IllegalArgumentException.class, () -> c.setYearInUse(YEAR + 1), "Năm tương lai");
        fails(NullPointerException.class, () -> c.setStatus(null), "Trạng thái null");
        fails(IllegalArgumentException.class, () -> printer(-1, false), "Trang âm");
        fails(IllegalArgumentException.class, () -> projector(-1), "Giờ âm");
        check(java.lang.reflect.Modifier.isFinal(Device.class.getDeclaredField("deviceId").getModifiers()),
                "Mã final");
        check(java.lang.reflect.Modifier.isAbstract(Device.class.getModifiers()), "Device trừu tượng");

        LabRoom room = new LabRoom("R", "Room", 4);
        check(room.findDevice("missing") == null, "Tìm không thấy");
        check(!room.removeDevice("missing"), "Gỡ không thấy");
        money(room.calculateAnnualMaintenanceCost(), "0", "Phòng rỗng");
        fails(NullPointerException.class, () -> room.addDevice(null), "Thêm null");
        room.addDevice(c);
        fails(IllegalArgumentException.class, () -> room.addDevice(computer("C", 0, true)), "Trùng mã");
        check(room.getDevices().size() == 1, "Không thêm khi lỗi");
        fails(UnsupportedOperationException.class, () -> room.getDevices().clear(), "Danh sách bảo vệ");
        check(room.getDevicesRequiringMaintenance().isEmpty(), "5 năm chưa cần bảo trì");
        Computer old = computer("OLD", 6, false);
        old.setStatus(DeviceStatus.Retired);
        room.addDevice(old);
        Printer maintenance = printer(0, false);
        maintenance.setStatus(DeviceStatus.UnderMaintenance);
        room.addDevice(maintenance);
        check(room.getDevicesRequiringMaintenance().size() == 2, "Điều kiện OR kể cả Retired cũ");
        room.getDevicesRequiringMaintenance().clear();
        check(room.getDevices().size() == 3, "Kết quả lọc độc lập");
        money(room.calculateAnnualMaintenanceCost(), "1500000", "Tổng đa hình");
        check(room.removeDevice("C"), "Gỡ thành công");
        check(c.getDeviceId().equals("C") && room.findDevice("C") == null, "Kết tập");
        LabRoom other = new LabRoom("OTHER", "Other", 1);
        other.addDevice(c);
        check(other.findDevice("C") == c, "Chuyển cùng đối tượng");
        fails(IllegalStateException.class, () -> other.addDevice(computer("D", 0, false)), "Phòng đầy");

        NetworkPrinter networkPrinter = new NetworkPrinter("NP", "Network printer", YEAR,
                new BigDecimal("10000000"), DeviceStatus.Active, PrinterType.Inkjet, 100001, true);
        check(!(printer(0, false) instanceof INetworkable), "Máy in thường không có mạng");
        check(!printer(0, false).supportsNetwork() && networkPrinter.supportsNetwork(), "Khả năng mạng");
        money(networkPrinter.calculateAnnualMaintenanceCost(), "1200000", "Kế thừa công thức máy in");
        testNetwork(c);
        testNetwork(networkPrinter);

        // Thêm một loại thiết bị mới để xác minh phòng không phụ thuộc lớp cụ thể.
        Device custom = new Device("CUSTOM", "Custom", YEAR, BigDecimal.ONE, DeviceStatus.Active) {
            @Override public BigDecimal calculateAnnualMaintenanceCost() {
                return new BigDecimal("123");
            }
        };
        LabRoom polymorphic = new LabRoom("POLY", "Poly", 1);
        polymorphic.addDevice(custom);
        money(polymorphic.calculateAnnualMaintenanceCost(), "123", "Đa hình loại mới");
        check(c.toString().contains("C") && c.toString().contains("RAM"), "toString");
        System.out.println("PASS: " + passed + " kiểm tra.");
    }
}
