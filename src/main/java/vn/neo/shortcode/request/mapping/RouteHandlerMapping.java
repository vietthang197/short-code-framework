package vn.neo.shortcode.request.mapping;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
Dùng để đăng ký handle request
Dữ liệu được đọc từ file xml rồi sẽ đăng ký ở đây
 */
@Component
@Order(1)
public class RouteHandlerMapping implements HandlerMapping {
    private final Map<String, Object> urlHandlers = new ConcurrentHashMap<>();

    public RouteHandlerMapping() {

    }

    public void addHandler(String url, Object handler) {
        this.urlHandlers.put(url, handler);
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String key = uri + ":" + method;
        Object handlerObj = urlHandlers.get(key);
        if (handlerObj != null)
            return new HandlerExecutionChain(handlerObj);
        return null;
    }
}
