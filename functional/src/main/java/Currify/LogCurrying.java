package Currify;

import java.time.LocalDateTime;
import java.util.function.Function;

public class LogCurrying {

    /* 1. 定义日志函数类型：最后一级才真正打印 */
    static final Function<String, Function<String, Function<String, Function<String, Void>>>>
            LOGGER = level -> userId -> requestId -> message -> {
        log(level, userId, requestId, message);
        return null;
    };

    static void log(String level, String userId, String requestId, String message) {
        System.out.printf("[%s][%s][%s][%s] %s%n",
                LocalDateTime.now(), level, userId, requestId, message);
    }

    public static void main(String[] args) {

        /* 2. 在服务入口把“公共上下文”固定住 */
        Function<String, Function<String, Void>> infoForUser =
                LOGGER.apply("INFO").apply("u123456");

        /* 3. 在 Controller/Service 里只关心本次请求 */
        infoForUser.apply("req-abc-001").apply("订单创建成功");

        /* 4. 换请求 ID 继续用同一套“用户级”日志器 */
        infoForUser.apply("req-abc-002").apply("库存扣减失败");
    }
}
