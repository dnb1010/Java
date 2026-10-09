/*Đồng Ngọc Bảo 202418849 */
package labequipment;

import java.math.BigDecimal;

public class NetworkPrinter extends Printer implements INetworkable{
    private final NetworkConnection network = new NetworkConnection();

    public NetworkPrinter(String id, String name, int year, BigDecimal price, DeviceStatus status, PrinterType type, long pages, boolean color){
        super(id, name, year, price, status, type, pages, color);
    }

    @Override 
    public boolean supportsNetwork(){
        return true;
    }

    @Override  
    public String getIpAddress(){
        return network.getIpAddress();
    }

    @Override 
    public boolean isConnected(){
        return network.isConnected();
    }

    @Override 
    public void connect(String ip){
        network.connect(ip);
    }

    @Override 
    public void disconnect(){
        network.disconnect();
    }

    public String toString() {
        return "Network" + super.toString();
    }
}
