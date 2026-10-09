package labequipment;

public interface INetworkable {
    String getIpAddress();
    void connect(String IpAddress);
    void disconnect();
    boolean isConnected();
}
