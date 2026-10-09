/*Đồng Ngọc Bảo 202418849 */
package labequipment;

final class NetworkConnection {
    private String ipAddress = "";
    private boolean connected;

    String getIpAddress(){
        return ipAddress;
    }

    boolean isConnected(){
        return connected;
    }

    void connect(String ip){
        if (ip == null || ip.trim().isEmpty())
            throw new IllegalArgumentException("Dia chi IP khong duoc rong");
        if (isConnected())
            throw new IllegalStateException("Thiet bi dang ket noi (" + ipAddress + "), khong the ket noi lai");
        this.ipAddress = ip.trim();
        connected = true;
    }

    void disconnect(){
        this.ipAddress = "";
        connected = false;
    }
}
