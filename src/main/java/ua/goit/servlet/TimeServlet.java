package ua.goit.servlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import ua.goit.timezone.TimezoneUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@WebServlet(name = "TimeServlet", urlPatterns = {"/time"})
public class TimeServlet extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setPrefix("C:\\Users\\Admin\\Documents\\GoIT Homework\\Part2Task11\\src\\main\\webapp\\WEB-INF\\templates\\"); // Путь относительно classpath
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String timezone = req.getParameter("timezone");

        // Если параметр timezone не передан, проверяем Cookie
        if (timezone == null || timezone.isEmpty()) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("lastTimezone".equals(cookie.getName())) {
                        // Декодируем значение из Cookie
                        timezone = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                        break;
                    }
                }
            }
        }

        // Если timezone не был передан и не найдено в Cookie, по умолчанию используем UTC
        if (timezone == null || timezone.isEmpty()) {
            timezone = "UTC";
        }

        // Обрабатываем текущее время для данного часового пояса
        String currentTime = TimezoneUtils.getTimeZone(timezone);

        // Сохраняем временную зону в Cookie (кодируем значение)
        Cookie lastTimezone = new Cookie("lastTimezone", URLEncoder.encode(timezone, StandardCharsets.UTF_8));
        lastTimezone.setMaxAge(60 * 60 * 24 * 30); // Срок действия cookie - 30 дней
        resp.addCookie(lastTimezone);

        // Создаем контекст Thymeleaf
        Locale locale = req.getLocale();
        WebContext context = new WebContext(req, resp, getServletContext(), locale);

        // Передаем переменные в шаблон
        context.setVariable("currentTime", currentTime);
        context.setVariable("timezone", timezone);

        // Рендерим шаблон time.html
        templateEngine.process("time", context, resp.getWriter());
    }

    public static String getTime(HttpServletRequest request) {
        return TimezoneUtils.getTimeZone(request.getParameter("timezone"));
    }
}

