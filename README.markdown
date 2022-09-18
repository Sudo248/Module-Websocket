Java WebSockets
===============

# About [java.nio](https://shareprogramming.net/java-nio-cong-cu-moi-thay-the-java-io/) 
Được giới thiệu trong JDK4 để thay thế cho `java.io`. Thực thi các thao tác IO với tốc độ nhanh và cung cấp nhiều tính năng hỗ trợ tối ưu hóa hiệu suất.
Hai tính năng quan trọng và nổi bật nhất của java.nio
 * **Non-blocking IO operation(concurrent):** đọc dữ liệu bất cứ khi nào chúng sẵn sàng. Trong lúc chờ dữ liệu Thread có thể làm các nhiệm vụ khác làm tăng hiệu suất.
 * **Buffer oriented approach:** truy cập dữ liệu sử dụng bộ nhớ đệm dữ liệu được đọc và lưu lại trong đó, bất cứ khi nào cần sẽ lấy ra và xử lý tại đây.

java.nio thực thi thông qua 2 thành phần chính:
 * **Buffers:** Mỗi buffer bản chất là một vùng nhớ mà có thể đọc và ghi dữ liệu từ đó. Vùng nhớ này được bao bọc trong một NIO Buffer object, cung cấp các method hỗ trợ thao tác dữ liệu với vùng nhớ này.
 * **Channel:** Gần tương đồng với Stream API, dùng để giao tác giữa giữa java.nio với bên ngoài. Với 1 channel có thể đọc, ghi dữ liệu từ 1 buffer.
 * **Selector:** Một selector là một object quản lý nhiều channel khi java.nio thực thi các hoạt động non-blocking io, selector sẽ có nhiệm vụ chọn ra các channel đang sẵn sàng để thực thi nhiệm vụ.

<img src="./assets/selector.png" alt="selector">
    

# About [draft](https://github.com/TooTallNate/Java-WebSocket/wiki/Drafts)
Draft là 1 base class cho các class truyền tải dữ liệu trong websocket. Nó không phổ biến như handshake hay frame.

Draft_6455: implement [RFC 6455](https://www.rfc-editor.org/rfc/rfc6455)