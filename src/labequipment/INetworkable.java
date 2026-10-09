/*Đồng Ngọc Bảo 202418849 */
package labequipment;

public interface INetworkable {
    String getIpAddress();
    void connect(String IpAddress);
    void disconnect();
    boolean isConnected();
}
